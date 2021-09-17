package com.demo.sys.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.demo.common.model.enums.DeleteStatusEnum;
import com.demo.common.mybatis.Result;
import com.demo.common.mybatis.ResultGenerator;
import com.demo.domain.model.sys.dto.TSysParamDto;
import com.demo.domain.model.sys.entity.TSysParam;
import com.demo.domain.model.sys.vo.TSysParamVO;
import com.demo.sys.service.ITSysParamService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by jky on 2021/08/10.
 */
@Api(tags = {"参数管理"})
@RestController
@RequestMapping("/sys/auth/tsysparam")
public class TSysParamController {
    @Resource
    private ITSysParamService tSysParamService;

    @ApiOperation(value = "TSysParam-新增", notes = "/sys/auth/tsysparam/insert")
    @PostMapping("/insert")
    public Result add(@RequestBody TSysParamVO tSysParamVO) {
        Result result = ResultGenerator.genFailResult();
        int count = tSysParamService.save(tSysParamVO);
        if (count > 0) {
            result = ResultGenerator.genSuccessResult();
        }
        return result;
    }

    @ApiOperation(value = "TSysParam-删除", notes = "/sys/auth/tsysparam/delete")
    @PostMapping("/delete")
    public Result delete(@RequestBody TSysParamVO tSysParamVO) {
        Result result = ResultGenerator.genFailResult();
        tSysParamVO.setDelFlag(DeleteStatusEnum.YES_ENUM.getCode());
        int count = tSysParamService.update(tSysParamVO);
        if (count > 0) {
            result = ResultGenerator.genSuccessResult();
        }
        return result;
    }

    @ApiOperation(value = "TSysParam-修改", notes = "/sys/auth/tsysparam/update")
    @PostMapping("/update")
    public Result update(@RequestBody TSysParamVO tSysParamVO) {
        Result result = ResultGenerator.genFailResult();
        int count = tSysParamService.update(tSysParamVO);
        if (count > 0) {
            result = ResultGenerator.genSuccessResult();
        }
        return result;
    }

    @ApiOperation(value = "TSysParam-查询实体", notes = "/sys/auth/tsysparam/getModel")
    @PostMapping("/getModel")
    public Result getModel(@RequestBody TSysParamVO tSysParamVO) {
        TSysParam tSysParam = tSysParamService.findByModel(tSysParamVO);
        return ResultGenerator.genSuccessResult(tSysParam);
    }

    @ApiOperation(value = "TSysParam-查询集合", notes = "/sys/auth/tsysparam/getList")
    @PostMapping("/getList")
    public Result getList(@RequestBody TSysParamVO tSysParamVO) {
        List<TSysParam> list = tSysParamService.findModelList(tSysParamVO);
        return ResultGenerator.genSuccessResult(list);
    }

    @ApiOperation(value = "TSysParam-分页查询", notes = "/sys/auth/tsysparam/pageList")
    @PostMapping("/pageList")
    public Result pageList(@RequestBody TSysParamVO tSysParamVO) {
        Integer page = tSysParamVO.getPageNum();
        Integer size = tSysParamVO.getPageSize();
        IPage<TSysParamDto> pageInfo = tSysParamService.selectDtoByPage(tSysParamVO, page, size);
        return ResultGenerator.genSuccessResult(pageInfo);
    }


}
