package com.demo.sys.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.demo.common.mybatis.Result;
import com.demo.common.mybatis.ResultGenerator;
import com.demo.domain.model.sys.dto.TOrgDictDto;
import com.demo.domain.model.sys.entity.TOrgDict;
import com.demo.domain.model.sys.vo.TOrgDictVO;
import com.demo.sys.service.ITOrgDictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
* Created by jky on 2021/08/10.
*/
@Api(tags={"机构管理"})
@RestController
@RequestMapping("/sys/auth/torgdict")
public class TOrgDictController {
    @Resource
    private ITOrgDictService tOrgDictService;

    @ApiOperation(value = "TOrgDict-新增", notes = "/sys/auth/torgdict/insert")
    @PostMapping("/insert")
    public Result add(@RequestBody TOrgDictVO tOrgDictVO) {
        Result result = ResultGenerator.genFailResult();
        int count = tOrgDictService.save(tOrgDictVO);
        if(count > 0){
            result = ResultGenerator.genSuccessResult();
        }
        return result;
    }

    @ApiOperation(value = "TMenuDict-删除", notes = "/sys/auth/torgdict/delete")
    @PostMapping("/delete")
    public Result delete(@RequestBody TOrgDictVO tOrgDictVO) {
        Result result = ResultGenerator.genFailResult();
        List<String> ids = new ArrayList<>();
        List<TOrgDict> list = tOrgDictService.findModelByList(tOrgDictVO);
        for(TOrgDict info : list){
            ids.add(info.getId());
            setids(info,ids);
        }
        String[] strIds = new String[list.size()];
        int count = tOrgDictService.updateDelModel(ids.toArray(strIds));
        if(count == ids.size()){
            result = ResultGenerator.genSuccessResult();
        }
        return result;
    }

    private void setids(TOrgDict info,List<String> ids){
        TOrgDictVO vo = new TOrgDictVO();
        vo.setParentUuid(info.getId());
        List<TOrgDict> childrenList = tOrgDictService.findModelByList(vo);
        for(TOrgDict item : childrenList){
            ids.add(item.getId());
            setids(item,ids);
        }
    }

    @ApiOperation(value = "TOrgDict-修改", notes = "/sys/auth/torgdict/update")
    @PostMapping("/update")
    public Result update(@RequestBody TOrgDictVO tOrgDictVO) {
        Result result = ResultGenerator.genFailResult();
        int count = tOrgDictService.update(tOrgDictVO);
        if(count > 0){
            result = ResultGenerator.genSuccessResult();
        }
        return result;
    }

    @ApiOperation(value = "TOrgDict-查询实体", notes = "/sys/auth/torgdict/getModel")
    @PostMapping("/getModel")
    public Result getModel(@RequestBody TOrgDictVO tOrgDictVO) {
        TOrgDict tOrgDict = tOrgDictService.findByModel(tOrgDictVO);
        return ResultGenerator.genSuccessResult(tOrgDict);
    }

    @ApiOperation(value = "TOrgDict-查询集合树形", notes = "/sys/auth/torgdict/getDtoList")
    @PostMapping("/getDtoList")
    public Result getDtoList(@RequestBody TOrgDictVO tOrgDictVO) {
        List<TOrgDictDto> list = tOrgDictService.findDtoModelList(tOrgDictVO);
        return ResultGenerator.genSuccessResult(list);
    }

    @ApiOperation(value = "TOrgDict-查询集合", notes = "/sys/auth/torgdict/getList")
    @PostMapping("/getList")
    public Result getList(@RequestBody TOrgDictVO tOrgDictVO) {
        List<TOrgDict> list = tOrgDictService.findModelByList(tOrgDictVO);
        return ResultGenerator.genSuccessResult(list);
    }

    @ApiOperation(value = "TOrgDict-根据当前登录人查询集合", notes = "/sys/auth/torgdict/getUserOrgList")
    @PostMapping("/getUserOrgList")
    public Result getUserOrgList(@RequestBody TOrgDictVO tOrgDictVO) {
        List<TOrgDictDto> list = tOrgDictService.findModelListByUserOrg(tOrgDictVO);
        return ResultGenerator.genSuccessResult(list);
    }

    @ApiOperation(value = "TOrgDict-分页查询", notes = "/sys/auth/torgdict/pageList")
    @PostMapping("/pageList")
    public Result pageList(@RequestBody TOrgDictVO tOrgDictVO) {
        Integer page=tOrgDictVO.getPageNum();
        Integer size=tOrgDictVO.getPageSize();
        IPage<TOrgDict> pageInfo = tOrgDictService.selectByPage(tOrgDictVO, page,  size);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

}
