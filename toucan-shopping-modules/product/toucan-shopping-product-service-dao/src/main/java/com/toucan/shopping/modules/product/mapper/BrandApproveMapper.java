package com.toucan.shopping.modules.product.mapper;

import com.toucan.shopping.modules.product.entity.BrandApprove;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface BrandApproveMapper {

    List<BrandApprove> queryAllList(BrandApprove queryModel);

}
