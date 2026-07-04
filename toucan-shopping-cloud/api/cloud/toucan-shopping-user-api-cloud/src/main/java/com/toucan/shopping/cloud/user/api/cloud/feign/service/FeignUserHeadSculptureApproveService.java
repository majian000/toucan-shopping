package com.toucan.shopping.cloud.user.api.cloud.feign.service;

import com.toucan.shopping.cloud.user.api.UserHeadSculptureApproveServiceAPI;
import com.toucan.shopping.cloud.user.api.cloud.feign.fallback.FeignUserHeadSculptureApproveServiceFallbackFactory;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 用户头像审核服务
 */
@FeignClient(value = "toucan-shopping-gateway",path = "/toucan-shopping-user-proxy/user/head/sculpture/approve",fallbackFactory = FeignUserHeadSculptureApproveServiceFallbackFactory.class)
public interface FeignUserHeadSculptureApproveService extends UserHeadSculptureApproveServiceAPI {


    /**
     * 保存用户实名
     * @param requestJsonVO
     * @return
     */
    @Override
    @RequestMapping(value="/save", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    ResultObjectVO save(@RequestBody RequestJsonVO requestJsonVO);


    /**
     * 修改用户实名
     * @param requestJsonVO
     * @return
     */
    @Override
    @RequestMapping(value="/update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    ResultObjectVO update(@RequestBody RequestJsonVO requestJsonVO);

    /**
     * 根据用户主ID查询
     * @param requestJsonVO
     * @return
     */
    @Override
    @RequestMapping(value="/queryByUserMainId", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    ResultObjectVO queryByUserMainId(@RequestBody RequestJsonVO requestJsonVO);


    /**
     * 根据用户主ID查询激活的的审批
     * @param requestJsonVO
     * @return
     */
    @Override
    @RequestMapping(value="/queryAliveByUserMainId", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    ResultObjectVO queryAliveByUserMainId(@RequestBody RequestJsonVO requestJsonVO);


    /**
     * 根据用户主ID查询,并且根据创建时间倒序
     * @param requestJsonVO
     * @return
     */
    @Override
    @RequestMapping(value="/queryListByUserMainIdAndOrderByUpdateDateDesc", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    ResultObjectVO queryListByUserMainIdAndOrderByUpdateDateDesc(@RequestBody RequestJsonVO requestJsonVO);

    /**
     * 查询列表页
     * @param requestVo
     * @return
     */
    @Override
    @RequestMapping(value="/list/page", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    ResultObjectVO queryListPage(@RequestBody RequestJsonVO requestVo);


    /**
     * 通过指定审核
     * @param requestVo
     * @return
     */
    @Override
    @RequestMapping(value="/pass/id", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    ResultObjectVO passById(@RequestBody RequestJsonVO requestVo);



    /**
     * 批量删除
     * @param requestVo
     * @return
     */
    @Override
    @RequestMapping(value="/delete/ids", produces = "application/json;charset=UTF-8", method = RequestMethod.DELETE)
    ResultObjectVO deleteByIds(@RequestBody RequestJsonVO requestVo);


    /**
     * 根据ID查询
     * @param requestJsonVO
     * @return
     */
    @Override
    @RequestMapping(value="/queryById", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    ResultObjectVO queryById(@RequestBody RequestJsonVO requestJsonVO);


    /**
     * 驳回指定
     * @param requestVo
     * @return
     */
    @Override
    @RequestMapping(value="/reject/id", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public ResultObjectVO rejectById(@RequestBody RequestJsonVO requestVo);
}
