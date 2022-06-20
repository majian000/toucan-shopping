package com.toucan.shopping.modules.product.service;


import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.product.entity.Brand;
import com.toucan.shopping.modules.product.page.BrandPageInfo;
import com.toucan.shopping.modules.product.vo.BrandVO;

import java.util.List;


/**
 * 品牌服务
 * @author majian
 */
public interface BrandService {



    /**
     * 查询列表页
     * @param queryPageInfo
     * @return
     */
    PageInfo<BrandVO> queryListPage(BrandPageInfo queryPageInfo);


    List<Brand> queryByIdList(List<Long> id);

    Brand findByIdIngoreDeleteStatus(Long id);


    List<Brand> queryAllList(Brand queryModel);

    List<Brand> queryList(BrandVO queryModel);

    int save(Brand entity);

    int update(Brand entity);

    int deleteById(Long id);

}
