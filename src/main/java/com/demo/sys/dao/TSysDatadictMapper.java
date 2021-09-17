package com.demo.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.demo.domain.model.sys.entity.TSysDatadict;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface TSysDatadictMapper extends BaseMapper<TSysDatadict> {

    List<TSysDatadict> findModelList(TSysDatadict tSysDatadict);

    IPage<TSysDatadict> findModelList(IPage<TSysDatadict> iPage, @Param("tvo") TSysDatadict tSysDatadict);
}