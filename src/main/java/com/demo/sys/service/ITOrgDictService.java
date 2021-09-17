package com.demo.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.demo.domain.model.sys.dto.TOrgDictDto;
import com.demo.domain.model.sys.entity.TOrgDict;
import com.demo.domain.model.sys.vo.TOrgDictVO;

import java.util.List;

/**
 * Created by jky on 2021/08/10.
 */
public interface ITOrgDictService extends IService<TOrgDict> {

    int save(TOrgDictVO tOrgDictVO);

    int deleteByModel(TOrgDictVO tOrgDictVO);

    int update(TOrgDictVO tOrgDictVO);

    TOrgDict findByModel(TOrgDictVO tOrgDictVO);

    TOrgDictDto findModelByVo(TOrgDictVO tOrgDictVO);

    List<TOrgDictDto> findDtoModelList(TOrgDictVO tOrgDictVO);

    IPage<TOrgDict> selectByPage(TOrgDictVO tOrgDictVO, Integer page, Integer size);

    int updateDelModel(String[] ids);

    List<TOrgDict> findModelByList(TOrgDictVO vo);

    List<TOrgDictDto> findModelListByUserOrg(TOrgDictVO tOrgDictVO);
}
