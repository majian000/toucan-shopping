package com.toucan.shopping.modules.admin.auth.vo;

import com.toucan.shopping.modules.admin.auth.entity.Dict;
import com.toucan.shopping.modules.admin.auth.entity.DictApp;
import com.toucan.shopping.modules.admin.auth.entity.DictCategory;
import com.toucan.shopping.modules.admin.auth.entity.DictCategoryApp;
import lombok.Data;

import java.util.List;

@Data
public class DictVO extends Dict {

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
     * 字典应用关联
     */
    private List<DictApp> dictApps;


    /**
     * 关联应用名称
     */
    private String appNames;


}
