package com.demo.sys.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.common.model.enums.DeleteStatusEnum;
import com.demo.common.model.enums.EnableStatusEnum;
import com.demo.common.utils.PinyinUtils;
import com.demo.common.utils.TreeDataUtils;
import com.demo.domain.model.sys.dto.TOrgDictDto;
import com.demo.domain.model.sys.dto.TSysUserDto;
import com.demo.domain.model.sys.entity.TOrgDict;
import com.demo.domain.model.sys.vo.TOrgDictVO;
import com.demo.domain.model.utils.UserSessionUtils;
import com.demo.sys.dao.TOrgDictMapper;
import com.demo.sys.service.ITOrgDictService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


/**
 * Created by jky on 2021/08/10.
 */
@Service
@Transactional
public class TOrgDictServiceImpl extends ServiceImpl<TOrgDictMapper, TOrgDict> implements ITOrgDictService {
    @Resource
    private TOrgDictMapper tOrgDictMapper;

    @Override
    public int save(TOrgDictVO tOrgDictVO) {
        TOrgDict tOrgDict = new TOrgDict();
        BeanUtils.copyProperties(tOrgDictVO, tOrgDict);
        tOrgDict.setInputCode(PinyinUtils.getPinYinHeadChar(tOrgDict.getOrgName()));
        return tOrgDictMapper.insert(tOrgDict);
    }

    @Override
    public int deleteByModel(TOrgDictVO tOrgDictVO) {
        TOrgDict tOrgDict = new TOrgDict();
        BeanUtils.copyProperties(tOrgDictVO, tOrgDict);
        return tOrgDictMapper.delete(Wrappers.<TOrgDict>lambdaUpdate(tOrgDict));
    }

    @Override
    public int update(TOrgDictVO tOrgDictVO) {
        TOrgDict tOrgDict = new TOrgDict();
        BeanUtils.copyProperties(tOrgDictVO, tOrgDict);
        tOrgDict.setInputCode(PinyinUtils.getPinYinHeadChar(tOrgDict.getOrgName()));
        return tOrgDictMapper.updateById(tOrgDict);
    }

    @Override
    public int updateDelModel(String[] ids) {
        int count = 0;
        if (ids.length > 0) {
            for (String id : ids) {
                TOrgDict tOrgDict = new TOrgDict();
                tOrgDict.setId(id);
                tOrgDict.setDelFlag(DeleteStatusEnum.YES_ENUM.getCode());
                count += tOrgDictMapper.updateById(tOrgDict);
            }
        }
        return count;
    }

    @Override
    public List<TOrgDict> findModelByList(TOrgDictVO vo) {
        TOrgDict tOrgDict = new TOrgDict();
        BeanUtils.copyProperties(vo, tOrgDict);
        return tOrgDictMapper.selectList(Wrappers.<TOrgDict>lambdaQuery(tOrgDict));
    }

    @Override
    public List<TOrgDictDto> findModelListByUserOrg(TOrgDictVO tOrgDictVO) {
        TSysUserDto tSysUser = UserSessionUtils.getCurrentUser();
        tOrgDictVO.setWhetherEnable(EnableStatusEnum.YES_ENUM.getCode());
        if (!tSysUser.getId().equals("1")) {
            tOrgDictVO.setId(tSysUser.getOrgUuid());
        }
        List<TOrgDictDto> list = tOrgDictMapper.findUserOrgListByVo(tOrgDictVO);
        list = (List<TOrgDictDto>) TreeDataUtils.getSysTreeNodeDto(list, "id", "parentUuid", "children");
        return list;
    }

    @Override
    public TOrgDict findByModel(TOrgDictVO tOrgDictVO) {
        TOrgDict tOrgDict = new TOrgDict();
        BeanUtils.copyProperties(tOrgDictVO, tOrgDict);
        return tOrgDictMapper.selectOne(Wrappers.<TOrgDict>lambdaQuery(tOrgDict));
    }

    @Override
    public TOrgDictDto findModelByVo(TOrgDictVO tOrgDictVO) {
        return tOrgDictMapper.findModelByVo(tOrgDictVO);
    }

    @Override
    public List<TOrgDictDto> findDtoModelList(TOrgDictVO tOrgDictVO) {
//        TSysUserDto tSysUser = UserSessionUtils.getCurrentUser();
//        if(!tSysUser.getId().equals("1")){
//            tOrgDictVO.setId(tSysUser.getOrgUuid());
//        }
        List<TOrgDictDto> list = tOrgDictMapper.findListByVo(tOrgDictVO);
        list = (List<TOrgDictDto>) TreeDataUtils.getSysTreeNodeDto(list, "id", "parentUuid", "children");
        return list;
    }

    @Override
    public IPage<TOrgDict> selectByPage(TOrgDictVO tOrgDictVO, Integer page, Integer size) {
        IPage<TOrgDict> iPage = new Page<>(page, size);
        TOrgDict tOrgDict = new TOrgDict();
        BeanUtils.copyProperties(tOrgDictVO, tOrgDict);
        return tOrgDictMapper.selectPage(iPage, Wrappers.<TOrgDict>lambdaQuery(tOrgDict));
    }
}
