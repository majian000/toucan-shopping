package com.toucan.shopping.cloud.admin.auth.api;

import com.toucan.shopping.modules.admin.auth.vo.DictVO;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultTypeObjectVO;

import java.util.List;


public interface DictServiceAPI {

    /**
     * 添加字典分类
     * @param requestVo
     * @return
     */
    ResultObjectVO save( RequestJsonVO requestVo);



    /**
     * 編輯字典分类
     * @param requestVo
     * @return
     */
    ResultObjectVO update( RequestJsonVO requestVo);



    /**
     * 查询列表分页
     * @param requestVo
     * @return
     */
    ResultObjectVO listPage( RequestJsonVO requestVo);



    /**
     * 根据ID查询
     * @param requestVo
     * @return
     */
    ResultObjectVO findById( RequestJsonVO requestVo);




    /**
     * 删除指定字典分类(仅限中台使用)
     * @param requestVo
     * @return
     */
    ResultObjectVO deleteById( RequestJsonVO requestVo);





    /**
     * 批量删除字典分类(仅限中台使用)
     * @param requestVo
     * @return
     */
    ResultObjectVO deleteByIds( RequestJsonVO requestVo);



    /**
     * 查询树表格
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO queryTreeTableByPid( RequestJsonVO requestJsonVO);




    /**
     * 查询指定节点下子节点
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO queryTreeChildByPid( RequestJsonVO requestJsonVO);



    /**
     * 查询分类下的字典
     * @param requestJsonVO
     * @return
     */
    ResultObjectVO queryDictByCodeAndCategoryCode( RequestJsonVO requestJsonVO);



    /**
     * 查询分类下的字典
     * @param requestJsonVO
     * @return
     */
    ResultTypeObjectVO<List<DictVO>> queryDictByCodesAndCategoryCode( RequestJsonVO requestJsonVO);

}
