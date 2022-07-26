package com.toucan.shopping.cloud.apps.admin.auth.scheduler.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.admin.auth.api.feign.service.FeignFunctionService;
import com.toucan.shopping.cloud.admin.auth.api.feign.service.FeignRoleFunctionService;
import com.toucan.shopping.modules.admin.auth.cache.service.FunctionCacheService;
import com.toucan.shopping.modules.admin.auth.page.FunctionTreeInfo;
import com.toucan.shopping.modules.admin.auth.page.RoleFunctionPageInfo;
import com.toucan.shopping.modules.admin.auth.vo.FunctionCacheVO;
import com.toucan.shopping.modules.admin.auth.vo.FunctionVO;
import com.toucan.shopping.modules.admin.auth.vo.RoleFunctionCacheVO;
import com.toucan.shopping.modules.admin.auth.vo.RoleFunctionVO;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.SignUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 功能项刷新缓存
 */
@RestController
@RequestMapping("/functionToESCache")
public class FunctionToESCacheController {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Toucan toucan;


    @Autowired
    private FeignFunctionService feignFunctionService;


    @Autowired
    private FunctionCacheService functionCacheService;


    public PageInfo queryPage(FunctionTreeInfo queryPageInfo) throws Exception
    {
        RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), queryPageInfo);
        ResultObjectVO resultObjectVO = feignFunctionService.list(SignUtil.sign(requestJsonVO), requestJsonVO);
        if (resultObjectVO.getCode().intValue() == ResultVO.SUCCESS.intValue()) {
            String dataJson=JSONObject.toJSONString(resultObjectVO.getData());
            logger.info("调用权限中台 返回查询功能项列表 {}",dataJson);
            PageInfo pageInfo = resultObjectVO.formatData(PageInfo.class);
            return pageInfo;
        }
        return null;
    }


    @RequestMapping(value="/flush/all")
    @ResponseBody
    public ResultObjectVO flushAll(){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {

            //删除索引
            functionCacheService.deleteIndex();

            //如果不存在索引就创建一个
            while (!functionCacheService.existsIndex()) {
                functionCacheService.createIndex();
            }


            int page = 1;
            int limit = 500;
            PageInfo pageInfo = null;
            do {
                logger.info(" 查询账号功能项列表 页码:{} 每页显示 {} ", page, limit);
                FunctionTreeInfo query = new FunctionTreeInfo();
                query.setLimit(limit);
                query.setPage(page);
                pageInfo = queryPage(query);
                page++;
                if (pageInfo != null && CollectionUtils.isNotEmpty(pageInfo.getList())) {
                    String functionListJson = JSONObject.toJSONString(pageInfo.getList());
                    List<FunctionVO> functionVOS = JSONArray.parseArray(functionListJson, FunctionVO.class);

                    logger.info("缓存功能项关联列表 到Elasticsearch {}", functionListJson);
                    for (FunctionVO functionVO : functionVOS) {
                        List<FunctionCacheVO> functionCacheVOS = functionCacheService.queryById(functionVO.getId());
                        //如果缓存不存在功能项将缓存起来
                        if (CollectionUtils.isEmpty(functionCacheVOS)) {
                            FunctionCacheVO functionCacheVO = new FunctionCacheVO();
                            BeanUtils.copyProperties(functionCacheVO, functionVO);
                            functionCacheService.save(functionCacheVO);
                        }
                    }
                }
            } while (pageInfo != null && CollectionUtils.isNotEmpty(pageInfo.getList()));
        } catch (Exception e) {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("刷新失败!");
            logger.warn(e.getMessage(), e);
        }

        return resultObjectVO;
    }


}
