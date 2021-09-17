package com.demo.sys.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.demo.common.model.enums.EnableStatusEnum;
import com.demo.common.model.vo.Constants;
import com.demo.common.mybatis.Result;
import com.demo.common.mybatis.ResultGenerator;
import com.demo.common.utils.JwtToken;
import com.demo.domain.model.sys.dto.TSysRoleDto;
import com.demo.domain.model.sys.dto.TSysUserDto;
import com.demo.domain.model.sys.entity.TSysUser;
import com.demo.domain.model.sys.vo.TSysRoleVO;
import com.demo.domain.model.sys.vo.TSysUserVO;
import com.demo.sys.service.ITSysRoleService;
import com.demo.sys.service.ITSysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description: 用户管理
 * @Author: yujunhong@aliyun.com
 * @Date: 2021/8/16 15:17
 */
@Api(tags = {"用户管理"})
@RestController
@RequestMapping("/sys")
public class TSysUserController {

    @Resource
    private ITSysUserService tSysUserService;

    @Resource
    private ITSysRoleService iTSysRoleService;

    @Value("${login_token_expire_duration}")
    private String expireDuration;

    @ApiOperation(value = "TSysUser-新增", notes = "/sys/auth/tsysuser/insert")
    @PostMapping("/auth/tsysuser/insert")
    public Result add(@RequestBody TSysUserVO tSysUserVO) {
        Result result = ResultGenerator.genFailResult();
        int count = tSysUserService.save(tSysUserVO);
        if (count > 0) {
            result = ResultGenerator.genSuccessResult();
        }
        return result;
    }

    @ApiOperation(value = "TSysUser-逻辑删除", notes = "/sys/auth/tsysuser/delete")
    @PostMapping("/auth/tsysuser/delete")
    public Result delete(@RequestBody TSysUserVO tSysUserVO) {
        Result result = ResultGenerator.genFailResult();
        int count = tSysUserService.deleteByModel(tSysUserVO);
        if (count > 0) {
            result = ResultGenerator.genSuccessResult();
        }
        return result;
    }

    @ApiOperation(value = "TSysUser-修改", notes = "/sys/auth/tsysuser/update")
    @PostMapping("/auth/tsysuser/update")
    public Result update(@RequestBody TSysUserVO tSysUserVO) {
        Result result = ResultGenerator.genFailResult();
        int count = tSysUserService.update(tSysUserVO);
        if (count > 0) {
            result = ResultGenerator.genSuccessResult();
        }
        return result;
    }

    @ApiOperation(value = "TSysUser-查询实体", notes = "/sys/auth/tsysuser/getModel")
    @PostMapping("/auth/tsysuser/getModel")
    public Result getModel(@RequestBody TSysUserVO tSysUserVO) {
        TSysUser tSysUser = tSysUserService.findByModel(tSysUserVO);
        return ResultGenerator.genSuccessResult(tSysUser);
    }

    @ApiOperation(value = "TSysUser-查询集合", notes = "/sys/auth/tsysuser/getList")
    @PostMapping("/auth/tsysuser/getList")
    public Result getList(@RequestBody TSysUserVO tSysUserVO) {
        List<TSysUser> list = tSysUserService.findModelList(tSysUserVO);
        return ResultGenerator.genSuccessResult(list);
    }

    @ApiOperation(value = "TSysUser-查询选定角色下未选中的用户集合", notes = "/sys/auth/tsysuser/getUncheckedList")
    @PostMapping("/auth/tsysuser/getUncheckedList")
    public Result getUncheckedList(@RequestBody TSysUserVO tSysUserVO) {
        List<TSysUser> list = tSysUserService.findUncheckedList(tSysUserVO);
        return ResultGenerator.genSuccessResult(list);
    }

