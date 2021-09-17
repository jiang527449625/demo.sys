package com.demo.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.demo.domain.model.sys.entity.TSysDatadictType;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface TSysDatadictTypeMapper extends BaseMapper<TSysDatadictType> {

    List<TSysDatadictType> findModelList(TSysDatadictType tSysDatadictType);

    IPage<TSysDatadictType> findModelList(IPage<TSysDatadictType> iPage, @Param("tvo") TSysDatadictType tSysDatadictType);
}