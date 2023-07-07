package com.toucan.shopping.modules.stock.page;

import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.stock.entity.ProductSkuStockLock;
import com.toucan.shopping.modules.stock.vo.ProductSkuStockLockVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 列表查询页
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class ProductSkuStockLockPageInfo extends PageInfo<ProductSkuStockLockVO> {


    // ===============查询条件===================

    private Long productSkuId; //商品SKU ID

    private Long userMainId; //用户ID

    private String mainOrderNo; //主订单编号

    private String orderNo; //子订单编号

    private Integer deleteStatus; //删除状态 -1:全部 0:未删除 1:已删除

    //==============================================





}
