package com.toucan.shopping.modules.product.service;


import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.product.entity.AttributeKey;
import com.toucan.shopping.modules.product.page.AttributeKeyPageInfo;
import com.toucan.shopping.modules.product.vo.AttributeKeyVO;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface AttributeKeyService {

    /**
     * 查询列表页
     * @param queryPageInfo
     * @return
     */
    PageInfo<AttributeKeyVO> queryListPage(AttributeKeyPageInfo queryPageInfo);


    /**
     * 查询列表页
     * @param queryPageInfo
     * @return
     */
    PageInfo<AttributeKeyVO> queryListPageBySortDesc(AttributeKeyPageInfo queryPageInfo);


    /**
     * 保存实体
     * @param attributeKey
     * @return
     */
    int save(AttributeKey attributeKey);


    /**
     * 修改实体
     * @param attributeKey
     * @return
     */
    int update(AttributeKey attributeKey);


    /**
     * 根据ID删除
     * @param id
     * @return
     */
    int deleteById(Long id);


    /**
     * 根据查询对象查询列表
     * @param query
     * @return
     */
    List<AttributeKeyVO> queryList(AttributeKeyVO query);

    /**
     * 查询所有子节点
     * @return
     */
    void queryChildList(List<AttributeKeyVO> allChildList,Long parentId);

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    AttributeKeyVO queryById(Long id);

    /**
     * 根据查询对象查询列表
     * @param query
     * @return
     */
    List<AttributeKeyVO> queryListBySortDesc(AttributeKeyVO query);


    Long queryCount(AttributeKeyVO attributeKeyVO);


    /**
     * 从一个集合中找到所有子节点并设置上
     * @param attributeKeyVOS
     * @param currentNode
     */
    void setChildren(List<AttributeKeyVO> attributeKeyVOS, AttributeKeyVO currentNode) throws InvocationTargetException, IllegalAccessException ;

    /**
     * 设置该节点的所有子节点和所有子节点的值
     * @param currentNode
     */
    void setChildNodeAndChildNodeValue(AttributeKeyVO currentNode) ;

}
