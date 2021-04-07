package com.toucan.shopping.admin.auth.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 应用
 */
@Data
public class App {
    private Long id;


    /**
     * 名称
     */
    private String name;



    /**
     * 启用状态 0:禁用 1启用
     */
    private Short enableStatus;


    /**
     *编码
     */
    private String code;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createDate;

    /**
     * 修改时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateDate;

    /**
     * 备注
     */
    private String remark;

    /**
     * 应用编码
     */
    private String appCode;



    /**
     * 删除状态 0未删除 1已删除
     */
    private Short deleteStatus;

    /**
     * 创建人
     */
    private String createAdminId;

    /**
     * 修改人
     */
    private String updateAdminId;


}
