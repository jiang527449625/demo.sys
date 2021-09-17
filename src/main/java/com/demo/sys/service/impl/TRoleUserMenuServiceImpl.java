package com.demo.sys.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.common.utils.SnowflakeIdUtil;
import com.demo.common.utils.StringUtil;
import com.demo.common.utils.TreeDataUtils;
import com.demo.domain.model.sys.dto.TMenuDictDto;
import com.demo.domain.model.sys.dto.TSysRoleDto;
import com.demo.domain.model.sys.entity.TRoleUserMenu;
import com.demo.domain.model.sys.entity.TSysRole;
import com.demo.domain.model.sys.vo.TMenuDictVO;
import com.demo.domain.model.sys.vo.TRoleUserMenuVO;
import com.demo.domain.model.sys.vo.TSysRoleVO;
import com.demo.sys.dao.TMenuDictMapper;
import com.demo.sys.dao.TRoleUserMenuMapper;
import com.demo.sys.dao.TSysRoleMapper;
import com.demo.sys.service.ITRoleUserMenuService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * Created by jky on 2021/08/10.
 */
@Service
@Transactional
public class TRoleUserMenuServiceImpl extends ServiceImpl<TRoleUserMenuMapper, TRoleUserMenu> implements ITRoleUserMenuService {
    @Resource
    private TRoleUserMenuMapper tRoleUserMenuMapper;

    @Resource
    private TSysRoleMapper tSysRoleMapper;

    @Resource
    private TMenuDictMapper tMenuDictMapper;

    @Override
    public int save(TRoleUserMenuVO tRoleUserMenuVO) {
        TRoleUserMenu tRoleUserMenu = new TRoleUserMenu();
        BeanUtils.copyProperties(tRoleUserMenuVO, tRoleUserMenu);
        String[] menuUuids = tRoleUserMenu.getMenuUuid().split(",");
        int count = 0;
        if (menuUuids.length > 0) {
            //先删除
            TRoleUserMenu roleUserMenu = new TRoleUserMenu();
            //角色/用户ID
            roleUserMenu.setRuUuid(tRoleUserMenu.getRuUuid());
            //先删除角色/用户ID对应的数据
            tRoleUserMenuMapper.delete(Wrappers.<TRoleUserMenu>lambdaUpdate(roleUserMenu));
            //后执行保存
            for (String menuUuid : menuUuids) {
                if (StringUtils.isNotBlank(menuUuid)) {
                    //主键
                    tRoleUserMenu.setId(SnowflakeIdUtil.newStringId());
                    //角色/用户ID
                    tRoleUserMenu.setRuUuid(tRoleUserMenu.getRuUuid());
                    //菜单ID
                    tRoleUserMenu.setMenuUuid(menuUuid);
                    //执行保存
                    tRoleUserMenuMapper.insert(tRoleUserMenu);
                    count += 1;
                }
            }
        }
        return count;
    }

    @Override
    public int deleteByModel(TRoleUserMenuVO tRoleUserMenuVO) {
        TRoleUserMenu tRoleUserMenu = new TRoleUserMenu();
        BeanUtils.copyProperties(tRoleUserMenuVO, tRoleUserMenu);
        return tRoleUserMenuMapper.delete(Wrappers.<TRoleUserMenu>lambdaUpdate(tRoleUserMenu));
    }

    @Override
    public int update(TRoleUserMenuVO tRoleUserMenuVO) {
        TRoleUserMenu tRoleUserMenu = new TRoleUserMenu();
        BeanUtils.copyProperties(tRoleUserMenuVO, tRoleUserMenu);
        return tRoleUserMenuMapper.updateById(tRoleUserMenu);
    }

    @Override
    public TRoleUserMenu findByModel(TRoleUserMenuVO tRoleUserMenuVO) {
        TRoleUserMenu tRoleUserMenu = new TRoleUserMenu();
        BeanUtils.copyProperties(tRoleUserMenuVO, tRoleUserMenu);
        return tRoleUserMenuMapper.selectOne(Wrappers.<TRoleUserMenu>lambdaQuery(tRoleUserMenu));
    }

    @Override
    public List<TRoleUserMenu> findModelList(TRoleUserMenuVO tRoleUserMenuVO) {
        TRoleUserMenu tRoleUserMenu = new TRoleUserMenu();
        BeanUtils.copyProperties(tRoleUserMenuVO, tRoleUserMenu);
        return tRoleUserMenuMapper.selectList(Wrappers.<TRoleUserMenu>lambdaQuery(tRoleUserMenu));
    }

    @Override
    public IPage<TRoleUserMenu> selectByPage(TRoleUserMenuVO tRoleUserMenuVO, Integer page, Integer size) {
        IPage<TRoleUserMenu> iPage = new Page<>(page, size);
        TRoleUserMenu tRoleUserMenu = new TRoleUserMenu();
        BeanUtils.copyProperties(tRoleUserMenuVO, tRoleUserMenu);
        return tRoleUserMenuMapper.selectPage(iPage, Wrappers.<TRoleUserMenu>lambdaQuery(tRoleUserMenu));
    }

