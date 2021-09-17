package com.demo.sys.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.common.model.enums.DeleteStatusEnum;
import com.demo.common.utils.PinyinUtils;
import com.demo.common.utils.StringUtil;
import com.demo.common.utils.TreeDataUtils;
import com.demo.domain.model.sys.dto.TSysRoleDto;
import com.demo.domain.model.sys.entity.TSysRole;
import com.demo.domain.model.sys.vo.TSysRoleVO;
import com.demo.sys.dao.TSysRoleMapper;
import com.demo.sys.service.ITSysRoleService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


/**
 * @Description: 角色管理
 * @Author: yujunhong@aliyun.com
 * @Date: 2021/8/16 15:17
 */
@Service
@Transactional
public class TSysRoleServiceImpl extends ServiceImpl<TSysRoleMapper, TSysRole> implements ITSysRoleService {
    @Resource
    private TSysRoleMapper tSysRoleMapper;

    @Override
    public int save(TSysRoleVO tSysRoleVO) {
        TSysRole tSysRole = new TSysRole();
        BeanUtils.copyProperties(tSysRoleVO, tSysRole);
        tSysRole.setInputCode(PinyinUtils.getPinYinHeadChar(tSysRole.getRoleName()));
        return tSysRoleMapper.insert(tSysRole);
    }

    @Override
    public int deleteByModel(TSysRoleVO tSysRoleVO) {
        TSysRole tSysRole = new TSysRole();
        BeanUtils.copyProperties(tSysRoleVO, tSysRole);
        return tSysRoleMapper.delete(Wrappers.<TSysRole>lambdaUpdate(tSysRole));
    }

    @Override
    public int update(TSysRoleVO tSysRoleVO) {
        TSysRole tSysRole = new TSysRole();
        BeanUtils.copyProperties(tSysRoleVO, tSysRole);
        tSysRole.setInputCode(PinyinUtils.getPinYinHeadChar(tSysRole.getRoleName()));
        return tSysRoleMapper.updateById(tSysRole);
    }

    @Override
    public TSysRole findByModel(TSysRoleVO tSysRoleVO) {
        TSysRole tSysRole = new TSysRole();
        BeanUtils.copyProperties(tSysRoleVO, tSysRole);
        return tSysRoleMapper.selectOne(Wrappers.<TSysRole>lambdaQuery(tSysRole));
    }

    @Override
    public List<TSysRole> findModelList(TSysRoleVO tSysRoleVO) {
        TSysRole tSysRole = new TSysRole();
        BeanUtils.copyProperties(tSysRoleVO, tSysRole);
        return tSysRoleMapper.selectList(Wrappers.<TSysRole>lambdaQuery(tSysRole));
    }

    @Override
    public IPage<TSysRoleDto> selectByPage(TSysRoleVO tSysRoleVO, Integer page, Integer size) {
        IPage<TSysRoleVO> iPage = new Page<>(page, size);
        return tSysRoleMapper.selectByDto(iPage, tSysRoleVO);
    }

    /**
     * @Description: 获取树形角色列表
     * @Author: yujunhong@aliyun.com
     * @Date: 2021/8/12 14:32
     */
    @Override
    public List<TSysRoleDto> findListByDto(TSysRoleVO tSysRoleVO) {
        List<TSysRoleDto> list = tSysRoleMapper.selectByDto(tSysRoleVO);
        list = (List<TSysRoleDto>) TreeDataUtils.getSysTreeNodeDto(list, "id", "parentUuid", "children");
        return list;
    }

    /**
     * @Description: 角色名称唯一性校验
     * @Author: yujunhong@aliyun.com
     * @Date: 2021/8/12 14:32
     */
    @Override
    public int roleNameCheck(TSysRoleVO tSysRoleVO) {
        //接收的参数有3个：id,roleName,parentUuid
        TSysRole tSysRole = new TSysRole();
        tSysRole.setRoleName(tSysRoleVO.getRoleName());
        tSysRole.setParentUuid(tSysRoleVO.getParentUuid());
        tSysRole.setDelFlag(DeleteStatusEnum.NO_ENUM.getCode());
        List<TSysRole> list = tSysRoleMapper.selectList(Wrappers.lambdaQuery(tSysRole));
        //ID不为空：修改
        //ID为空：新增
        if (null != list) {
            int size = list.size();
            //集合的长度大于1时，则认为已经存在，返回1
            //集合的长度小于1时，则认为不存在，返回0
            //集合的长度为1时，有两种判断
            //1、和当前记录的ID相同，则认为就是本身，返回0
            //2、和当前记录的ID不相同，则认为已经存在，返回1；
            if (size == 1) {
                String id = tSysRoleVO.getId();
                if (StringUtil.isNotNull(id) && list.get(0).getId().equals(id)) {
                    return 0;
                } else {
                    return 1;
                }
            } else if (size > 1) {
                return 1;
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }
}
