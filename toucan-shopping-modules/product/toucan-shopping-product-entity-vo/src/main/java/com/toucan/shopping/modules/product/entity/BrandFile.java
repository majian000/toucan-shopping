package com.toucan.shopping.modules.product.entity;

import lombok.Data;

import java.util.Date;

/**
 * 品牌证明材料表
 * @author majian
 */
@Data
public class BrandFile {
    private Long id; //主键
    private Long brandId; //品牌ID
    private String filePath; //文件路径


}
