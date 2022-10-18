package com.toucan.shopping.modules.seller.mapper;

import com.toucan.shopping.modules.seller.entity.FreightTemplateDefaultRule;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface FreightTemplateDefaultRuleMapper {

    int insert(FreightTemplateDefaultRule entity);
}
