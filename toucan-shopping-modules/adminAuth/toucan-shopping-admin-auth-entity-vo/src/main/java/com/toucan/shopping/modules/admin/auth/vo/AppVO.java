package com.toucan.shopping.modules.admin.auth.vo;

import com.toucan.shopping.modules.admin.auth.entity.App;
import lombok.Data;

import java.util.Date;

/**
 * 应用
 */
@Data
public class AppVO extends App {

    /**
     * 创建人
     */
    private String createAdminUsername;


    /**
     * 修改人
     */
    private String updateAdminUsername;


    /**
     * 多个ID用,分割
     */
    private String ids;

}
