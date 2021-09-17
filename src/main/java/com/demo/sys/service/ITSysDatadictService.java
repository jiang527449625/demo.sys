package com.demo.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.demo.domain.model.sys.entity.TSysDatadict;
import com.demo.domain.model.sys.vo.TSysDatadictVO;

import java.util.List;

/**
 * Created by jky on 2021/08/10.
 */
public interface ITSysDatadictService extends IService<TSysDatadict> {

    int save(TSysDatadictVO tSysDatadictVO);

    int deleteByModel(TSysDatadictVO tSysDatadictVO);

    int update(TSysDatadictVO tSysDatadictVO);

    TSysDatadict findByModel(TSysDatadictVO tSysDatadictVO);

    List<TSysDatadict> findModelList(TSysDatadictVO tSysDatadictVO);

    IPage<TSysDatadict> selectByPage(TSysDatadictVO tSysDatadictVO, Integer page, Integer size);
}
