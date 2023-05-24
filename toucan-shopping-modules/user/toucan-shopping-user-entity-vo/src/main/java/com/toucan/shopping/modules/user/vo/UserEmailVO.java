package com.toucan.shopping.modules.user.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.toucan.shopping.modules.user.entity.UserEmail;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 用户与邮箱子表
 */
@Data
public class UserEmailVO extends UserEmail {


    /**
     * 所属应用
     */
    private String appCode ="10001001";

}
