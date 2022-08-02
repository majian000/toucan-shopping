package com.toucan.shopping.modules.column.vo;

import com.toucan.shopping.modules.column.entity.ColumnRecommendLabel;
import com.toucan.shopping.modules.column.vo.ColumnVO;

import java.util.List;

public class PcIndexColumnVO extends ColumnVO {

    private List<ColumnRecommendLabel> topLabels; //顶部标签

    private List<ColumnRecommendLabel> leftLabels; //左侧标签

}
