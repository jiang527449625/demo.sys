package com.demo.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.demo.common.mybatis.Result;
import com.demo.domain.model.sys.entity.SysLog;
import com.demo.domain.model.sys.vo.SysLogVO;

import java.util.List;
import java.util.Map;

/**
 * Created by jky on 2021/04/28.
 */
public interface ISysLogService extends IService<SysLog> {

    void save(SysLogVO sysLogVO);

    void deleteByModel(SysLogVO sysLogVO);

    void update(SysLogVO sysLogVO);

    SysLog findByModel(SysLogVO sysLogVO);

    List<SysLog> findModelList(SysLogVO sysLogVO);

    IPage<SysLog> selectByPage(SysLogVO sysLogVO, Integer page, Integer size);

    Result saveEs(SysLogVO sysLogVO);

    /**
     * 根据表名和主键查询数据
     * @param tableName 表名
     * @param id 主键
     * @return
     */
    List<Map> getTableNameByList(String tableName, String id);
}
