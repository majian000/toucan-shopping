package com.toucan.shopping.modules.seller.service.impl;

import com.toucan.shopping.modules.seller.entity.FreightTemplateAreaRule;
import com.toucan.shopping.modules.seller.entity.FreightTemplateDefaultRule;
import com.toucan.shopping.modules.seller.mapper.FreightTemplateAreaRuleMapper;
import com.toucan.shopping.modules.seller.mapper.FreightTemplateDefaultRuleMapper;
import com.toucan.shopping.modules.seller.service.FreightTemplateAreaRuleService;
import com.toucan.shopping.modules.seller.service.FreightTemplateDefaultRuleService;
import com.toucan.shopping.modules.seller.vo.FreightTemplateAreaRuleVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 运费默认规则
 * @author majian
 * @date 2022-10-18 16:11:16
 */
@Service
public class FreightTemplateAreaRuleServiceImpl implements FreightTemplateAreaRuleService {


    @Autowired
    private FreightTemplateAreaRuleMapper freightTemplateAreaRuleMapper;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public int saves(List<FreightTemplateAreaRule> entitys) {
        return freightTemplateAreaRuleMapper.inserts(entitys);
    }

    @Override
    public List<FreightTemplateAreaRule> queryListByVO(FreightTemplateAreaRuleVO query) {
        return freightTemplateAreaRuleMapper.queryListByVO(query);
    }

    @Override
    public int deleteByTemplateId(Long templateId) {
        return freightTemplateAreaRuleMapper.deleteByTemplateId(templateId);
    }

    @Override
    public int updateResumeByIdList(List<Long> idList) {
        return freightTemplateAreaRuleMapper.updateResumeByIdList(idList);
    }
}
