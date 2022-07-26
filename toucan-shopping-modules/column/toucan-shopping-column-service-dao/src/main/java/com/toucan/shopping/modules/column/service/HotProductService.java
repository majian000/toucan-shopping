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

    HotProduct findById(Long id);



    /**
     * 修改
     * @param hotProduct
     * @return
     */
    int update(HotProduct hotProduct);


    /**
     * 根据ID删除
     * @param id
     * @return
     */
    int deleteById(Long id);

    /**
     * 查询PC端首页热门商品
     * @param hotProductVO
     * @return
     */
    List<HotProductVO> queryPcIndexHotProducts(HotProductVO hotProductVO);

}
