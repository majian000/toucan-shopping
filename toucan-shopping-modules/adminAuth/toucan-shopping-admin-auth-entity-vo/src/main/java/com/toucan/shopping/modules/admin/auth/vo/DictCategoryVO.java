package com.toucan.shopping.modules.admin.auth.vo;

import com.toucan.shopping.modules.admin.auth.entity.DictCategory;
import lombok.Data;

import java.util.List;

@Data
public class DictCategoryVO extends DictCategory {

    /**
     * 创建人
     */
    private String createAdminUsername;


    /**
     * 修改人
     */
    private String updateAdminUsername;

    /**
     * 应用编码
     */
    private List<String> appCodes;



    /**
     * 关联应用名称
     */
    private String appName;


}
