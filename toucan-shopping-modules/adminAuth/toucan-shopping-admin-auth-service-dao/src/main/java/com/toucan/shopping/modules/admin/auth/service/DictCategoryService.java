package com.toucan.shopping.modules.admin.auth.service;


import com.toucan.shopping.modules.admin.auth.entity.DictCategory;
import com.toucan.shopping.modules.admin.auth.page.DictCategoryPageInfo;
import com.toucan.shopping.modules.admin.auth.vo.DictCategoryVO;
import com.toucan.shopping.modules.common.page.PageInfo;

import java.util.List;

public interface DictCategoryService {

    /**
     * 根据查询对象返回列表
     * @param entity
     * @return
     */
    List<DictCategoryVO> findListByEntity(DictCategory entity);

    /**
     * 保存
     * @param entity
     * @return
     */
    int save(DictCategory entity);

    /**
     * 更新
     * @param entity
     * @return
     */
    int update(DictCategory entity);



    /**
     * 判断是否存在
     * @param name
     * @return
     */
    boolean exists(String name);

    /**
     * 查询列表页
     * @param pageInfo
     * @return
     */
    PageInfo<DictCategory> queryListPage(DictCategoryPageInfo pageInfo);

    /**
     *  根据ID删除
     * @param id
     * @return
     */
    int deleteById(Long id);


    /**
     * 根据应用编码查询列表
     * @param appCode
     * @return
     */
    List<DictCategoryVO> queryListByAppCode(String appCode);








}
