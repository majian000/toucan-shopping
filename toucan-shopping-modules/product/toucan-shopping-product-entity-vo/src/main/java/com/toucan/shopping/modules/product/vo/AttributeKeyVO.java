package com.toucan.shopping.modules.product.vo;

import com.toucan.shopping.modules.product.entity.AttributeKey;
import lombok.Data;

import java.util.List;

/**
 * 属性键
 *
 * @author majian
 */
@Data
public class AttributeKeyVO extends AttributeKey {


    private String categoryName; //类别名称

    private String createAdminName; //创建人ID
    private String updateAdminName; //修改人ID


    private Long[] idArray; //主键列表

    /**
     * 可选值列表
     */
    private List<AttributeValueVO> values;

}
