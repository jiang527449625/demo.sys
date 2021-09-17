package com.demo.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.demo.domain.model.sys.dto.TMenuDictDto;
import com.demo.domain.model.sys.entity.TMenuDict;
import com.demo.domain.model.sys.vo.TMenuDictVO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface TMenuDictMapper extends BaseMapper<TMenuDict> {
    /**
     * 根据vo查询树形集合
     *
     * @param tMenuDictVO
     * @return
     */
    List<TMenuDictDto> selectByDto(TMenuDictVO tMenuDictVO);

    /**
     * @Description: 查询当前角色/用户对应的菜单集合
     * @Author: yujunhong@aliyun.com
     * @Date: 2021/9/6 16:21
     */
    List<TMenuDictDto> selectCurrentByDto(TMenuDictVO tMenuDictVO);

    /**
     * @Description: 获取当前已经选中的所有按钮
     * @Author: yujunhong@aliyun.com
     * @Date: 2021/9/9 8:51
     */
    List<TMenuDict> findCheckedModelList(TMenuDictVO tMenuDictVO);

    /**
     * @Description: 更新操作
     * @Author: yujunhong@aliyun.com
     * @Date: 2021/9/9 10:26
     */
    int updateModel(TMenuDictVO tMenuDictVO);

    /**
     * 根据vo查询树形集合
     *
     * @param iPage
     * @param tMenuDictVO
     * @return
     */
    IPage<TMenuDictDto> selectByDto(IPage<TMenuDictDto> iPage, TMenuDictVO tMenuDictVO);
}