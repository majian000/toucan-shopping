package com.toucan.shopping.modules.seller.vo;

import com.toucan.shopping.modules.seller.entity.FreightTemplate;
import com.toucan.shopping.modules.seller.entity.SellerLoginHistory;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 运费模板
 * @author majian
 * @date 2022-10-4 14:44:06
 */
@Data
public class FreightTemplateVO extends FreightTemplate {


    //运费规则查询单项 如:3平邮
    private String oneTransportModel;

    //运费规则 1:快递 2:EMS 3:平邮
    private String transportModelExpress; //快递
    private String transportModelEms; //EMS
    private String transportModelOrdinaryMail; //平邮



    private FreightTemplateDefaultRuleVO expressDefaultRule; //快递默认运费规则

    private List<FreightTemplateAreaRuleVO> expressAreaRules; //快递地区规则

    private FreightTemplateDefaultRuleVO emsDefaultRule; //EMS默认运费规则

    private List<FreightTemplateAreaRuleVO> emsAreaRules; //EMS地区规则

    private FreightTemplateDefaultRuleVO ordinaryMailDefaultRule; //平邮默认运费规则

    private List<FreightTemplateAreaRuleVO> ordinaryMailAreaRules; //平邮地区规则


    private Map<String,String> cityCodeToProvinceCode; //市级编码对应的省级编码

    private Map<String,String> cityNameToCityCode; //市级名称对应市级编码

    private Map<String,String> cityNameToProvinceName; //市级名称对应省级名称



}
