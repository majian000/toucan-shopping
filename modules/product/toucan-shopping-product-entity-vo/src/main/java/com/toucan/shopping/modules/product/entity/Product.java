package com.toucan.shopping.modules.product.entity;

import com.toucan.shopping.modules.common.util.DateUtils;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Map;

/**
 * 商品
 *
 * @author majian
 */
@Data
public class Product {
    private Long id; //主键
    private Integer categoryId; //所属类别
    private String uuid; //SPU UUID
    private String name; //商品名称
    private String attributes; //商品所有属性
    private Date createDate; //创建时间
    private String appCode; //所属应用
    private Long createUserId; //创建人ID





}
