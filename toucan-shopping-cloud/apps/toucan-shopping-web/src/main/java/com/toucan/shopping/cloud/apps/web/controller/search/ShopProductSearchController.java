package com.toucan.shopping.cloud.apps.web.controller.search;

import com.toucan.shopping.cloud.product.api.feign.service.FeignAttributeKeyService;
import com.toucan.shopping.cloud.product.api.feign.service.FeignBrandService;
import com.toucan.shopping.cloud.search.api.feign.service.FeignProductSearchService;
import com.toucan.shopping.cloud.seller.api.feign.service.FeignSellerShopService;
import com.toucan.shopping.cloud.seller.api.feign.service.FeignShopCategoryService;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.SignUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.image.upload.service.ImageUploadService;
import com.toucan.shopping.modules.product.vo.AttributeKeyVO;
import com.toucan.shopping.modules.product.vo.AttributeValueVO;
import com.toucan.shopping.modules.product.vo.BrandVO;
import com.toucan.shopping.modules.search.vo.ProductSearchAttributeVO;
import com.toucan.shopping.modules.search.vo.ProductSearchResultVO;
import com.toucan.shopping.modules.search.vo.ProductSearchVO;
import com.toucan.shopping.modules.seller.entity.SellerShop;
import com.toucan.shopping.modules.seller.vo.SellerShopVO;
import com.toucan.shopping.modules.seller.vo.ShopCategoryVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 店铺内商品搜索
 */
