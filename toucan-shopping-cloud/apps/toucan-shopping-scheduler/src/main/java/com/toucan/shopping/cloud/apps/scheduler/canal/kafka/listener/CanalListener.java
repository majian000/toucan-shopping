package com.toucan.shopping.cloud.apps.scheduler.canal.kafka.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.apps.scheduler.vo.CanalMessage;
import com.toucan.shopping.cloud.common.data.api.feign.service.FeignCategoryService;
import com.toucan.shopping.cloud.product.api.feign.service.FeignAttributeKeyValueService;
import com.toucan.shopping.cloud.product.api.feign.service.FeignBrandCategoryService;
import com.toucan.shopping.cloud.product.api.feign.service.FeignBrandService;
import com.toucan.shopping.cloud.search.helper.service.ProductSearchHelper;
import com.toucan.shopping.cloud.seller.api.feign.service.FeignShopCategoryService;
import com.toucan.shopping.modules.category.vo.CategoryVO;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.product.vo.*;
import com.toucan.shopping.modules.search.service.ProductSearchService;
import com.toucan.shopping.modules.search.vo.ProductSearchAttributeVO;
import com.toucan.shopping.modules.search.vo.ProductSearchResultVO;
import com.toucan.shopping.modules.seller.vo.ShopCategoryVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
public class CanalListener {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ProductSearchService productSearchService;

    @Autowired
    private ProductSearchHelper productSearchHelper;


    @Autowired
    private Toucan toucan;

    @KafkaListener(topics = "canal_topic",groupId = "canal_topic_group",  concurrency = "5")
    public void consumer(ConsumerRecord<String, String> record){
        try {
            logger.info("接收canal消息 {} ", record.value());
            if (StringUtils.isNotEmpty(record.value())) {
                CanalMessage canalMessage = JSONObject.parseObject(record.value(), CanalMessage.class);
                //修改了SKU
                if (canalMessage.getTable().startsWith("t_product_sku_")) {
                    if (CollectionUtils.isNotEmpty(canalMessage.getData())) {
                        canalMessage.getData().get(0).put("create_date",null);
                        canalMessage.getData().get(0).put("update_date",null);
                        ProductSkuVO productSkuVO = JSONObject.parseObject(JSONObject.toJSONString(canalMessage.getData().get(0)), ProductSkuVO.class);
                        if (productSkuVO != null) {
                            String typeUp = canalMessage.getType().toUpperCase();
                            if ("INSERT".equals(typeUp) || "UPDATE".equals(typeUp)) {
                                productSearchHelper.refresh(productSkuVO);
                            }else if("DELETE".equals(typeUp))
                            {
                                List<Long> deleteFaildList = new ArrayList<>();
                                productSearchService.removeById(productSkuVO.getId(),deleteFaildList);
                            }
                        }
                    }
                }
            }
        }catch(Exception e)
        {
            logger.error(e.getMessage(),e);
        }
    }




}