    @ApiOperation(value = "TSysUser-用户账号唯一性校验", notes = "/sys/auth/tsysuser/userAccountCheck")
    @PostMapping("/auth/tsysuser/userAccountCheck")
    public Result userAccountCheck(@RequestBody TSysUserVO tSysUserVO) {
        Result result = ResultGenerator.genFailResult();
        int count = tSysUserService.userAccountCheck(tSysUserVO);
        if (count == 0) {
            result = ResultGenerator.genSuccessResult();
        }
        return result;
    }

    @ApiOperation(value = "TSysUser-分页查询", notes = "/sys/auth/tsysuser/pageList")
    @PostMapping("/auth/tsysuser/pageList")
    public Result pageList(@RequestBody TSysUserVO tSysUserVO) {
        Integer page = tSysUserVO.getPageNum();
        Integer size = tSysUserVO.getPageSize();
        IPage<TSysUserDto> pageInfo = tSysUserService.selectDtoByPage(tSysUserVO, page, size);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     * @Description: 用户登入
     * @Author: yujunhong@aliyun.com
     * @Date: 2021/8/11 11:35
     */
    @ApiOperation(value = "TSysUser-用户登录", notes = "/sys/noAuth/tsysuser/login")
    @PostMapping("/noAuth/tsysuser/login")
    public Result login(@RequestBody TSysUserVO vo, HttpServletRequest request) {
        Result result;
        try {
            String userPassword = null;
            //根据账户名查询
            TSysUserDto user = tSysUserService.selectByVo(vo);
            if (null != user) {
                //此处暂时明文获取密码
                userPassword = user.getUserPassword();
            }
            if (null == user) {
                result = ResultGenerator.genFailResult("用户名或密码不正确");
            } else if (EnableStatusEnum.NO_ENUM.getCode().equals(user.getWhetherEnable())) {
                result = ResultGenerator.genFailResult("用户名或密码不正确");
            } else if (!userPassword.equals(vo.getUserPassword())) {
                result = ResultGenerator.genFailResult("用户名或密码不正确");
            } else {
                //角色IDS
                List<String> roleIds = user.getSysRoleDtoList().stream().map(TSysRoleDto::getId).collect(Collectors.toList());

                //根据角色ids递归获取角色信息
                boolean flag = (null != roleIds && roleIds.size() > 0);
                if (flag) {
                    TSysRoleVO tSysRoleVO = new TSysRoleVO();
                    tSysRoleVO.setRoleUuidList(roleIds);
                    user.setSysRoleDtoList(iTSysRoleService.findListByDto(tSysRoleVO));
                }

                //生成token
                String token = JwtToken.createToken(user.getId(), user.getUserAccount(), JSON.toJSONString(roleIds), user.getOrgUuid(), Calendar.MINUTE, Integer.parseInt(expireDuration));
                Map<String, Object> obj = new HashMap<>();
                obj.put(Constants.TOKEN, token);
                obj.put(Constants.USER, user);

                request.setAttribute(Constants.USERID, user.getId());
                request.setAttribute(Constants.USERNAME, user.getUserAccount());
                request.setAttribute(Constants.ROLEIDS, JSON.toJSONString(roleIds));
                request.setAttribute(Constants.USER, user);
                request.setAttribute(Constants.ORGUUID, user.getOrgUuid());

                result = ResultGenerator.genSuccessResult("登录成功", obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = ResultGenerator.genFailResult();
        }
        return result;
    }

    /**
     * @Description: 用户登出
     * @Author: yujunhong@aliyun.com
     * @Date: 2021/8/11 11:35
     */
    @ApiOperation(value = "TSysUser-用户登出", notes = "/sys/auth/tsysuser/logout")
    @PostMapping("/auth/tsysuser/logout")
    public Result logout(HttpServletRequest request) {
        Result result;
        try {
            result = ResultGenerator.genSuccessResult();
            //销毁session
            request.getSession().invalidate();
        } catch (Exception e) {
            e.printStackTrace();
            result = ResultGenerator.genFailResult();
        }
        return result;
    }
}