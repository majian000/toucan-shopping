package com.toucan.shopping.modules.product.service;


import com.toucan.shopping.modules.product.entity.ShopProductApproveImg;
import com.toucan.shopping.modules.product.vo.ShopProductApproveImgVO;

import java.util.List;

public interface ShopProductApproveImgService {

    List<ShopProductApproveImg> queryAllList(ShopProductApproveImg queryModel);


    List<ShopProductApproveImg> queryList(ShopProductApproveImgVO queryModel);


    List<ShopProductApproveImg> queryListOrderByCreateDateAsc(ShopProductApproveImgVO queryModel);

    List<ShopProductApproveImg> queryListOrderByImgSortAsc(ShopProductApproveImgVO queryModel);

    int saves(List<ShopProductApproveImg> entitys);


    List<ShopProductApproveImg> queryByIdList(List<Long> ids);


    int deleteByProductApproveId(Long productApproveId);

    List<ShopProductApproveImg> queryListByApproveId(Long approveId);

}
