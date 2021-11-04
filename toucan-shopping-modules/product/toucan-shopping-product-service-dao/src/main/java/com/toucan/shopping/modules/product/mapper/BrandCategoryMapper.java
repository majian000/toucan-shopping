package com.toucan.shopping.modules.product.mapper;

import com.toucan.shopping.modules.product.entity.Brand;
import com.toucan.shopping.modules.product.entity.BrandCategory;
import com.toucan.shopping.modules.product.page.BrandCategoryPageInfo;
import com.toucan.shopping.modules.product.vo.BrandCategoryVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface BrandCategoryMapper {

    List<BrandCategory> queryAllList(BrandCategory queryModel);

    List<BrandCategory> queryList(BrandCategory queryModel);

    int insert(BrandCategory entity);

    List<BrandCategoryVO> queryListByBrandIds(List<Long> brandIds);



    /**
     * 查询列表页
     * @param pageInfo
     * @return
     */
    List<BrandCategoryVO> queryListPage(BrandCategoryPageInfo pageInfo);




    /**
     * 返回列表页数量
     * @param pageInfo
     * @return
     */
    Long queryListPageCount(BrandCategoryPageInfo pageInfo);

    /**
     * 返回所有品牌ID
     * @param categoryIds
     * @return
     */
    List<Long> queryBrandIdListByCategoryId(List<Long> categoryIds);

    int deleteByBrandId(Long brandId);

}
