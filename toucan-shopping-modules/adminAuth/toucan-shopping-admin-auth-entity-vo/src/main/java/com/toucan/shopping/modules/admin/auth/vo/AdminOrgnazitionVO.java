package com.toucan.shopping.modules.admin.auth.vo;

import com.toucan.shopping.modules.admin.auth.entity.AdminApp;
import com.toucan.shopping.modules.admin.auth.entity.AdminOrgnazition;
import lombok.Data;

import java.util.List;

@Data
public class AdminOrgnazitionVO extends AdminOrgnazition {

    private String selectAppCode; //选择操作的应用

    /**
     * 机构列表
     */
    private List<AdminOrgnazition> adminOrgnazitions;
}
