package com.toucan.shopping.modules.user.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * 用户登录
 */
@Data
public class UserLoginVO extends UserVO {


    /**
     * 手机号、邮箱、用户名
     */
    private String loginUserName;



    /**
     * 登录token
     */
    private String loginToken;

    /**
     * 权限token (用户忽略权限校验,优化流量开销)
     */
    private String loginAuthToken;


    /**
     * 要登录的应用
     */
    private String appCode;

    /**
     * 登录用户的IP地址
     */
    private String loginIp;

    /**
     * 登录源头 1:PC
     */
    private Integer srcType;

}
