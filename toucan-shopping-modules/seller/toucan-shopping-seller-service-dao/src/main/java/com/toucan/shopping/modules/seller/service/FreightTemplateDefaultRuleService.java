package com.toucan.shopping.modules.seller.service;

import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.seller.entity.FreightTemplate;
import com.toucan.shopping.modules.seller.entity.FreightTemplateDefaultRule;
import com.toucan.shopping.modules.seller.page.FreightTemplatePageInfo;
import com.toucan.shopping.modules.seller.vo.FreightTemplateDefaultRuleVO;
import com.toucan.shopping.modules.seller.vo.FreightTemplateVO;

import java.util.List;

/**
 * 运费默认规则
 */
public interface FreightTemplateDefaultRuleService {

    int save(FreightTemplateDefaultRule entity);

    List<FreightTemplateDefaultRule> queryListByVO(FreightTemplateDefaultRuleVO freightTemplateDefaultRuleVO);

    int deleteByTemplateId(Long templateId);

}
