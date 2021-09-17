package com.demo.sys.config;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.demo.common.config.LogAnno;
import com.demo.common.model.vo.Constants;
import com.demo.common.model.vo.SysLogAopVO;
import com.demo.common.utils.AopMethodUtil;
import com.demo.common.utils.StringUtil;
import com.demo.domain.model.sys.vo.SysLogVO;
import com.demo.sys.service.ISysLogService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

@Aspect
@Component
@Slf4j
public class LogAopConfig {

    @Autowired
    private ISysLogService sysLogService;

    /**
     * 环绕通知记录日志通过注解匹配到需要增加日志功能的方法
     *
     * @return Object
     */
    @Around("@annotation(com.demo.common.config.LogAnno)")
    public Object aroundAdvice(ProceedingJoinPoint pjp) {
        Object result = null;
        try {
            // 获取方法签名
            MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
            // 获取方法
            Method method = methodSignature.getMethod();
            // 获取方法上面的注解
            LogAnno logAnno = method.getAnnotation(LogAnno.class);

            SysLogAopVO logTableAopVO = AopMethodUtil.loginsert(pjp, logAnno);
            result = logTableAopVO.getResult();
            SysLogVO logTableVO = new SysLogVO();
            BeanUtils.copyProperties(logTableAopVO,logTableVO);
            //        获取原数据
            String id = "";
            for(Object obj : pjp.getArgs()){
                JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(obj));
                id = jsonObject.getString("id");
                if(!StringUtil.isEmpty(id)){
                    break;
                }
            }
//          新增不保存原来的值，修改和删除保存
            if(!logTableVO.getLogExplain().equals(Constants.AOPLOGINSERT)){
                List<Map> list = getTableNameByList(logAnno.logTableName(),id);
                if(list != null){
                    logTableVO.setLogResult(JSONArray.toJSONString(list));
                }
            }
            sysLogService.save(logTableVO);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return result;
    }

    public List<Map> getTableNameByList(String tableName, String id){
        List<Map> list = null;
        if(!StringUtil.isEmpty(id)){
            list = sysLogService.getTableNameByList(tableName,id);
        }
        return list;
    }
}
