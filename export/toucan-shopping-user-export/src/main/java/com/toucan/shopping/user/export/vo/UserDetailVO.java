package com.toucan.shopping.user.export.vo;

import lombok.Data;

import java.util.Date;

@Data
public class UserDetailVO {

    /**
     * 主键
     */
    private Long id;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 真实姓名
     */
    private String trueName;

    /**
     * 头像
     */
    private String headSculpture;


    /**
     * 身份证
     */
    private String idCard;

    /**
     * 用户ID,用该字段分库分表
     */
    private Long userMainId;

    /**
     * 性别 0:女 1:男
     */
    private Short sex;

    /**
     * 用户类型 0:买家 1:卖家 2:既是买家又是卖家
     */
    private Short type;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 删除状态 0未删除 1已删除
     */
    private Short deleteStatus;


}
