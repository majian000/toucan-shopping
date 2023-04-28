package com.toucan.shopping.cloud.apps.scheduler.canal.kafka.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.apps.scheduler.vo.CanalMessage;
import com.toucan.shopping.cloud.common.data.api.feign.service.FeignCategoryService;
import com.toucan.shopping.cloud.product.api.feign.service.FeignBrandCategoryService;
import com.toucan.shopping.cloud.product.api.feign.service.FeignBrandService;
import com.toucan.shopping.modules.category.vo.CategoryVO;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.product.vo.BrandCategoryVO;
import com.toucan.shopping.modules.product.vo.BrandVO;
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

    @Autowired
    private FeignCategoryService feignCategoryService;

    @Autowired
    private FeignBrandService feignBrandService;

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
                            if (productSkuVO.getDeleteStatus() != null
                                    && productSkuVO.getDeleteStatus().intValue() == 0
                                    && productSkuVO.getStatus()!=null
                                    && productSkuVO.getStatus().intValue()==1) {
                                List<ProductSearchResultVO> productSearchResultVOS = productSearchService.queryBySkuId(productSkuVO.getId());
                                ProductSearchResultVO productSearchResultVO = new ProductSearchResultVO();
                                productSearchResultVO.setId(productSkuVO.getId());
                                productSearchResultVO.setSkuId(productSkuVO.getId());
                                productSearchResultVO.setName(productSkuVO.getName());
                                productSearchResultVO.setPrice(productSkuVO.getPrice());
                                productSearchResultVO.setProductPreviewPath(productSkuVO.getProductPreviewPath());
                                productSearchResultVO.setBrandId(productSkuVO.getBrandId());
                                productSearchResultVO.setCategoryId(productSkuVO.getCategoryId());
                                productSearchResultVO.setAttributeValueGroup(productSkuVO.getAttributeValueGroup());

                                ProductSearchResultVO productSearchResultVOResult = null;
                                if (!CollectionUtils.isEmpty(productSearchResultVOS)) {
                                    productSearchResultVOResult = productSearchResultVOS.get(0);
                                }

                                //查询品牌名称
                                if (productSearchResultVOResult == null
                                        || productSearchResultVOResult.getBrandId() == null
                                        || productSearchResultVOResult.getBrandId().longValue() != productSearchResultVO.getBrandId().longValue()) {

                                    BrandVO queryBrand = new BrandVO();
                                    queryBrand.setId(productSkuVO.getBrandId());
                                    RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), queryBrand);
                                    ResultObjectVO resultObjectVO = feignBrandService.findById(requestJsonVO.sign(), requestJsonVO);
                                    if (resultObjectVO.isSuccess() && resultObjectVO.getData() != null) {
                                        List<BrandVO> brands = resultObjectVO.formatDataList(BrandVO.class);
                                        if (CollectionUtils.isNotEmpty(brands)) {
                                            BrandVO brandVO = brands.get(0);
                                            if (StringUtils.isNotEmpty(brandVO.getChineseName()) && StringUtils.isNotEmpty(brandVO.getEnglishName())) {
                                                productSearchResultVO.setBrandName(brandVO.getChineseName() + "/" + brandVO.getEnglishName());
                                            } else {
                                                if (StringUtils.isNotEmpty(brandVO.getChineseName())) {
                                                    productSearchResultVO.setBrandName(brandVO.getChineseName());
                                                }
                                                if (StringUtils.isNotEmpty(brandVO.getEnglishName())) {
                                                    productSearchResultVO.setBrandName(brandVO.getEnglishName());
                                                }
                                            }
                                        }
                                    }
                                }

                                //查询分类名称
                                if (productSearchResultVOResult == null
                                        || productSearchResultVOResult.getCategoryId() == null
                                        || productSearchResultVOResult.getCategoryId().longValue() != productSearchResultVO.getCategoryId().longValue()) {

                                    CategoryVO queryCategoryVO = new CategoryVO();
                                    queryCategoryVO.setId(productSkuVO.getCategoryId());
                                    RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), queryCategoryVO);
                                    ResultObjectVO resultObjectVO = feignCategoryService.queryById(requestJsonVO);
                                    if (resultObjectVO.isSuccess() && resultObjectVO.getData() != null) {
                                        CategoryVO categoryVO = resultObjectVO.formatData(CategoryVO.class);
                                        productSearchResultVO.setCategoryName(categoryVO.getName());
                                    }
                                }

                                if (CollectionUtils.isEmpty(productSearchResultVOS)) {
                                    productSearchService.save(productSearchResultVO);
                                } else {
                                    productSearchService.update(productSearchResultVO);
                                }
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
