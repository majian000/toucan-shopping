package com.toucan.shopping.modules.admin.auth.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.toucan.shopping.modules.admin.auth.entity.AdminApp;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
public class AdminAppVO extends AdminApp {

    /**
     * 应用名
     */
    private String name;

    /**
     * checkbox是否选中
     */
    private boolean checked;

    /**
     * 应用编码 多个用,分割
     */
    private String appCodes;

    /**
     * 账号ID
     */
    private String adminId;

    /**
     * 应用名称
     */
    private String appName;


    /**
     * 账号名称
     */
    private String username;

    /**
     * 登录时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date loginDate;

}
