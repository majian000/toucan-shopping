package com.toucan.shopping.modules.column.mapper;

import com.toucan.shopping.modules.column.entity.HotProduct;
import com.toucan.shopping.modules.column.page.HotProductPageInfo;
import com.toucan.shopping.modules.column.vo.HotProductVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 * 栏目推荐商品
 */
@Mapper
public interface HotProductMapper {


    int inserts(List<HotProduct> entitys);


    /**
     * 查询列表页
     * @param pageInfo
     * @return
     */
    List<HotProductVO> queryListPage(HotProductPageInfo pageInfo);

    /**
     * 返回列表页数量
     * @param pageInfo
     * @return
     */
    Long queryListPageCount(HotProductPageInfo pageInfo);


    int insert(HotProduct hotProduct);

    List<HotProductVO> queryList(HotProductVO query);

    HotProduct findById(Long id);

}
