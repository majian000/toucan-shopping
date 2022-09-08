package com.toucan.shopping.modules.area.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 地区编码
 *
 * @author majian
 */
@Data
public class Area {

    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id; //主键
    private String code; //编码
    private String parentCode; //上级编码
    private String province; //省
    private String bigAreaName; //大区域名称
    private String bigAreaCode; //大区域编码 1:华东 2:华北 3:华中 4:华南 5:东北 6:西北 7:西南 8:港澳台
    private String countryName; //国家名称
    private String countryCode; //国家编码 CHN:中国

    /**
     * 上级节点ID
     */
    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long pid; //上级ID
    private String city; //市
    private String area; //区县
    private Short isMunicipality=0; //是否直辖市 0:否 1:是
    private Long areaSort; //排序
    private Short type; //1省 2市 3区县

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createDate; //创建时间

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateDate; //修改时间

    private String appCode; //所属应用
    private String createAdminId; //创建人ID
    private String updateAdminId; //修改人ID
    private String remark; //备注

    /**
     * 删除状态 0未删除 1已删除
     */
    private Short deleteStatus;



    private Long[] idArray; //ID数组

    private String[] codeArray; //编码数组

}
