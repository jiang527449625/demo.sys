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
import com.demo.domain.model.sys.dto.SysCustomerFormDto;
import com.demo.domain.model.sys.entity.SysCustomerForm;
import com.demo.domain.model.sys.vo.SysCustomerFormVO;
import com.demo.sys.dao.SysCustomerFormMapper;
import com.demo.sys.service.ISysCustomerFormService;
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
public class SysCustomerFormServiceImpl extends ServiceImpl<SysCustomerFormMapper, SysCustomerForm> implements ISysCustomerFormService {
    @Resource
    private SysCustomerFormMapper sysCustomerFormMapper;

    @Override
    public int save(SysCustomerFormVO sysCustomerFormVO) {
        sysCustomerFormVO.setFormFile(GZIPUtils.compress(sysCustomerFormVO.getFormFile()));
        sysCustomerFormVO.setFormJson(GZIPUtils.compress(sysCustomerFormVO.getFormJson()));
        SysCustomerForm sysCustomerForm = new SysCustomerForm();
        sysCustomerForm.setFormFile((null != sysCustomerFormVO.getFormFile()) ? sysCustomerFormVO.getFormFile().getBytes() : null);
        sysCustomerForm.setFormJson((null != sysCustomerFormVO.getFormJson()) ? sysCustomerFormVO.getFormJson().getBytes() : null);
        BeanUtils.copyProperties(sysCustomerFormVO, sysCustomerForm);
        sysCustomerForm.setInputCode(PinyinUtils.getPinYinHeadChar(sysCustomerForm.getFormName()));
        return sysCustomerFormMapper.insert(sysCustomerForm);
    }

    @Override
    public int deleteByModel(SysCustomerFormVO sysCustomerFormVO) {
        SysCustomerForm sysCustomerForm = new SysCustomerForm();
        BeanUtils.copyProperties(sysCustomerFormVO, sysCustomerForm);
        return sysCustomerFormMapper.delete(Wrappers.<SysCustomerForm>lambdaUpdate().eq(SysCustomerForm::getId, sysCustomerFormVO.getId()));
    }

    @Override
    public int update(SysCustomerFormVO sysCustomerFormVO) {
        boolean flag_json = (StringUtil.isNotNull(sysCustomerFormVO.getFormJson()) ? (Base64Utils.isBase64Encode(sysCustomerFormVO.getFormJson()) ? true : false) : false);
        boolean flag_file = (StringUtil.isNotNull(sysCustomerFormVO.getFormFile()) ? (Base64Utils.isBase64Encode(sysCustomerFormVO.getFormFile()) ? true : false) : false);
        sysCustomerFormVO.setFormFile(flag_file ? GZIPUtils.uncompress(sysCustomerFormVO.getFormFile()) : sysCustomerFormVO.getFormFile());
        sysCustomerFormVO.setFormJson(flag_json ? GZIPUtils.uncompress(sysCustomerFormVO.getFormJson()) : sysCustomerFormVO.getFormJson());
        SysCustomerForm sysCustomerForm = new SysCustomerForm();
        BeanUtils.copyProperties(sysCustomerFormVO, sysCustomerForm);
        sysCustomerForm.setFormFile((null != sysCustomerFormVO.getFormFile()) ? GZIPUtils.compress(sysCustomerFormVO.getFormFile()).getBytes() : null);
        sysCustomerForm.setFormJson((null != sysCustomerFormVO.getFormJson()) ? GZIPUtils.compress(sysCustomerFormVO.getFormJson()).getBytes() : null);
        BeanUtils.copyProperties(sysCustomerFormVO, sysCustomerForm);
        sysCustomerForm.setInputCode(PinyinUtils.getPinYinHeadChar(sysCustomerForm.getFormName()));
        return sysCustomerFormMapper.updateById(sysCustomerForm);
    }

    @Override
    public int updateDelModel(String[] ids) {
        int count = 0;
        if (ids.length > 0) {
            for (String id : ids) {
                SysCustomerForm sysCustomerForm = new SysCustomerForm();
                sysCustomerForm.setId(id);
                sysCustomerForm.setDelFlag(DeleteStatusEnum.YES_ENUM.getCode());
                count += sysCustomerFormMapper.updateById(sysCustomerForm);
            }
        }
        return count;
    }

    @Override
    public SysCustomerForm findByModel(SysCustomerFormVO sysCustomerFormVO) {
        SysCustomerForm sysCustomerForm = new SysCustomerForm();
        BeanUtils.copyProperties(sysCustomerFormVO, sysCustomerForm);
        return sysCustomerFormMapper.selectOne(Wrappers.<SysCustomerForm>lambdaQuery().eq(SysCustomerForm::getId, sysCustomerFormVO.getId()));
    }

    @Override
    public List<SysCustomerForm> findModelList(SysCustomerFormVO sysCustomerFormVO) {
        SysCustomerForm sysCustomerForm = new SysCustomerForm();
        BeanUtils.copyProperties(sysCustomerFormVO, sysCustomerForm);
        return sysCustomerFormMapper.selectList(Wrappers.<SysCustomerForm>lambdaQuery(sysCustomerForm));
    }

    @Override
    public IPage<SysCustomerForm> selectByPage(SysCustomerFormVO sysCustomerFormVO, Integer page, Integer size) {
        IPage<SysCustomerForm> iPage = new Page<>(page, size);
        SysCustomerForm sysCustomerForm = new SysCustomerForm();
        BeanUtils.copyProperties(sysCustomerFormVO, sysCustomerForm);
        return sysCustomerFormMapper.selectPage(iPage, Wrappers.<SysCustomerForm>lambdaQuery(sysCustomerForm));
    }

    @Override
    public IPage<SysCustomerFormDto> selectDtoByPage(SysCustomerFormVO sysCustomerFormVO, Integer page, Integer size) {
        IPage<SysCustomerFormVO> iPage = new Page<>(page, size);
        return sysCustomerFormMapper.findDtoModelList(iPage, sysCustomerFormVO);
    }
}
