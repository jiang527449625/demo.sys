package com.demo.sys.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.common.model.enums.DeleteStatusEnum;
import com.demo.common.model.enums.EnableStatusEnum;
import com.demo.common.utils.SnowflakeIdUtil;
import com.demo.domain.model.sys.dto.TSysRoleDto;
import com.demo.domain.model.sys.dto.TSysUserDto;
import com.demo.domain.model.sys.entity.TRoleUser;
import com.demo.domain.model.sys.entity.TSysRole;
import com.demo.domain.model.sys.entity.TSysUser;
import com.demo.domain.model.sys.vo.TRoleUserVO;
import com.demo.domain.model.sys.vo.TSysRoleVO;
import com.demo.sys.dao.TRoleUserMapper;
import com.demo.sys.dao.TSysRoleMapper;
import com.demo.sys.dao.TSysUserMapper;
import com.demo.sys.service.ITRoleUserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by jky on 2021/08/10.
 */
@Service
@Transactional
public class TRoleUserServiceImpl extends ServiceImpl<TRoleUserMapper, TRoleUser> implements ITRoleUserService {
    @Resource
    private TRoleUserMapper tRoleUserMapper;

    @Resource
    private TSysUserMapper tSysUserMapper;

    @Resource
    private TSysRoleMapper tSysRoleMapper;

    @Override
    public int save(TRoleUserVO tRoleUserVO) {
        TRoleUser tRoleUser = new TRoleUser();
        BeanUtils.copyProperties(tRoleUserVO, tRoleUser);
        return tRoleUserMapper.insert(tRoleUser);
    }

    @Override
    public int deleteByModel(TRoleUserVO tRoleUserVO) {
        TRoleUser tRoleUser = new TRoleUser();
        BeanUtils.copyProperties(tRoleUserVO, tRoleUser);
        return tRoleUserMapper.delete(Wrappers.<TRoleUser>lambdaUpdate(tRoleUser));
    }

    @Override
    public int update(TRoleUserVO tRoleUserVO) {
        TRoleUser tRoleUser = new TRoleUser();
        BeanUtils.copyProperties(tRoleUserVO, tRoleUser);
        return tRoleUserMapper.updateById(tRoleUser);
    }

    @Override
    public TRoleUser findByModel(TRoleUserVO tRoleUserVO) {
        TRoleUser tRoleUser = new TRoleUser();
        BeanUtils.copyProperties(tRoleUserVO, tRoleUser);
        return tRoleUserMapper.selectOne(Wrappers.<TRoleUser>lambdaQuery(tRoleUser));
    }

    @Override
    public List<TRoleUser> findModelList(TRoleUserVO tRoleUserVO) {
        TRoleUser tRoleUser = new TRoleUser();
        BeanUtils.copyProperties(tRoleUserVO, tRoleUser);
        return tRoleUserMapper.selectList(Wrappers.<TRoleUser>lambdaQuery(tRoleUser));
    }

    @Override
    public IPage<TRoleUser> selectByPage(TRoleUserVO tRoleUserVO, Integer page, Integer size) {
        IPage<TRoleUser> iPage = new Page<>(page, size);
        TRoleUser tRoleUser = new TRoleUser();
        BeanUtils.copyProperties(tRoleUserVO, tRoleUser);
        return tRoleUserMapper.selectPage(iPage, Wrappers.<TRoleUser>lambdaQuery(tRoleUser));
    }

    /**
     * @Description: 查询当前角色下对应的用户信息
     * @Author: yujunhong@aliyun.com
     * @Date: 2021/8/19 14:58
     */
    @Override
    public List<TSysUserDto> getRoleUserList(TRoleUserVO tRoleUserVO) {
        TRoleUser tRoleUser = new TRoleUser();
        BeanUtils.copyProperties(tRoleUserVO, tRoleUser);
        //获取当前角色对应的角色信息
        TSysRole tSysRole = tSysRoleMapper.selectById(tRoleUserVO.getRoleUuid());
        //获取当前节点下的所有角色信息
        return getRoleUserList(tSysRole);
    }

