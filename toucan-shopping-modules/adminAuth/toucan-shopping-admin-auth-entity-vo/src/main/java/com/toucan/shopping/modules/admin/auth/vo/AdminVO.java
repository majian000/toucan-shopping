package com.toucan.shopping.modules.admin.auth.vo;

import com.toucan.shopping.modules.admin.auth.entity.Admin;
import com.toucan.shopping.modules.admin.auth.entity.AdminApp;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 用户
 */
@Data
public class AdminVO extends Admin {



    /**
     * 前台传入
     */
    private String vcode;

    /**
     * 关联应用名称
     */
    private String appNames;

    /**
     * 应用编码
     */
    private List<String> appCodes;

    /**
     * 登录IP
     */
    private String loginIp;


    /**
     * 登录源头 1:PC
     */
    private Integer loginSrcType;


    /**
     * 创建人
     */
    private String createAdminUsername;


    /**
     * 修改人
     */
    private String updateAdminUsername;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 行呗
     */
    private Integer gender;

    /**
     * 身份证号
     */
    private String idCard;

    /**
     * 生日
     */
    private Date birthday;

    /**
     * 地址
     */
    private String address;

}
