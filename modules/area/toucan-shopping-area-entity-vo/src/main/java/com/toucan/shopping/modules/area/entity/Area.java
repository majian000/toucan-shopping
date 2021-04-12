package com.toucan.shopping.modules.area.entity;

import lombok.Data;

import java.util.Date;

/**
 * 地区编码
 *
 * @author majian
 */
@Data
public class Area {
    private Long id; //主键
    private String code; //编码
    private String parentCode; //上级编码
    private String province; //省
    private String city; //市
    private String area; //区县
    private Short isMunicipality=0; //是否直辖市 0:否 1:是
    private Long areaSort; //排序
    private Short type; //1省 2市 3区县
    private Date createDate; //创建时间
    private Date updateDate; //创建时间
    private String appCode; //所属应用
    private Long createAdminId; //创建人ID
    private Long updateAdminId; //修改人ID
    private String remark; //备注

    /**
     * 删除状态 0未删除 1已删除
     */
    private Short deleteStatus;
}
