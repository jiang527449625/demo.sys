package com.demo.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.demo.domain.model.sys.dto.TSysRoleDto;
import com.demo.domain.model.sys.entity.TSysRole;
import com.demo.domain.model.sys.vo.TSysRoleVO;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * @Description: 角色管理
 * @Author: yujunhong@aliyun.com
 * @Date: 2021/8/16 15:17
 */
@Component
public interface TSysRoleMapper extends BaseMapper<TSysRole> {

    /**
     * @Description: 向下递归获取所有的角色信息
     * @Author: yujunhong@aliyun.com
     * @Date: 2021/8/12 14:32
     */
    List<TSysRoleDto> selectRolesByDownward(TSysRoleVO tSysRoleVO);

    /**
     * @Description: 向上递归获取根节点
     * @Author: yujunhong@aliyun.com
     * @Date: 2021/8/12 14:32
     */
    TSysRoleDto selectRolesByUpward(TSysRoleVO tSysRoleVO);

    /**
     * @Description: 获取当前节点的上一节点数据
     * @Author: yujunhong@aliyun.com
     * @Date: 2021/8/12 14:32
     */
    TSysRoleDto selectRolesWithUpperLevel(TSysRoleVO tSysRoleVO);

    /**
     * @Description: 单行查询
     * @Author: yujunhong@aliyun.com
     * @Date: 2021/8/12 14:32
     */
    TSysRole findOne(TSysRole tSysRole);

    /**
     * @Description: 获取树形角色列表
     * @Author: yujunhong@aliyun.com
     * @Date: 2021/8/12 14:32
     */
    List<TSysRoleDto> selectByDto(TSysRoleVO tSysRoleVO);

    /**
     * @Description: 获取树形角色列表
     * @Author: jky
     * @Date: 2021/8/12 14:32
     */
    IPage<TSysRoleDto> selectByDto(IPage<TSysRoleVO> ipage, TSysRoleVO tSysRoleVO);
}