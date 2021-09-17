package com.demo.sys.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.common.model.enums.DeleteStatusEnum;
import com.demo.common.model.enums.EnableStatusEnum;
import com.demo.common.utils.PinyinUtils;
import com.demo.domain.model.sys.dto.TSysUserDto;
import com.demo.domain.model.sys.entity.TSysUser;
import com.demo.domain.model.sys.vo.TSysUserVO;
import com.demo.domain.model.utils.UserSessionUtils;
import com.demo.sys.dao.TSysUserMapper;
import com.demo.sys.service.ITSysUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


/**
 * @Description: 用户管理
 * @Author: yujunhong@aliyun.com
 * @Date: 2021/8/16 15:17
 */
@Service
@Transactional
public class TSysUserServiceImpl extends ServiceImpl<TSysUserMapper, TSysUser> implements ITSysUserService {
    @Resource
    private TSysUserMapper tSysUserMapper;

    @Override
    public int save(TSysUserVO tSysUserVO) {
        TSysUser tSysUser = new TSysUser();
        BeanUtils.copyProperties(tSysUserVO, tSysUser);
        tSysUser.setInputCode(PinyinUtils.getPinYinHeadChar(tSysUser.getUserRealName()));
        return tSysUserMapper.insert(tSysUser);
    }

    /**
     * @Description: 此处实际调用修改的接口 super.update
     * @Author: yujunhong@aliyun.com
     * @Date: 2021/8/26 16:15
     */
    @Override
    public int deleteByModel(TSysUserVO tSysUserVO) {
        //假删标志
        tSysUserVO.setDelFlag(DeleteStatusEnum.YES_ENUM.getCode());
        TSysUser tSysUser = new TSysUser();
        BeanUtils.copyProperties(tSysUserVO, tSysUser);
        return tSysUserMapper.updateById(tSysUser);
    }

    @Override
    public int update(TSysUserVO tSysUserVO) {
        TSysUser tSysUser = new TSysUser();
        BeanUtils.copyProperties(tSysUserVO, tSysUser);
        tSysUser.setInputCode(PinyinUtils.getPinYinHeadChar(tSysUser.getUserRealName()));
        return tSysUserMapper.updateById(tSysUser);
    }

    @Override
    public TSysUser findByModel(TSysUserVO tSysUserVO) {
        TSysUser tSysUser = new TSysUser();
        BeanUtils.copyProperties(tSysUserVO, tSysUser);
        return tSysUserMapper.selectOne(Wrappers.lambdaQuery(tSysUser));
    }

    @Override
    public List<TSysUser> findModelList(TSysUserVO tSysUserVO) {
        //删除状态--未删除
        tSysUserVO.setDelFlag(DeleteStatusEnum.NO_ENUM.getCode());
        //启用状态--启用
        tSysUserVO.setWhetherEnable(EnableStatusEnum.YES_ENUM.getCode());
        TSysUser tSysUser = new TSysUser();
        BeanUtils.copyProperties(tSysUserVO, tSysUser);
        return tSysUserMapper.selectList(Wrappers.lambdaQuery(tSysUser));
    }

    @Override
    public IPage<TSysUser> selectByPage(TSysUserVO tSysUserVO, Integer page, Integer size) {
        IPage<TSysUser> iPage = new Page<>(page, size);
        //只查询未删除的用户
        tSysUserVO.setDelFlag(DeleteStatusEnum.NO_ENUM.getCode());
        tSysUserVO.setOrgIdSQL(UserSessionUtils.getCurrentUser().getOrgUuid());
        TSysUser tSysUser = new TSysUser();
        BeanUtils.copyProperties(tSysUserVO, tSysUser);
        return tSysUserMapper.selectPage(iPage, Wrappers.lambdaQuery(tSysUser));
    }

    @Override
    public TSysUserDto selectByVo(TSysUserVO tSysUserVO) {
        TSysUserDto tSysUserDto = tSysUserMapper.selectByVo(tSysUserVO);
        return tSysUserDto;
    }

    /**
     * @Description: 查询选定角色下未选中的用户集合
     * @Author: yujunhong@aliyun.com
     * @Date: 2021/8/20 9:56
     */
    @Override
    public List<TSysUser> findUncheckedList(TSysUserVO tSysUserVO) {
        TSysUser tSysUser = new TSysUser();
        BeanUtils.copyProperties(tSysUserVO, tSysUser);
        return tSysUserMapper.findUncheckedList(tSysUser);
    }

    /**
     * @Description: 用户账号唯一性校验
     * @Author: yujunhong@aliyun.com
     * @Date: 2021/8/27 10:49
     */
    @Override
    public int userAccountCheck(TSysUserVO tSysUserVO) {
        //先根据ID查询获取用户账户
        String userAccount = null;
        if (null != tSysUserVO.getId()) {
            TSysUser tSysUser = new TSysUser();
            tSysUser.setId(tSysUserVO.getId());
            userAccount = tSysUserMapper.findOne(tSysUser).getUserAccount();
        }
        //比对传入的用户账号和查询出的用户账号
        boolean flag = tSysUserVO.getUserAccount().equals(userAccount);
        //如果查询出的用户账号和传入的账号一致，则直接返回成功
        if (flag) {
            return 0;
        } else {
            //如果查询出的用户账号和传入的账号不一致，则用传入的账号去扫描数据库，如果返回值大于1，则说明重复；否则不重复
            TSysUser sysUser = new TSysUser();
            sysUser.setUserAccount(tSysUserVO.getUserAccount());
            List<TSysUser> list = tSysUserMapper.selectList(Wrappers.lambdaQuery(sysUser));
            return (null != list && list.size() > 0) ? 1 : 0;
        }
    }

    @Override
    public IPage<TSysUserDto> selectDtoByPage(TSysUserVO tSysUserVO, Integer page, Integer size) {
        IPage<TSysUserVO> iPage = new Page<>(page, size);
        return tSysUserMapper.findDtoModelList(iPage, tSysUserVO);
    }
}
