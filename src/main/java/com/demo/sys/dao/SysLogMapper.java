package com.demo.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.demo.domain.model.sys.entity.SysLog;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface SysLogMapper extends BaseMapper<SysLog> {
    /**
     * 根据表名和主键查询数据
     * @param tableName 表名
     * @param id 主键
     * @return
     */
    List<Map> selectTableNameByList(@Param("tableName") String tableName, @Param("id") String id);
}