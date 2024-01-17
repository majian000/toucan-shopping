package com.toucan.shopping.cloud.shop.product;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.apps.seller.web.app.CloudSellerWebApplication;
import com.toucan.shopping.cloud.product.api.feign.service.FeignShopProductApproveService;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.DateUtils;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.product.vo.PublishProductApproveVO;
import com.toucan.shopping.modules.product.vo.ShopProductApproveSkuAttribute;
import com.toucan.shopping.modules.product.vo.ShopProductApproveSkuVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = CloudSellerWebApplication.class)
public class ShopProductApproveTest {

    @Autowired
    private FeignShopProductApproveService feignShopProductApproveService;

    @Autowired
    private Toucan toucan;


    /**
     * 批量插入店铺商品
     */
    @Test
    public void batchInsertShopProductApprove() throws NoSuchAlgorithmException {
        Random rand = new Random();
        double randomNum = 1000.0 + rand.nextDouble() * 1000.0;
        String shopProductApproveJson="{\"appCode\":\"10001001\",\"articleNumber\":\"\",\"attributes\":\"{\\\"颜色\\\":[\\\"黑色\\\",\\\"白色\\\",\\\"红色\\\"],\\\"版本\\\":[\\\"128G\\\",\\\"256G\\\"]}\",\"brandId\":\"984040620811092014\",\"buckleInventoryMethod\":2,\"categoryId\":\"889589266080858188\",\"changeOrReturn\":1,\"createUserId\":\"891099441195384848\",\"etractMethod\":\"1\",\"freightTemplateId\":\"1032316349239525394\",\"freightTemplateName\":\"包邮模板\",\"giveInvoice\":1,\"mainPhotoFilePath\":\"group1/M00/00/10/rB5PVWWnf4qAQMSUAACgYFKM-yU16..JPG\",\"name\":\"华为mate60pro\",\"num\":8322,\"payMethod\":1,\"productDescription\":{\"productDescriptionImgs\":[{\"imgSort\":1,\"link\":\"\",\"title\":\"\",\"type\":1,\"width\":\"750\",\"widthUnit\":\"px\"}]},\"productSkuVOList\":[{\"appCode\":\"10001001\",\"attributeMap\":{\"颜色\":\"黑色\",\"版本\":\"128G\"},\"attributeValueGroup\":\"黑色_128G\",\"attributes\":\"{\\\"颜色\\\":\\\"黑色\\\",\\\"版本\\\":\\\"128G\\\"}\",\"name\":\"华为mate60pro 黑色 128G\",\"onlyName\":\"华为mate60pro\",\"price\":3699.00,\"productPreviewPath\":\"group1/M00/00/10/rB5PVWWnf4qAK95tAACgYFKM-yU17..JPG\",\"roughWeight\":100,\"stockNum\":6000,\"suttle\":100},{\"appCode\":\"10001001\",\"attributeMap\":{\"颜色\":\"黑色\",\"版本\":\"256G\"},\"attributeValueGroup\":\"黑色_256G\",\"attributes\":\"{\\\"颜色\\\":\\\"黑色\\\",\\\"版本\\\":\\\"256G\\\"}\",\"name\":\"华为mate60pro 黑色 256G\",\"onlyName\":\"华为mate60pro\",\"price\":123,\"productPreviewPath\":\"group1/M00/00/10/rB5PVWWnf4qABIHVAACgYFKM-yU98..JPG\",\"roughWeight\":100,\"stockNum\":500,\"suttle\":10},{\"appCode\":\"10001001\",\"attributeMap\":{\"颜色\":\"白色\",\"版本\":\"128G\"},\"attributeValueGroup\":\"白色_128G\",\"attributes\":\"{\\\"颜色\\\":\\\"白色\\\",\\\"版本\\\":\\\"128G\\\"}\",\"name\":\"华为mate60pro 白色 128G\",\"onlyName\":\"华为mate60pro\",\"price\":1969.00,\"productPreviewPath\":\"group1/M00/00/10/rB5PVWWnf4qAbFWTAACgYFKM-yU59..JPG\",\"roughWeight\":10,\"stockNum\":900,\"suttle\":10},{\"appCode\":\"10001001\",\"attributeMap\":{\"颜色\":\"白色\",\"版本\":\"256G\"},\"attributeValueGroup\":\"白色_256G\",\"attributes\":\"{\\\"颜色\\\":\\\"白色\\\",\\\"版本\\\":\\\"256G\\\"}\",\"name\":\"华为mate60pro 白色 256G\",\"onlyName\":\"华为mate60pro\",\"price\":1969.00,\"productPreviewPath\":\"group1/M00/00/10/rB5PVWWnf4uAEuJfAACgYFKM-yU05..JPG\",\"roughWeight\":10,\"stockNum\":900,\"suttle\":10},{\"appCode\":\"10001001\",\"attributeMap\":{\"颜色\":\"红色\",\"版本\":\"128G\"},\"attributeValueGroup\":\"红色_128G\",\"attributes\":\"{\\\"颜色\\\":\\\"红色\\\",\\\"版本\\\":\\\"128G\\\"}\",\"name\":\"华为mate60pro 红色 128G\",\"onlyName\":\"华为mate60pro\",\"price\":111,\"productPreviewPath\":\"group1/M00/00/10/rB5PVWWnf4uALgVeAACgYFKM-yU93..JPG\",\"roughWeight\":10,\"stockNum\":11,\"suttle\":10},{\"appCode\":\"10001001\",\"attributeMap\":{\"颜色\":\"红色\",\"版本\":\"256G\"},\"attributeValueGroup\":\"红色_256G\",\"attributes\":\"{\\\"颜色\\\":\\\"红色\\\",\\\"版本\\\":\\\"256G\\\"}\",\"name\":\"华为mate60pro 红色 256G\",\"onlyName\":\"华为mate60pro\",\"price\":111,\"productPreviewPath\":\"group1/M00/00/10/rB5PVWWnf4uAZn0bAACgYFKM-yU78..JPG\",\"roughWeight\":10,\"stockNum\":11,\"suttle\":10}],\"sellerNo\":\"\",\"shelvesStatus\":2,\"shopCategoryId\":\"983770268977594441\",\"shopId\":\"983769356921995303\",\"shopProductApproveDescriptionJson\":\"{}\",\"vcode\":\"jq75\"}";
        PublishProductApproveVO publishProductVO = JSONObject.parseObject(shopProductApproveJson,PublishProductApproveVO.class);
        publishProductVO.setName(publishProductVO.getName()+"_"+DateUtils.currentDate().getTime());
        for(ShopProductApproveSkuVO shopProductApproveSkuVO:publishProductVO.getProductSkuVOList())
        {
            shopProductApproveSkuVO.setPrice(new BigDecimal(randomNum));
        }
        RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), publishProductVO);
        ResultObjectVO resultObjectVO = feignShopProductApproveService.publish(requestJsonVO);


        PublishProductApproveVO queryPublishProductApprove=new PublishProductApproveVO();
        queryPublishProductApprove.setShopId(983769356921995303L);
        resultObjectVO = feignShopProductApproveService.queryApproveListByShopId(RequestJsonVOGenerator.generator(toucan.getAppCode(), queryPublishProductApprove));
    }


}
