package com.toucan.shopping.modules.product.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.toucan.shopping.modules.product.entity.AttributeKey;
import com.toucan.shopping.modules.product.entity.AttributeValue;
import lombok.Data;

import java.util.List;

/**
 * 属性值
 *
 * @author majian
 */
@Data
public class AttributeValueVO extends AttributeValue {


    private String categoryName; //类别名称
    private String keyName; //属性名称

    private String createAdminName; //创建人ID
    private String updateAdminName; //修改人ID


    private Long[] idArray; //主键列表

    private List<Long> attributeKeyIdList; //属性键ID列表

    private String rgbColor; //颜色值

    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long attributeValueId;


}
