package com.toucan.shopping.modules.admin.auth.vo;

import com.toucan.shopping.modules.admin.auth.entity.AdminApp;
import lombok.Data;

@Data
public class AdminAppVO extends AdminApp {

    /**
     * 应用名
     */
    private String name;

    /**
     * checkbox是否选中
     */
    private boolean checked;

}
