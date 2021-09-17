package com.demo.sys.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.demo.common.mybatis.Result;
import com.demo.common.mybatis.ResultGenerator;
import com.demo.common.utils.GZIPUtils;
import com.demo.common.utils.SnowflakeIdUtil;
import com.demo.common.utils.StringUtil;
import com.demo.domain.model.sys.dto.SysCustomerScreenFormDto;
import com.demo.domain.model.sys.entity.SysCustomerScreenForm;
import com.demo.domain.model.sys.vo.SysCustomerScreenFormVO;
import com.demo.sys.service.ISysCustomerScreenFormService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by jky on 2021/08/24.
 */
@Api(tags = {"SysCustomerScreenForm"})
@RestController
@RequestMapping("/sys/auth/syscustomerscreenform")
public class SysCustomerScreenFormController {
    @Resource
    private ISysCustomerScreenFormService sysCustomerScreenFormService;

    @ApiOperation(value = "SysCustomerScreenForm-新增", notes = "/sys/auth/syscustomerscreenform/insert")
    @PostMapping("/insert")
    public Result add(@RequestBody SysCustomerScreenFormVO sysCustomerScreenFormVO) {
        Result result = ResultGenerator.genFailResult();
        sysCustomerScreenFormVO.setId(SnowflakeIdUtil.newStringId());
        int count = sysCustomerScreenFormService.save(sysCustomerScreenFormVO);
        if (count > 0) {
            result = ResultGenerator.genSuccessResult(sysCustomerScreenFormVO);
        }
        return result;
    }

    @ApiOperation(value = "SysCustomerScreenForm-删除", notes = "/sys/auth/syscustomerscreenform/delete")
    @PostMapping("/delete")
    public Result delete(@RequestBody SysCustomerScreenFormVO sysCustomerScreenFormVO) {
        Result result = ResultGenerator.genFailResult();
        int count = sysCustomerScreenFormService.deleteByModel(sysCustomerScreenFormVO);
        if (count > 0) {
            result = ResultGenerator.genSuccessResult();
        }
        return result;
    }

    @ApiOperation(value = "SysCustomerScreenForm-修改", notes = "/sys/auth/syscustomerscreenform/update")
    @PostMapping("/update")
    public Result update(@RequestBody SysCustomerScreenFormVO sysCustomerScreenFormVO) {
        Result result = ResultGenerator.genFailResult();
        int count = sysCustomerScreenFormService.update(sysCustomerScreenFormVO);
        if (count > 0) {
            result = ResultGenerator.genSuccessResult();
        }
        return result;
    }

    @ApiOperation(value = "SysCustomerScreenForm-批量假删", notes = "/sys/auth/syscustomerscreenform/deleteByids")
    @PostMapping("/deleteByids")
    public Result deleteByids(@RequestBody String[] ids) {
        Result result = ResultGenerator.genFailResult();
        int count = sysCustomerScreenFormService.updateDelModel(ids);
        if (count == ids.length) {
            result = ResultGenerator.genSuccessResult();
        }
        return result;
    }

    @ApiOperation(value = "SysCustomerScreenForm-查询实体", notes = "/sys/auth/syscustomerscreenform/getModel")
    @PostMapping("/getModel")
    public Result getModel(@RequestBody SysCustomerScreenFormVO sysCustomerScreenFormVO) {
        SysCustomerScreenForm sysCustomerForm = sysCustomerScreenFormService.findByModel(sysCustomerScreenFormVO);
        return ResultGenerator.genSuccessResult(sysCustomerForm);
    }

    @ApiOperation(value = "SysCustomerScreenForm-根据主键ID查询表单回填JSON", notes = "/sys/auth/syscustomerscreenform/getFormJson")
    @PostMapping("/getFormJson")
    public Result getFormJson(@RequestBody SysCustomerScreenFormVO sysCustomerScreenFormVO) {
        Result result = ResultGenerator.genFailResult();
        if (StringUtil.isNotNull(sysCustomerScreenFormVO.getId())) {
            SysCustomerScreenForm entity = sysCustomerScreenFormService.findByModel(sysCustomerScreenFormVO);
            if (null != entity && StringUtil.isNotNull(entity.getScreenJson())) {
                String formJsonZipStr = new String(entity.getScreenJson());
                String formJson = GZIPUtils.uncompress(formJsonZipStr);
                JSONObject jsonObject = JSONObject.parseObject(formJson);
                result = ResultGenerator.genSuccessResult(jsonObject);
            }
        }
        return result;
    }

    @ApiOperation(value = "SysCustomerScreenForm-查询集合", notes = "/sys/auth/syscustomerscreenform/getList")
    @PostMapping("/getList")
    public Result getList(@RequestBody SysCustomerScreenFormVO sysCustomerScreenFormVO) {
        List<SysCustomerScreenForm> list = sysCustomerScreenFormService.findModelList(sysCustomerScreenFormVO);
        return ResultGenerator.genSuccessResult(list);
    }

    @ApiOperation(value = "SysCustomerScreenForm-分页查询", notes = "/sys/auth/syscustomerscreenform/pageList")
    @PostMapping("/pageList")
    public Result pageList(@RequestBody SysCustomerScreenFormVO sysCustomerScreenFormVO) {
        Integer page = sysCustomerScreenFormVO.getPageNum();
        Integer size = sysCustomerScreenFormVO.getPageSize();
        IPage<SysCustomerScreenFormDto> pageInfo = sysCustomerScreenFormService.selectDtoByPage(sysCustomerScreenFormVO, page, size);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}
