package com.demo.sys.config.mybatisplus;

import com.demo.common.model.enums.EntityDataAopEnum;
import com.demo.common.service.ModelMethodChannelService;
import com.demo.common.service.ModelMethodFactory;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;


@Aspect
@Component
@Slf4j
public class MybatisParamAop {
    @Pointcut("execution(* com.demo.*.dao.*.update*(..))")
    public void excudeUpdateService(){}

    @Pointcut("execution(* com.demo.*.dao.*.insert*(..))")
    public void excudeAddService(){}

    @Around("excudeUpdateService()")
    public Object updateAround(ProceedingJoinPoint thisJoinPoint){
        Object obj = null;
        try {
            Object[] args = thisJoinPoint.getArgs();
            if(args != null && args.length > 0) {
                Object argument = args[0];
                updateModelFile(argument, EntityDataAopEnum.TYPE_UPDATE.getCode());
                obj = thisJoinPoint.proceed(args);
            }
        } catch (Throwable throwable) {
            log.error("调用update dao方法之前的数据补全环绕拦截异常！！",throwable);
        }
        return obj;
    }

    @Around("excudeAddService()")
    public Object addAround(ProceedingJoinPoint thisJoinPoint){
        Object obj = null;
        try {
            Object[] args = thisJoinPoint.getArgs();
            if(args != null && args.length > 0) {
                Object argument = args[0];
                updateModelFile(argument, EntityDataAopEnum.TYPE_INSERT.getCode());
                obj = thisJoinPoint.proceed(args);
            }
        } catch (Throwable throwable) {
            log.error("调用insert dao方法之前的数据补全环绕拦截异常！！",throwable);
        }
        return obj;
    }

    /**
     * 数据补全
     * @param model
     * @param type
     * @return
     */
    private void updateModelFile(Object model, String type){
        try{
            Class<?> tClass = model.getClass();
            //得到所有属性
            Field[] fields = tClass.getDeclaredFields();

            for(Field field : fields){
                field.setAccessible(true);
                String name = field.getName().replaceFirst(field.getName().substring(0, 1), field.getName().substring(0, 1).toUpperCase());
                Method method = tClass.getMethod("set"+name,new Class[]{field.getType()});
                Method getMethod = tClass.getMethod("get"+name);
                Object valObj = getMethod.invoke(model);
                if(valObj == null){
                    ModelMethodFactory modelMethodFactory = new ModelMethodFactory();
                    ModelMethodChannelService channelService =  modelMethodFactory.buildService(field.getName(),type);
                    if(channelService != null){
                        channelService.executionMethod(field,method,model);
                    }
                }
            }
        }catch(Exception e){
            log.info("AbstractService - 没有这个属性");
        }
    }
}
