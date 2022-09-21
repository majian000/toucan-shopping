package com.toucan.shopping.modules.seller.service;

import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.seller.entity.FreightTemplate;
import com.toucan.shopping.modules.seller.page.FreightTemplatePageInfo;

/**
 * 运费模板
 */
public interface FreightTemplateService {

    public PageInfo<FreightTemplate> queryListPage(FreightTemplatePageInfo queryPageInfo);

}
