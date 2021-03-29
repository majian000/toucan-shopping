package com.toucan.shopping.product.export.entity;

import com.toucan.shopping.common.util.DateUtils;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Map;

/**
 * 商品SKU
 *
 * @author majian
 */
@Data
public class ProductSku {


    private Long id; //主键
    private String attributes; //商品所有属性
    private String productUuid; //SPU的UUID
    private String uuid; //SKU的UUID
    private Double price; //价格
    private String remark; //备注
    private Short status; //是否上架 0:未上架 1:已上架
    private String appCode; //所属应用
    private Long createUserId; //创建人ID


    /**
     * 业务相关字段
     */
    private Integer stockNum = 0;

}
