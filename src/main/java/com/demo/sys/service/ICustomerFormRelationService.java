package com.demo.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.demo.domain.model.sys.entity.CustomerFormRelation;
import com.demo.domain.model.sys.vo.CustomerFormRelationVO;

import java.util.List;

/**
 * Created by jky on 2021/08/24.
 */
public interface ICustomerFormRelationService extends IService<CustomerFormRelation> {

    int save(CustomerFormRelationVO customerFormRelationVO);

    int deleteByModel(CustomerFormRelationVO customerFormRelationVO);

    int update(CustomerFormRelationVO customerFormRelationVO);

    int updateDelModel(String[] ids);

    CustomerFormRelation findByModel(CustomerFormRelationVO customerFormRelationVO);

    List<CustomerFormRelation> findModelList(CustomerFormRelationVO customerFormRelationVO);

    IPage<CustomerFormRelation> selectByPage(CustomerFormRelationVO customerFormRelationVO, Integer page, Integer size);
}
