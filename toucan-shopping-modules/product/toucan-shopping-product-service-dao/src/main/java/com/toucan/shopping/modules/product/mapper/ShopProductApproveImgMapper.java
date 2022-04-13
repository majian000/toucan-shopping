package com.toucan.shopping.modules.product.mapper;

import com.toucan.shopping.modules.product.entity.ShopProductApproveImg;
import com.toucan.shopping.modules.product.vo.ShopProductApproveImgVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface ShopProductApproveImgMapper {

    List<ShopProductApproveImg> queryAllList(ShopProductApproveImg queryModel);


    List<ShopProductApproveImg> queryList(ShopProductApproveImgVO queryModel);

    List<ShopProductApproveImg> queryListOrderByCreateDateAsc(ShopProductApproveImgVO queryModel);

    int inserts(List<ShopProductApproveImg> entitys);

    List<ShopProductApproveImg> queryByIdList(List<Long> ids);


    int deleteByProductApproveId(Long productApproveId);

    List<ShopProductApproveImg> queryListByApproveId(Long approveId);


}
