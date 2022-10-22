package com.toucan.shopping.modules.seller.service;

import com.toucan.shopping.modules.seller.entity.FreightTemplateAreaRule;
import com.toucan.shopping.modules.seller.entity.FreightTemplateDefaultRule;
import com.toucan.shopping.modules.seller.vo.FreightTemplateAreaRuleVO;

import java.util.List;

/**
 * 运费地区规则
 */
public interface FreightTemplateAreaRuleService {

    int saves(List<FreightTemplateAreaRule> entitys);


    List<FreightTemplateAreaRule> queryListByVO(FreightTemplateAreaRuleVO query);

    int deleteByTemplateId(Long templateId);

    /**
     * 将删除记录恢复
     * @param idList
     * @return
     */
    int updateResumeByIdList(List<Long> idList);

}
