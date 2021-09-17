package com.demo.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.demo.domain.model.sys.dto.SysCustomerScreenFormDto;
import com.demo.domain.model.sys.dto.TSysUserDto;
import com.demo.domain.model.sys.entity.SysCustomerScreenForm;
import com.demo.domain.model.sys.vo.SysCustomerScreenFormVO;
import com.demo.domain.model.sys.vo.TSysUserVO;

import java.util.List;

/**
 * Created by jky on 2021/08/24.
 */
public interface ISysCustomerScreenFormService extends IService<SysCustomerScreenForm> {

    int save(SysCustomerScreenFormVO sysCustomerScreenFormVO);

    int deleteByModel(SysCustomerScreenFormVO sysCustomerScreenFormVO);

    int update(SysCustomerScreenFormVO sysCustomerScreenFormVO);

    int updateDelModel(String[] ids);

    SysCustomerScreenForm findByModel(SysCustomerScreenFormVO sysCustomerScreenFormVO);

    List<SysCustomerScreenForm> findModelList(SysCustomerScreenFormVO sysCustomerScreenFormVO);

    IPage<SysCustomerScreenForm> selectByPage(SysCustomerScreenFormVO sysCustomerScreenFormVO, Integer page, Integer size);

    IPage<SysCustomerScreenFormDto> selectDtoByPage(SysCustomerScreenFormVO sysCustomerScreenFormVO, Integer page, Integer size);
}
