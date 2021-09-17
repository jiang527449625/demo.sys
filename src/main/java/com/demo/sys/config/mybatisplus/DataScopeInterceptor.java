package com.demo.sys.config.mybatisplus;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.extension.handlers.AbstractSqlParserHandler;
import com.demo.common.utils.StringUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Properties;

@Slf4j
@AllArgsConstructor
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
@Component
public class DataScopeInterceptor  extends AbstractSqlParserHandler implements Interceptor {
    private static final Logger logger= LoggerFactory.getLogger(DataScopeInterceptor.class);

    private DataSource dataSource;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = PluginUtils.realTarget(invocation.getTarget());
        MetaObject metaObject = SystemMetaObject.forObject(statementHandler);
        this.sqlParser(metaObject);
        // 获取操作类型 SELECT / insert / update...
        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");

        BoundSql boundSql = (BoundSql) metaObject.getValue("delegate.boundSql");
        // 执行的SQL语句
        String originalSql = boundSql.getSql();
        // SQL语句的参数
        Object parameterObject = boundSql.getParameterObject();
        // 根据操作类型去修改sql
        updateSql(mappedStatement,parameterObject,invocation,originalSql,metaObject,boundSql);
        return invocation.proceed();
    }

    /**
     * 生成拦截对象的代理
     *
     * @param target 目标对象
     * @return 代理对象
     */
    @Override
    public Object plugin(Object target) {
        if (target instanceof StatementHandler) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    /**
     * mybatis配置的属性
     *
     * @param properties mybatis配置的属性
     */
    @Override
    public void setProperties(Properties properties) {

    }

    /**
     * sql改写器
     * @param mappedStatement
     * @param parameterObject
     * @param invocation
     * @param originalSql
     * @param metaObject
     * @throws Throwable
     */
    private void updateSql(MappedStatement mappedStatement,Object parameterObject, Invocation invocation, String originalSql, MetaObject metaObject, BoundSql boundSql) throws Throwable {
        if (SqlCommandType.SELECT.equals(mappedStatement.getSqlCommandType())) {
//          查询SQL模式
            updateSelectOrg(parameterObject, originalSql, metaObject);
        }
    }

    /**
     * 查询方法拦截添加数据权限
     * @param parameterObject
     * @param originalSql
     * @param metaObject
     * @throws Throwable
     */
    private void updateSelectOrg(Object parameterObject, String originalSql, MetaObject metaObject) throws Throwable{
        if(parameterObject == null){
            return;
        }
        JSONObject jsonObject = null;
        try {
            jsonObject = JSONObject.parseObject(JSONObject.toJSONString(parameterObject));
        }catch (Exception e){
            return;
        }
        if(jsonObject != null && !jsonObject.containsKey("orgIdSQL")){
            return;
        }
        // 组装新的 sql
        StringBuffer newSql = new StringBuffer();
        newSql.append(originalSql);
        StringBuffer orgIds = new StringBuffer();
        String ids = String.valueOf(jsonObject.get("orgIdSQL"));
        if(StringUtil.isNotNull(ids)){
            String [] idArray = ids.split(",");
            for(String orgId : idArray){
                if(StringUtil.isNotNull(orgId)) {
                    orgIds.append("'").append(orgId).append("',");
                }
            }
        }
        if(orgIds.length() > 0){
            orgIds.deleteCharAt(orgIds.length() - 1);
        }
        newSql.append(" and exists (select t.id from (select t.id from T_ORG_DICT t ")
                .append(" where t.del_flag = 0 and t.whether_enable = 0 connect by PRIOR t.id = t.parent_uuid ")
                .append(" start with 1 = 1 and t.id in (")
                .append(orgIds).append(")) t where a.org_uuid = t.id) ");
        metaObject.setValue("delegate.boundSql.sql", newSql.toString());
    }
}