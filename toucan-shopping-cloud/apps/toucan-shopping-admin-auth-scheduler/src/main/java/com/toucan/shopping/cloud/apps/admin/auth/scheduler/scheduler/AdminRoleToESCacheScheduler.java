package com.toucan.shopping.cloud.apps.admin.auth.scheduler.scheduler;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.admin.auth.api.feign.service.FeignAdminRoleService;
import com.toucan.shopping.modules.admin.auth.es.service.AdminRoleElasticSearchService;
import com.toucan.shopping.modules.admin.auth.page.AdminRolePageInfo;
import com.toucan.shopping.modules.admin.auth.vo.AdminRoleElasticSearchVO;
import com.toucan.shopping.modules.admin.auth.vo.AdminRoleVO;
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
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * 缓存账号角色数据到es中
 * 每天凌晨开始清空es后重新刷新数据
 */
@Component
@EnableScheduling
public class AdminRoleToESCacheScheduler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Toucan toucan;


    @Autowired
    private FeignAdminRoleService feignAdminRoleService;


    @Autowired
    private AdminRoleElasticSearchService adminRoleElasticSearchService;


    public PageInfo queryPage(AdminRolePageInfo queryPageInfo) throws Exception
    {
        RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), queryPageInfo);
        ResultObjectVO resultObjectVO = feignAdminRoleService.list(SignUtil.sign(requestJsonVO), requestJsonVO);
        if (resultObjectVO.getCode().intValue() == ResultVO.SUCCESS.intValue()) {
            String dataJson=JSONObject.toJSONString(resultObjectVO.getData());
            logger.info("调用权限中台 返回查询账号角色列表 {}",dataJson);
            PageInfo pageInfo = resultObjectVO.formatData(PageInfo.class);
            return pageInfo;
        }
        return null;
    }

    /**
     * 每天凌晨开始执行
     */
    @Scheduled(cron = "0 0 0 1/1 * ?")
    public void rerun()
    {
        if(toucan.getAdminAuthScheduler().isLoopEsCache()) {
            logger.info("缓存账号角色信息到ElasticSearch 开始=====================");
            try {

                //删除索引
                adminRoleElasticSearchService.deleteIndex();


                //如果不存在索引就创建一个
                while (!adminRoleElasticSearchService.existsIndex()) {
                    adminRoleElasticSearchService.createIndex();
                }


                int page = 1;
                int limit = 500;
                PageInfo pageInfo = null;
                do {
                    logger.info(" 查询账号角色列表 页码:{} 每页显示 {} ", page, limit);
                    AdminRolePageInfo query = new AdminRolePageInfo();
                    query.setLimit(limit);
                    query.setPage(page);
                    pageInfo = queryPage(query);
                    page++;
                    if (pageInfo != null && org.apache.commons.collections.CollectionUtils.isNotEmpty(pageInfo.getList())) {
                        String adminRoleListJson = JSONObject.toJSONString(pageInfo.getList());
                        List<AdminRoleVO> adminRoleVOS = JSONArray.parseArray(adminRoleListJson, AdminRoleVO.class);

                        logger.info("缓存账号角色列表 到Elasticsearch {}", adminRoleListJson);
                        for (AdminRoleVO adminRoleVO : adminRoleVOS) {
                            List<AdminRoleElasticSearchVO> adminRoleElasticSearchVOS = adminRoleElasticSearchService.queryById(adminRoleVO.getId());
                            //如果缓存不存在账号角色将缓存起来
                            if (org.apache.commons.collections.CollectionUtils.isEmpty(adminRoleElasticSearchVOS)) {
                                AdminRoleElasticSearchVO adminRoleElasticSearchVO = new AdminRoleElasticSearchVO();
                                BeanUtils.copyProperties(adminRoleElasticSearchVO, adminRoleVO);
                                adminRoleElasticSearchService.save(adminRoleElasticSearchVO);
                            }
                        }
                    }
                } while (pageInfo != null && CollectionUtils.isNotEmpty(pageInfo.getList()));
            } catch (Exception e) {
                logger.warn(e.getMessage(), e);
            }
            logger.info("缓存账号角色信息到ElasticSearch 结束=====================");
        }
    }

}
