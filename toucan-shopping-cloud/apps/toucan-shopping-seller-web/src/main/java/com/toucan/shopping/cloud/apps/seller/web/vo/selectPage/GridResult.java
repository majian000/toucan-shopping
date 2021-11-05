package com.toucan.shopping.cloud.apps.seller.web.vo.selectPage;

import lombok.Data;

import java.util.List;

@Data
public class GridResult {
    private Long pageSize;
    private int pageNumber;
    private Long totalRow;
    private Long totalPage;

    private List<Object> list;

}