@Controller("shopProductSearchController")
@RequestMapping("/api/shop/product")
public class ShopProductSearchController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Toucan toucan;

    @Autowired
    private FeignProductSearchService feignProductSearchService;

    @Autowired
    private ImageUploadService imageUploadService;

    @Autowired
    private FeignAttributeKeyService feignAttributeKeyService;

    @Autowired
    private FeignBrandService feignBrandService;

    @Autowired
    private FeignSellerShopService feignSellerShopService;

    @Autowired
    private FeignShopCategoryService feignShopCategoryService;

    private String doSearch(ProductSearchVO productSearchVO, HttpServletRequest httpServletRequest)
    {
        try {
            RequestJsonVO requestJsonVO =null;
            ResultObjectVO resultObjectVO = null;
            productSearchVO.setSize(20);
            PageInfo pageInfo = null;
            /**
             * 优先级先查询品牌,如果关键字能匹配上品牌就查询出该品牌下所有商品
             * 如果不能匹配上匹配就关键字全量搜索
             */
            List<BrandVO> hitBrands = new ArrayList<>();
            String keyword = productSearchVO.getKeyword();
            if("t".equals(productSearchVO.getQbs())) {

                productSearchVO.setBn(productSearchVO.getKeyword());
                productSearchVO.setKeyword(null);

                requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), productSearchVO);

                productSearchVO.setBn(null);
                productSearchVO.setKeyword(keyword);

                resultObjectVO = feignProductSearchService.search(requestJsonVO);
                if(resultObjectVO.isSuccess()) {
                    pageInfo = resultObjectVO.formatData(PageInfo.class);
                    List<ProductSearchResultVO> productResult = pageInfo.formatDataList(ProductSearchResultVO.class);
                    if (CollectionUtils.isNotEmpty(productResult)) {

                        //没有关键字,默认查询了全部商品,就不回显品牌
                        if(StringUtils.isNotEmpty(keyword)) {
                            String[] ebids = null; //不查询这个品牌的商品
                            if (StringUtils.isNotEmpty(productSearchVO.getEbids())) {
                                ebids = productSearchVO.getEbids().split(",");
                            }
                            for (ProductSearchResultVO productSearchResultVO : productResult) {
                                if (StringUtils.isNotEmpty(productSearchResultVO.getBrandName())) {
                                    BrandVO brandVO = new BrandVO();
                                    brandVO.setName(productSearchResultVO.getBrandName());
                                    brandVO.setId(productSearchResultVO.getBrandId());
                                    if (ebids != null) {
                                        for (String ebid : ebids) {
                                            if (!ebid.equals(String.valueOf(brandVO.getId()))&&keyword.equals(brandVO.getName())) {
                                                hitBrands.add(brandVO);
                                                break;
                                            }
                                        }
                                    }else if(keyword.equals(brandVO.getName())){
                                        hitBrands.add(brandVO);
                                    }
                                }
                            }
                        }
                    }
                }

                if (CollectionUtils.isNotEmpty(hitBrands)) {
                    hitBrands = hitBrands.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(
                            () -> new TreeSet<>(Comparator.comparing(BrandVO::getId))), ArrayList::new));
                    productSearchVO.setBrandIds(hitBrands.stream().map(BrandVO::getId).distinct().collect(Collectors.toList()));
                }
            }

            if(StringUtils.isNotEmpty(productSearchVO.getBid())&&StringUtils.isNotEmpty(productSearchVO.getBn()))
            {
                boolean existsHitBrand=false;
                for(BrandVO hitBrandVO:hitBrands)
                {
                    if(hitBrandVO!=null&&hitBrandVO.getId().equals(productSearchVO.getBid()))
                    {
                        existsHitBrand = true;
                        break;
                    }
                }
                if(!existsHitBrand) {
                    if(StringUtils.isNotEmpty(productSearchVO.getBn())) {
                        BrandVO brandVO = new BrandVO();
                        brandVO.setId(Long.parseLong(productSearchVO.getBid()));
                        brandVO.setName(productSearchVO.getBn());
                        hitBrands.add(brandVO);
                    }
                }
            }


            //选择的查询属性
            if(StringUtils.isNotEmpty(productSearchVO.getAb())&&StringUtils.isNotEmpty(productSearchVO.getAbids()))
            {
                productSearchVO.setSearchAttributes(new LinkedList<>());
                String[] abArray = productSearchVO.getAb().split(",");
                String[] abidsArray = productSearchVO.getAbids().split(",");
                if(abArray!=null&&abidsArray!=null&&abidsArray.length==abidsArray.length)
                {
                    for(int i=0;i<abArray.length;i++) {
                        String[] akv = abArray[i].split(":");
                        ProductSearchAttributeVO productSearchAttributeVO = new ProductSearchAttributeVO();
                        productSearchAttributeVO.setName(akv[0]);
                        productSearchAttributeVO.setValue(akv[1]);
                        productSearchAttributeVO.setNameId(Long.parseLong(abidsArray[i]));
                        productSearchVO.getSearchAttributes().add(productSearchAttributeVO);
                    }
                }
            }

            //品牌名称
            httpServletRequest.setAttribute("hitBrands", hitBrands);
            //关键字
            httpServletRequest.setAttribute("keyword",productSearchVO.getKeyword());
            //分类ID
            httpServletRequest.setAttribute("cid",productSearchVO.getCid());
            //店铺分类ID
            httpServletRequest.setAttribute("scid",productSearchVO.getScid());
            //店铺ID
            httpServletRequest.setAttribute("sid",productSearchVO.getSid());
            //将这些品牌ID排除出查询条件
            httpServletRequest.setAttribute("ebids",productSearchVO.getEbids());
            //是否先查询品牌
            httpServletRequest.setAttribute("qbs",productSearchVO.getQbs());
            //属性ID列表
            httpServletRequest.setAttribute("abids",productSearchVO.getAbids());
            //属性集合字符串
            httpServletRequest.setAttribute("ab",productSearchVO.getAb());
            //属性集合
            httpServletRequest.setAttribute("selectSearchAttributes",productSearchVO.getSearchAttributes());
            //品牌ID
            httpServletRequest.setAttribute("bid",productSearchVO.getBid());
            //品牌名称
            httpServletRequest.setAttribute("bn",productSearchVO.getBn());
            //价格区间
            httpServletRequest.setAttribute("ps",productSearchVO.getPs());
            httpServletRequest.setAttribute("pe",productSearchVO.getPe());
            //店铺ID
            httpServletRequest.setAttribute("curspid",productSearchVO.getSid());
            httpServletRequest.setAttribute("sid",productSearchVO.getSid());

            //默认排序
            httpServletRequest.setAttribute("stt","default");
            //价格排序
            httpServletRequest.setAttribute("pst",productSearchVO.getPst());
            if(StringUtils.isNotEmpty(productSearchVO.getPst()))
            {
                httpServletRequest.setAttribute("stt","price");
            }
            //新品排序
            httpServletRequest.setAttribute("pdst",productSearchVO.getPdst());
            if(StringUtils.isNotEmpty(productSearchVO.getPdst()))
            {
                httpServletRequest.setAttribute("stt","newest");
            }

            if(StringUtils.isNotEmpty(productSearchVO.getPs()))
            {
                try{
                    productSearchVO.setPsd(Double.parseDouble(productSearchVO.getPs()));
                }catch(Exception e)
                {
                    logger.error(e.getMessage(),e);
                }
            }

            if(StringUtils.isNotEmpty(productSearchVO.getPe()))
            {
                try{
                    productSearchVO.setPed(Double.parseDouble(productSearchVO.getPe()));
                }catch(Exception e)
                {
                    logger.error(e.getMessage(),e);
                }
            }

            //上面的品牌查询没有任何匹配
            if(pageInfo==null||CollectionUtils.isEmpty(pageInfo.getList())) {
                requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), productSearchVO);
                resultObjectVO = feignProductSearchService.search(requestJsonVO);
                if (resultObjectVO.isSuccess()) {
                    pageInfo = resultObjectVO.formatData(PageInfo.class);
                }
            }

            List<ProductSearchResultVO> productResult = pageInfo.formatDataList(ProductSearchResultVO.class);
            if(CollectionUtils.isNotEmpty(productResult))
            {
                for(ProductSearchResultVO productSearchResultVO:productResult)
                {
                    productSearchResultVO.setHttpProductPreviewPath(imageUploadService.getImageHttpPrefix()+productSearchResultVO.getProductPreviewPath());
                }
            }


            httpServletRequest.setAttribute("productResult",productResult);
            httpServletRequest.setAttribute("page",pageInfo.getPage());
            httpServletRequest.setAttribute("total",pageInfo.getTotal());
            httpServletRequest.setAttribute("pageTotal",pageInfo.getPageTotal());

            //查询搜索属性列表
            if(StringUtils.isNotEmpty(productSearchVO.getSid())) {
                ShopCategoryVO queryShopCategory=new ShopCategoryVO();
                queryShopCategory.setShopId(Long.parseLong(productSearchVO.getSid()));
                resultObjectVO = feignShopCategoryService.queryListByShopId(RequestJsonVOGenerator.generator(toucan.getAppCode(),queryShopCategory));
                if(resultObjectVO.isSuccess())
                {
                    List<ShopCategoryVO> shopCategoryVOS = resultObjectVO.formatDataList(ShopCategoryVO.class);
                    List<AttributeKeyVO> attributes = new LinkedList<>();
                    AttributeKeyVO attributeKeyVO = new AttributeKeyVO();
                    attributeKeyVO.setAttributeName("商品分类");
                    attributeKeyVO.setValues(new LinkedList<>());
                    if(CollectionUtils.isNotEmpty(shopCategoryVOS))
                    {
                        for(ShopCategoryVO shopCategoryVO:shopCategoryVOS)
                        {
                            AttributeValueVO attributeValueVO = new AttributeValueVO();
                            attributeValueVO.setAttributeValue(shopCategoryVO.getName());
                            attributeKeyVO.getValues().add(attributeValueVO);
                        }
                    }
                    attributes.add(attributeKeyVO);
                    httpServletRequest.setAttribute("searchAttributes",attributes);
                }
            }


            //查询品牌列表(没有查询到指定品牌),就查询出所有可查询的品牌
            if(CollectionUtils.isEmpty(hitBrands))
            {
                BrandVO queryBrandVO = new BrandVO();
                if(StringUtils.isNotEmpty(productSearchVO.getCid()))
                {
                    queryBrandVO.setCategoryId(productSearchVO.getCid());
                }else {
                    if(CollectionUtils.isNotEmpty(productResult)) {
                        queryBrandVO.setCategoryId(String.valueOf(productResult.get(0).getCategoryId()));
                    }
                }
                requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),queryBrandVO);
                resultObjectVO = feignBrandService.queryListByCategoryId(requestJsonVO);
                if(resultObjectVO.isSuccess())
                {
                    List<BrandVO> brandVOS= resultObjectVO .formatDataList(BrandVO.class);
                    if(!CollectionUtils.isEmpty(brandVOS)){
                        for(BrandVO brandVO:brandVOS) {
                            if (StringUtils.isNotEmpty(brandVO.getChineseName()) && StringUtils.isNotEmpty(brandVO.getEnglishName())) {
                                brandVO.setName(brandVO.getChineseName() + "/" + brandVO.getEnglishName());
                            } else {
                                if (StringUtils.isNotEmpty(brandVO.getChineseName())) {
                                    brandVO.setName(brandVO.getChineseName());
                                } else {
                                    brandVO.setName(brandVO.getEnglishName());
                                }
                            }
                        }
                        httpServletRequest.setAttribute("searchBrands",brandVOS);
                    }
                }
            }

            this.setShopInfo(productSearchVO,httpServletRequest);

        }catch(Exception e)
        {
            logger.error(e.getMessage(),e);
        }
        return "search/shop_product_list";
    }

    private void setShopInfo(ProductSearchVO productSearchVO,HttpServletRequest httpServletRequest) throws Exception{

        if(StringUtils.isNotEmpty(productSearchVO.getSid())) {
            SellerShop entity = new SellerShop();
            entity.setId(Long.parseLong(productSearchVO.getSid()));
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(), entity);
            ResultObjectVO resultObjectVO = feignSellerShopService.findById(SignUtil.sign(requestJsonVO), requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                List<SellerShopVO> sellerShops = resultObjectVO.formatDataList(SellerShopVO.class);
                if(CollectionUtils.isNotEmpty(sellerShops))
                {
                    SellerShopVO sellerShopVO = sellerShops.get(0);
                    if(StringUtils.isNotEmpty(sellerShopVO.getLogo()))
                    {
                        sellerShopVO.setHttpLogo(imageUploadService.getImageHttpPrefix()+sellerShopVO.getLogo());
                    }
                    httpServletRequest.setAttribute("shop",sellerShopVO);
                }
            }
        }
    }

    /**
     * 商品搜索 支持两种方式
     *  /p/search POST
     *  /g/search GET
     * @param productSearchVO
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(value = "/p/search",method = RequestMethod.POST)
    public String searchByPost(ProductSearchVO productSearchVO, HttpServletRequest httpServletRequest){
        if(productSearchVO==null)
        {
            productSearchVO = new ProductSearchVO();
        }
        if(StringUtils.isEmpty(productSearchVO.getCid())&&StringUtils.isEmpty(productSearchVO.getScid())) {
            if (StringUtils.isEmpty(productSearchVO.getKeyword())) {
                productSearchVO.setKeyword("手机"); //默认关键字
            }else if(productSearchVO.getKeyword().length() > 50)
            {
                productSearchVO.setKeyword(productSearchVO.getKeyword().substring(0,50)); //截取字符
            }
        }
        return this.doSearch(productSearchVO,httpServletRequest);
    }

    /**
     * 商品搜索 支持两种方式
     *  /p/search POST
     *  /g/search GET
     * @param productSearchVO
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(value = "/g/search",method = RequestMethod.GET)
    public String searchByGet(ProductSearchVO productSearchVO, HttpServletRequest httpServletRequest){
        if(productSearchVO==null)
        {
            productSearchVO = new ProductSearchVO();
        }
        if(StringUtils.isEmpty(productSearchVO.getCid())&&StringUtils.isEmpty(productSearchVO.getScid())) {
            if (StringUtils.isEmpty(productSearchVO.getKeyword())) {
                productSearchVO.setKeyword("手机"); //默认关键字
            }else if(productSearchVO.getKeyword().length() > 50)
            {
                productSearchVO.setKeyword(productSearchVO.getKeyword().substring(0,50)); //截取字符
            }
        }
        return this.doSearch(productSearchVO,httpServletRequest);
    }



}
