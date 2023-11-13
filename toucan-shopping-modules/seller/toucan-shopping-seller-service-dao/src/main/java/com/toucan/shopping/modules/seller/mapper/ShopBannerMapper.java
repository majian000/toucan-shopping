package com.toucan.shopping.modules.seller.mapper;

import com.toucan.shopping.modules.seller.entity.ShopBanner;
import com.toucan.shopping.modules.seller.page.ShopBannerPageInfo;
import com.toucan.shopping.modules.seller.vo.ShopBannerVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface ShopBannerMapper {

    List<ShopBannerVO> queryList(ShopBannerVO entity);

    List<ShopBannerVO> queryIndexList(ShopBannerVO entity);

    int insert(ShopBanner banner);

    int deleteById(Long id);

    int update(ShopBanner banner);

    ShopBanner findById(Long id);


    /**
     * 查询列表页
     * @param pageInfo
     * @return
     */
    List<ShopBannerVO> queryListPage(ShopBannerPageInfo pageInfo);

    /**
     * 返回列表页数量
     * @param pageInfo
     * @return
     */
    Long queryListPageCount(ShopBannerPageInfo pageInfo);

}
