package com.toucan.shopping.cloud.product.api;

import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;

public interface AttributeKeyValueServiceAPI {

    /**
     * 根据分类ID查询
     * @param requestVo
     * @return
     */
    ResultObjectVO findByCategoryId(RequestJsonVO requestVo);

    /**
     * 根据分类ID查询属性树
     * @param requestVo
     * @return
     */
    ResultObjectVO queryAttributeTreePage(RequestJsonVO requestVo);

    /**
     * 根据SPUID和SKU中的分类ID以及属性 查询可被搜索的属性列表
     * 注:同一级分类下属性名称不允许重复
     * 例如：手机分类下有颜色属性，游戏手机分类下也允许有颜色属性
     * 结构:
     *      手机 颜色属性
     *      手机》游戏手机 颜色属性
     * @param requestVo
     * @return
     */
    ResultObjectVO querySearchAttributeList(RequestJsonVO requestVo);

}
