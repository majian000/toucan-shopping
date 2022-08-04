package com.toucan.shopping.modules.column.page;

import com.toucan.shopping.modules.column.vo.ColumnTypeVO;
import com.toucan.shopping.modules.column.vo.ColumnVO;
import com.toucan.shopping.modules.common.page.PageInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 列表查询页对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class ColumnPageInfo extends PageInfo<ColumnVO> {


    // ===============查询条件===================

    private Integer id;

    private String columnTypeCode;



    /**
     * 应用编码
     */
    private String appCode;

    private Long[] idArray; //ID数组

    //==============================================

}
