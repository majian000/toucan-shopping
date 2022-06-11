package com.toucan.shopping.modules.admin.auth.vo;

import lombok.Data;


/**
 * 应用在线登录用户
 * @author majian
 * @date 2022-6-11 16:51:41
 */
@Data
public class AppLoginUserVO {


    private Long loginCount; //登录用户数量

    private String appCode; //应用编码

    private String appName; //应用名称

}
