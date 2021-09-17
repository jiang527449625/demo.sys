package com.demo.sys.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.demo.common.mybatis.Result;
import com.demo.common.mybatis.ResultGenerator;
import com.demo.domain.model.sys.entity.SysLog;
import com.demo.domain.model.sys.vo.SysLogVO;
import com.demo.sys.service.ISysLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
* Created by jky on 2021/04/28.
*/

@Api(tags={"日志管理"})
@RestController
@RequestMapping("/sys/noAuth/syslog")
@Slf4j
public class SysLogController {
    @Resource
    private ISysLogService sysLogService;
//    @Resource
//    private MessageProduce messageProduce;
//    @Value("${rabbitmq.departure.task.routingKey.demo}")
//    private String routingKeyRx;

    @ApiOperation(value = "新增日志", notes = "/sys/syslog/insert")
    @PostMapping("/insert")
    @ResponseBody
    public Result add(@RequestBody SysLogVO sysLogVO) {
//        messageProduce.sendMessageMq(routingKeyRx, JSONObject.parseObject(JSONObject.toJSONString(sysLogVO)));
        sysLogService.save(sysLogVO);
        return ResultGenerator.genSuccessResult();
    }

    @ApiOperation(value = "删除日志", notes = "/sys/syslog/delete")
    @PostMapping("/delete")
    @ResponseBody
    public Result delete(@RequestBody SysLogVO sysLogVO) {
        sysLogService.deleteByModel(sysLogVO);
        return ResultGenerator.genSuccessResult();
    }

    @ApiOperation(value = "修改日志", notes = "/sys/syslog/update")
    @PostMapping("/update")
    @ResponseBody
    public Result update(@RequestBody SysLogVO sysLogVO) {
        sysLogService.update(sysLogVO);
        return ResultGenerator.genSuccessResult();
    }

    @ApiOperation(value = "查看日志实体", notes = "/sys/syslog/getModel")
    @PostMapping("/getModel")
    @ResponseBody
    public Result getModel(@RequestBody SysLogVO sysLogVO) {
        SysLog sysLog = sysLogService.findByModel(sysLogVO);
        return ResultGenerator.genSuccessResult(sysLog);
    }

    @ApiOperation(value = "查看日志集合", notes = "/sys/syslog/getList")
    @PostMapping("/getList")
    @ResponseBody
    public Result getList(@RequestBody SysLogVO sysLogVO) {
        List<SysLog> list = sysLogService.findModelList(sysLogVO);
        return ResultGenerator.genSuccessResult(list);
    }

    @ApiOperation(value = "查看日志集合分页", notes = "/sys/syslog/pageList")
    @PostMapping("/pageList")
    @ResponseBody
    public Result pageList(@RequestBody SysLogVO sysLogVO) {
        Integer page=sysLogVO.getPageNum();
        Integer size=sysLogVO.getPageSize();
        IPage<SysLog> pageInfo = sysLogService.selectByPage(sysLogVO, page,  size);
        return ResultGenerator.genSuccessResult(pageInfo);
    }


}
