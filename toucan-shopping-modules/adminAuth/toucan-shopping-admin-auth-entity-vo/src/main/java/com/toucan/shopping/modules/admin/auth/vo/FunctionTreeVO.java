package com.toucan.shopping.modules.admin.auth.vo;

import com.toucan.shopping.modules.admin.auth.entity.Function;
import lombok.Data;

import java.util.List;

/**
 * 功能项
 */
@Data
public class FunctionTreeVO extends Function {


    /**
     * 节点名
     */
    private String title;

    /**
     * 子节点
     */
    private List<FunctionTreeVO> children;


    /**
     * 创建人
     */
    private String createAdminUsername;


    /**
     * 修改人
     */
    private String updateAdminUsername;

    /**
     * 是否支持checkbox
     */
    private boolean checked;

}
