package com.toucan.shopping.modules.area.page;

import com.toucan.shopping.modules.area.vo.BannerVO;
import com.toucan.shopping.modules.common.page.PageInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;
import java.util.List;

/**
 * 列表查询页对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class BannerPageInfo extends PageInfo<BannerVO> {


    // ===============查询条件===================

    private Integer id;


    /**
     * 标题
     */
    private String title;


    private Long[] idArray; //ID数组

    //==============================================

}
