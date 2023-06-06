package com.toucan.shopping.cloud.apps.scheduler.canal.kafka.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.apps.scheduler.vo.CanalMessage;
import com.toucan.shopping.cloud.common.data.api.feign.service.FeignCategoryService;
import com.toucan.shopping.cloud.product.api.feign.service.FeignAttributeKeyValueService;
import com.toucan.shopping.cloud.product.api.feign.service.FeignBrandCategoryService;
import com.toucan.shopping.cloud.product.api.feign.service.FeignBrandService;
import com.toucan.shopping.modules.category.vo.CategoryVO;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.product.vo.*;
import com.toucan.shopping.modules.search.service.ProductSearchService;
import com.toucan.shopping.modules.search.vo.ProductSearchAttributeVO;
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
import java.util.*;

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
    private FeignAttributeKeyValueService feignAttributeKeyValueService;

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
                                if (productSkuVO.getDeleteStatus() != null
                                        && productSkuVO.getDeleteStatus().intValue() == 0
                                        && productSkuVO.getStatus() != null
                                        && productSkuVO.getStatus().intValue() == 1
                                        && productSkuVO.getStockNum().intValue() > 0) {
                                    List<ProductSearchResultVO> productSearchResultVOS = productSearchService.queryBySkuId(productSkuVO.getId());
                                    ProductSearchResultVO productSearchResultVO = new ProductSearchResultVO();
                                    productSearchResultVO.setId(productSkuVO.getId());
                                    productSearchResultVO.setSkuId(productSkuVO.getId());
                                    productSearchResultVO.setName(productSkuVO.getName());
                                    productSearchResultVO.setPrice(productSkuVO.getPrice());
                                    productSearchResultVO.setProductPreviewPath(productSkuVO.getProductPreviewPath());
                                    productSearchResultVO.setBrandId(productSkuVO.getBrandId());
                                    productSearchResultVO.setShopId(productSkuVO.getShopId());
                                    productSearchResultVO.setCategoryId(productSkuVO.getCategoryId());
                                    productSearchResultVO.setAttributeValueGroup(productSkuVO.getAttributeValueGroup());
                                    productSearchResultVO.setAttributes(new LinkedList<>());
                                    //设置属性列表
                                    productSkuVO.setAttributeMap(JSONObject.parseObject(productSkuVO.getAttributes(), HashMap.class));
                                    Set<String> keys = productSkuVO.getAttributeMap().keySet();
                                    List<String> queryAttributeKeys = new LinkedList<>();
                                    queryAttributeKeys.addAll(keys);
                                    List<String> queryAttributeValues = new LinkedList<>();
                                    for(String key:keys){
                                        ProductSearchAttributeVO productSearchAttributeVO = new ProductSearchAttributeVO();
                                        productSearchAttributeVO.setName(key);
                                        productSearchAttributeVO.setValue(productSkuVO.getAttributeMap().get(key));
                                        queryAttributeValues.add(productSkuVO.getAttributeMap().get(key));
                                        productSearchResultVO.getAttributes().add(productSearchAttributeVO);
                                    }

                                    //设置搜索属性列表
                                    AttributeKeyValueVO attributeKeyValueVO=new AttributeKeyValueVO();
                                    attributeKeyValueVO.setProductSpuId(productSkuVO.getProductId());
                                    attributeKeyValueVO.setCategoryId(productSkuVO.getCategoryId());
                                    attributeKeyValueVO.setAttributeKeyList(queryAttributeKeys);
                                    attributeKeyValueVO.setAttributeValueList(queryAttributeValues);
                                    ResultObjectVO resultObjectVO = feignAttributeKeyValueService.querySearchAttributeList(RequestJsonVOGenerator.generator(toucan.getAppCode(),attributeKeyValueVO));
                                    if(resultObjectVO.isSuccess())
                                    {
                                        List<AttributeKeyVO> attributeKeys = resultObjectVO.formatDataList(AttributeKeyVO.class);
                                        if(CollectionUtils.isNotEmpty(attributeKeys)){
                                            productSearchResultVO.setSearchAttributes(new LinkedList<>());
                                            for(AttributeKeyVO attributeKeyVO:attributeKeys)
                                            {
                                                if(CollectionUtils.isNotEmpty(attributeKeyVO.getValues()))
                                                {
                                                    for(AttributeValueVO attributeValueVO:attributeKeyVO.getValues()) {
                                                        ProductSearchAttributeVO productSearchAttributeVO = new ProductSearchAttributeVO();
                                                        productSearchAttributeVO.setNameId(attributeKeyVO.getId());
                                                        productSearchAttributeVO.setName(attributeKeyVO.getAttributeName());
                                                        productSearchAttributeVO.setValueId(attributeValueVO.getId());
                                                        productSearchAttributeVO.setValue(attributeValueVO.getAttributeValue());
                                                        productSearchResultVO.getSearchAttributes().add(productSearchAttributeVO);
                                                    }
                                                }
                                            }
                                        }
                                    }


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
                                        resultObjectVO = feignBrandService.findById(requestJsonVO.sign(), requestJsonVO);
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
                                        resultObjectVO = feignCategoryService.queryById(requestJsonVO);
                                        if (resultObjectVO.isSuccess() && resultObjectVO.getData() != null) {
                                            CategoryVO categoryVO = resultObjectVO.formatData(CategoryVO.class);
                                            //反转ID
                                            Collections.reverse(categoryVO.getIdPath());
                                            productSearchResultVO.setCategoryIds(new LinkedList<>());
                                            for(Long categoryId:categoryVO.getIdPath())
                                            {
                                                productSearchResultVO.getCategoryIds().add(String.valueOf(categoryId));
                                            }
                                            productSearchResultVO.setCategoryName(categoryVO.getName());
                                        }
                                    }

                                    if (CollectionUtils.isEmpty(productSearchResultVOS)) {
                                        productSearchService.save(productSearchResultVO);
                                    } else {
                                        productSearchResultVO.setCreateDate(productSearchResultVOS.get(0).getCreateDate());
                                        productSearchService.update(productSearchResultVO);
                                    }
                                }else if(productSkuVO.getDeleteStatus().intValue() == 1)
                                {
                                    List<Long> deleteFaildList = new ArrayList<>();
                                    productSearchService.removeById(productSkuVO.getId(),deleteFaildList);
                                }
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
