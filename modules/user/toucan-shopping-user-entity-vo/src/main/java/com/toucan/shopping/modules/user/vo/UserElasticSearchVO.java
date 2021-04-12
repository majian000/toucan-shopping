package com.toucan.shopping.modules.user.vo;

import lombok.Data;

@Data
public class UserElasticSearchVO extends UserVO{

    /**
     * 模糊查询 手机号、昵称、用户名、用户ID、邮箱、身份证号
     */
    private String keyword;
}
