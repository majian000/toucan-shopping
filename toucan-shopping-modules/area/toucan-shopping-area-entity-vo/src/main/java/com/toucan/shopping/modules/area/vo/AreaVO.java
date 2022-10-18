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
     * 上级节点名称
     */
    private String parentName;


    /**
     * 子节点
     */
    private List children;



    private String createAdminName; //创建人姓名
    private String updateAdminName; //修改人姓名


    private List<String> cityNameList; //地市名称列表

}
