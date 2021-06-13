package com.toucan.shopping.modules.common.properties;


import lombok.Data;

/**
 * redis相关配置
 */
@Data
public class Redis {

    private String host;
    private String port;
    private String password;
    private Long timeout;

}
