package com.toucan.shopping.modules.search.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商品搜索属性
 * @author majian
 * @date 2023-6-5 17:02:58
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductSearchAttributeVO {

    private Long nameId; //属性名ID

    private String name; //属性名

    private Long valueId; //属性值ID

    private String value; //属性值

}
