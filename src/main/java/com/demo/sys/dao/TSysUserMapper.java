package com.demo.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.demo.domain.model.sys.dto.TSysUserDto;
import com.demo.domain.model.sys.entity.TSysUser;
import com.demo.domain.model.sys.vo.TSysUserVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Description: 用户管理
 * @Author: yujunhong@aliyun.com
 * @Date: 2021/8/16 15:17
 */
@Component
public interface TSysUserMapper extends BaseMapper<TSysUser> {

    /**
     * @Description: 查询VO返回DTO
     * @Author: yujunhong@aliyun.com
     * @Date: 2021/8/19 10:11
     */
    TSysUserDto selectByVo(TSysUserVO tSysUserVO);

    /**
     * @Description: 查询DTO
     * @Author: yujunhong@aliyun.com
     * @Date: 2021/8/19 10:11
     */
    List<TSysUserDto> findModeListByDto(TSysUserDto tSysUserDto);

    /**
     * @Description: 查询选定角色下未选中的用户集合
     * @Author: yujunhong@aliyun.com
     * @Date: 2021/8/20 9:56
     */
    List<TSysUser> findUncheckedList(TSysUser tSysUser);

    /**
     * @Description: 获取单一查询
     * @Author: yujunhong@aliyun.com
     * @Date: 2021/8/23 16:57
     */
    TSysUser findOne(TSysUser tSysUser);

    List<TSysUserDto> findDtoModelList(TSysUserVO tSysUserVO);

    IPage<TSysUserDto> findDtoModelList(IPage<TSysUserVO> iPage, @Param("tvo") TSysUserVO tSysUserVO);
}