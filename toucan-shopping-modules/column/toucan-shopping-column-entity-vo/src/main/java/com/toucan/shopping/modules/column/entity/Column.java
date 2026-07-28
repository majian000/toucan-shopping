package com.toucan.shopping.modules.column.entity;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 栏目
 *
 * @author majian
 */
@Data
public class Column {

    @JsonFormat(shape=JsonFormat.Shape.STRING)
    private Long id; //主键

    @JsonFormat(shape=JsonFormat.Shape.STRING)
    private Long pid = -1L; //上级ID

    private String title; //标题
    private String code; //编码
    private String type; //类型 10:pc端 11:移动端
    private Integer showStatus; //显示状态 0隐藏 1显示
    private String position; //栏目位置 1 PC门户首页
    private String clickPath; //点击跳转


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
    private Integer columnSort; //排序


    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date startShowDate; //开始展示时间

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date endShowDate; //结束展示时间

    private String extendProperty; //扩展属性
    /**
     * 删除状态 0未删除 1已删除
     */
    private Short deleteStatus;

    /**
     * 栏目类型编码
     */
    private String columnTypeCode;


}
