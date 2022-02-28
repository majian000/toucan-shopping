package com.toucan.shopping.modules.product.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 品牌证明材料表
 * @author majian
 */
@Data
public class BrandFile {

    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id; //主键

    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long brandId; //品牌ID

    private String filePath; //文件路径

    private Integer deleteStatus; //删除状态 0未删除 1已删除


}
