package com.toucan.shopping.cloud.apps.admin.auth.scheduler.scheduler;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.admin.auth.api.feign.service.FeignAdminAppService;
import com.toucan.shopping.modules.admin.auth.entity.AdminApp;
import com.toucan.shopping.modules.admin.auth.page.AdminAppPageInfo;
import com.toucan.shopping.modules.admin.auth.page.AdminRolePageInfo;
import com.toucan.shopping.modules.admin.auth.redis.AdminCenterRedisKey;
import com.toucan.shopping.modules.admin.auth.vo.AdminAppVO;
import com.toucan.shopping.modules.admin.auth.vo.AdminRoleVO;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.SignUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

/**
 * 循环查询登录用户(将超时的key状态改成未登录)
 */
@Component
@EnableScheduling
public class AdminLoginStatusScheduler {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Toucan toucan;

    @Autowired
    private FeignAdminAppService feignAdminAppService;

    @Autowired
    private StringRedisTemplate redisTemplate;



    public PageInfo queryPage(AdminAppPageInfo queryPageInfo) throws Exception
    {
        RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), queryPageInfo);
        ResultObjectVO resultObjectVO = feignAdminAppService.loginList(requestJsonVO);
        if (resultObjectVO.getCode().intValue() == ResultVO.SUCCESS.intValue()) {
            String dataJson= JSONObject.toJSONString(resultObjectVO.getData());
            logger.info("调用权限中台 返回查询登录的用户列表 {}",dataJson);
            PageInfo pageInfo = resultObjectVO.formatData(PageInfo.class);
            return pageInfo;
        }
        return null;
    }

    /**
     * 10分钟刷新一次
     */
    @Scheduled(cron = "0 0/3 * * * ? ")
    public void rerun() {
        if (toucan.getAdminAuthScheduler().isLoopLoginCache()) {
            logger.info("刷新登录用户状态 开始.......");
            try {
                int page = 1;
                int limit = 500;
                PageInfo pageInfo = null;
                do {
                    logger.info(" 查询账号登录列表 页码:{} 每页显示 {} ", page, limit);
                    AdminAppPageInfo query = new AdminAppPageInfo();
                    query.setLimit(limit);
                    query.setPage(page);
                    query.setLoginStatus((short)1);
                    pageInfo = queryPage(query);
                    page++;
                    if (pageInfo != null && CollectionUtils.isNotEmpty(pageInfo.getList())) {
                        String adminAppListJson = JSONObject.toJSONString(pageInfo.getList());
                        logger.info("登录用户列表 {} ",adminAppListJson);
                        List<AdminAppVO> adminAppVOS = JSONArray.parseArray(adminAppListJson, AdminAppVO.class);
                        List<AdminAppVO> offlineAdminApps = new LinkedList<>();
                        for(AdminAppVO adminAppVO:adminAppVOS) {
                            if(StringUtils.isNotEmpty(adminAppVO.getAdminId())&&StringUtils.isNotEmpty(adminAppVO.getAppCodes())) {
                                String[] appCodeArray = adminAppVO.getAppCodes().split(",");
                                for(String appCode:appCodeArray) {
                                    Object loginTokenObject = redisTemplate.opsForHash().get(
                                            AdminCenterRedisKey.getLoginTokenGroupKey(adminAppVO.getAdminId()),
                                            AdminCenterRedisKey.getLoginTokenAppKey(adminAppVO.getAdminId(), appCode));
                                    //自动超时下线
                                    if(loginTokenObject==null)
                                    {
                                        AdminAppVO offlineAdminApp = new AdminAppVO();
                                        offlineAdminApp.setAdminId(adminAppVO.getAdminId());
                                        offlineAdminApp.setAppCode(appCode);
                                        offlineAdminApps.add(offlineAdminApp);
                                    }
                                }
                            }
                        }
                        //将那些超时下线的登录状态修改
                        if(CollectionUtils.isNotEmpty(offlineAdminApps))
                        {
                            feignAdminAppService.batchUpdateLoginStatus(RequestJsonVOGenerator.generator(toucan.getAppCode(),offlineAdminApps));
                        }
                    }
                } while (pageInfo != null && CollectionUtils.isNotEmpty(pageInfo.getList()));
            }catch(Exception e)
            {
                logger.error(e.getMessage(),e);
            }
            logger.info("刷新登录用户状态 结束.......");
        }
    }
}