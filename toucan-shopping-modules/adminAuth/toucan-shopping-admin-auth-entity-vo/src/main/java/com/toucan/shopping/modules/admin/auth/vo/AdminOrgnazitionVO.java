package com.toucan.shopping.modules.admin.auth.vo;

import com.toucan.shopping.modules.admin.auth.entity.AdminApp;
import com.toucan.shopping.modules.admin.auth.entity.AdminOrgnazition;
import lombok.Data;

import java.util.List;

@Data
public class AdminOrgnazitionVO extends AdminOrgnazition {


    /**
     * 机构列表
     */
    private List<AdminOrgnazition> adminOrgnazitions;
}
