package com.demo.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.demo.domain.model.sys.dto.TMenuDictDto;
import com.demo.domain.model.sys.entity.TRoleUserMenu;
import com.demo.domain.model.sys.vo.TRoleUserMenuVO;

import java.util.List;
import java.util.Map;

/**
 * Created by jky on 2021/08/10.
 */
public interface ITRoleUserMenuService extends IService<TRoleUserMenu> {

    int save(TRoleUserMenuVO tRoleUserMenuVO);

    int deleteByModel(TRoleUserMenuVO tRoleUserMenuVO);

    int update(TRoleUserMenuVO tRoleUserMenuVO);

    TRoleUserMenu findByModel(TRoleUserMenuVO tRoleUserMenuVO);

    List<TRoleUserMenu> findModelList(TRoleUserMenuVO tRoleUserMenuVO);

    IPage<TRoleUserMenu> selectByPage(TRoleUserMenuVO tRoleUserMenuVO, Integer page, Integer size);

    /**
     * @Description: 查询待选菜单集合
     * @Author: yujunhong@aliyun.com
     * @Date: 2021/8/23 15:56
     */
    Map<String, List<TMenuDictDto>> getCheckedMenuList(TRoleUserMenuVO tRoleUserMenuVO);

}