    /**
     * @Description: 获取当前节点下的角色信息
     * @Author: yujunhong@aliyun.com
     * @Date: 2021/8/19 19:25
     */
    private List<TSysUserDto> getRoleUserList(TSysRole tSysRole) {
        //要返回的用户信息
        List<TSysUserDto> userDtoList = new ArrayList<>();
        //遍历用户-角色信息，获取对应的用户信息
        if (null != tSysRole) {
            //从角色-用户表中获取角色-用户信息
            List<TRoleUser> roleUserList = tRoleUserMapper.selectList(Wrappers.<TRoleUser>lambdaQuery().eq(TRoleUser::getRoleUuid, tSysRole.getId()));
            if (null != roleUserList && roleUserList.size() > 0) {
                //遍历用户表，从用户表中获取信息
                roleUserList.stream().forEach(args -> {
                    TSysUserDto tSysUserDto = new TSysUserDto();
                    //设置主键
                    tSysUserDto.setId(args.getUserUuid());
                    //未删除
                    tSysUserDto.setDelFlag(DeleteStatusEnum.NO_ENUM.getCode());
                    List<TSysUserDto> userList = tSysUserMapper.findModeListByDto(tSysUserDto);
                    if (null != userList && userList.size() > 0) {
                        userDtoList.addAll(userList);
                    }
                });
            }
        }
        return userDtoList;
    }

    /**
     * @Description: 更新角色下的用户信息
     * @Author: yujunhong@aliyun.com
     * @Date: 2021/8/19 16:58
     */
    @Override
    public int updateRoleUsers(TRoleUserVO tRoleUserVO) {
        //要返回的数据
        int count = 0;
        if (StringUtils.isNotBlank(tRoleUserVO.getRoleUuid())) {
            //1、以当前角色为删除条件，删除角色-用户表中数据信息
            tRoleUserMapper.delete(Wrappers.<TRoleUser>lambdaUpdate().eq(TRoleUser::getRoleUuid, tRoleUserVO.getRoleUuid()));
            //2、将当前的角色-用户信息保存至角色-用户表中
            String[] userUuids = tRoleUserVO.getUserUuid().split(",");
            if (userUuids.length > 0) {
                for (String userUuid : userUuids) {
                    TRoleUser roleUser = new TRoleUser();
                    roleUser.setId(SnowflakeIdUtil.newStringId());
                    roleUser.setRoleUuid(tRoleUserVO.getRoleUuid());
                    roleUser.setUserUuid(userUuid);
                    int insert = tRoleUserMapper.insert(roleUser);
                    if (insert > 0) {
                        count += 1;
                    }
                }
            }
        }
        return count;
    }

    /**
     * @Description: 移除角色下的用户信息
     * @Author: yujunhong@aliyun.com
     * @Date: 2021/8/19 16:58
     */
    @Override
    public int deleteRoleUsers(TRoleUserVO tRoleUserVO) {
        //要返回的数据
        int count = 0;
        TRoleUser tRoleUser = new TRoleUser();
        tRoleUser.setRoleUuid(tRoleUserVO.getRoleUuid());
        if (StringUtils.isNotBlank(tRoleUser.getRoleUuid())) {
            String[] userUuids = tRoleUserVO.getUserUuid().split(",");
            if (userUuids.length > 0) {
                for (String userUuid : userUuids) {
                    int delete = tRoleUserMapper.delete(Wrappers.<TRoleUser>lambdaUpdate()
                            .eq(TRoleUser::getRoleUuid, tRoleUser.getRoleUuid())
                            .eq(TRoleUser::getUserUuid, userUuid));
                    if (delete > 0) {
                        count += 1;
                    }
                }
            }
        }
        return count;
    }

