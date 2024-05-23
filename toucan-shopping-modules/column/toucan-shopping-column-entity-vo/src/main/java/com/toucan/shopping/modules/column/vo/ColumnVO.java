package com.toucan.shopping.modules.column.vo;

import com.toucan.shopping.modules.column.entity.Column;
import com.toucan.shopping.modules.column.entity.ColumnRecommendLabel;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 栏目VO
 *
 * @author majian
 */
@Data
public class ColumnVO extends Column {


    /**
     * 地区编码
     */
    private String areaCode;

    private Long[] idArray; //ID数组


    private String createAdminName; //创建人姓名
    private String updateAdminName; //修改人姓名

    private List<Long> productIds; //关联商品
    private String selectProductIds; //选择关联商品 多个用,分割

    private List<ColumnAreaVO> columnAreas;


    private String parentTitle; //上级节点名称

    /**
     * 是否有子节点
     */
    private Boolean haveChild = false;

    private List<Long> idList; //主键集合

    private String typeNames; //栏目类型名称

    private String positionNames; //位置名称

}
