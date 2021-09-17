package com.demo.sys.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.demo.common.mybatis.Result;
import com.demo.common.mybatis.ResultGenerator;
import com.demo.domain.model.sys.entity.CustomerFormRelation;
import com.demo.domain.model.sys.vo.CustomerFormRelationVO;
import com.demo.sys.service.ICustomerFormRelationService;
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
@Api(tags={"自定义表单、按钮关系"})
@RestController
@RequestMapping("/sys/auth/customerformrelation")
public class CustomerFormRelationController {
    @Resource
    private ICustomerFormRelationService customerFormRelationService;

    @ApiOperation(value = "CustomerFormRelation-新增", notes = "/sys/auth/customerformrelation/insert")
    @PostMapping("/insert")
    public Result add(@RequestBody CustomerFormRelationVO customerFormRelationVO) {
        Result result = ResultGenerator.genFailResult();
        int count = customerFormRelationService.save(customerFormRelationVO);
        if(count > 0){
            result = ResultGenerator.genSuccessResult();
        }
        return result;
    }

    @ApiOperation(value = "CustomerFormRelation-删除", notes = "/sys/auth/customerformrelation/delete")
    @PostMapping("/delete")
    public Result delete(@RequestBody CustomerFormRelationVO customerFormRelationVO) {
        Result result = ResultGenerator.genFailResult();
        int count = customerFormRelationService.deleteByModel(customerFormRelationVO);
        if(count > 0){
            result = ResultGenerator.genSuccessResult();
        }
        return result;
    }

    @ApiOperation(value = "CustomerFormRelation-修改", notes = "/sys/auth/customerformrelation/update")
    @PostMapping("/update")
    public Result update(@RequestBody CustomerFormRelationVO customerFormRelationVO) {
        Result result = ResultGenerator.genFailResult();
        int count = customerFormRelationService.update(customerFormRelationVO);
        if(count > 0){
            result = ResultGenerator.genSuccessResult();
        }
        return result;
    }

    @ApiOperation(value = "CustomerFormRelation-批量假删", notes = "/sys/auth/customerformrelation/deleteByids")
    @PostMapping("/deleteByids")
    public Result deleteByids(@RequestBody String[] ids) {
        Result result = ResultGenerator.genFailResult();
        int count = customerFormRelationService.updateDelModel(ids);
        if(count == ids.length){
            result = ResultGenerator.genSuccessResult();
        }
        return result;
    }

    @ApiOperation(value = "CustomerFormRelation-查询实体", notes = "/sys/auth/customerformrelation/getModel")
    @PostMapping("/getModel")
    public Result getModel(@RequestBody CustomerFormRelationVO customerFormRelationVO) {
        CustomerFormRelation customerFormRelation = customerFormRelationService.findByModel(customerFormRelationVO);
        return ResultGenerator.genSuccessResult(customerFormRelation);
    }

    @ApiOperation(value = "CustomerFormRelation-查询集合", notes = "/sys/auth/customerformrelation/getList")
    @PostMapping("/getList")
    public Result getList(@RequestBody CustomerFormRelationVO customerFormRelationVO) {
        List<CustomerFormRelation> list = customerFormRelationService.findModelList(customerFormRelationVO);
        return ResultGenerator.genSuccessResult(list);
    }

    @ApiOperation(value = "CustomerFormRelation-分页查询", notes = "/sys/auth/customerformrelation/pageList")
    @PostMapping("/pageList")
    public Result pageList(@RequestBody CustomerFormRelationVO customerFormRelationVO) {
        Integer page=customerFormRelationVO.getPageNum();
        Integer size=customerFormRelationVO.getPageSize();
        IPage<CustomerFormRelation> pageInfo = customerFormRelationService.selectByPage(customerFormRelationVO, page,  size);
        return ResultGenerator.genSuccessResult(pageInfo);
    }


}
