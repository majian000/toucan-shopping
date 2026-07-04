package com.toucan.shopping.cloud.admin.auth.api.cloud.feign.service;

import com.toucan.shopping.cloud.admin.auth.api.DictServiceAPI;
import com.toucan.shopping.cloud.admin.auth.api.cloud.feign.fallback.FeignDictServiceFallbackFactory;
import com.toucan.shopping.modules.admin.auth.vo.DictVO;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultTypeObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(value = "toucan-shopping-gateway",path = "/toucan-shopping-admin-auth-proxy/dict",fallbackFactory = FeignDictServiceFallbackFactory.class)
public interface FeignDictService extends DictServiceAPI {

    /**
     * 添加字典分类
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/save", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    ResultObjectVO save(@RequestBody RequestJsonVO requestVo);



    /**
     * 編輯字典分类
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    ResultObjectVO update(@RequestBody RequestJsonVO requestVo);



    /**
     * 查询列表分页
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/list/page", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    ResultObjectVO listPage(@RequestBody RequestJsonVO requestVo);



    /**
     * 根据ID查询
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/find/id", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    ResultObjectVO findById(@RequestBody RequestJsonVO requestVo);




    /**
     * 删除指定字典分类(仅限中台使用)
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/delete/id", produces = "application/json;charset=UTF-8", method = RequestMethod.DELETE)
    ResultObjectVO deleteById(@RequestBody RequestJsonVO requestVo);





    /**
     * 批量删除字典分类(仅限中台使用)
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/delete/ids", produces = "application/json;charset=UTF-8", method = RequestMethod.DELETE)
    ResultObjectVO deleteByIds(@RequestBody RequestJsonVO requestVo);



    /**
     * 查询树表格
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/tree/table/by/pid", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    ResultObjectVO queryTreeTableByPid(@RequestBody RequestJsonVO requestJsonVO);




    /**
     * 查询指定节点下子节点
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/tree/child", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    ResultObjectVO queryTreeChildByPid(@RequestBody RequestJsonVO requestJsonVO);



    /**
     * 查询分类下的字典
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="query/dict/by/code/category/code", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    ResultObjectVO queryDictByCodeAndCategoryCode(@RequestBody RequestJsonVO requestJsonVO);



    /**
     * 查询分类下的字典
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="query/dict/by/codes/category/code", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    ResultTypeObjectVO<List<DictVO>> queryDictByCodesAndCategoryCode(@RequestBody RequestJsonVO requestJsonVO);

}
