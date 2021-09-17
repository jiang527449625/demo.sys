package com.demo.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.demo.domain.model.sys.dto.TSysRoleDto;
import com.demo.domain.model.sys.entity.TSysRole;
import com.demo.domain.model.sys.vo.TSysRoleVO;

import java.util.List;


/**
 * @Description: 角色管理
 * @Author: yujunhong@aliyun.com
 * @Date: 2021/8/16 15:17
 */
public interface ITSysRoleService extends IService<TSysRole> {

    int save(TSysRoleVO tSysRoleVO);

    int deleteByModel(TSysRoleVO tSysRoleVO);

    int update(TSysRoleVO tSysRoleVO);

    TSysRole findByModel(TSysRoleVO tSysRoleVO);

    List<TSysRole> findModelList(TSysRoleVO tSysRoleVO);

    IPage<TSysRoleDto> selectByPage(TSysRoleVO tSysRoleVO, Integer page, Integer size);

    /**
     * @Description: 获取树形角色列表
     * @Author: yujunhong@aliyun.com
     * @Date: 2021/8/12 14:32
     */
    List<TSysRoleDto> findListByDto(TSysRoleVO tSysRoleVO);

    /**
     * @Description: 角色名称唯一性校验
     * @Author: yujunhong@aliyun.com
     * @Date: 2021/8/12 14:32
     */
    int roleNameCheck(TSysRoleVO tSysRoleVO);
}
