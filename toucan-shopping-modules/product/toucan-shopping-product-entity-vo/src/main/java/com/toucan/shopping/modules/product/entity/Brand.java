package com.toucan.shopping.modules.product.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 品牌
 *
 * @author majian
 */
@Data
public class Brand {

    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id; //主键
    private Integer trademarkAreaType; //商标注册地区 1:中国大陆地区 2:香港、澳门特别行政区，台湾省和境外国家
    private String chineseName; //品牌名(中文)
    private String englishName; //品牌名(英文)
    private String registNumber1; //商标注册号
    private String registNumber2; //商标注册号
    private String seminary; //发源地 (仅在trademarkAreaType值为2时不为空)
    private String ownerName; //所有人姓名


    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createDate; //创建时间
    private Long createAdminId; //创建人ID
    private Integer deleteStatus; //删除状态 0未删除 1已删除
    private Integer enabledStatus; //启用状态 0禁用 1启用(审核通过后启用)





}
