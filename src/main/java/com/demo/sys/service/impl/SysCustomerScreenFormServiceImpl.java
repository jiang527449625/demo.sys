package com.demo.sys.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.common.model.enums.DeleteStatusEnum;
import com.demo.common.utils.Base64Utils;
import com.demo.common.utils.GZIPUtils;
import com.demo.common.utils.PinyinUtils;
import com.demo.common.utils.StringUtil;
import com.demo.domain.model.sys.dto.SysCustomerScreenFormDto;
import com.demo.domain.model.sys.entity.SysCustomerScreenForm;
import com.demo.domain.model.sys.vo.SysCustomerScreenFormVO;
import com.demo.sys.dao.SysCustomerScreenFormMapper;
import com.demo.sys.service.ISysCustomerScreenFormService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


/**
 * Created by jky on 2021/08/24.
 */
@Service
@Transactional
public class SysCustomerScreenFormServiceImpl extends ServiceImpl<SysCustomerScreenFormMapper, SysCustomerScreenForm> implements ISysCustomerScreenFormService {

    @Resource
    private SysCustomerScreenFormMapper sysCustomerScreenFormMapper;

    @Override
    public int save(SysCustomerScreenFormVO sysCustomerScreenFormVO) {
        sysCustomerScreenFormVO.setScreenJson(GZIPUtils.compress(sysCustomerScreenFormVO.getScreenJson()));
        SysCustomerScreenForm sysCustomerScreenForm = new SysCustomerScreenForm();
        sysCustomerScreenForm.setScreenJson((null != sysCustomerScreenFormVO.getScreenJson()) ? sysCustomerScreenFormVO.getScreenJson().getBytes() : null);
        BeanUtils.copyProperties(sysCustomerScreenFormVO, sysCustomerScreenForm);
        sysCustomerScreenForm.setInputCode(PinyinUtils.getPinYinHeadChar(sysCustomerScreenForm.getScreenName()));
        return sysCustomerScreenFormMapper.insert(sysCustomerScreenForm);
    }

    @Override
    public int deleteByModel(SysCustomerScreenFormVO sysCustomerScreenFormVO) {
        SysCustomerScreenForm sysCustomerScreenForm = new SysCustomerScreenForm();
        BeanUtils.copyProperties(sysCustomerScreenFormVO, sysCustomerScreenForm);
        return sysCustomerScreenFormMapper.delete(Wrappers.<SysCustomerScreenForm>lambdaQuery(sysCustomerScreenForm));
    }

    @Override
    public int update(SysCustomerScreenFormVO sysCustomerScreenFormVO) {
        boolean flag = (StringUtil.isNotNull(sysCustomerScreenFormVO.getScreenJson()) ? (Base64Utils.isBase64Encode(sysCustomerScreenFormVO.getScreenJson()) ? true : false) : false);
        sysCustomerScreenFormVO.setScreenJson(flag ? GZIPUtils.uncompress(sysCustomerScreenFormVO.getScreenJson()) : sysCustomerScreenFormVO.getScreenJson());
        SysCustomerScreenForm sysCustomerScreenForm = new SysCustomerScreenForm();
        BeanUtils.copyProperties(sysCustomerScreenFormVO, sysCustomerScreenForm);
        sysCustomerScreenForm.setScreenJson((null != sysCustomerScreenFormVO.getScreenJson()) ? GZIPUtils.compress(sysCustomerScreenFormVO.getScreenJson()).getBytes() : null);
        sysCustomerScreenForm.setInputCode(PinyinUtils.getPinYinHeadChar(sysCustomerScreenForm.getScreenName()));
        return sysCustomerScreenFormMapper.updateById(sysCustomerScreenForm);
    }

    @Override
    public int updateDelModel(String[] ids) {
        int count = 0;
        if (ids.length > 0) {
            for (String id : ids) {
                SysCustomerScreenForm sysCustomerScreenForm = new SysCustomerScreenForm();
                sysCustomerScreenForm.setId(id);
                sysCustomerScreenForm.setDelFlag(DeleteStatusEnum.YES_ENUM.getCode());
                count += sysCustomerScreenFormMapper.updateById(sysCustomerScreenForm);
            }
        }
        return count;
    }

    @Override
    public SysCustomerScreenForm findByModel(SysCustomerScreenFormVO sysCustomerScreenFormVO) {
        SysCustomerScreenForm sysCustomerScreenForm = new SysCustomerScreenForm();
        BeanUtils.copyProperties(sysCustomerScreenFormVO, sysCustomerScreenForm);
        return sysCustomerScreenFormMapper.selectOne(Wrappers.<SysCustomerScreenForm>lambdaQuery(sysCustomerScreenForm));
    }

    @Override
    public List<SysCustomerScreenForm> findModelList(SysCustomerScreenFormVO sysCustomerScreenFormVO) {
        SysCustomerScreenForm sysCustomerScreenForm = new SysCustomerScreenForm();
        BeanUtils.copyProperties(sysCustomerScreenFormVO, sysCustomerScreenForm);
        return sysCustomerScreenFormMapper.selectList(Wrappers.<SysCustomerScreenForm>lambdaQuery(sysCustomerScreenForm));
    }

    @Override
    public IPage<SysCustomerScreenForm> selectByPage(SysCustomerScreenFormVO sysCustomerScreenFormVO, Integer page, Integer size) {
        IPage<SysCustomerScreenForm> iPage = new Page<>(page, size);
        SysCustomerScreenForm sysCustomerScreenForm = new SysCustomerScreenForm();
        BeanUtils.copyProperties(sysCustomerScreenFormVO, sysCustomerScreenForm);
        return sysCustomerScreenFormMapper.selectPage(iPage, Wrappers.<SysCustomerScreenForm>lambdaQuery(sysCustomerScreenForm));
    }

    @Override
    public IPage<SysCustomerScreenFormDto> selectDtoByPage(SysCustomerScreenFormVO sysCustomerScreenFormVO, Integer page, Integer size) {
        IPage<SysCustomerScreenFormVO> iPage = new Page<>(page, size);
        return sysCustomerScreenFormMapper.findDtoModelList(iPage, sysCustomerScreenFormVO);
    }
}
