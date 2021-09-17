package com.demo.sys.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.demo.common.model.enums.DeleteStatusEnum;
import com.demo.common.mybatis.Result;
import com.demo.common.mybatis.ResultGenerator;
import com.demo.domain.model.sys.dto.TSysRoleDto;
import com.demo.domain.model.sys.entity.TSysRole;
import com.demo.domain.model.sys.vo.TSysRoleVO;
import com.demo.sys.service.ITSysRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description: 角色管理
 * @Author: yujunhong@aliyun.com
 * @Date: 2021/8/16 15:17
 */
@Api(tags = {"角色管理"})
@RestController
@RequestMapping("/sys/auth/tsysrole")
public class TSysRoleController {
    @Resource
    private ITSysRoleService tSysRoleService;

    @ApiOperation(value = "TSysRole-新增", notes = "/sys/auth/tsysrole/insert")
    @PostMapping("/insert")
    public Result add(@RequestBody TSysRoleVO tSysRoleVO) {
        Result result = ResultGenerator.genFailResult();
        int count = tSysRoleService.save(tSysRoleVO);
        if (count > 0) {
            result = ResultGenerator.genSuccessResult();
        }
        return result;
    }

    @ApiOperation(value = "TSysRole-删除", notes = "/sys/auth/tsysrole/delete")
    @PostMapping("/delete")
    public Result delete(@RequestBody TSysRoleVO tSysRoleVO) {
        Result result = ResultGenerator.genFailResult();
        tSysRoleVO.setDelFlag(DeleteStatusEnum.YES_ENUM.getCode());
        int count = tSysRoleService.update(tSysRoleVO);
        if (count > 0) {
            result = ResultGenerator.genSuccessResult();
        }
        return result;
    }

    @ApiOperation(value = "TSysRole-修改", notes = "/sys/auth/tsysrole/update")
    @PostMapping("/update")
    public Result update(@RequestBody TSysRoleVO tSysRoleVO) {
        Result result = ResultGenerator.genFailResult();
        int count = tSysRoleService.update(tSysRoleVO);
        if (count > 0) {
            result = ResultGenerator.genSuccessResult();
        }
        return result;
    }

    @ApiOperation(value = "TSysRole-查询实体", notes = "/sys/auth/tsysrole/getModel")
    @PostMapping("/getModel")
    public Result getModel(@RequestBody TSysRoleVO tSysRoleVO) {
        TSysRole tSysRole = tSysRoleService.findByModel(tSysRoleVO);
        return ResultGenerator.genSuccessResult(tSysRole);
    }

    @ApiOperation(value = "TSysRole-查询集合", notes = "/sys/auth/tsysrole/getList")
    @PostMapping("/getList")
    public Result getList(@RequestBody TSysRoleVO tSysRoleVO) {
        List<TSysRole> list = tSysRoleService.findModelList(tSysRoleVO);
        return ResultGenerator.genSuccessResult(list);
    }

    @ApiOperation(value = "TSysRole-分页查询", notes = "/sys/auth/tsysrole/pageList")
    @PostMapping("/pageList")
    public Result pageList(@RequestBody TSysRoleVO tSysRoleVO) {
        Integer page = tSysRoleVO.getPageNum();
        Integer size = tSysRoleVO.getPageSize();
        IPage<TSysRoleDto> pageInfo = tSysRoleService.selectByPage(tSysRoleVO, page, size);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @ApiOperation(value = "TSysRole-用户角色唯一性校验", notes = "/sys/auth/tsysrole/roleNameCheck")
    @PostMapping("/roleNameCheck")
    public Result roleNameCheck(@RequestBody TSysRoleVO tSysRoleVO) {
        Result result = ResultGenerator.genFailResult();
        int count = tSysRoleService.roleNameCheck(tSysRoleVO);
        if (count == 0) {
            result = ResultGenerator.genSuccessResult();
        }
        return result;
    }

    /**
     * @Description: 获取树形角色列表
     * @Author: yujunhong@aliyun.com
     * @Date: 2021/8/12 14:32
     */
    @ApiOperation(value = "TSysRole-树形列表", notes = "/sys/auth/tsysrole/treeData")
    @PostMapping("/treeData")
    public Result treeData(@RequestBody TSysRoleVO tSysRoleVO) {
        List<TSysRoleDto> list = tSysRoleService.findListByDto(tSysRoleVO);
        return ResultGenerator.genSuccessResult(list);
    }
}
