package com.toucan.shopping.cloud.apps.scheduler.canal.kafka.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.apps.scheduler.vo.CanalMessage;
import com.toucan.shopping.modules.product.vo.ProductSkuVO;
import com.toucan.shopping.modules.search.service.ProductSearchService;
import com.toucan.shopping.modules.search.vo.ProductSearchResultVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class CanalListener {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ProductSearchService productSearchService;

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
                            List<ProductSearchResultVO> productSearchResultVOS = productSearchService.queryBySkuId(productSkuVO.getId());
                            ProductSearchResultVO productSearchResultVO = new ProductSearchResultVO();
                            productSearchResultVO.setId(productSkuVO.getId());
                            productSearchResultVO.setSkuId(productSkuVO.getId());
                            productSearchResultVO.setName(productSkuVO.getName());
                            productSearchResultVO.setPrice(productSkuVO.getPrice());
                            productSearchResultVO.setProductPreviewPath(productSkuVO.getProductPreviewPath());
                            if(CollectionUtils.isEmpty(productSearchResultVOS)) {
                                productSearchService.save(productSearchResultVO);
                            }else{
                                productSearchService.update(productSearchResultVO);
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
