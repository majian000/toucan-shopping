package com.toucan.shopping.modules.seller.vo;

import com.toucan.shopping.modules.seller.entity.FreightTemplate;
import com.toucan.shopping.modules.seller.entity.SellerLoginHistory;
import lombok.Data;

import java.util.List;

/**
 * 运费模板
 * @author majian
 * @date 2022-10-4 14:44:06
 */
@Data
public class FreightTemplateVO extends FreightTemplate {

    private FreightTemplateDefaultRuleVO expressDefaultRule; //快递默认运费规则

    private List<FreightTemplateAreaRuleVO> expressAreaRules; //快递地区规则

    private FreightTemplateDefaultRuleVO emsDefaultRule; //EMS默认运费规则

    private List<FreightTemplateAreaRuleVO> emsAreaRules; //EMS地区规则

    private FreightTemplateDefaultRuleVO ordinaryMailDefaultRule; //平邮默认运费规则

    private List<FreightTemplateAreaRuleVO> ordinaryMailAreaRules; //平邮地区规则




}
