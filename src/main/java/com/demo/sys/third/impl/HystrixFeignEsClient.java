package com.demo.sys.third.impl;

import com.alibaba.fastjson.JSONArray;
import com.demo.common.mybatis.Result;
import com.demo.common.mybatis.ResultGenerator;
import com.demo.sys.third.FeignEsClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author :jky
 * @Description:
 * @ Date: Created in 2021-5-8 14:22
 */
@Component
@Slf4j
public class HystrixFeignEsClient implements FeignEsClient {

    @Override
    public Result saveAdsStatisticsCount(String index, String type, JSONArray adsStatisticsList) {
        return ResultGenerator.genFailResult("熔断了");
    }
}

