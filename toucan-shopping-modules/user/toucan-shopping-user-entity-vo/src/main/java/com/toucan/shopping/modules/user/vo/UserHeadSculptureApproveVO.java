package com.toucan.shopping.modules.user.vo;

import com.toucan.shopping.modules.user.entity.UserTrueNameApprove;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * 用户审核
 */
@Data
public class UserHeadSculptureApproveVO extends UserTrueNameApprove {


    /**
     * 验证码
     */
    private String vcode;



    /**
     * 审核管理员ID
     */
    private String approveAdminId;



}
