package com.demo.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.demo.domain.model.sys.dto.SysCustomerScreenFormDto;
import com.demo.domain.model.sys.entity.SysCustomerScreenForm;
import com.demo.domain.model.sys.vo.SysCustomerScreenFormVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysCustomerScreenFormMapper extends BaseMapper<SysCustomerScreenForm> {

    List<SysCustomerScreenFormDto> findDtoModelList(SysCustomerScreenFormVO tSysParamVO);

    IPage<SysCustomerScreenFormDto> findDtoModelList(IPage<SysCustomerScreenFormVO> iPage, @Param("tvo") SysCustomerScreenFormVO tSysParamVO);
}