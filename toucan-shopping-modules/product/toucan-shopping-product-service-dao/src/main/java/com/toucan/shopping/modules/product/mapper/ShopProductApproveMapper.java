package com.toucan.shopping.modules.product.mapper;

import com.toucan.shopping.modules.product.entity.ShopProductApprove;
import com.toucan.shopping.modules.product.page.ShopProductApprovePageInfo;
import com.toucan.shopping.modules.product.vo.ShopProductApproveVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface ShopProductApproveMapper {

    List<ShopProductApprove> queryAllList(ShopProductApprove queryModel);

    int insert(ShopProductApprove entity);

    int deleteById(Long id);

    int updateApproveStatus(Long id, Integer approveStatus);

    int updateApproveStatusAndProductId(Long id, Integer approveStatus, Long productId, String productUuid);


    /**
     * 查询列表页
     * @param pageInfo
     * @return
     */
    List<ShopProductApproveVO> queryListPage(ShopProductApprovePageInfo pageInfo);

    /**
     * 返回列表页数量
     * @param pageInfo
     * @return
     */
    Long queryListPageCount(ShopProductApprovePageInfo pageInfo);

    List<ShopProductApproveVO> queryList(ShopProductApproveVO shopProductVO);

    ShopProductApproveVO findById(Long id);

}
