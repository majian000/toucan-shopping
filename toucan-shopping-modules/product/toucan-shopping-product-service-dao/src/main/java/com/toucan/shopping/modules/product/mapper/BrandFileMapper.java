package com.toucan.shopping.modules.product.mapper;

import com.toucan.shopping.modules.product.entity.BrandFile;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 * 品牌证明材料表
 */
@Mapper
public interface BrandFileMapper {

    List<BrandFile> queryAllList(BrandFile queryModel);

}
