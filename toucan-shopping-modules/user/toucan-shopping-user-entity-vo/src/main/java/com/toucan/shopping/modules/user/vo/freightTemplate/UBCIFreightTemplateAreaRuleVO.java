package com.toucan.shopping.modules.user.vo.freightTemplate;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.List;

/**
 * 运费模板地区规则
 *
 * @author majian
 * @date 2022-9-7 15:39:24
 */
@Data
public class UBCIFreightTemplateAreaRuleVO {


    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id; //主键

    /**
     * 所在省份编码
     */
    private String provinceCode;

    /**
     * 所在地市编码
     */
    private String cityCode;

    /**
     * 所在区县编码
     */
    private String areaCode;


    /**
     * 所在省份名称
     */
    private String provinceName;

    /**
     * 所在地市名称
     */
    private String cityName;

    /**
     * 所在区县名称
     */
    private String areaName;

    private String transportModel; //运送方式 1:快递 2:EMS 3:平邮

    private Double firstWeight; //首重量 单位KG/件

    private Double firstWeightMoney; //首重金额

    private Double appendWeight; //续重量 单位KG/件

    private Double appendWeightMoney; //续重金额

    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long groupId; //分组ID



    /**
     * 默认规则ID
     */
    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long defaultRuleId;

    private String selectAreas; //选择区域名称

    private String selectAreaCodes; //选择区域编码

    private List<UBCIFreightTemplateAreaRuleVO> selectItems; //所有选择项(可能存在选择多个省情况)

}
