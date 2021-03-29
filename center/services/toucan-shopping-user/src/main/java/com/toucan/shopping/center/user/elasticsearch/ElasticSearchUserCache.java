package com.toucan.shopping.center.user.elasticsearch;


import com.toucan.shopping.center.user.entity.User;
import com.toucan.shopping.center.user.service.UserElasticSearchService;
import com.toucan.shopping.center.user.vo.UserElasticSearchVO;
import com.toucan.shopping.center.user.service.UserService;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 用户初始化到ES
 */
@Component
@Order(value = 1)
public class ElasticSearchUserCache implements ApplicationRunner {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserService userService;


    @Autowired
    private UserElasticSearchService esUserService;

    @Autowired
    private ElasticsearchRestTemplate elasticsearchTemplate;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.info(" 缓存用户信息到ElasticSearch ........");
        //TODO:增加分页,开启线程做初始化
        List<User> userList = userService.findListByEntity(null);
        if(CollectionUtils.isNotEmpty(userList))
        {
            for(User user:userList){
                UserElasticSearchVO esUserVO = new UserElasticSearchVO();
                BeanUtils.copyProperties(esUserVO,user);
                esUserService.save(esUserVO);
            }
        }
    }
}
