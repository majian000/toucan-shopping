package com.toucan.shopping.modules.common.properties;

import lombok.Data;

/**
 * 用户配置
 */
@Data
public class User {

    /**
     * 头像最大限制
     */
    private Long headSculptureMaxSize;

    /**
     * 用户默认头像
     */
    private String defaultHeadSculpture = "";

}
