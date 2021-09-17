package com.demo.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.demo.domain.model.sys.dto.TSysParamDto;
import com.demo.domain.model.sys.entity.TSysParam;
import com.demo.domain.model.sys.vo.TSysParamVO;

import java.util.List;

/**
 * Created by jky on 2021/08/10.
 */
public interface ITSysParamService extends IService<TSysParam> {

    int save(TSysParamVO tSysParamVO);

    int deleteByModel(TSysParamVO tSysParamVO);

    int update(TSysParamVO tSysParamVO);

    TSysParam findByModel(TSysParamVO tSysParamVO);

    List<TSysParam> findModelList(TSysParamVO tSysParamVO);

    IPage<TSysParam> selectByPage(TSysParamVO tSysParamVO, Integer page, Integer size);

    IPage<TSysParamDto> selectDtoByPage(TSysParamVO tSysParamVO, Integer page, Integer size);
}
