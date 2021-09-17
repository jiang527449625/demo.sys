package com.demo.sys.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.demo.common.mybatis.Result;
import com.demo.common.mybatis.ResultGenerator;
import com.demo.domain.model.sys.dto.TSysUserDto;
import com.demo.domain.model.sys.entity.TRoleUser;
import com.demo.domain.model.sys.entity.TSysUser;
import com.demo.domain.model.sys.vo.TRoleUserVO;
import com.demo.sys.service.ITRoleUserService;
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
@Api(tags = {"角色、用户关系"})
@RestController
@RequestMapping("/sys/auth/troleuser")
public class TRoleUserController {
    @Resource
    private ITRoleUserService tRoleUserService;

    @ApiOperation(value = "TRoleUser-新增", notes = "/sys/auth/troleuser/insert")
    @PostMapping("/insert")
    public Result add(@RequestBody TRoleUserVO tRoleUserVO) {
        Result result = ResultGenerator.genFailResult();
        int count = tRoleUserService.save(tRoleUserVO);
        if (count > 0) {
            result = ResultGenerator.genSuccessResult();
        }
        return result;
    }

    @ApiOperation(value = "TRoleUser-删除", notes = "/sys/auth/troleuser/delete")
    @PostMapping("/delete")
    public Result delete(@RequestBody TRoleUserVO tRoleUserVO) {
        Result result = ResultGenerator.genFailResult();
        int count = tRoleUserService.deleteByModel(tRoleUserVO);
        if (count > 0) {
            result = ResultGenerator.genSuccessResult();
        }
        return result;
    }

    @ApiOperation(value = "TRoleUser-修改", notes = "/sys/auth/troleuser/update")
    @PostMapping("/update")
    public Result update(@RequestBody TRoleUserVO tRoleUserVO) {
        Result result = ResultGenerator.genFailResult();
        int count = tRoleUserService.update(tRoleUserVO);
        if (count > 0) {
            result = ResultGenerator.genSuccessResult();
        }
        return result;
    }

    @ApiOperation(value = "TRoleUser-查询实体", notes = "/sys/auth/troleuser/getModel")
    @PostMapping("/getModel")
    public Result getModel(@RequestBody TRoleUserVO tRoleUserVO) {
        TRoleUser tRoleUser = tRoleUserService.findByModel(tRoleUserVO);
        return ResultGenerator.genSuccessResult(tRoleUser);
    }

    @ApiOperation(value = "TRoleUser-查询集合", notes = "/sys/auth/troleuser/getList")
    @PostMapping("/getList")
    public Result getList(@RequestBody TRoleUserVO tRoleUserVO) {
        List<TRoleUser> list = tRoleUserService.findModelList(tRoleUserVO);
        return ResultGenerator.genSuccessResult(list);
    }

    @ApiOperation(value = "TRoleUser-查询当前角色对应的用户信息集合", notes = "/sys/auth/troleuser/getRoleUserList")
    @PostMapping("/getRoleUserList")
    public Result getRoleUserList(@RequestBody TRoleUserVO tRoleUserVO) {
        List<TSysUserDto> list = tRoleUserService.getRoleUserList(tRoleUserVO);
        return ResultGenerator.genSuccessResult(list);
    }

    @ApiOperation(value = "TRoleUser-分页查询", notes = "/sys/auth/troleuser/pageList")
    @PostMapping("/pageList")
    public Result pageList(@RequestBody TRoleUserVO tRoleUserVO) {
        Integer page = tRoleUserVO.getPageNum();
        Integer size = tRoleUserVO.getPageSize();
        IPage<TRoleUser> pageInfo = tRoleUserService.selectByPage(tRoleUserVO, page, size);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @ApiOperation(value = "TRoleUser-更新当前角色下的用户信息", notes = "/sys/auth/troleuser/updateRoleUsers")
    @PostMapping("/updateRoleUsers")
    public Result updateRoleUsers(@RequestBody TRoleUserVO tRoleUserVO) {
        Result result = ResultGenerator.genFailResult();
        int count = tRoleUserService.updateRoleUsers(tRoleUserVO);
        if (count > 0) {
            result = ResultGenerator.genSuccessResult();
        }
        return result;
    }

    @ApiOperation(value = "TRoleUser-移除当前角色下的用户信息", notes = "/sys/auth/troleuser/deleteRoleUsers")
    @PostMapping("/deleteRoleUsers")
    public Result deleteRoleUsers(@RequestBody TRoleUserVO tRoleUserVO) {
        Result result = ResultGenerator.genFailResult();
        int count = tRoleUserService.deleteRoleUsers(tRoleUserVO);
        if (count > 0) {
            result = ResultGenerator.genSuccessResult();
        }
        return result;
    }

    @ApiOperation(value = "TSysUser-查询选定角色下未选中的用户集合", notes = "/sys/auth/troleuser/getUncheckedList")
    @PostMapping("/getUncheckedList")
    public Result getUncheckedList(@RequestBody TRoleUserVO tRoleUserVO) {
        List<TSysUser> list = tRoleUserService.getUncheckedList(tRoleUserVO);
        return ResultGenerator.genSuccessResult(list);
    }
}
