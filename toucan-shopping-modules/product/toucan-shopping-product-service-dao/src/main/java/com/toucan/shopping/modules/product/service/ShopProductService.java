package com.toucan.shopping.modules.product.service;


import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.product.entity.ShopProduct;
import com.toucan.shopping.modules.product.page.ShopProductPageInfo;
import com.toucan.shopping.modules.product.vo.ShopProductVO;

import java.util.Date;
import java.util.List;

public interface ShopProductService {

    List<ShopProduct> queryAllList(ShopProduct queryModel);

    int save(ShopProduct entity);

    int deleteById(Long id);

    int deleteByIdAndInsertDeleteDate(Long id, Date deleteDate);

    ShopProductVO findById(Long id);

    ShopProductVO findByIdAndStatus(Long id,int status);

    /**
     * 修改上架/下架状态
     * @param id
     * @param status
     * @return
     */
    int updateStatus(Long id,Long shopId,Integer status);

    /**
     * 查询列表页
     * @param queryPageInfo
     * @return
     */
    PageInfo<ShopProductVO> queryListPage(ShopProductPageInfo queryPageInfo);

    List<ShopProductVO> queryList(ShopProductVO shopProductVO);


    ShopProductVO queryOne(ShopProductVO shopProductVO);

}
