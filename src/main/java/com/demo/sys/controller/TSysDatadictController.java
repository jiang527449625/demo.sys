package com.demo.sys.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.demo.common.mybatis.Result;
import com.demo.common.mybatis.ResultGenerator;
import com.demo.domain.model.sys.entity.TSysDatadict;
import com.demo.domain.model.sys.vo.TSysDatadictVO;
import com.demo.sys.service.ITSysDatadictService;
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
@Api(tags={"字典项管理"})
@RestController
@RequestMapping("/sys/auth/tsysdatadict")
public class TSysDatadictController {
    @Resource
    private ITSysDatadictService tSysDatadictService;

    @ApiOperation(value = "TSysDatadict-新增", notes = "/sys/auth/tsysdatadict/insert")
    @PostMapping("/insert")
    public Result add(@RequestBody TSysDatadictVO tSysDatadictVO) {
        Result result = ResultGenerator.genFailResult();
        int count = tSysDatadictService.save(tSysDatadictVO);
        if(count > 0){
            result = ResultGenerator.genSuccessResult();
        }
        return result;
    }

    @ApiOperation(value = "TSysDatadict-删除", notes = "/sys/auth/tsysdatadict/delete")
    @PostMapping("/delete")
    public Result delete(@RequestBody TSysDatadictVO tSysDatadictVO) {
        Result result = ResultGenerator.genFailResult();
        int count = tSysDatadictService.deleteByModel(tSysDatadictVO);
        if(count > 0){
            result = ResultGenerator.genSuccessResult();
        }
        return result;
    }

    @ApiOperation(value = "TSysDatadict-修改", notes = "/sys/auth/tsysdatadict/update")
    @PostMapping("/update")
    public Result update(@RequestBody TSysDatadictVO tSysDatadictVO) {
        Result result = ResultGenerator.genFailResult();
        int count = tSysDatadictService.update(tSysDatadictVO);
        if(count > 0){
            result = ResultGenerator.genSuccessResult();
        }
        return result;
    }

    @ApiOperation(value = "TSysDatadict-查询实体", notes = "/sys/auth/tsysdatadict/getModel")
    @PostMapping("/getModel")
    public Result getModel(@RequestBody TSysDatadictVO tSysDatadictVO) {
        TSysDatadict tSysDatadict = tSysDatadictService.findByModel(tSysDatadictVO);
        return ResultGenerator.genSuccessResult(tSysDatadict);
    }

    @ApiOperation(value = "TSysDatadict-查询集合", notes = "/sys/auth/tsysdatadict/getList")
    @PostMapping("/getList")
    public Result getList(@RequestBody TSysDatadictVO tSysDatadictVO) {
        List<TSysDatadict> list = tSysDatadictService.findModelList(tSysDatadictVO);
        return ResultGenerator.genSuccessResult(list);
    }

    @ApiOperation(value = "TSysDatadict-分页查询", notes = "/sys/auth/tsysdatadict/pageList")
    @PostMapping("/pageList")
    public Result pageList(@RequestBody TSysDatadictVO tSysDatadictVO) {
        Integer page=tSysDatadictVO.getPageNum();
        Integer size=tSysDatadictVO.getPageSize();
        IPage<TSysDatadict> pageInfo = tSysDatadictService.selectByPage(tSysDatadictVO, page,  size);
        return ResultGenerator.genSuccessResult(pageInfo);
    }


}
