package com.toucan.shopping.modules.user.vo.freightTemplate;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * 运费模板默认规则
 *
 * @author majian
 * @date 2022-9-7 15:39:24
 */
@Data
public class UBCIFreightTemplateDefaultRuleVO {


    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id; //主键

    private String transportModel; //运送方式 1:快递 2:EMS 3:平邮

    private Double defaultWeight; //默认XXX.XX重量以内 单位KG/件

    private Double defaultWeightMoney; //默认XXX.XX重量以内金额

    private Double defaultAppendWeight; //默认增加XXX.XX重量以内 单位KG/件

    private Double defaultAppendWeightMoney; //默认增加XXX.XX重量以内金额



}
