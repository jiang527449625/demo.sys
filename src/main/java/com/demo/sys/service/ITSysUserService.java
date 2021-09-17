package com.demo.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.demo.domain.model.sys.dto.TSysUserDto;
import com.demo.domain.model.sys.entity.TSysUser;
import com.demo.domain.model.sys.vo.TSysUserVO;

import java.util.List;

/**
 * @Description: 用户管理
 * @Author: yujunhong@aliyun.com
 * @Date: 2021/8/16 15:17
 */
public interface ITSysUserService extends IService<TSysUser> {

    int save(TSysUserVO tSysUserVO);

    int deleteByModel(TSysUserVO tSysUserVO);

    int update(TSysUserVO tSysUserVO);

    TSysUser findByModel(TSysUserVO tSysUserVO);

    List<TSysUser> findModelList(TSysUserVO tSysUserVO);

    IPage<TSysUser> selectByPage(TSysUserVO tSysUserVO, Integer page, Integer size);

    IPage<TSysUserDto> selectDtoByPage(TSysUserVO tSysUserVO, Integer page, Integer size);

    TSysUserDto selectByVo(TSysUserVO tSysUserVO);

    /**
     * @Description: 查询选定角色下未选中的用户集合
     * @Author: yujunhong@aliyun.com
     * @Date: 2021/8/20 9:56
     */
    List<TSysUser> findUncheckedList(TSysUserVO tSysUserVO);

    /**
     * @Description: 用户账号唯一性校验
     * @Author: yujunhong@aliyun.com
     * @Date: 2021/8/27 10:49
     */
    int userAccountCheck(TSysUserVO tSysUserVO);
}
