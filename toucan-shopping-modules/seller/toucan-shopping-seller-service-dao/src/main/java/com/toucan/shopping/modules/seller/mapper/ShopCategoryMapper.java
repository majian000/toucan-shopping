package com.toucan.shopping.modules.seller.mapper;

import com.toucan.shopping.modules.seller.entity.ShopCategory;
import com.toucan.shopping.modules.seller.page.ShopCategoryTreeInfo;
import com.toucan.shopping.modules.seller.vo.ShopCategoryVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface ShopCategoryMapper {

    List<ShopCategory> queryList(ShopCategoryVO shopCategory);

    List<ShopCategory> queryListOrderByCategorySortAsc(ShopCategoryVO shopCategory);

    List<ShopCategory> queryTop1(ShopCategoryVO shopCategory);

    List<ShopCategory> queryBottom1(ShopCategoryVO shopCategory);

    List<ShopCategory> queryPcIndexList(ShopCategoryVO shopCategory);

    int insert(ShopCategory shopCategory);

    int inserts(ShopCategory[] entitys);

    ShopCategory queryById(Long id);

    ShopCategory queryByIdAndUserMainIdAndShopId(Long id,Long userMainId,Long shopId);

    ShopCategory queryByIdAndShopId(Long id,Long shopId);

    List<ShopCategory> queryByParentId(Long parentId);

    int deleteById(Long id);

    Long queryMaxSort(Long userMainId,Long shopId);

    int update(ShopCategory shopCategory);

    int updateCategorySort(ShopCategory shopCategory);

    int updateName(ShopCategory shopCategory);

    Long queryCount(ShopCategory shopCategory);

    /**
     * 查询表格树
     * @param queryTreeInfo
     * @return
     */
    List<ShopCategoryVO> findTreeTableByPageInfo(ShopCategoryTreeInfo queryTreeInfo);


}
