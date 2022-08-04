package com.toucan.shopping.modules.column.vo;

import com.toucan.shopping.modules.column.entity.ColumnRecommendLabel;
import com.toucan.shopping.modules.column.vo.ColumnVO;
import lombok.Data;

import java.util.List;

@Data
public class PcIndexColumnVO extends ColumnVO {

    private List<ColumnRecommendLabel> topLabels; //顶部标签

    private List<ColumnRecommendLabel> leftLabels; //左侧标签

    private List<ColumnBannerVO> columnLeftBannerVOS; //栏目左侧轮播图

    private ColumnBannerVO rightTopBanner; //右侧顶部图片

    private ColumnBannerVO rightBottomBanner; //右侧底部图片

}
