package com.toucan.shopping.cloud.admin.auth.api;

import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;

public interface DictCategoryServiceAPI {

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
     * 查询列表
     * @param requestVo
     * @return
     */
    ResultObjectVO queryList( RequestJsonVO requestVo);


    /**
     * 查询详情
     * @param requestVo
     * @return
     */
    ResultObjectVO queryDetail( RequestJsonVO requestVo);


}
