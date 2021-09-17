package com.demo.sys.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.demo.common.mybatis.Result;
import com.demo.common.mybatis.ResultGenerator;
import com.demo.domain.model.sys.dto.TMenuDictDto;
import com.demo.domain.model.sys.entity.TRoleUserMenu;
import com.demo.domain.model.sys.vo.TRoleUserMenuVO;
import com.demo.sys.service.ITRoleUserMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by jky on 2021/08/10.
 */
@Api(tags = {"角色、用户菜单关系"})
@RestController
@RequestMapping("/sys/auth/troleusermenu")
public class TRoleUserMenuController {
    @Resource
    private ITRoleUserMenuService tRoleUserMenuService;

    @ApiOperation(value = "TRoleUserMenu-新增", notes = "/sys/auth/troleusermenu/insert")
    @PostMapping("/insert")
    public Result add(@RequestBody TRoleUserMenuVO tRoleUserMenuVO) {
        Result result = ResultGenerator.genFailResult();
        int count = tRoleUserMenuService.save(tRoleUserMenuVO);
        if (count > 0) {
            result = ResultGenerator.genSuccessResult();
        }
        return result;
    }

    @ApiOperation(value = "TRoleUserMenu-删除", notes = "/sys/auth/troleusermenu/delete")
    @PostMapping("/delete")
    public Result delete(@RequestBody TRoleUserMenuVO tRoleUserMenuVO) {
        Result result = ResultGenerator.genFailResult();
        int count = tRoleUserMenuService.deleteByModel(tRoleUserMenuVO);
        if (count > 0) {
            result = ResultGenerator.genSuccessResult();
        }
        return result;
    }

    @ApiOperation(value = "TRoleUserMenu-修改", notes = "/sys/auth/troleusermenu/update")
    @PostMapping("/update")
    public Result update(@RequestBody TRoleUserMenuVO tRoleUserMenuVO) {
        Result result = ResultGenerator.genFailResult();
        int count = tRoleUserMenuService.update(tRoleUserMenuVO);
        if (count > 0) {
            result = ResultGenerator.genSuccessResult();
        }
        return result;
    }

    @ApiOperation(value = "TRoleUserMenu-查询实体", notes = "/sys/auth/troleusermenu/getModel")
    @PostMapping("/getModel")
    public Result getModel(@RequestBody TRoleUserMenuVO tRoleUserMenuVO) {
        TRoleUserMenu tRoleUserMenu = tRoleUserMenuService.findByModel(tRoleUserMenuVO);
        return ResultGenerator.genSuccessResult(tRoleUserMenu);
    }

    @ApiOperation(value = "TRoleUserMenu-查询集合", notes = "/sys/auth/troleusermenu/getList")
    @PostMapping("/getList")
    public Result getList(@RequestBody TRoleUserMenuVO tRoleUserMenuVO) {
        List<TRoleUserMenu> list = tRoleUserMenuService.findModelList(tRoleUserMenuVO);
        return ResultGenerator.genSuccessResult(list);
    }

    @ApiOperation(value = "TRoleUserMenu-分页查询", notes = "/sys/auth/troleusermenu/pageList")
    @PostMapping("/pageList")
    public Result pageList(@RequestBody TRoleUserMenuVO tRoleUserMenuVO) {
        Integer page = tRoleUserMenuVO.getPageNum();
        Integer size = tRoleUserMenuVO.getPageSize();
        IPage<TRoleUserMenu> pageInfo = tRoleUserMenuService.selectByPage(tRoleUserMenuVO, page, size);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @ApiOperation(value = "TRoleUserMenu-查询待选菜单集合", notes = "/sys/auth/troleusermenu/getCheckedMenuList")
    @PostMapping("/getCheckedMenuList")
    public Result getCheckedMenuList(@RequestBody TRoleUserMenuVO tRoleUserMenuVO) {
        Map<String, List<TMenuDictDto>> map = tRoleUserMenuService.getCheckedMenuList(tRoleUserMenuVO);
        return ResultGenerator.genSuccessResult(map);
    }
}
