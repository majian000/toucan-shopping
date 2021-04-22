package com.toucan.shopping.modules.common.vo.bootstrap;


import lombok.Data;

/**
 * bootstrap treeview状态属性
 */
@Data
public class State {

    /**
     * 是否选中
     */
    private boolean checked;

    /**
     * 是否禁用
     */
    private boolean disabled = false;

    /**
     * 是否展开
     */
    private boolean expanded = true;

    /**
     * 是否可被选中
     */
    private boolean selected = true;

}
