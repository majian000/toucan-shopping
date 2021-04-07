package com.toucan.shopping.user.export.page;

import com.toucan.shopping.user.export.entity.UserApp;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 应用列表查询页
 */
@Data
public class UserPageInfo {

    /**
     * 页码
     */
    private int page;


    /**
     * 每页显示数量(前台控件用)
     */
    private int limit;




    /**
     * 主键 雪花算法生成
     */
    private Long id;



    /**
     * 密码
     */
    private String password;



    /**
     * 手机号
     */
    private String mobilePhone;



    /**
     * 用户名
     */
    private String username;


    /**
     * 昵称
     */
    private String nickName;

    /**
     * 邮箱,分表用
     */
    private String email;


    /**
     * 启用状态 0:禁用 1启用
     */
    private Short enableStatus;


    /**
     * 创建时间
     */
    private Date createDate;


    /**
     * 用户所属应用
     */
    private List<UserApp> userApps;


    /**
     * 删除状态 0未删除 1已删除
     */
    private Short deleteStatus;


    private Long[] idArray; //ID数组


    //前台传入

    /**
     * 验证码
     */
    private String vcode;

    /**
     * 关键字 模糊查询 手机号、昵称、用户名、用户ID、邮箱、身份证号
     */
    private String keyword;

    /**
     * elasticsearch当前页末尾字段值
     */
    private String[] sortValues;

}
