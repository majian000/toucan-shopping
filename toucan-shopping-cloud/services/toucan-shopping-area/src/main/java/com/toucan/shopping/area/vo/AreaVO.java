package com.toucan.shopping.area.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 地区编码
 *
 * @author majian
 */
@Data
public class AreaVO {
    private Long id; //主键
    private String code; //编码
    private String parentCode; //上级编码
    private String province; //省
    private String city; //市
    private String area; //区县
    private Long areaSort; //排序
    private Short isMunicipality=0; //是否直辖市 0:否 1:是
    private Short type; //1省 2市 3区县
    private String appCode; //所属应用
    private Long updateAdminId;
    private Date updateDate;
    private Date createDate; //创建时间
    private String createUserId; //创建人ID
    private String updateUserId; //修改人ID

    private List<AreaVO> children;

}
