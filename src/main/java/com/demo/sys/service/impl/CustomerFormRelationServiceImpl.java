package com.demo.sys.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.domain.model.sys.entity.CustomerFormRelation;
import com.demo.domain.model.sys.vo.CustomerFormRelationVO;
import com.demo.sys.dao.CustomerFormRelationMapper;
import com.demo.sys.service.ICustomerFormRelationService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


/**
 * Created by jky on 2021/08/24.
 */
@Service
@Transactional
public class CustomerFormRelationServiceImpl extends ServiceImpl<CustomerFormRelationMapper, CustomerFormRelation> implements ICustomerFormRelationService {
    @Resource
    private CustomerFormRelationMapper customerFormRelationMapper;

    @Override
    public int save(CustomerFormRelationVO customerFormRelationVO) {
//      先删除原按钮绑定的表单
        customerFormRelationMapper.delete(Wrappers.<CustomerFormRelation>lambdaUpdate().eq(CustomerFormRelation :: getMenuId, customerFormRelationVO.getMenuId()));
        CustomerFormRelation customerFormRelation = new CustomerFormRelation();
        BeanUtils.copyProperties(customerFormRelationVO,customerFormRelation);
        return customerFormRelationMapper.insert(customerFormRelation);
    }

    @Override
    public int deleteByModel(CustomerFormRelationVO customerFormRelationVO) {
        return customerFormRelationMapper.delete(Wrappers.<CustomerFormRelation>lambdaUpdate().eq(CustomerFormRelation :: getMenuId, customerFormRelationVO.getMenuId()));
    }

    @Override
    public int update(CustomerFormRelationVO customerFormRelationVO) {
        CustomerFormRelation customerFormRelation = new CustomerFormRelation();
        BeanUtils.copyProperties(customerFormRelationVO,customerFormRelation);
        return customerFormRelationMapper.updateById(customerFormRelation);
    }

    @Override
    public int updateDelModel(String[] ids) {
        int count = 0;
        if(ids.length > 0){
            for(String id : ids){
                count += customerFormRelationMapper.delete(Wrappers.<CustomerFormRelation>lambdaUpdate().eq(CustomerFormRelation :: getMenuId, id));
            }
        }
        return count;
    }

    @Override
    public CustomerFormRelation findByModel(CustomerFormRelationVO customerFormRelationVO) {
        return customerFormRelationMapper.selectOne(Wrappers.<CustomerFormRelation>lambdaQuery().eq(CustomerFormRelation :: getMenuId, customerFormRelationVO.getId()));
    }

    @Override
    public List<CustomerFormRelation> findModelList(CustomerFormRelationVO customerFormRelationVO) {
        CustomerFormRelation customerFormRelation = new CustomerFormRelation();
        BeanUtils.copyProperties(customerFormRelationVO,customerFormRelation);
        return customerFormRelationMapper.selectList(Wrappers.<CustomerFormRelation>lambdaQuery(customerFormRelation));
    }

    @Override
    public IPage<CustomerFormRelation> selectByPage(CustomerFormRelationVO customerFormRelationVO, Integer page, Integer size) {
        IPage<CustomerFormRelation> iPage = new Page<>(page,size);
        CustomerFormRelation customerFormRelation = new CustomerFormRelation();
        BeanUtils.copyProperties(customerFormRelationVO,customerFormRelation);
        return customerFormRelationMapper.selectPage(iPage,Wrappers.<CustomerFormRelation>lambdaQuery(customerFormRelation));
    }
}
