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
    private String createAdminName;


    /**
     * 修改人
     */
    private String updateAdminName;

    /**
     * 应用编码
     */
    private List<String> appCodes;


    /**
     * 字典应用关联
     */
    private List<DictApp> dictApps;


    /**
     * 是否有子节点
     */
    private Boolean haveChild = false;

    /**
     * 路径
     */
    private String path;

    /**
     * 关联应用名称
     */
    private String appNames;

    private Long[] idArray; //主键列表

    private List<Long> idList; //主键集合

    private String parentName; //上级节点名称

    private List<Long> categoryIdList; //分类ID列表

    private String appCodesStr;

}
