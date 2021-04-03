package com.toucan.shopping.user.vo;

import com.toucan.shopping.user.export.vo.UserElasticSearchVO;
import lombok.Data;

import java.util.List;

@Data
public class SearchAfterPage {
    /**
     * 当前页数据
     */
    private List<UserElasticSearchVO> userElasticSearchVOS;

    /**
     * 当前页的sort vlaues
     */
    private Object[] sortValues;

    /**
     * 总条数
     */
    private Long total = 0L;
}
