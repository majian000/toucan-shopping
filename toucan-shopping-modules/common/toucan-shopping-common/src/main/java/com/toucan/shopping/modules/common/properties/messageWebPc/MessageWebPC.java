package com.toucan.shopping.modules.common.properties.messageWebPc;

import com.toucan.shopping.modules.common.properties.shoppingPc.freemarker.Freemarker;
import lombok.Data;

import java.util.List;

/**
 * 消息服务PC端配置
 */
@Data
public class MessageWebPC {

    /**
     *
     * 根路径
     */
    private String basePath;

    /**
     * 允许跨域访问的地址
     */
    private List<String> allowedOrigins;

}
