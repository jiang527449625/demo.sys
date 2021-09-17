package com.demo.sys.config.web;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter4;
import com.demo.common.model.vo.Constants;
import com.demo.common.mybatis.*;
import com.demo.common.utils.IpUtils;
import com.demo.common.utils.JwtToken;
import com.demo.domain.model.sys.dto.TSysRoleDto;
import com.demo.domain.model.sys.dto.TSysUserDto;
import com.demo.domain.model.sys.vo.TSysRoleVO;
import com.demo.domain.model.sys.vo.TSysUserVO;
import com.demo.sys.config.vo.LoginUserVo;
import com.demo.sys.service.ITOrgDictService;
import com.demo.sys.service.ITRoleUserService;
import com.demo.sys.service.ITSysRoleService;
import com.demo.sys.service.ITSysUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.DigestUtils;
import org.springframework.util.unit.DataSize;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.MultipartConfigElement;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Spring MVC 配置
 */
@Configuration
@Slf4j
public class WebMvcConfigurer extends WebMvcConfigurerAdapter {

    @Value("${spring.profiles.active}")
    private String env;//当前激活的配置文件
    @Value("${spring.file.baseRootDir}")
    private String baseRootDir;

    @Resource
    private ITSysUserService tSysUserService;

    @Resource
    private ITRoleUserService iTRoleUserService;

    @Resource
    private ITSysRoleService iTSysRoleService;

    @Resource
    private ITOrgDictService iTOrgDictService;

