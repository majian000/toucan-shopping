package com.toucan.shopping.cloud.apps.admin.auth.scheduler.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.admin.auth.api.feign.service.FeignAdminRoleService;
import com.toucan.shopping.cloud.admin.auth.api.feign.service.FeignRoleFunctionService;
import com.toucan.shopping.modules.admin.auth.cache.service.RoleFunctionCacheService;
import com.toucan.shopping.modules.admin.auth.page.AdminRolePageInfo;
import com.toucan.shopping.modules.admin.auth.page.RoleFunctionPageInfo;
import com.toucan.shopping.modules.admin.auth.vo.AdminRoleCacheVO;
import com.toucan.shopping.modules.admin.auth.vo.AdminRoleVO;
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
 * 角色功能项刷新缓存
 */
@RestController
@RequestMapping("/roleFunctionToESCache")
public class RoleFunctionToESCacheController {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Toucan toucan;

    @Autowired
    private FeignRoleFunctionService feignRoleFunctionService;


    @Autowired
    private RoleFunctionCacheService roleFunctionCacheService;


    public PageInfo queryPage(RoleFunctionPageInfo queryPageInfo) throws Exception
    {
        RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), queryPageInfo);
        ResultObjectVO resultObjectVO = feignRoleFunctionService.list(SignUtil.sign(requestJsonVO), requestJsonVO);
        if (resultObjectVO.getCode().intValue() == ResultVO.SUCCESS.intValue()) {
            String dataJson=JSONObject.toJSONString(resultObjectVO.getData());
            logger.info("调用权限中台 返回查询角色功能项列表 {}",dataJson);
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
            roleFunctionCacheService.deleteIndex();


            //如果不存在索引就创建一个
            while (!roleFunctionCacheService.existsIndex()) {
                roleFunctionCacheService.createIndex();
            }


            int page = 1;
            int limit = 500;
            PageInfo pageInfo = null;
            do {
                logger.info(" 查询账号角色-功能项关联列表 页码:{} 每页显示 {} ", page, limit);
                RoleFunctionPageInfo query = new RoleFunctionPageInfo();
                query.setLimit(limit);
                query.setPage(page);
                pageInfo = queryPage(query);
                page++;
                if (pageInfo != null && CollectionUtils.isNotEmpty(pageInfo.getList())) {
                    String roleFunctionListJson = JSONObject.toJSONString(pageInfo.getList());
                    List<RoleFunctionVO> roleFunctionVOS = JSONArray.parseArray(roleFunctionListJson, RoleFunctionVO.class);

                    logger.info("缓存角色-功能项关联列表 到Elasticsearch {}", roleFunctionListJson);
                    for (RoleFunctionVO roleFunctionVO : roleFunctionVOS) {
                        List<RoleFunctionCacheVO> roleFunctionCacheVOS = roleFunctionCacheService.queryById(roleFunctionVO.getId());
                        //如果缓存不存在角色功能项将缓存起来
                        if (CollectionUtils.isEmpty(roleFunctionCacheVOS)) {
                            RoleFunctionCacheVO roleFunctionCacheVO = new RoleFunctionCacheVO();
                            BeanUtils.copyProperties(roleFunctionCacheVO, roleFunctionVO);
                            roleFunctionCacheService.save(roleFunctionCacheVO);
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
