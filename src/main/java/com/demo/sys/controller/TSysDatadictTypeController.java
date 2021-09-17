package com.demo.sys.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.demo.common.mybatis.Result;
import com.demo.common.mybatis.ResultGenerator;
import com.demo.domain.model.sys.entity.TSysDatadictType;
import com.demo.domain.model.sys.vo.TSysDatadictTypeVO;
import com.demo.sys.service.ITSysDatadictTypeService;
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
@Api(tags={"字典类型管理"})
@RestController
@RequestMapping("/sys/auth/tsysdatadicttype")
public class TSysDatadictTypeController {
    @Resource
    private ITSysDatadictTypeService tSysDatadictTypeService;

    @ApiOperation(value = "TSysDatadictType-新增", notes = "/sys/auth/tsysdatadicttype/insert")
    @PostMapping("/insert")
    public Result add(@RequestBody TSysDatadictTypeVO tSysDatadictTypeVO) {
        Result result = ResultGenerator.genFailResult();
        int count = tSysDatadictTypeService.save(tSysDatadictTypeVO);
        if(count > 0){
            result = ResultGenerator.genSuccessResult();
        }
        return result;
    }

    @ApiOperation(value = "TSysDatadictType-删除", notes = "/sys/auth/tsysdatadicttype/delete")
    @PostMapping("/delete")
    public Result delete(@RequestBody TSysDatadictTypeVO tSysDatadictTypeVO) {
        Result result = ResultGenerator.genFailResult();
        int count = tSysDatadictTypeService.deleteByModel(tSysDatadictTypeVO);
        if(count > 0){
            result = ResultGenerator.genSuccessResult();
        }
        return result;
    }

    @ApiOperation(value = "TSysDatadictType-修改", notes = "/sys/auth/tsysdatadicttype/update")
    @PostMapping("/update")
    public Result update(@RequestBody TSysDatadictTypeVO tSysDatadictTypeVO) {
        Result result = ResultGenerator.genFailResult();
        int count = tSysDatadictTypeService.update(tSysDatadictTypeVO);
        if(count > 0){
            result = ResultGenerator.genSuccessResult();
        }
        return result;
    }

    @ApiOperation(value = "TSysDatadictType-查询实体", notes = "/sys/auth/tsysdatadicttype/getModel")
    @PostMapping("/getModel")
    public Result getModel(@RequestBody TSysDatadictTypeVO tSysDatadictTypeVO) {
        TSysDatadictType tSysDatadictType = tSysDatadictTypeService.findByModel(tSysDatadictTypeVO);
        return ResultGenerator.genSuccessResult(tSysDatadictType);
    }

    @ApiOperation(value = "TSysDatadictType-查询集合", notes = "/sys/auth/tsysdatadicttype/getList")
    @PostMapping("/getList")
    public Result getList(@RequestBody TSysDatadictTypeVO tSysDatadictTypeVO) {
        List<TSysDatadictType> list = tSysDatadictTypeService.findModelList(tSysDatadictTypeVO);
        return ResultGenerator.genSuccessResult(list);
    }

    @ApiOperation(value = "TSysDatadictType-分页查询", notes = "/sys/auth/tsysdatadicttype/pageList")
    @PostMapping("/pageList")
    public Result pageList(@RequestBody TSysDatadictTypeVO tSysDatadictTypeVO) {
        Integer page=tSysDatadictTypeVO.getPageNum();
        Integer size=tSysDatadictTypeVO.getPageSize();
        IPage<TSysDatadictType> pageInfo = tSysDatadictTypeService.selectByPage(tSysDatadictTypeVO, page,  size);
        return ResultGenerator.genSuccessResult(pageInfo);
    }


}
