package com.demo.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.demo.domain.model.sys.dto.TSysParamDto;
import com.demo.domain.model.sys.entity.TSysParam;
import com.demo.domain.model.sys.vo.TSysParamVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface TSysParamMapper extends BaseMapper<TSysParam> {

    List<TSysParamDto> findDtoModelList(TSysParamVO tSysParamVO);

    IPage<TSysParamDto> findDtoModelList(IPage<TSysParamVO> iPage,@Param("tvo") TSysParamVO tSysParamVO);
}