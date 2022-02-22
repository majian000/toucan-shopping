package com.toucan.shopping.modules.seller.service;




import com.toucan.shopping.modules.seller.entity.ShopCategory;
import com.toucan.shopping.modules.seller.page.ShopCategoryTreeInfo;
import com.toucan.shopping.modules.seller.vo.ShopCategoryTreeVO;
import com.toucan.shopping.modules.seller.vo.ShopCategoryVO;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface ShopCategoryService {

    List<ShopCategory> queryList(ShopCategoryVO shopCategory);

    List<ShopCategory> queryListOrderByCategorySortAsc(ShopCategoryVO shopCategory);


    List<ShopCategory> queryTop1(ShopCategoryVO shopCategory);

    List<ShopCategory> queryBottom1(ShopCategoryVO shopCategory);

    List<ShopCategory> queryPcIndexList(ShopCategoryVO shopCategory);

    /**
     * 保存实体
     * @param shopCategory
     * @return
     */
    int save(ShopCategory shopCategory);

    /**
     * 保存实体
     * @param shopCategorys
     * @return
     */
    int saves(ShopCategory[] shopCategorys);


    int update(ShopCategory shopCategory);


    int updateCategorySort(ShopCategory shopCategory);

    int updateName(ShopCategory shopCategory);

    ShopCategory queryById(Long id);

    ShopCategory queryByIdAndUserMainIdAndShopId(Long id,Long userMainId,Long shopId);


    ShopCategory queryByIdAndShopId(Long id,Long shopId);

    int deleteById(Long id);

    List<ShopCategory> findByParentId(Long parentId);


    Long queryCount(ShopCategory shopCategory);

    /**
     * 删除所有子节点
     * @param id
     */
    void deleteChildrenByParentId(Long id);

    /**
     * 查询最大排序值
     * @param userMainId
     * @param shopId
     * @return
     */
    Long queryMaxSort(Long userMainId,Long shopId);


    /**
     * 从一个集合中找到所有子节点并设置上
     * @param ShopCategoryVOS
     * @param currentNode
     */
    void setChildren(List<ShopCategory> ShopCategoryVOS, ShopCategoryTreeVO currentNode) throws InvocationTargetException, IllegalAccessException ;


    /**
     * 查询树表格
     * @param queryTreeInfo
     * @return
     */
    List<ShopCategoryVO> findTreeTable(ShopCategoryTreeInfo queryTreeInfo);



    /**
     * 查询所有子节点
     * @param children
     * @param query
     */
    void queryChildren(List<ShopCategory> children, ShopCategory query);



    /**
     * 设置分类名称路径
     * @param categoryVOS
     */
    void setNamePath(List<ShopCategoryVO> categoryVOS,List<Long> parentIdList);


}
