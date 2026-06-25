package com.toucan.shopping.cloud.apps.scheduler.scheduler.article;

import com.toucan.shopping.cloud.content.api.feign.service.FeignArticleImageService;
import com.toucan.shopping.cloud.order.api.cloud.feign.service.FeignOrderService;
import com.toucan.shopping.cloud.user.api.feign.service.FeignUserStatisticService;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.DateUtils;
import com.toucan.shopping.modules.content.page.DeleteArticleImagePageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;


/**
 * 删除上传后没用到的图片
 */
@Component
@EnableScheduling
public class ArticleImageScheduler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private FeignOrderService feignOrderService;


    @Autowired
    private Toucan toucan;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private FeignArticleImageService feignArticleImageService;

    /**
     * 每天0:20执行
     */
    @Scheduled(cron = "0 20 0 * * ?")
    public void rerun()
    {
        logger.info("删除文章图片 开始=====================");
        try {
            DeleteArticleImagePageInfo deleteArticleImagePageInfo = new DeleteArticleImagePageInfo();
            //删除三天前的图片
            String endDateStr = DateUtils.FORMATTER_DD.get().format(DateUtils.advanceDay(new Date(),3));
            endDateStr+=" 23:59:59";
            deleteArticleImagePageInfo.setEndDate(DateUtils.FORMATTER_SS.get().parse(endDateStr));
            feignArticleImageService.deleteInvalidData(RequestJsonVOGenerator.generator(toucan.getAppCode(),deleteArticleImagePageInfo));
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
        logger.info("删除文章图片 结束=====================");
    }




}
