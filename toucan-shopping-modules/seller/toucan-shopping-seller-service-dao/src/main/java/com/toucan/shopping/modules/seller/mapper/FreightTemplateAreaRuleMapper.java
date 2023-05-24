package com.toucan.shopping.modules.seller.mapper;

import com.toucan.shopping.modules.seller.entity.FreightTemplateAreaRule;
import com.toucan.shopping.modules.seller.vo.FreightTemplateAreaRuleVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface FreightTemplateAreaRuleMapper {

    int inserts(List<FreightTemplateAreaRule> entitys);

    List<FreightTemplateAreaRule> queryListByVO(FreightTemplateAreaRuleVO query);

    int deleteByTemplateId(Long templateId);

    int updateResumeByIdList(List<Long> idList);

}
