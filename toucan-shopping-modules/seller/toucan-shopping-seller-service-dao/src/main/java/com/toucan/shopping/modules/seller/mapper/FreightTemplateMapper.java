package com.toucan.shopping.modules.seller.mapper;

import com.toucan.shopping.modules.seller.entity.FreightTemplate;
import com.toucan.shopping.modules.seller.entity.SellerShop;
import com.toucan.shopping.modules.seller.page.FreightTemplatePageInfo;
import com.toucan.shopping.modules.seller.page.SellerShopPageInfo;
import com.toucan.shopping.modules.seller.vo.FreightTemplateVO;
import com.toucan.shopping.modules.seller.vo.SellerShopVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface FreightTemplateMapper {

    List<FreightTemplate> queryListPage(FreightTemplatePageInfo queryPageInfo);

    Long queryListPageCount(FreightTemplatePageInfo queryPageInfo);

    Long queryCount(FreightTemplateVO freightTemplateVO);

    int insert(FreightTemplate entity);

}
