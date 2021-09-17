package com.demo.sys.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.demo.common.config.LogAnno;
import com.demo.common.model.vo.Constants;
import com.demo.common.mybatis.Result;
import com.demo.common.mybatis.ResultGenerator;
import com.demo.common.utils.StringUtil;
import com.demo.domain.model.sys.dto.TMenuDictDto;
import com.demo.domain.model.sys.entity.TMenuDict;
import com.demo.domain.model.sys.vo.TMenuDictVO;
import com.demo.sys.dao.SysCustomerFormMapper;
import com.demo.sys.service.ISysCustomerFormService;
import com.demo.sys.service.ITMenuDictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by jky on 2021/08/10.
 */
@Api(tags = {"菜单管理"})
@RestController
@RequestMapping("/sys/auth/tmenudict")
public class TMenuDictController {

    @Resource
    private ITMenuDictService tMenuDictService;

    @Resource
    ISysCustomerFormService iSysCustomerFormService;

    @ApiOperation(value = "TMenuDict-新增", notes = "/sys/auth/tmenudict/insert")
    @LogAnno(logTableName = "t_menu_dict",logModular ="菜单" ,logExplain = Constants.AOPLOGINSERT,logRemark = "测试")
    @PostMapping("/insert")
    public Result add(@RequestBody TMenuDictVO tMenuDictVO) {
        Result result = ResultGenerator.genFailResult();
        int count = tMenuDictService.save(tMenuDictVO);
        if (count > 0) {
            result = ResultGenerator.genSuccessResult();
        }
        return result;
    }

    @ApiOperation(value = "按钮保存", notes = "/sys/auth/tmenudict/buttonSave")
    @PostMapping("/buttonSave")
    public Result buttonSave(@RequestBody List<TMenuDictVO> tMenuDictVOList) {
        Result result = ResultGenerator.genFailResult();
        int count = tMenuDictService.buttonSave(tMenuDictVOList);
        if (count > 0) {
            result = ResultGenerator.genSuccessResult();
        }
        return result;
    }

    @ApiOperation(value = "TMenuDict-删除", notes = "/sys/auth/tmenudict/delete")
    @PostMapping("/delete")
    public Result delete(@RequestBody TMenuDictVO tMenuDictVO) {
        Result result = ResultGenerator.genFailResult();
        List<String> ids = new ArrayList<>();
        List<TMenuDict> list = tMenuDictService.findModelList(tMenuDictVO);
        for (TMenuDict info : list) {
            ids.add(info.getId());
            setids(info, ids);
        }
        String[] strIds = new String[list.size()];
        int count = tMenuDictService.updateDelModel(ids.toArray(strIds));
        if (count == ids.size()) {
            result = ResultGenerator.genSuccessResult();
        }
        return result;
    }

    private void setids(TMenuDict info, List<String> ids) {
        TMenuDictVO vo = new TMenuDictVO();
        vo.setParentUuid(info.getId());
        List<TMenuDict> childrenList = tMenuDictService.findModelList(vo);
        for (TMenuDict item : childrenList) {
            ids.add(item.getId());
            setids(item, ids);
        }
    }

    @ApiOperation(value = "TMenuDict-修改", notes = "/sys/auth/tmenudict/update")
    @PostMapping("/update")
    public Result update(@RequestBody TMenuDictVO tMenuDictVO) {
        Result result = ResultGenerator.genFailResult();
        int count = tMenuDictService.update(tMenuDictVO);
        if (count > 0) {
            result = ResultGenerator.genSuccessResult();
        }
        return result;
    }

    @ApiOperation(value = "TMenuDict-分配权限", notes = "/sys/auth/tmenudict/assignPermissions")
    @PostMapping("/assignPermissions")
    public Result assignPermissions(@RequestBody TMenuDictVO tMenuDictVO) {
        Map<String, Object> map = new Hashtable<>();
        Result result = ResultGenerator.genFailResult();
        //此处需要根据按钮ID查询出JSONID反推出自定义大屏ID
//        iSysCustomerFormService.getById()
        if (StringUtil.isNotNull(tMenuDictVO.getId())) {
            int count = tMenuDictService.assignPermissions(tMenuDictVO);
            if (count > 0) {
                result = ResultGenerator.genSuccessResult();
            }
        } else {
            //获取当前记录的选中按钮
            List<TMenuDict> currentModelList = tMenuDictService.findModelList(tMenuDictVO);
            map.put("current", currentModelList);
            //获取除当前记录外的已选中按钮
            TMenuDictVO dictVO = new TMenuDictVO();
            dictVO.setMenuType("2");
            dictVO.setRuUuidList(currentModelList.stream().map(TMenuDict::getMenuAssemblyHref).collect(Collectors.toList()));
            map.put("checked", tMenuDictService.findCheckedModelList(dictVO));
            result = ResultGenerator.genSuccessResult(map);
        }
        return result;
    }

    @ApiOperation(value = "TMenuDict-查询实体", notes = "/sys/auth/tmenudict/getModel")
    @PostMapping("/getModel")
    public Result getModel(@RequestBody TMenuDictVO tMenuDictVO) {
        TMenuDict tMenuDict = tMenuDictService.findByModel(tMenuDictVO);
        return ResultGenerator.genSuccessResult(tMenuDict);
    }

    @ApiOperation(value = "TMenuDict-查询所有菜单集合", notes = "/sys/auth/tmenudict/getAllList")
    @PostMapping("/getAllList")
    public Result getAllList(@RequestBody TMenuDictVO tMenuDictVO) {
        List<TMenuDictDto> list = tMenuDictService.findAllListByDto(tMenuDictVO);
        return ResultGenerator.genSuccessResult(list);
    }

    @ApiOperation(value = "TMenuDict-查询菜单集合", notes = "/sys/auth/tmenudict/getList")
    @PostMapping("/getList")
    public Result getList(@RequestBody TMenuDictVO tMenuDictVO) {
        List<TMenuDictDto> list = tMenuDictService.findListByDto(tMenuDictVO);
        return ResultGenerator.genSuccessResult(list);
    }

    @ApiOperation(value = "TMenuDict-分页查询", notes = "/sys/auth/tmenudict/pageList")
    @PostMapping("/pageList")
    public Result pageList(@RequestBody TMenuDictVO tMenuDictVO) {
        Integer page=tMenuDictVO.getPageNum();
        Integer size=tMenuDictVO.getPageSize();
        IPage<TMenuDictDto> pageInfo = tMenuDictService.selectByPage(tMenuDictVO, page,  size);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

}
