package com.toucan.shopping.modules.seller.mapper;

import com.toucan.shopping.modules.seller.entity.ShopImage;
import com.toucan.shopping.modules.seller.entity.ShopImage;
import com.toucan.shopping.modules.seller.page.ShopImagePageInfo;
import com.toucan.shopping.modules.seller.vo.ShopImageVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 * 店铺图片
 * @author majian
 */
@Mapper
public interface ShopImageMapper {

    ShopImage findById(Long id);




    /**
     * 查询列表页
     * @param pageInfo
     * @return
     */
    List<ShopImageVO> queryListPage(ShopImagePageInfo pageInfo);

    /**
     * 返回列表页数量
     * @param pageInfo
     * @return
     */
    Long queryListPageCount(ShopImagePageInfo pageInfo);
    
}
