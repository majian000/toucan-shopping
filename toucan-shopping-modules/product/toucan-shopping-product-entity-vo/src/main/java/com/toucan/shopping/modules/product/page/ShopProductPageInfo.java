package com.toucan.shopping.modules.product.page;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.toucan.shopping.modules.common.page.PageInfo;
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
public class ShopProductPageInfo extends PageInfo<ShopProductVO> {

    // ===============查询条件===================
    private Long categoryId; //分类ID

    private Long productApproveId;

    private Integer approveStatus; //审批状态  1审核中 2审核通过 3审核驳回

    private Long id; //商品ID

    private String uuid; //商品UUID

    private Long shopId; //店铺ID

    private Long brandId; //品牌ID

    private String name; //商品名称

    private Short status; //是否上架 0:未上架 1:已上架

    private List<Long> categoryIdList; //分类ID列表


    private String orderColumn; //排序列

    private String orderSort; //升序还是降序

    private Long[] ids; //商品ID数组


    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm",timezone = "GMT+8")
    private Date startDateYMDHS; //开始日期



    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm",timezone = "GMT+8")
    private Date endDateYMDHS; //结束日期

    //==============================================

}
