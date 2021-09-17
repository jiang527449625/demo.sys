package com.demo.sys.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.common.model.enums.DeleteStatusEnum;
import com.demo.common.model.enums.EnableStatusEnum;
import com.demo.common.utils.PinyinUtils;
import com.demo.common.utils.SnowflakeIdUtil;
import com.demo.common.utils.StringUtil;
import com.demo.common.utils.TreeDataUtils;
import com.demo.domain.model.sys.dto.TMenuDictDto;
import com.demo.domain.model.sys.dto.TSysRoleDto;
import com.demo.domain.model.sys.dto.TSysUserDto;
import com.demo.domain.model.sys.entity.CustomerFormRelation;
import com.demo.domain.model.sys.entity.TMenuDict;
import com.demo.domain.model.sys.vo.TMenuDictVO;
import com.demo.domain.model.utils.UserSessionUtils;
import com.demo.sys.dao.CustomerFormRelationMapper;
import com.demo.sys.dao.TMenuDictMapper;
import com.demo.sys.service.ITMenuDictService;
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
public class TMenuDictServiceImpl extends ServiceImpl<TMenuDictMapper, TMenuDict> implements ITMenuDictService {
    @Resource
    private TMenuDictMapper tMenuDictMapper;
    @Resource
    private CustomerFormRelationMapper customerFormRelationMapper;

    @Override
    public int save(TMenuDictVO tMenuDictVO) {
//      保存菜单
        TMenuDict tMenuDict = new TMenuDict();
        BeanUtils.copyProperties(tMenuDictVO, tMenuDict);
        tMenuDict.setInputCode(PinyinUtils.getPinYinHeadChar(tMenuDict.getMenuName()));
        return tMenuDictMapper.insert(tMenuDict);
    }

    @Override
    public int buttonSave(List<TMenuDictVO> tMenuDictVOList) {
        int count = 0;
        for (TMenuDictVO tMenuDictVO : tMenuDictVOList) {
            TMenuDict tMenuDict = new TMenuDict();
            BeanUtils.copyProperties(tMenuDictVO, tMenuDict);
            switch (tMenuDictVO.getCzlx()) {
                case "0"://新增
                    tMenuDict.setInputCode(PinyinUtils.getPinYinHeadChar(tMenuDict.getMenuName()));
                    count += tMenuDictMapper.insert(tMenuDict);
                    if (StringUtil.isNotNull(tMenuDictVO.getFormUuid())) {
                        CustomerFormRelation customerFormRelation = new CustomerFormRelation();
                        customerFormRelation.setId(SnowflakeIdUtil.newStringId());
                        customerFormRelation.setCustomerFormId(tMenuDictVO.getFormUuid());
                        customerFormRelation.setMenuId(tMenuDict.getId());
                        count += customerFormRelationMapper.insert(customerFormRelation);
                    }
                    break;
                case "1"://修改
                    count += tMenuDictMapper.updateById(tMenuDict);
                    if (StringUtil.isNotNull(tMenuDictVO.getFormUuid())) {
//                      先删除原来的关系信息
                        CustomerFormRelation customerFormRelation = new CustomerFormRelation();
                        customerFormRelation.setMenuId(tMenuDict.getId());
                        customerFormRelationMapper.delete(Wrappers.<CustomerFormRelation>lambdaUpdate(customerFormRelation));
//                      新增新的关系信息
                        customerFormRelation.setId(SnowflakeIdUtil.newStringId());
                        customerFormRelation.setCustomerFormId(tMenuDictVO.getFormUuid());
                        customerFormRelationMapper.insert(customerFormRelation);
                    }
                    break;
                case "2"://删除
                    tMenuDict.setDelFlag(DeleteStatusEnum.YES_ENUM.getCode());
                    count += tMenuDictMapper.updateById(tMenuDict);
                    break;
                default://未操作的czlx为null跳过
                    break;
            }
        }
        return count;
    }

    @Override
    public int deleteByModel(TMenuDictVO tMenuDictVO) {
        TMenuDict tMenuDict = new TMenuDict();
        BeanUtils.copyProperties(tMenuDictVO, tMenuDict);
        return tMenuDictMapper.delete(Wrappers.<TMenuDict>lambdaUpdate(tMenuDict));
    }

    @Override
    public int update(TMenuDictVO tMenuDictVO) {
        TMenuDict tMenuDict = new TMenuDict();
        BeanUtils.copyProperties(tMenuDictVO, tMenuDict);
        return tMenuDictMapper.updateById(tMenuDict);
    }

    @Override
    public TMenuDict findByModel(TMenuDictVO tMenuDictVO) {
        TMenuDict tMenuDict = new TMenuDict();
        BeanUtils.copyProperties(tMenuDictVO, tMenuDict);
        return tMenuDictMapper.selectOne(Wrappers.<TMenuDict>lambdaUpdate(tMenuDict));
    }

