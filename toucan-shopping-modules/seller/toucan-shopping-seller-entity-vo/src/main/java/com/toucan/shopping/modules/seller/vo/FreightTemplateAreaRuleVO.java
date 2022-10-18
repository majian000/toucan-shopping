package com.toucan.shopping.modules.seller.vo;

import com.toucan.shopping.modules.seller.entity.FreightTemplate;
import com.toucan.shopping.modules.seller.entity.FreightTemplateAreaRule;
import lombok.Data;

import java.util.Map;

/**
 * 运费模板地区规则
 *
 * @author majian
 * @date 2022-9-7 15:39:24
 */
@Data
public class FreightTemplateAreaRuleVO extends FreightTemplateAreaRule {

    private String selectAreas; //选择区域名称

    private String selectAreaCodes; //选择区域编码


}
