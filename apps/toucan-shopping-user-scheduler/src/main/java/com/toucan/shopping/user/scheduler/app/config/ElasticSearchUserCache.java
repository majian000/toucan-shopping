package com.toucan.shopping.user.scheduler.app.config;


import com.toucan.shopping.center.user.api.feign.service.FeignUserService;
import com.toucan.shopping.center.user.export.page.UserPageInfo;
import com.toucan.shopping.center.user.service.UserElasticSearchService;
import com.toucan.shopping.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.common.properties.Toucan;
import com.toucan.shopping.common.util.SignUtil;
import com.toucan.shopping.common.vo.RequestJsonVO;
import com.toucan.shopping.common.vo.ResultObjectVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Component;

/**
 * 用户初始化到ES
 */
@Component
@Order(value = 1)
public class ElasticSearchUserCache implements ApplicationRunner {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private FeignUserService feignUserService;


    @Autowired
    private UserElasticSearchService esUserService;

    @Autowired
    private Toucan toucan;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        if(toucan.getUserCenterScheduler().isInitEsCache()) {
            logger.info(" 缓存用户信息到ElasticSearch ........");
            //TODO:增加分页,开启线程做初始化
            UserPageInfo userPageInfo = new UserPageInfo();
            userPageInfo.setSize(10);
            userPageInfo.setLimit(0);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), userPageInfo);
            ResultObjectVO resultObjectVO = feignUserService.list(SignUtil.sign(requestJsonVO), requestJsonVO);
    //        if(CollectionUtils.isNotEmpty(userList))
    //        {
    //            for(User user:userList){
    //                UserElasticSearchVO esUserVO = new UserElasticSearchVO();
    //                BeanUtils.copyProperties(esUserVO,user);
    //                esUserService.save(esUserVO);
    //            }
    //        }
        }
    }
}
