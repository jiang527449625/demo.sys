package com.demo.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.demo.domain.model.sys.dto.SysCustomerFormDto;
import com.demo.domain.model.sys.entity.SysCustomerForm;
import com.demo.domain.model.sys.vo.SysCustomerFormVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface SysCustomerFormMapper extends BaseMapper<SysCustomerForm> {

    List<SysCustomerFormDto> findDtoModelList(SysCustomerFormVO sysCustomerFormVO);

    IPage<SysCustomerFormDto> findDtoModelList(IPage<SysCustomerFormVO> iPage, @Param("tvo") SysCustomerFormVO sysCustomerFormVO);
}