    @Override
    public Map<String, List<TMenuDictDto>> getCheckedMenuList(TRoleUserMenuVO tRoleUserMenuVO) {
        Map<String, List<TMenuDictDto>> map = new HashMap<>();
        //角色ID
        String roleUuid = tRoleUserMenuVO.getRoleUuid();
        //用户ID
        String userUuid = tRoleUserMenuVO.getUserUuid();
        //菜单名称或者拼音码
        String menuNameOrInputCode = tRoleUserMenuVO.getMenuNameOrInputCode();
        //角色
        TSysRole tSysRole = new TSysRole();
        //获取角色信息
        if (StringUtil.isNotNull(roleUuid)) {
            //查角色
            tSysRole = tSysRoleMapper.selectById(roleUuid);
        }

        // 角色/用户-菜单中间表 TRoleUserMenu
        //角色：
        //父角色，则渲染的模板就是所有的菜单集合；同时返回该角色对应的ids
        //子角色，则渲染的模板就是父类的菜单集合；同时返回该角色对应的菜单ids
        //用户--必须是处于角色之下：
        // ①用户渲染的模板就是当前角色的菜单集合；
        // ②同时返回该用户对应的ids

        if (null != tSysRole) {
            //用户为空，则说明选择的是角色
            if (StringUtil.isValNull(userUuid)) {
                //父角色
                if ("0".equals(tSysRole.getParentUuid())) {
                    //获取当前角色菜单
                    map.put("result", getCurrentNodeMenus(roleUuid, null));
                    //获取根角色菜单
                    TMenuDictVO tMenuDictVO = new TMenuDictVO();
                    if (StringUtil.isNotNull(menuNameOrInputCode)) {
                        tMenuDictVO.setMenuNameOrInputCode(menuNameOrInputCode);
                    }
                    map.put("template", (List<TMenuDictDto>) TreeDataUtils.getSysTreeNodeDto(tMenuDictMapper.selectByDto(tMenuDictVO), "id", "parentUuid", "children"));
                } else {
                    //子角色
                    //获取当前角色的菜单
                    map.put("result", getCurrentNodeMenus(roleUuid, null));
                    //获取当前子角色的上一级角色菜单
                    map.put("template", getUpperLevelRoleMenus(roleUuid, menuNameOrInputCode));
                }
            } else {
                //用户不为空，则说明选择的不是角色而是用户
                // 返回当前用户对应的菜单集合
                map.put("result", getCurrentNodeMenus(userUuid, null));
                //返回当前用户的角色选中的菜单集合
                map.put("template", getCurrentNodeMenus(roleUuid, menuNameOrInputCode));
            }
        }
        return map;
    }

    /**
     * @Description: 获取当前角色上一级节点的菜单
     * @Author: yujunhong@aliyun.com
     * @Date: 2021/9/3 17:29
     */
    private List<TMenuDictDto> getUpperLevelRoleMenus(String args, String menuNameOrInputCode) {
        TSysRoleVO tSysRoleVO = new TSysRoleVO();
        tSysRoleVO.setId(args);
        TSysRoleDto tSysRoleDto = tSysRoleMapper.selectRolesWithUpperLevel(tSysRoleVO);
        TRoleUserMenu tRoleUserMenu = new TRoleUserMenu();
        //获取上一级角色的ID
        tRoleUserMenu.setRuUuid(tSysRoleDto.getId());
        //根据ID查询菜单IDS
        List<TRoleUserMenu> tRoleUserMenuList = tRoleUserMenuMapper.selectList(Wrappers.lambdaQuery(tRoleUserMenu));
        if (null != tRoleUserMenuList && tRoleUserMenuList.size() > 0) {
            List<String> ruUuidList = tRoleUserMenuList.stream().map(TRoleUserMenu::getRuUuid).collect(Collectors.toList());
            TMenuDictVO tMenuDictVO = new TMenuDictVO();
            tMenuDictVO.setRuUuidList(ruUuidList);
            if (StringUtil.isNotNull(menuNameOrInputCode)) {
                tMenuDictVO.setMenuNameOrInputCode(menuNameOrInputCode);
            }
            return (List<TMenuDictDto>) TreeDataUtils.getSysTreeNodeDto(tMenuDictMapper.selectByDto(tMenuDictVO), "id", "parentUuid", "children");
        } else {
            return null;
        }
    }

    /**
     * @Description: 获取当前角色节点的菜单
     * @Author: yujunhong@aliyun.com
     * @Date: 2021/9/3 17:29
     */
    private List<TMenuDictDto> getCurrentNodeMenus(String args, String menuNameOrInputCode) {
        TRoleUserMenu tRoleUserMenu = new TRoleUserMenu();
        //获取当前角色的ID
        tRoleUserMenu.setRuUuid(args);
        //根据ID查询菜单IDS
        List<TRoleUserMenu> tRoleUserMenuList = tRoleUserMenuMapper.selectList(Wrappers.lambdaQuery(tRoleUserMenu));
        if (null != tRoleUserMenuList && tRoleUserMenuList.size() > 0) {
            List<String> ruUuidList = tRoleUserMenuList.stream().map(TRoleUserMenu::getRuUuid).collect(Collectors.toList());
            TMenuDictVO tMenuDictVO = new TMenuDictVO();
            tMenuDictVO.setRuUuidList(ruUuidList);
            if (StringUtil.isNotNull(menuNameOrInputCode)) {
                tMenuDictVO.setMenuNameOrInputCode(menuNameOrInputCode);
            }
            return (List<TMenuDictDto>) TreeDataUtils.getSysTreeNodeDto(tMenuDictMapper.selectCurrentByDto(tMenuDictVO), "id", "parentUuid", "children");
        } else {
            return null;
        }
    }
}
