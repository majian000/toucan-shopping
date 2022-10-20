package com.toucan.shopping.modules.seller.service.impl;

import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.seller.entity.FreightTemplate;
import com.toucan.shopping.modules.seller.entity.FreightTemplateDefaultRule;
import com.toucan.shopping.modules.seller.mapper.FreightTemplateDefaultRuleMapper;
import com.toucan.shopping.modules.seller.mapper.FreightTemplateMapper;
import com.toucan.shopping.modules.seller.page.FreightTemplatePageInfo;
import com.toucan.shopping.modules.seller.service.FreightTemplateDefaultRuleService;
import com.toucan.shopping.modules.seller.service.FreightTemplateService;
import com.toucan.shopping.modules.seller.vo.FreightTemplateDefaultRuleVO;
import com.toucan.shopping.modules.seller.vo.FreightTemplateVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 运费默认规则
 * @author majian
 * @date 2022-10-18 15:25:03
 */
@Service
public class FreightTemplateDefaultRuleServiceImpl implements FreightTemplateDefaultRuleService {


    @Autowired
    private FreightTemplateDefaultRuleMapper freightTemplateDefaultRuleMapper;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public int save(FreightTemplateDefaultRule entity) {
        return freightTemplateDefaultRuleMapper.insert(entity);
    }

    @Override
    public List<FreightTemplateDefaultRule> queryListByVO(FreightTemplateDefaultRuleVO freightTemplateDefaultRuleVO) {
        return freightTemplateDefaultRuleMapper.queryListByVO(freightTemplateDefaultRuleVO);
    }
}