    /**
     * @Description: 查询选定角色下未选中的用户集合
     * @Author: yujunhong@aliyun.com
     * @Date: 2021/8/23 10:08
     */
    @Override
    public List<TSysUser> getUncheckedList(TRoleUserVO tRoleUserVO) {
        //根据角色获取用户-角色信息
        TSysRole tSysRole = tSysRoleMapper.selectById(tRoleUserVO.getRoleUuid());
        //如果是顶级父节点,则返回所有的数据
        //否则返回当前顶级父节点包括顶级父节点外其他子节点尚未被选择的用户
        //先判断当前的节点是否是父节点
        boolean flag = "0".equals(tSysRole.getParentUuid());
        if (!flag) {
            return findLeafUncheckedUserList(tRoleUserVO);
        } else {
            return findUncheckedList(tRoleUserVO);
        }
    }

    /**
     * @Description: 获取尚未被子节点选中的父节点用户信息
     * @Author: yujunhong@aliyun.com
     * @Date: 2021/8/23 14:23
     */
    private List<TSysUser> findLeafUncheckedUserList(TRoleUserVO tRoleUserVO) {
        List<TSysUser> userList = new ArrayList<>();
        //获取当前顶级父节点及其子节点
        TSysRoleVO tSysRoleVO = new TSysRoleVO();
        tSysRoleVO.setId(tRoleUserVO.getRoleUuid());
        //向上递归获取选定角色的根节点
        TSysRoleDto tSysRoleDto = tSysRoleMapper.selectRolesByUpward(tSysRoleVO);
        if (null != tSysRoleDto) {
            //获取根节点的所有用户信息
            List<TRoleUser> uncheckedUserList = tRoleUserMapper.selectList(Wrappers.<TRoleUser>lambdaQuery().eq(TRoleUser::getRoleUuid, tSysRoleDto.getId()));
            if (null != uncheckedUserList && uncheckedUserList.size() > 0) {
                uncheckedUserList.stream().forEach(t -> {
                    TSysUser sysUser = tSysUserMapper.selectOne(Wrappers.<TSysUser>lambdaQuery().eq(TSysUser::getId, t.getUserUuid()));
                    if (null != sysUser) {
                        userList.add(sysUser);
                    }
                });
            }
        }
        //向下递归获取所有的子节点
        TSysRoleVO sysRoleVO = new TSysRoleVO();
        sysRoleVO.setId(tSysRoleDto.getId());
        List<TSysRoleDto> tSysRoleDtoList = tSysRoleMapper.selectRolesByDownward(sysRoleVO);
        List<TRoleUser> leafRoleUserList = new ArrayList<>();
        //遍历子节点信息，从角色-用户表中获取子节点对应的用户信息
        tSysRoleDtoList.stream().forEach(args -> {
            leafRoleUserList.addAll(tRoleUserMapper.selectList(Wrappers.<TRoleUser>lambdaQuery().eq(TRoleUser::getRoleUuid, args.getId())));
        });
        if (null != leafRoleUserList && leafRoleUserList.size() > 0) {
            leafRoleUserList.stream().forEach(t -> {
                TSysUser sysUser = tSysUserMapper.selectOne(Wrappers.<TSysUser>lambdaQuery().eq(TSysUser::getId, t.getUserUuid()));
                if (null != sysUser) {
                    userList.remove(sysUser);
                }
            });
        }
        return userList;
    }

    /**
     * @Description: 在所有用户中获取父节点除了已选用户外的其他用户
     * @Author: yujunhong@aliyun.com
     * @Date: 2021/8/23 14:24
     */
    private List<TSysUser> findUncheckedList(TRoleUserVO tRoleUserVO) {
        TSysUser tSysUser = new TSysUser();
        //主键
        tSysUser.setId(tRoleUserVO.getUserUuid());
        //删除状态--未删除
        tSysUser.setDelFlag(DeleteStatusEnum.NO_ENUM.getCode());
        //启用状态--启用
        tSysUser.setWhetherEnable(EnableStatusEnum.YES_ENUM.getCode());
        //查询获取当前角色下除了已选用户外的其他用户
        return tSysUserMapper.findUncheckedList(tSysUser);
    }
}
