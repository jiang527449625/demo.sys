package com.demo.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.demo.domain.model.sys.dto.TMenuDictDto;
import com.demo.domain.model.sys.entity.TMenuDict;
import com.demo.domain.model.sys.vo.TMenuDictVO;

import java.util.List;

/**
 * Created by jky on 2021/08/10.
 */
public interface ITMenuDictService extends IService<TMenuDict> {

    int save(TMenuDictVO tMenuDictVO);

    int deleteByModel(TMenuDictVO tMenuDictVO);

    int update(TMenuDictVO tMenuDictVO);

    TMenuDict findByModel(TMenuDictVO tMenuDictVO);

    List<TMenuDict> findModelList(TMenuDictVO tMenuDictVO);

    IPage<TMenuDictDto> selectByPage(TMenuDictVO tMenuDictVO, Integer page, Integer size);

    /**
     * 根据vo查询树形集合
     * @param tMenuDictVO
     * @return
     */
    List<TMenuDictDto> findListByDto(TMenuDictVO tMenuDictVO);

    int updateDelModel(String[] ids);

    /**
     * 查询所有菜单
     * @param tMenuDictVO
     * @return
     */
    List<TMenuDictDto> findAllListByDto(TMenuDictVO tMenuDictVO);

    /**
     * 保存按钮
     * @param tMenuDictVOList
     * @return
     */
    int buttonSave(List<TMenuDictVO> tMenuDictVOList);

    /**
     * @Description: 自定义表单分配权限
     * @Author: yujunhong@aliyun.com
     * @Date: 2021/9/8 15:56
     */
    int assignPermissions(TMenuDictVO tMenuDictVO);

    /**
     * @Description: 获取当前已经选中的所有按钮
     * @Author: yujunhong@aliyun.com
     * @Date: 2021/9/9 8:51
     */
    List<TMenuDict> findCheckedModelList(TMenuDictVO tMenuDictVO);
}
