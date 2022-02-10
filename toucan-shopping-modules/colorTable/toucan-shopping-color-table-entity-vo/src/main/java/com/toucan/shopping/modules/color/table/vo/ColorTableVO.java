package com.toucan.shopping.modules.color.table.vo;

import com.toucan.shopping.modules.color.table.entity.ColorTable;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 颜色表
 *
 * @author majian
 */
@Data
public class ColorTableVO extends ColorTable {


    /**
     * 名称集合
     */
    private List<String> nameList;

    /**
     * 创建人
     */
    private String createAdminName;


    /**
     * 修改人
     */
    private String updateAdminName;

}
