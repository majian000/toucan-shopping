package com.toucan.shopping.modules.product.vo;

import com.toucan.shopping.modules.product.entity.AttributeKey;
import com.toucan.shopping.modules.product.entity.AttributeValue;
import lombok.Data;

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


}
