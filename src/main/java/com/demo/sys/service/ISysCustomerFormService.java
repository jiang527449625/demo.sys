package com.demo.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.demo.domain.model.sys.dto.SysCustomerFormDto;
import com.demo.domain.model.sys.entity.SysCustomerForm;
import com.demo.domain.model.sys.vo.SysCustomerFormVO;

import java.util.List;

/**
 * Created by jky on 2021/08/24.
 */
public interface ISysCustomerFormService extends IService<SysCustomerForm> {

    int save(SysCustomerFormVO sysCustomerFormVO);

    int deleteByModel(SysCustomerFormVO sysCustomerFormVO);

    int update(SysCustomerFormVO sysCustomerFormVO);

    int updateDelModel(String[] ids);

    SysCustomerForm findByModel(SysCustomerFormVO sysCustomerFormVO);

    List<SysCustomerForm> findModelList(SysCustomerFormVO sysCustomerFormVO);

    IPage<SysCustomerForm> selectByPage(SysCustomerFormVO sysCustomerFormVO, Integer page, Integer size);

    IPage<SysCustomerFormDto> selectDtoByPage(SysCustomerFormVO sysCustomerFormVO, Integer page, Integer size);
}
