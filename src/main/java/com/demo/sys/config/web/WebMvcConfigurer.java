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
 * Spring MVC ??????
 */
@Configuration
@Slf4j
public class WebMvcConfigurer extends WebMvcConfigurerAdapter {

    @Value("${spring.profiles.active}")
    private String env;//???????????????????????????
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
     * ??????session key
     */
    public final static String SESSION_KEY = "user";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("file:" + baseRootDir);
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        super.addResourceHandlers(registry);
    }

    /**
     * ????????????????????????
     */
    @Bean
    MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        // factory.setLocation(importBaseRootDir);
        factory.setMaxFileSize(DataSize.ofMegabytes(9000L));//??????????????????9000mb
        factory.setMaxRequestSize(DataSize.ofMegabytes(9000L));//???????????????????????????9000mb
        return factory.createMultipartConfig();
    }

    //???????????? FastJson ??????JSON MessageConverter
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        FastJsonHttpMessageConverter4 converter = new FastJsonHttpMessageConverter4();
        FastJsonConfig config = new FastJsonConfig();
        config.setSerializerFeatures(SerializerFeature.WriteMapNullValue,//??????????????????
                SerializerFeature.WriteNullStringAsEmpty,//String null -> ""
                SerializerFeature.WriteNullNumberAsZero);//Number null -> 0
        converter.setFastJsonConfig(config);
        converter.setDefaultCharset(Charset.forName("UTF-8"));
        converters.add(converter);
    }


    //??????????????????
    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
        exceptionResolvers.add(new HandlerExceptionResolver() {
            @Override
            public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e) {
                Result result = new Result();
                if (handler instanceof HandlerMethod) {
                    HandlerMethod handlerMethod = (HandlerMethod) handler;

                    if (e instanceof ServiceException) {//??????????????????????????????????????????????????????
                        Head head = new Head(e.getMessage(), ResultCode.FAIL, "false");
                        result.setHead(head);
                        log.info(e.getMessage());
                    } else {
                        Head head = new Head("?????? [" + request.getRequestURI() + "] ?????????????????????????????????", ResultCode.INTERNAL_SERVER_ERROR, "false");
                        result.setHead(head);
                        String message = String.format("?????? [%s] ????????????????????????%s.%s??????????????????%s",
                                request.getRequestURI(),
                                handlerMethod.getBean().getClass().getName(),
                                handlerMethod.getMethod().getName(),
                                e.getMessage());
                        log.error(message, e);
                    }
                } else {
                    if (e instanceof NoHandlerFoundException) {

                        Head head = new Head("?????? [" + request.getRequestURI() + "] ?????????", ResultCode.NOT_FOUND, "false");

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

    //????????????????????????????????????????????????????????????????????????
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

        // ????????????
        addInterceptor.excludePathPatterns("/error");
        addInterceptor.excludePathPatterns("/file/**");
//      ??????????????????(Swagger)
        addInterceptor.excludePathPatterns("/swagger-ui.html/**");
        addInterceptor.excludePathPatterns("/webjars/springfox-swagger-ui/**");
        addInterceptor.excludePathPatterns("/swagger-resources/**");
        addInterceptor.excludePathPatterns("/v2/api-docs");

        addInterceptor.excludePathPatterns("/sys/auth/tsysuser/login");
        addInterceptor.excludePathPatterns("/sys/noAuth/**");

        // ????????????
        addInterceptor.addPathPatterns("/**");
    }

    private class SecurityInterceptor extends HandlerInterceptorAdapter {

        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
            try {
                String url = request.getRequestURI();
                log.info("?????????url?????????" + url);
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
                    String token = request.getHeader("Authorization"); //??????????????????
                    boolean pass = JwtToken.validToken(token);
                    if (pass) {
                        //????????????????????????
                        TSysUserVO tSysUserVO = new TSysUserVO();
                        tSysUserVO.setUserAccount(LoginUserVo.getUserName(token));
                        TSysUserDto user = tSysUserService.selectByVo(tSysUserVO);

                        //????????????????????????????????????
                        if (null != user) {
                            //??????IDS
                            List<String> roleIds = user.getSysRoleDtoList().stream().map(TSysRoleDto::getId).collect(Collectors.toList());

                            //????????????ids????????????????????????
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
                            log.warn("?????????????????????????????????{}?????????IP???{}??????????????????{}", request.getRequestURI(), IpUtils.getClientAddress(request), JSON.toJSONString(request.getParameterMap()));
                            Result result = ResultGenerator.genUnauthorizedResult("???????????????");
                            responseResult(response, result);
                            return false;
                        }
                    } else {
                        //Tocken?????????????????????????????????
                        log.warn("????????????????????????????????????{}?????????IP???{}??????????????????{}", request.getRequestURI(), IpUtils.getClientAddress(request), JSON.toJSONString(request.getParameterMap()));
                        Result result = ResultGenerator.genUnauthorizedResult("??????????????????");
                        responseResult(response, result);
                        return false;
                    }
                }
            } catch (Exception e) {
                log.error("?????????????????????", e);
                return false;
            }
        }
    }

    /**
     * ???????????????????????????????????????
     * 1. ??????????????????ascii?????????
     * 2. ?????????a=value&b=value...??????????????????????????????sign???
     * 3. ???????????????secret?????????md5?????????????????????????????????????????????
     */
    @SuppressWarnings("unused")
    private boolean validateSign(HttpServletRequest request) {
        String requestSign = request.getParameter("sign");//????????????????????????sign=19e907700db7ad91318424a97c54ed57
        if (StringUtils.isEmpty(requestSign)) {
            return false;
        }
        List<String> keys = new ArrayList<String>(request.getParameterMap().keySet());
        keys.remove("sign");//??????sign??????
        Collections.sort(keys);//??????

        StringBuilder sb = new StringBuilder();
        for (String key : keys) {
            sb.append(key).append("=").append(request.getParameter(key)).append("&");//???????????????
        }
        String linkString = sb.toString();
        linkString = StringUtils.substring(linkString, 0, linkString.length() - 1);//??????????????????'&'

        String secret = "Potato";//?????????????????????
        String sign = DigestUtils.md5DigestAsHex((linkString + secret).getBytes());//????????????md5

        return StringUtils.equals(sign, requestSign);//??????
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
//        // ??????????????????????????????????????????ip????????????ip
//        if (ip != null && ip.indexOf(",") != -1) {
//            ip = ip.substring(0, ip.indexOf(",")).trim();
//        }
//        return ip;
//    }

}