    @Override
    public List<TMenuDict> findModelList(TMenuDictVO tMenuDictVO) {
        TMenuDict tMenuDict = new TMenuDict();
        BeanUtils.copyProperties(tMenuDictVO, tMenuDict);
        return tMenuDictMapper.selectList(Wrappers.<TMenuDict>lambdaUpdate(tMenuDict));
    }


    @Override
    public IPage<TMenuDictDto> selectByPage(TMenuDictVO tMenuDictVO, Integer page, Integer size) {
        IPage<TMenuDictDto> iPage = new Page<>(page, size);
        return tMenuDictMapper.selectByDto(iPage, tMenuDictVO);
    }

    @Override
    public List<TMenuDictDto> findAllListByDto(TMenuDictVO tMenuDictVO) {
        tMenuDictVO.setWhetherEnable(EnableStatusEnum.YES_ENUM.getCode());
        List<TMenuDictDto> list = tMenuDictMapper.selectByDto(tMenuDictVO);
        list = (List<TMenuDictDto>) TreeDataUtils.getSysTreeNodeDto(list, "id", "parentUuid", "children");
        return list;
    }

    @Override
    public List<TMenuDictDto> findListByDto(TMenuDictVO tMenuDictVO) {
        List<TMenuDictDto> list;
        TSysUserDto tSysUser = UserSessionUtils.getCurrentUser();
//      选择角色/用户查询已分配的菜单  或者  登陆人查询已分配菜单
        if ((tMenuDictVO.getRuUuidList() != null && tMenuDictVO.getRuUuidList().size() > 0) || tSysUser.getId().equals("1")) {
            list = tMenuDictMapper.selectByDto(tMenuDictVO);
        } else {// 根据当前登陆人去查询已分配的菜单
//          先获取用户权限
            List<String> ruUuidList = new ArrayList<>();
            ruUuidList.add(tSysUser.getId());
            tMenuDictVO.setRuUuidList(ruUuidList);
            list = tMenuDictMapper.selectByDto(tMenuDictVO);
            if (list == null || list.size() == 0) {
//              用户无菜单获取该用户角色菜单
                ruUuidList = new ArrayList<>();
                for (TSysRoleDto role : tSysUser.getSysRoleDtoList()) {
                    ruUuidList.add(role.getId());
                }
                tMenuDictVO.setRuUuidList(ruUuidList);
                list = tMenuDictMapper.selectByDto(tMenuDictVO);
            }
        }
        list = (List<TMenuDictDto>) TreeDataUtils.getSysTreeNodeDto(list, "id", "parentUuid", "children");
        return list;
    }

    @Override
    public int updateDelModel(String[] ids) {
        int count = 0;
        if (ids.length > 0) {
            for (String id : ids) {
                TMenuDict tMenuDict = new TMenuDict();
                tMenuDict.setId(id);
                tMenuDict.setDelFlag(DeleteStatusEnum.YES_ENUM.getCode());
                count += tMenuDictMapper.updateById(tMenuDict);
            }
        }
        return count;
    }

    /**
     * @Description: 自定义表单分配权限
     * @Author: yujunhong@aliyun.com
     * @Date: 2021/9/8 15:56
     */
    @Override
    public int assignPermissions(TMenuDictVO tMenuDictVO) {
        int count = 0;
        String[] ids = tMenuDictVO.getId().split(",");
        if (ids.length > 0) {
            //将当前ID对应的权限分配链接置为null
            TMenuDictVO menuDictVO = new TMenuDictVO();
            menuDictVO.setMenuAssemblyHref(tMenuDictVO.getMenuAssemblyHref());
            tMenuDictMapper.updateModel(menuDictVO);
            //最后执行当前更新
            for (String id : ids) {
                //如果不是按钮，则不执行保存操作
                boolean flag = tMenuDictMapper.selectById(id).getMenuType().equals("2");
                if (flag) {
                    TMenuDict tMenuDict = new TMenuDict();
                    BeanUtils.copyProperties(tMenuDictVO, tMenuDict);
                    tMenuDict.setId(id);
                    count += tMenuDictMapper.updateById(tMenuDict);
                }
            }
        }
        return count;
    }

    /**
     * @Description: 获取当前已经选中的所有按钮
     * @Author: yujunhong@aliyun.com
     * @Date: 2021/9/9 8:51
     */
    @Override
    public List<TMenuDict> findCheckedModelList(TMenuDictVO tMenuDictVO) {
        return tMenuDictMapper.findCheckedModelList(tMenuDictVO);
    }
}
