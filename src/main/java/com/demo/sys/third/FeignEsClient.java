package com.demo.sys.third;

import com.alibaba.fastjson.JSONArray;
import com.demo.common.mybatis.Result;
import com.demo.sys.third.impl.HystrixFeignEsClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * @Author jky
 * @Time 2021年4月21日09:52:09
 * @Description
 */

@FeignClient(value = "${cloud.service.name}",
        fallback = HystrixFeignEsClient.class)
public interface FeignEsClient {
    /**
     * 添加数据至ES
     *
     * @param adsStatisticsList
     * @return app->{index}
     * readcount->{type}
     */
    @RequestMapping(value = "/elasticsearch/auth/add/{index}/index/{type}/type", method = {RequestMethod.POST})
    @ResponseBody
	Result saveAdsStatisticsCount(
            @PathVariable("index") String index,
            @PathVariable("type") String type,
            @RequestBody JSONArray adsStatisticsList);

}

