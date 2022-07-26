package com.toucan.shopping.modules.column.vo;

import com.toucan.shopping.modules.column.entity.Column;
import com.toucan.shopping.modules.column.entity.ColumnArea;
import lombok.Data;

import java.util.Date;

/**
 * 栏目地区VO
 *
 * @author majian
 */
@Data
public class ColumnAreaVO extends ColumnArea {

    private Long[] idArray; //ID数组

}
