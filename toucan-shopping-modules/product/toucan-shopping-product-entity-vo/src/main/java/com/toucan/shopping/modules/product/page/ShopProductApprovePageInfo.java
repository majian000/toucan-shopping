package com.toucan.shopping.modules.product.page;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.product.vo.ShopProductApproveVO;
import com.toucan.shopping.modules.product.vo.ShopProductVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * 列表查询页对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class ShopProductApprovePageInfo extends PageInfo<ShopProductApproveVO> {

    // ===============查询条件===================
    private Long categoryId; //分类ID

    private Integer approveStatus; //审批状态  1审核中 2审核通过 3审核驳回

    private Long id; //商品ID

    private String uuid; //商品UUID

    private Long shopId; //店铺ID

    private Long brandId; //品牌ID

    private String name; //商品名称

    private List<Long> categoryIdList; //分类ID列表


    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm",timezone = "GMT+8")
    private Date startDateYMDHS; //开始日期



    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm",timezone = "GMT+8")
    private Date endDateYMDHS; //结束日期


    //==============================================

}
