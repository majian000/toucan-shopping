package com.toucan.shopping.modules.seller.mapper;

import com.toucan.shopping.modules.seller.entity.FreightTemplateDefaultRule;
import com.toucan.shopping.modules.seller.vo.FreightTemplateDefaultRuleVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface FreightTemplateDefaultRuleMapper {

    int insert(FreightTemplateDefaultRule entity);

    List<FreightTemplateDefaultRule> queryListByVO(FreightTemplateDefaultRuleVO freightTemplateDefaultRuleVO);

    int deleteByTemplateId(Long templateId);


}