    /**
     * 登录session key
     */
    public final static String SESSION_KEY = "user";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("file:" + baseRootDir);
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        super.addResourceHandlers(registry);
    }

    /**
     * 文件上传临时路径
     */
    @Bean
    MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        // factory.setLocation(importBaseRootDir);
        factory.setMaxFileSize(DataSize.ofMegabytes(9000L));//单个文件大小9000mb
        factory.setMaxRequestSize(DataSize.ofMegabytes(9000L));//设置总上传数据大小9000mb
        return factory.createMultipartConfig();
    }

    //使用阿里 FastJson 作为JSON MessageConverter
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        FastJsonHttpMessageConverter4 converter = new FastJsonHttpMessageConverter4();
        FastJsonConfig config = new FastJsonConfig();
        config.setSerializerFeatures(SerializerFeature.WriteMapNullValue,//保留空的字段
                SerializerFeature.WriteNullStringAsEmpty,//String null -> ""
                SerializerFeature.WriteNullNumberAsZero);//Number null -> 0
        converter.setFastJsonConfig(config);
        converter.setDefaultCharset(Charset.forName("UTF-8"));
        converters.add(converter);
    }


    //统一异常处理
    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
        exceptionResolvers.add(new HandlerExceptionResolver() {
            @Override
            public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e) {
                Result result = new Result();
                if (handler instanceof HandlerMethod) {
                    HandlerMethod handlerMethod = (HandlerMethod) handler;

                    if (e instanceof ServiceException) {//业务失败的异常，如“账号或密码错误”
                        Head head = new Head(e.getMessage(), ResultCode.FAIL, "false");
                        result.setHead(head);
                        log.info(e.getMessage());
                    } else {
                        Head head = new Head("接口 [" + request.getRequestURI() + "] 内部错误，请联系管理员", ResultCode.INTERNAL_SERVER_ERROR, "false");
                        result.setHead(head);
                        String message = String.format("接口 [%s] 出现异常，方法：%s.%s，异常摘要：%s",
                                request.getRequestURI(),
                                handlerMethod.getBean().getClass().getName(),
                                handlerMethod.getMethod().getName(),
                                e.getMessage());
                        log.error(message, e);
                    }
                } else {
                    if (e instanceof NoHandlerFoundException) {

                        Head head = new Head("接口 [" + request.getRequestURI() + "] 不存在", ResultCode.NOT_FOUND, "false");

                        result.setHead(head);
                    } else {
                        Head head = new Head(e.getMessage(), ResultCode.INTERNAL_SERVER_ERROR, "false");

                        result.setHead(head);
                        log.error(e.getMessage(), e);
                    }
                }
                responseResult(response, result);
                return new ModelAndView();
            }
        });
    }

    private void responseResult(HttpServletResponse response, Result result) {

        response.setCharacterEncoding("UTF-8");
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Content-type", "application/json;charset=UTF-8");
        response.setStatus(200);

        try {
            response.getWriter().write(JSON.toJSONString(result));
            response.getWriter().flush();
            response.getWriter().close();
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
    }

    //解决跨域问题（经过和前端测试未生效，前端以处理）
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**");
    }

    @Bean
    public SecurityInterceptor getSecurityInterceptor() {
        return new SecurityInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration addInterceptor = registry.addInterceptor(getSecurityInterceptor());

        // 排除配置
        addInterceptor.excludePathPatterns("/error");
        addInterceptor.excludePathPatterns("/file/**");
//      上线可以注掉(Swagger)
        addInterceptor.excludePathPatterns("/swagger-ui.html/**");
        addInterceptor.excludePathPatterns("/webjars/springfox-swagger-ui/**");
        addInterceptor.excludePathPatterns("/swagger-resources/**");
        addInterceptor.excludePathPatterns("/v2/api-docs");

        addInterceptor.excludePathPatterns("/sys/auth/tsysuser/login");
        addInterceptor.excludePathPatterns("/sys/noAuth/**");

        // 拦截配置
        addInterceptor.addPathPatterns("/**");
    }

    private class SecurityInterceptor extends HandlerInterceptorAdapter {

        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
            try {
                String url = request.getRequestURI();
                log.info("访问的url地址：" + url);
                if (url.indexOf("/noAuth/") > 0) {
                    return true;
                } else {
                    if (request.getMethod().contains("OPTIONS")) {
                        Result result = new Result();
                        Head head = new Head("", ResultCode.SUCCESS, "true");
                        result.setHead(head);
                        responseResult(response, result);
                        return true;
                    }
                    String token = request.getHeader("Authorization"); //拿到授权信息
                    boolean pass = JwtToken.validToken(token);
                    if (pass) {
                        //获取当前登录账户
                        TSysUserVO tSysUserVO = new TSysUserVO();
                        tSysUserVO.setUserAccount(LoginUserVo.getUserName(token));
                        TSysUserDto user = tSysUserService.selectByVo(tSysUserVO);

                        //验证当前登录用户是否存在
                        if (null != user) {
                            //角色IDS
                            List<String> roleIds = user.getSysRoleDtoList().stream().map(TSysRoleDto::getId).collect(Collectors.toList());

                            //根据角色ids递归获取角色信息
                            boolean flag = (null != roleIds && roleIds.size() > 0);
                            if (flag) {
                                TSysRoleVO tSysRoleVO = new TSysRoleVO();
                                tSysRoleVO.setRoleUuidList(roleIds);
                                user.setSysRoleDtoList(iTSysRoleService.findListByDto(tSysRoleVO));
                            }

                            request.setAttribute(Constants.USERID, user.getId());
                            request.setAttribute(Constants.USERNAME, user.getUserAccount());
                            request.setAttribute(Constants.ROLEIDS, JSON.toJSONString(roleIds));
                            request.setAttribute(Constants.USER, user);
                            request.setAttribute(Constants.ORGUUID, user.getOrgUuid());
                            return true;
                        } else {
                            log.warn("用户不存在，请求接口：{}，请求IP：{}，请求参数：{}", request.getRequestURI(), IpUtils.getClientAddress(request), JSON.toJSONString(request.getParameterMap()));
                            Result result = ResultGenerator.genUnauthorizedResult("用户不存在");
                            responseResult(response, result);
                            return false;
                        }
                    } else {
                        //Tocken认证失败跳转至登录页面
                        log.warn("签名认证失败，请求接口：{}，请求IP：{}，请求参数：{}", request.getRequestURI(), IpUtils.getClientAddress(request), JSON.toJSONString(request.getParameterMap()));
                        Result result = ResultGenerator.genUnauthorizedResult("签名认证失败");
                        responseResult(response, result);
                        return false;
                    }
                }
            } catch (Exception e) {
                log.error("拦截出现异常，", e);
                return false;
            }
        }
    }

    /**
     * 一个简单的签名认证，规则：
     * 1. 将请求参数按ascii码排序
     * 2. 拼接为a=value&b=value...这样的字符串（不包含sign）
     * 3. 混合密钥（secret）进行md5获得签名，与请求的签名进行比较
     */
    @SuppressWarnings("unused")
    private boolean validateSign(HttpServletRequest request) {
        String requestSign = request.getParameter("sign");//获得请求签名，如sign=19e907700db7ad91318424a97c54ed57
        if (StringUtils.isEmpty(requestSign)) {
            return false;
        }
        List<String> keys = new ArrayList<String>(request.getParameterMap().keySet());
        keys.remove("sign");//排除sign参数
        Collections.sort(keys);//排序

        StringBuilder sb = new StringBuilder();
        for (String key : keys) {
            sb.append(key).append("=").append(request.getParameter(key)).append("&");//拼接字符串
        }
        String linkString = sb.toString();
        linkString = StringUtils.substring(linkString, 0, linkString.length() - 1);//去除最后一个'&'

        String secret = "Potato";//密钥，自己修改
        String sign = DigestUtils.md5DigestAsHex((linkString + secret).getBytes());//混合密钥md5

        return StringUtils.equals(sign, requestSign);//比较
    }

//    private String getIpAddress(HttpServletRequest request) {
//        String ip = request.getHeader("x-forwarded-for");
//        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//            ip = request.getHeader("Proxy-Client-IP");
//        }
//        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//            ip = request.getHeader("WL-Proxy-Client-IP");
//        }
//        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//            ip = request.getHeader("HTTP_CLIENT_IP");
//        }
//        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
//        }
//        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//            ip = request.getRemoteAddr();
//        }
//        // 如果是多级代理，那么取第一个ip为客户端ip
//        if (ip != null && ip.indexOf(",") != -1) {
//            ip = ip.substring(0, ip.indexOf(",")).trim();
//        }
//        return ip;
//    }

}
