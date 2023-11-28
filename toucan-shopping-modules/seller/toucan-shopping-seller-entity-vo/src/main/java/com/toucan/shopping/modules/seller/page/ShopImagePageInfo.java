package com.toucan.shopping.modules.seller.page;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.seller.vo.ShopBannerVO;
import com.toucan.shopping.modules.seller.vo.ShopImageVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 列表查询页对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class ShopImagePageInfo extends PageInfo<ShopImageVO> {


    // ===============查询条件===================

    private Integer id;




    /**
     * 标题
     */
    private String title;


    private Long[] idArray; //ID数组

    private Long shopId; //店铺ID

    private String orderColumn; //排序列

    private String orderSort; //升序还是降序


    //==============================================

}
