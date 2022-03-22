package com.toucan.shopping.modules.product.mapper;

import com.toucan.shopping.modules.product.entity.ShopProduct;
import com.toucan.shopping.modules.product.page.ShopProductPageInfo;
import com.toucan.shopping.modules.product.vo.ShopProductVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface ShopProductMapper {

    List<ShopProduct> queryAllList(ShopProduct queryModel);

    int insert(ShopProduct entity);

    int deleteById(Long id);

    int updateApproveStatus(Long id, Integer approveStatus);


    /**
     * 查询列表页
     * @param pageInfo
     * @return
     */
    List<ShopProductVO> queryListPage(ShopProductPageInfo pageInfo);

    /**
     * 返回列表页数量
     * @param pageInfo
     * @return
     */
    Long queryListPageCount(ShopProductPageInfo pageInfo);

    List<ShopProductVO> queryList(ShopProductVO shopProductVO);

    ShopProductVO findById(Long id);

}
