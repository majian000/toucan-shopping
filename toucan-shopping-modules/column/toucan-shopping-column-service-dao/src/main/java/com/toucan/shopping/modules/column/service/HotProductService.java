package com.toucan.shopping.modules.column.service;

import com.toucan.shopping.modules.column.entity.HotProduct;
import com.toucan.shopping.modules.column.page.HotProductPageInfo;
import com.toucan.shopping.modules.column.vo.HotProductVO;
import com.toucan.shopping.modules.common.page.PageInfo;

import java.util.List;

public interface HotProductService {

    int saves(List<HotProduct> entitys);


    /**
     * 查询列表页
     * @param queryPageInfo
     * @return
     */
    PageInfo<HotProductVO> queryListPage(HotProductPageInfo queryPageInfo);

    /**
     * 保存
     * @param hotProduct
     * @return
     */
    int save(HotProduct hotProduct);

    List<HotProductVO> queryList(HotProductVO query);

}
