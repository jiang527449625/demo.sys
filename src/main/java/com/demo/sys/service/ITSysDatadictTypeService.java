package com.demo.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.demo.domain.model.sys.entity.TSysDatadictType;
import com.demo.domain.model.sys.vo.TSysDatadictTypeVO;

import java.util.List;

/**
 * Created by jky on 2021/08/10.
 */
public interface ITSysDatadictTypeService extends IService<TSysDatadictType> {

    int save(TSysDatadictTypeVO tSysDatadictTypeVO);

    int deleteByModel(TSysDatadictTypeVO tSysDatadictTypeVO);

    int update(TSysDatadictTypeVO tSysDatadictTypeVO);

    TSysDatadictType findByModel(TSysDatadictTypeVO tSysDatadictTypeVO);

    List<TSysDatadictType> findModelList(TSysDatadictTypeVO tSysDatadictTypeVO);

    IPage<TSysDatadictType> selectByPage(TSysDatadictTypeVO tSysDatadictTypeVO, Integer page, Integer size);
}
