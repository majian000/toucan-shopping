package com.toucan.shopping.modules.user.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.toucan.shopping.modules.user.entity.UserTrueNameApprove;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Date;

/**
 * 用户审核
 */
@Data
public class UserTrueNameApproveVO extends UserTrueNameApprove {


    /**
     * 验证码
     */
    private String vcode;

    /**
     * 身份证正面照片
     */
    private MultipartFile idcardImg1File;

    /**
     * 身份证背面照片
     */
    private MultipartFile idcardImg2File;


    /**
     * 正面照片
     */
    private String httpIdcardImg1;

    /**
     * 背面照片
     */
    private String httpIdcardImg2;



}
