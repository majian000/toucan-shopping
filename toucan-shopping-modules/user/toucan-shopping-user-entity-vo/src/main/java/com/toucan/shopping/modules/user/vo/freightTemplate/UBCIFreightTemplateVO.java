package com.toucan.shopping.modules.user.vo.freightTemplate;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.List;
import java.util.Map;


/**
 * 用户购物车选择项运费模板
 * @author majian
 * @date 2022-11-1 15:22:21
 */
@Data
public class UBCIFreightTemplateVO {
    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id; //主键

    private String name; //模板名称

    private Short freightStatus; //运费状态 1:自定义运费 2:包邮


    private String transportModel; //运送方式 1:快递 2:EMS 3:平邮 (多个用,分割)

    private Short valuationMethod; //计价方式 1:按件数 2:按重量 3:按体积



    private List<Long> idList;

    //运费规则查询单项 如:3平邮
    private String oneTransportModel;

    //运费规则 1:快递 2:EMS 3:平邮
    private String transportModelExpress; //快递
    private String transportModelEms; //EMS
    private String transportModelOrdinaryMail; //平邮


    private UBCIFreightTemplateDefaultRuleVO expressDefaultRule; //快递默认运费规则

    private List<UBCIFreightTemplateAreaRuleVO> expressAreaRules; //快递地区规则

    private UBCIFreightTemplateDefaultRuleVO emsDefaultRule; //EMS默认运费规则

    private List<UBCIFreightTemplateAreaRuleVO> emsAreaRules; //EMS地区规则

    private UBCIFreightTemplateDefaultRuleVO ordinaryMailDefaultRule; //平邮默认运费规则

    private List<UBCIFreightTemplateAreaRuleVO> ordinaryMailAreaRules; //平邮地区规则


    private Map<String,String> cityCodeToProvinceCode; //市级编码对应的省级编码

    private Map<String,String> cityNameToCityCode; //市级名称对应市级编码

    private Map<String,String> cityNameToProvinceName; //市级名称对应省级名称

}
