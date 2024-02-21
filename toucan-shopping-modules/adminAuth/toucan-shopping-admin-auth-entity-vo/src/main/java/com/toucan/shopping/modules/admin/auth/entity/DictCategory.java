package com.toucan.shopping.modules.admin.auth.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 字典分类应用关联
 * 说明
 *      字典分类名称、编码允许重复,支持应用创建只属自己的分类字典名称和编码
 * 注意
 *      比如字典分类{name:性别,code:sex}可以关联A应用和B应用
 *      A应用修改成了{name:性别,code:xingbie},B应用也会同步更新成最新的{name:性别,code:xingbie}
 *      如果A应用想{name:性别,code:xingbie}的这次修改只影响自己,可以由A应用创建一个新的字典分类{name:性别,code:xingbie}
 *      然后在中台中把{name:性别,code:sex}这个关联只保留B应用即可
 *      应用端的删除操作,只做关联删除,当该字典分类没有关联任何应用的时候才做逻辑删除
 */
@Data
public class DictCategory {


    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Integer id;

    private String name; //名称

    private String code; //编码

    private Integer dictCategorySort; //排序

    /**
     * 启用状态 0:禁用 1启用
     */
    private Short enableStatus;

    /**
     * 删除状态 0未删除 1已删除
     */
    private Short deleteStatus;

    private String remark;

    /**
     * 所属应用
     */
    private String appCode;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createDate;


    /**
     * 创建人
     */
    private String createAdminId;



    /**
     * 修改时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateDate;


    /**
     * 修改人
     */
    private String updateAdminId;

}
