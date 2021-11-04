package com.toucan.shopping.modules.product.mapper;

import com.toucan.shopping.modules.product.entity.Brand;
import com.toucan.shopping.modules.product.page.BrandPageInfo;
import com.toucan.shopping.modules.product.vo.BrandVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface BrandMapper {



    /**
     * 查询列表页
     * @param pageInfo
     * @return
     */
    List<BrandVO> queryListPage(BrandPageInfo pageInfo);


    List<Brand> queryByIdList(List<Long> idList);


    /**
     * 返回列表页数量
     * @param pageInfo
     * @return
     */
    Long queryListPageCount(BrandPageInfo pageInfo);

    List<Brand> queryAllList(Brand queryModel);

    List<Brand> queryList(Brand queryModel);

    int insert(Brand entity);

    int update(Brand entity);

    int deleteById(Long id);


}
