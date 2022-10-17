package com.toucan.shopping.modules.seller.service;

import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.seller.entity.FreightTemplate;
import com.toucan.shopping.modules.seller.page.FreightTemplatePageInfo;
import com.toucan.shopping.modules.seller.vo.FreightTemplateVO;

/**
 * 运费模板
 */
public interface FreightTemplateService {

    PageInfo<FreightTemplate> queryListPage(FreightTemplatePageInfo queryPageInfo);

    Long queryCount(FreightTemplateVO freightTemplateVO);



    int save(FreightTemplate entity);

}
