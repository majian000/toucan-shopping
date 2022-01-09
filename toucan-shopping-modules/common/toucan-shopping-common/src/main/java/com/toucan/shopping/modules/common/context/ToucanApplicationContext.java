package com.toucan.shopping.modules.common.context;


/**
 * 犀鸟商城应用上下文
 * @author majian
 * @date 2022-1-9 13:14:56
 */
public class ToucanApplicationContext {

    /**
     * 单个文件最大上传
     */
    private static String maxFileSize;

    public static String getMaxFileSize() {
        return maxFileSize;
    }

    public static void setMaxFileSize(String maxFileSize) {
        ToucanApplicationContext.maxFileSize = maxFileSize;
    }
}
