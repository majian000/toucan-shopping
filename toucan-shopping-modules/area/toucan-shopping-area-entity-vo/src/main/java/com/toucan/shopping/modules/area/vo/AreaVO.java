package com.toucan.shopping.modules.area.vo;

import com.toucan.shopping.modules.area.entity.Area;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 地区编码
 *
 * @author majian
 */
@Data
public class AreaVO extends Area {

    /**
     * 省、市、区县都放会在这里
     */
    private String name;

    /**
     * layui 重写组件相关(是否是父节点)
     */
    private boolean isParent = false;

    /**
     * 上级节点名称
     */
    private String parentName;


    private List<AreaVO> children;


    /**
     * 创建人
     */
    private String createAdminUsername;


    /**
     * 修改人
     */
    private String updateAdminUsername;

}
