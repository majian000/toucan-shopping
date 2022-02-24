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
public class ProductSpu {
    private Long id; //主键
    private Integer categoryId; //所属类别
    private String uuid; //SPU UUID
    private String name; //商品名称
    private Short status; //是否上架 0:未上架 1:已上架
    private Date createDate; //创建时间
    private Date updateDate; //创建时间
    private String appCode; //所属应用
    private Long createAdminId; //创建人ID
    private Long updateAdminId; //创建人ID
    private Integer deleteStatus; //删除状态 0未删除 1已删除
    private String attributes; //这个SPU的属性





}
