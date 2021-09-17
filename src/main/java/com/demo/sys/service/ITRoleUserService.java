package com.demo.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.demo.domain.model.sys.dto.TSysUserDto;
import com.demo.domain.model.sys.entity.TRoleUser;
import com.demo.domain.model.sys.entity.TSysUser;
import com.demo.domain.model.sys.vo.TRoleUserVO;

import java.util.List;

/**
 * Created by jky on 2021/08/10.
 */
public interface ITRoleUserService extends IService<TRoleUser> {

    int save(TRoleUserVO tRoleUserVO);

    int deleteByModel(TRoleUserVO tRoleUserVO);

    int update(TRoleUserVO tRoleUserVO);

    TRoleUser findByModel(TRoleUserVO tRoleUserVO);

    List<TRoleUser> findModelList(TRoleUserVO tRoleUserVO);

    IPage<TRoleUser> selectByPage(TRoleUserVO tRoleUserVO, Integer page, Integer size);

    /**
     * @Description: 查询当前角色下对应的用户信息
     * @Author: yujunhong@aliyun.com
     * @Date: 2021/8/19 14:58
     */
    List<TSysUserDto> getRoleUserList(TRoleUserVO tRoleUserVO);

    /**
     * @Description: 更新角色下的用户信息
     * @Author: yujunhong@aliyun.com
     * @Date: 2021/8/19 16:58
     */
    int updateRoleUsers(TRoleUserVO tRoleUserVO);

    /**
     * @Description: 移除角色下的用户信息
     * @Author: yujunhong@aliyun.com
     * @Date: 2021/8/19 16:58
     */
    int deleteRoleUsers(TRoleUserVO tRoleUserVO);

    /**
     * @Description: 查询选定角色下未选中的用户集合
     * @Author: yujunhong@aliyun.com
     * @Date: 2021/8/23 10:08
     */
    List<TSysUser> getUncheckedList(TRoleUserVO tRoleUserVO);
}
