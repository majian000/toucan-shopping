package com.toucan.shopping.modules.column.vo;

import com.toucan.shopping.modules.column.entity.ColumnArea;
import com.toucan.shopping.modules.column.entity.ColumnBanner;
import lombok.Data;

/**
 * 栏目轮播图VO
 *
 * @author majian
 */
@Data
public class ColumnBannerVO extends ColumnBanner {

    private Long[] idArray; //ID数组

}
