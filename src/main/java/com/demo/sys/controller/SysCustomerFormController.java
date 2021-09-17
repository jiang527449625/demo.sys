package com.demo.sys.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.demo.common.mybatis.Result;
import com.demo.common.mybatis.ResultGenerator;
import com.demo.common.utils.GZIPUtils;
import com.demo.common.utils.SnowflakeIdUtil;
import com.demo.common.utils.StringUtil;
import com.demo.domain.model.sys.dto.SysCustomerFormDto;
import com.demo.domain.model.sys.entity.SysCustomerForm;
import com.demo.domain.model.sys.vo.SysCustomerFormVO;
import com.demo.sys.service.ISysCustomerFormService;
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
@Api(tags = {"自定义表单管理"})
@RestController
@RequestMapping("/sys/auth/syscustomerform")
public class SysCustomerFormController {
    @Resource
    private ISysCustomerFormService sysCustomerFormService;

    @ApiOperation(value = "SysCustomerForm-新增", notes = "/sys/auth/syscustomerform/insert")
    @PostMapping("/insert")
    public Result add(@RequestBody SysCustomerFormVO sysCustomerFormVO) {
        Result result = ResultGenerator.genFailResult();
        sysCustomerFormVO.setId(SnowflakeIdUtil.newStringId());
        int count = sysCustomerFormService.save(sysCustomerFormVO);
        if (count > 0) {
            result = ResultGenerator.genSuccessResult(sysCustomerFormVO);
        }
        return result;
    }

    @ApiOperation(value = "SysCustomerForm-删除", notes = "/sys/auth/syscustomerform/delete")
    @PostMapping("/delete")
    public Result delete(@RequestBody SysCustomerFormVO sysCustomerFormVO) {
        Result result = ResultGenerator.genFailResult();
        int count = sysCustomerFormService.deleteByModel(sysCustomerFormVO);
        if (count > 0) {
            result = ResultGenerator.genSuccessResult();
        }
        return result;
    }

    @ApiOperation(value = "SysCustomerForm-修改", notes = "/sys/auth/syscustomerform/update")
    @PostMapping("/update")
    public Result update(@RequestBody SysCustomerFormVO sysCustomerFormVO) {
        Result result = ResultGenerator.genFailResult();
        int count = sysCustomerFormService.update(sysCustomerFormVO);
        if (count > 0) {
            result = ResultGenerator.genSuccessResult();
        }
        return result;
    }

    @ApiOperation(value = "SysCustomerForm-批量假删", notes = "/sys/auth/syscustomerform/deleteByids")
    @PostMapping("/deleteByids")
    public Result deleteByids(@RequestBody String[] ids) {
        Result result = ResultGenerator.genFailResult();
        int count = sysCustomerFormService.updateDelModel(ids);
        if (count == ids.length) {
            result = ResultGenerator.genSuccessResult();
        }
        return result;
    }

    @ApiOperation(value = "SysCustomerForm-查询实体", notes = "/sys/auth/syscustomerform/getModel")
    @PostMapping("/getModel")
    public Result getModel(@RequestBody SysCustomerFormVO sysCustomerFormVO) {
        SysCustomerForm sysCustomerForm = sysCustomerFormService.findByModel(sysCustomerFormVO);
        return ResultGenerator.genSuccessResult(sysCustomerForm);
    }

    @ApiOperation(value = "根据主键ID查询表单回填JSON", notes = "/sys/auth/syscustomerform/getFormJson")
    @PostMapping("/getFormJson")
    public Result getFormJson(@RequestBody SysCustomerFormVO sysCustomerFormVO) {
        Result result = ResultGenerator.genFailResult();
        if (StringUtil.isNotNull(sysCustomerFormVO.getId())) {
            SysCustomerForm entity = sysCustomerFormService.findByModel(sysCustomerFormVO);
            if (null != entity && StringUtil.isNotNull(entity.getFormJson())) {
                String formJsonZipStr = new String(entity.getFormJson());
                String formJson = GZIPUtils.uncompress(formJsonZipStr);
                JSONObject jsonObject = JSONObject.parseObject(formJson);
                result = ResultGenerator.genSuccessResult(jsonObject);
            }
        }
        return result;
    }

    @ApiOperation(value = "SysCustomerForm-查询集合", notes = "/sys/auth/syscustomerform/getList")
    @PostMapping("/getList")
    public Result getList(@RequestBody SysCustomerFormVO sysCustomerFormVO) {
        List<SysCustomerForm> list = sysCustomerFormService.findModelList(sysCustomerFormVO);
        return ResultGenerator.genSuccessResult(list);
    }

    @ApiOperation(value = "SysCustomerForm-分页查询", notes = "/sys/auth/syscustomerform/pageList")
    @PostMapping("/pageList")
    public Result pageList(@RequestBody SysCustomerFormVO sysCustomerFormVO) {
        Integer page = sysCustomerFormVO.getPageNum();
        Integer size = sysCustomerFormVO.getPageSize();
        IPage<SysCustomerFormDto> pageInfo = sysCustomerFormService.selectDtoByPage(sysCustomerFormVO, page, size);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}
