package com.toucan.shopping.modules.product.entity;

import lombok.Data;

import java.util.Date;

/**
 * 店铺维度的SPU与商品预览图关联
 *
 * @author majian
 */
@Data
public class ShopProductSpuImg {
    private Long id; //主键
    private Long shopProductId; //店铺维度的SPU的ID
    private String filePath; //文件路径
    private Short isMainPhoto; //是否是商品主图 0:否 1:是
    private Date createDate; //创建时间
    private String appCode; //所属应用
    private Long createUserId; //创建人ID
    private Integer deleteStatus; //删除状态 0未删除 1已删除





}
