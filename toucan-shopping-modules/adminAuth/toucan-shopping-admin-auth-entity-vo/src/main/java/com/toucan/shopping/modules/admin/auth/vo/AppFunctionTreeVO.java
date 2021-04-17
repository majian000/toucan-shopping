package com.toucan.shopping.modules.admin.auth.vo;

import com.toucan.shopping.modules.admin.auth.entity.Function;
import lombok.Data;

import java.util.List;

/**
 * 应用功能项
 */
@Data
public class AppFunctionTreeVO extends Function {


    /**
     * 节点标题
     */
    private String title;

    /**
     * 子节点
     */
    private List<FunctionTreeVO> children;


}
