package com.toucan.shopping.center.user.export.vo;

import com.toucan.shopping.center.user.export.entity.UserApp;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class UserElasticSearchVO extends UserVO{

    /**
     * 模糊查询 手机号、昵称、用户名、用户ID、邮箱、身份证号
     */
    private String keyword;
}
