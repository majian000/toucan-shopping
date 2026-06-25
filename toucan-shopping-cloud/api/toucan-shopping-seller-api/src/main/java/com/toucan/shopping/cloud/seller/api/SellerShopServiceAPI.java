package com.toucan.shopping.cloud.seller.api;

import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;

/**
 * 商家店铺
 * @author majian
 */
public interface SellerShopServiceAPI {

    /**
     * 保存店铺
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO save(RequestJsonVO requestJsonVO);

    /**
     * 查询列表页
     * @param requestVo
     * @return
     */
    ResultObjectVO queryListPage(RequestJsonVO requestVo);

    /**
     * 查询指定用户下店铺
     * @param requestVo
     * @return
     */
    ResultObjectVO findByUser(RequestJsonVO requestVo);

    /**
     * 根据ID集合查询
     * @param requestVo
     * @return
     */
    ResultObjectVO findByIdList(RequestJsonVO requestVo);

    /**
     * 刷新缓存
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO flushCache(RequestJsonVO requestJsonVO);

    /**
     * 启用禁用店铺
     * @param requestVo
     * @return
     */
    ResultObjectVO disabledEnabled(RequestJsonVO requestVo);

    /**
     * 批量删除店铺
     * @param requestVo
     * @return
     */
    ResultObjectVO deleteByIds(RequestJsonVO requestVo);

    /**
     * 删除店铺
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO deleteById(RequestJsonVO requestJsonVO);

    /**
     * 根据ID查询
     * @param requestVo
     * @return
     */
    ResultObjectVO findById(RequestJsonVO requestVo);

    /**
     * 更新
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO update(RequestJsonVO requestJsonVO);

    /**
     * 更新图标
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO updateLogo(RequestJsonVO requestJsonVO);

    /**
     * 更新店铺信息
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO updateInfo(RequestJsonVO requestJsonVO);

}
