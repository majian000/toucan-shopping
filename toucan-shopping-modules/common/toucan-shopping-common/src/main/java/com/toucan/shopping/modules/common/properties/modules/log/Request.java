package com.toucan.shopping.modules.common.properties.modules.log;


import lombok.Data;

import java.util.List;


/**
 * 请求日志
 */
@Data
public class Request {


    /**
     * 是否开启
     */
    private boolean enabled;

    /**
     * 内容类型
     */
    private List<String> contentTypeList;

    /**
     * 忽略URL
     */
    private List<String> ignoreUrls;


}
