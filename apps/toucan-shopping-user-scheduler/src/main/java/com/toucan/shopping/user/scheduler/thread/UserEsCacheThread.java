package com.toucan.shopping.user.scheduler.thread;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.toucan.shopping.user.api.feign.service.FeignUserService;
import com.toucan.shopping.user.export.entity.User;
import com.toucan.shopping.user.export.page.UserPageInfo;
import com.toucan.shopping.user.export.vo.UserElasticSearchVO;
import com.toucan.shopping.user.export.vo.UserVO;
import com.toucan.shopping.user.service.UserElasticSearchService;
import com.toucan.shopping.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.common.properties.Toucan;
import com.toucan.shopping.common.util.SignUtil;
import com.toucan.shopping.common.vo.RequestJsonVO;
import com.toucan.shopping.common.vo.ResultObjectVO;
import com.toucan.shopping.common.vo.ResultVO;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserEsCacheThread extends Thread {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Toucan toucan;


    @Autowired
    private FeignUserService feignUserService;


    @Autowired
    private UserElasticSearchService esUserService;

    public PageInfo queryUserPage(UserPageInfo userPageInfo) throws Exception
    {
        RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), userPageInfo);
        ResultObjectVO resultObjectVO = feignUserService.list(SignUtil.sign(requestJsonVO), requestJsonVO);
        if (resultObjectVO.getCode().intValue() == ResultVO.SUCCESS.intValue()) {
            String dataJson=JSONObject.toJSONString(resultObjectVO.getData());
            logger.info("调用用户中心 返回查询用户列表 {}",dataJson);
            PageInfo pageInfo = JSONArray.parseObject(dataJson, PageInfo.class);
            return pageInfo;
        }
        return null;
    }


    @Override
    public void run() {
        if (toucan.getUserCenterScheduler().isLoopEsCache()) {
            while(true) {
                try {
                    logger.info(" 缓存用户信息到ElasticSearch ........");
                    int page = 1;
                    int size = 1000;
                    PageInfo pageInfo = null;
                    boolean isUpdate = false;
                    do {
                        logger.info(" 查询用户列表 页码:{} 每页显示 {} ", page, size);
                        UserPageInfo query = new UserPageInfo();
                        query.setSize(size);
                        query.setPage(page);
                        pageInfo = queryUserPage(query);
                        page++;
                        if (pageInfo != null && CollectionUtils.isNotEmpty(pageInfo.getList())) {
                            String userListJson = JSONObject.toJSONString(pageInfo.getList());
                            List<UserVO> userList = JSONArray.parseArray(userListJson, UserVO.class);

                            logger.info("缓存用户列表 到Elasticsearch {}", userListJson);
                            for (UserVO user : userList) {
                                isUpdate = false;
                                List<UserElasticSearchVO> userElasticSearchVOS = esUserService.queryById(user.getId());
                                //如果缓存不存在用户将缓存起来
                                if (CollectionUtils.isEmpty(userElasticSearchVOS)) {
                                    UserElasticSearchVO userElasticSearchVO = new UserElasticSearchVO();
                                    BeanUtils.copyProperties(userElasticSearchVO, user);
                                    esUserService.save(userElasticSearchVO);
                                } else {
                                    //判断用户昵称是否做了修改
                                    UserElasticSearchVO userElasticSearchVO = userElasticSearchVOS.get(0);
                                    if (!StringUtils.equals(userElasticSearchVO.getNickName(), user.getNickName())) {
                                        userElasticSearchVO.setNickName(user.getNickName());
                                        isUpdate = true;
                                    }

                                    if (isUpdate) {
                                        logger.info(" 用户数据发生修改 更新缓存 用户ID:{}", userElasticSearchVO.getId());
                                        esUserService.update(userElasticSearchVO);
                                    }

                                }
                            }
                        }
                    } while (pageInfo != null && CollectionUtils.isNotEmpty(pageInfo.getList()));
                    //休眠5秒
                    this.sleep(10000);
                }catch(Exception e)
                {
                    logger.warn(e.getMessage(),e);
                }
            }
        }
    }
}
