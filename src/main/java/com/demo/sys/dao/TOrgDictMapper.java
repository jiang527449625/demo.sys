package com.demo.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.demo.domain.model.sys.dto.TOrgDictDto;
import com.demo.domain.model.sys.entity.TOrgDict;
import com.demo.domain.model.sys.vo.TOrgDictVO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface TOrgDictMapper extends BaseMapper<TOrgDict> {

    List<TOrgDictDto> findListByVo(TOrgDictVO tOrgDictVO);

    List<TOrgDictDto> findUserOrgListByVo(TOrgDictVO tOrgDictVO);

    /**
     * @Description: 根据VO查询但会DTO
     * @Author: yujunhong@aliyun.com
     * @Date: 2021/8/26 11:18
     */
    TOrgDictDto findModelByVo(TOrgDictVO tOrgDictVO);
}