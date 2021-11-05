package com.toucan.shopping.cloud.apps.seller.web.vo.selectPage;

import lombok.Data;

import java.util.List;

@Data
public class GridResult {
    /**
     * 每页显示数量
     */
    private Integer pageSize;
    /**
     * 页码
     */
    private int pageNumber;
    /**
     * 记录总数
     */
    private Long totalRow;
    /**
     * 总页数
     */
    private Long totalPage;

    private List<Object> list;

}
