package com.toucan.shopping.modules.user.vo;

import com.toucan.shopping.modules.user.entity.UserHeadSculptureApprove;
import com.toucan.shopping.modules.user.entity.UserTrueNameApprove;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * 用户头像审核
 */
@Data
public class UserHeadSculptureApproveVO extends UserHeadSculptureApprove {



    /**
     * 审核管理员ID
     */
    private String approveAdminId;


    /**
     * 头像照片
     */
    private MultipartFile headSculptureFile;

}
