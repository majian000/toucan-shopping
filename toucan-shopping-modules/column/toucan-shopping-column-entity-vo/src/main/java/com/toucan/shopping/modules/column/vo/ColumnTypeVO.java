package com.toucan.shopping.modules.column.vo;

import com.toucan.shopping.modules.column.entity.ColumnType;
import lombok.Data;

@Data
public class ColumnTypeVO extends ColumnType {


    private String createAdminName; //创建人姓名
    private String updateAdminName; //修改人姓名


}
