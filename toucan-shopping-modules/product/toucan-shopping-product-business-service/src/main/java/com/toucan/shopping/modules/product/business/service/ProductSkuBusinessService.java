package com.toucan.shopping.modules.product.business.service;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.toucan.shopping.modules.common.annotation.RequestCheck;
import com.toucan.shopping.modules.common.exception.BusinessValidationException;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.common.util.Check;
import com.toucan.shopping.modules.common.vo.*;
import com.toucan.shopping.modules.product.constant.ProductConstant;
import com.toucan.shopping.modules.product.entity.*;
import com.toucan.shopping.modules.product.page.ProductSkuPageInfo;
import com.toucan.shopping.modules.product.redis.ShopProductRedisLockKey;
import com.toucan.shopping.modules.product.service.*;
import com.toucan.shopping.modules.product.vo.*;
import com.toucan.shopping.modules.skylark.lock.service.SkylarkLock;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductSkuBusinessService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ProductSkuService productSkuService;

    @Autowired
    private SkylarkLock skylarkLock;

    @Autowired
    private ProductSkuRedisService productSkuRedisService;

    @Autowired
    private ShopProductService shopProductService;

    @Autowired
    private ShopProductImgService shopProductImgService;

    @Autowired
    private ShopProductDescriptionService shopProductDescriptionService;

    @Autowired
    private ShopProductDescriptionImgService shopProductDescriptionImgService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private IdGenerator idGenerator;

    private ProductSkuVO queryProductSkuByCacheOrDB(Long skuId, int status) throws InvocationTargetException, IllegalAccessException {
        ProductSkuVO shopProductSkuVO = productSkuRedisService.queryProductSkuById(String.valueOf(skuId));
        if (shopProductSkuVO == null) { //查询数据库然后同步缓存
            shopProductSkuVO = productSkuService.queryVOByIdAndStatus(skuId, status); //查询数据库

            if (shopProductSkuVO != null) { //如果数据库中这条记录没被删除,就刷新到缓存
                ShopProductVO shopProductVO = shopProductService.findByIdAndStatus(shopProductSkuVO.getShopProductId(), status);
                if (shopProductVO != null) {
                    shopProductSkuVO.setProductAttributes(shopProductVO.getAttributes());
                    shopProductSkuVO.setPreviewPhotoPaths(new LinkedList<>());
                    shopProductSkuVO.setFreightTemplateId(shopProductVO.getFreightTemplateId());
                    shopProductSkuVO.setBuckleInventoryMethod(shopProductVO.getBuckleInventoryMethod()); //库存扣减方式

                    //查询商品图片
                    ShopProductImgVO shopProductImgVO = new ShopProductImgVO();
                    shopProductImgVO.setShopProductId(shopProductVO.getId());
                    List<ShopProductImg> shopProductImgs = shopProductImgService.queryListOrderByImgSortAsc(shopProductImgVO);
                    if (org.apache.commons.collections.CollectionUtils.isNotEmpty(shopProductImgs)) {
                        for (ShopProductImg shopProductImg : shopProductImgs) {
                            //如果是商品主图
                            if (shopProductImg.getImgType().intValue() == 1) {
                                shopProductSkuVO.setMainPhotoFilePath(shopProductImg.getFilePath());
                            } else if (shopProductImg.getImgType().intValue() == 2) {
                                shopProductSkuVO.getPreviewPhotoPaths().add(shopProductImg.getFilePath());
                            }
                        }
                    }

                    List<ProductSkuVO> productSkuVOS = productSkuService.queryVOListByShopProductIdAndStatus(shopProductVO.getId(), status);
                    //查询商品SKU列表
                    shopProductSkuVO.setProductSkuVOList(productSkuVOS);

                    //查询商品介绍
                    ShopProductDescription shopProductDescription = shopProductDescriptionService.queryByShopProductId(shopProductVO.getId());
                    if (shopProductDescription != null) {
                        ShopProductDescriptionVO shopProductDescriptionVO = new ShopProductDescriptionVO();
                        BeanUtils.copyProperties(shopProductDescriptionVO, shopProductDescription);

                        List<ShopProductDescriptionImgVO> shopProductDescriptionImgVOS = new LinkedList<>();
                        //店铺商品介绍预览图
                        shopProductDescriptionImgVOS.addAll(shopProductDescriptionImgService.queryVOListByProductIdAndDescriptionIdAndTypeOrderBySortDesc(shopProductVO.getId(), shopProductDescription.getId(), 1));
                        //SKU介绍预览图
                        if (!CollectionUtils.isEmpty(productSkuVOS)) {
                            shopProductDescriptionImgVOS.addAll(shopProductDescriptionImgService.queryVOListBySkuIdAndDescriptionIdOrderBySortDesc(skuId, shopProductDescription.getId()));
                        }
                        shopProductDescriptionVO.setProductDescriptionImgs(shopProductDescriptionImgVOS);
                        shopProductSkuVO.setShopProductDescriptionVO(shopProductDescriptionVO);

                        //商品介绍中的属性列表
                        List<ProductSkuAttribute> attributes = new ArrayList<>();

                        ProductSkuAttribute productNameAttribute = new ProductSkuAttribute("", new ArrayList<>());
                        productNameAttribute.setKey("商品名称");
                        productNameAttribute.getValues().add(shopProductVO.getName());
                        attributes.add(productNameAttribute);

                        if (shopProductSkuVO.getSuttle() != null) {
                            ProductSkuAttribute suttleAttribute = new ProductSkuAttribute("", new ArrayList<>());
                            suttleAttribute.setKey("商品净重");
                            suttleAttribute.getValues().add(String.valueOf(shopProductSkuVO.getSuttle()) + "kg");
                            attributes.add(suttleAttribute);
                        }

                        Brand brand = brandService.findByIdIngoreDeleteStatus(shopProductVO.getBrandId());
                        if (brand != null) {
                            ProductSkuAttribute brandNameAttribute = new ProductSkuAttribute("", new ArrayList<>());
                            brandNameAttribute.setKey("品牌");

                            if (StringUtils.isNotEmpty(brand.getChineseName()) && StringUtils.isNotEmpty(brand.getEnglishName())) {
                                brandNameAttribute.getValues().add(brand.getChineseName() + "/" + brand.getEnglishName());
                            } else {
                                if (StringUtils.isNotEmpty(brand.getChineseName())) {
                                    brandNameAttribute.getValues().add(brand.getChineseName());
                                }
                                if (StringUtils.isNotEmpty(brand.getEnglishName())) {
                                    brandNameAttribute.getValues().add(brand.getEnglishName());
                                }
                            }

                            shopProductDescriptionVO.setBrandNameAttribute(brandNameAttribute);

                            ProductSkuAttribute brandSeminaryAttribute = new ProductSkuAttribute("", new ArrayList<>());
                            brandSeminaryAttribute.setKey("商品产地");
                            if (StringUtils.isNotEmpty(brand.getSeminary())) {
                                brandSeminaryAttribute.getValues().add(brand.getSeminary());
                            } else {
                                brandSeminaryAttribute.getValues().add("");
                            }

                            attributes.add(brandSeminaryAttribute);
                        }

                        shopProductDescriptionVO.setAttributes(attributes);
                    }

                    //刷新到缓存
                    productSkuRedisService.addToCache(shopProductSkuVO);
                }
            }
        }
        return shopProductSkuVO;
    }

    @RequestCheck
    public ResultListVO queryShelvesList(RequestJsonVO requestJsonVO) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("status", 1);
        params.put("deleteStatus", 0);
        params.put("appCode", requestJsonVO.getAppCode());

        ResultListVO<ProductSku> resultListVO = new ResultListVO<>();
        return resultListVO;
    }

    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryByIdForFront(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            ProductSku productSku = JSONObject.parseObject(requestJsonVO.getEntityJson(), ProductSku.class);

            Check.notNull(productSku.getId(), ResultVO.FAILD, "商品ID不能为空!");
            ProductSkuVO shopProductSkuVO = queryProductSkuByCacheOrDB(productSku.getId(), 1);
            shopProductSkuVO.setSkuStatusList(productSkuService.queryShelvesBuyStatus(shopProductSkuVO.getShopProductId()));
            resultObjectVO.setData(shopProductSkuVO);
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("查询失败!");
        }

        return resultObjectVO;
    }

    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryByIdForFrontPreview(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            ProductSku productSku = JSONObject.parseObject(requestJsonVO.getEntityJson(), ProductSku.class);

            Check.notNull(productSku.getId(), ResultVO.FAILD, "商品ID不能为空!");
            ProductSkuVO shopProductApproveSkuVO = queryProductSkuByCacheOrDB(productSku.getId(), -1);
            resultObjectVO.setData(shopProductApproveSkuVO);
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("查询失败!");
        }

        return resultObjectVO;
    }

    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryOneByShopProductIdForFront(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            ShopProductVO shopProductVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), ShopProductVO.class);
            Check.notNull(shopProductVO.getId(), ResultVO.FAILD, "商品ID不能为空!");

            ProductSku productSku = productSkuService.queryFirstOneByShopProductIdAndAttrPath(shopProductVO.getId(), shopProductVO.getAttrPath());
            //如果当前属性路径没有商品,在往上查询,直到返回一个商品
            if (productSku == null) {
                if (StringUtils.isNotEmpty(shopProductVO.getAttrPath())) {
                    String[] attrPaths = shopProductVO.getAttrPath().split("_");
                    int loopCount = 1; //减去上面的那次查询
                    int maxQueryCount = attrPaths.length - 1;
                    String newAttrPath;
                    while (maxQueryCount > 0) {
                        newAttrPath = "";
                        //开始生成新的属性值路径
                        for (int i = 0; i < attrPaths.length - loopCount; i++) {
                            newAttrPath += attrPaths[i];
                            if (i + 1 < attrPaths.length - loopCount) {
                                newAttrPath += "_";
                            }
                        }
                        loopCount++;
                        productSku = productSkuService.queryFirstOneByShopProductIdAndAttrPath(shopProductVO.getId(), newAttrPath);
                        if (productSku != null) {
                            break;
                        }
                        maxQueryCount--;
                    }
                }
            }
            if (productSku != null) {
                ProductSkuVO productSkuVO = queryProductSkuByCacheOrDB(productSku.getId(), 1);
                if (productSkuVO != null) {
                    productSkuVO.setSkuStatusList(productSkuService.queryShelvesBuyStatus(productSkuVO.getShopProductId()));
                    resultObjectVO.setData(productSkuVO);
                }
            }
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("查询失败!");
        }

        return resultObjectVO;
    }

    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryOneByShopProductIdForFrontPreview(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            ShopProductVO shopProductVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), ShopProductVO.class);
            Check.notNull(shopProductVO.getId(), ResultVO.FAILD, "商品ID不能为空!");

            ProductSku productSku = productSkuService.queryFirstOneByShopProductIdAndStatus(shopProductVO.getId(), -1);
            if (productSku != null) {
                ProductSkuVO shopProductSkuVO = queryProductSkuByCacheOrDB(productSku.getId(), -1);
                resultObjectVO.setData(shopProductSkuVO);
            }
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("查询失败!");
        }

        return resultObjectVO;
    }

    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryListPage(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            ProductSkuPageInfo queryPageInfo = JSONObject.parseObject(requestJsonVO.getEntityJson(), ProductSkuPageInfo.class);
            PageInfo<ProductSkuVO> pageInfo = productSkuService.queryListPage(queryPageInfo);
            if (!CollectionUtils.isEmpty(pageInfo.getList())) {
                if (queryPageInfo.getShopProductId() != null) {
                    ShopProductDescription shopProductDescription = shopProductDescriptionService.queryByShopProductId(queryPageInfo.getShopProductId());
                    if (shopProductDescription != null) {
                        List<ShopProductDescriptionImgVO> shopProductDescriptionImgVOS = shopProductDescriptionImgService.queryVOListByProductIdAndDescriptionIdOrderBySortDesc(queryPageInfo.getShopProductId(), shopProductDescription.getId());
                        if (!CollectionUtils.isEmpty(shopProductDescriptionImgVOS)) {
                            for (ProductSkuVO productSkuVO : pageInfo.getList()) {
                                for (ShopProductDescriptionImgVO shopProductDescriptionImgVO : shopProductDescriptionImgVOS) {
                                    if (shopProductDescriptionImgVO.getType() == 2
                                            && shopProductDescriptionImgVO.getProductSkuId() != null
                                            && productSkuVO.getId().longValue() == shopProductDescriptionImgVO.getProductSkuId().longValue()) {
                                        productSkuVO.setDescriptionImgFilePath(shopProductDescriptionImgVO.getFilePath());
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            resultObjectVO.setData(pageInfo);
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("查询失败!");
        }

        return resultObjectVO;
    }

    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryById(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            ProductSku productSku = JSONObject.parseObject(requestJsonVO.getEntityJson(), ProductSku.class);
            productSku = productSkuService.queryById(productSku.getId());
            ProductSkuVO productSkuVO = new ProductSkuVO();
            BeanUtils.copyProperties(productSkuVO, productSku);
            //查询介绍图
            ShopProductDescription shopProductDescription = shopProductDescriptionService.queryByShopProductId(productSkuVO.getShopProductId());
            if (shopProductDescription != null) {
                List<ShopProductDescriptionImgVO> shopProductDescriptionImgVOS = shopProductDescriptionImgService.queryVOListByProductIdAndDescriptionIdOrderBySortDesc(productSkuVO.getShopProductId(), shopProductDescription.getId());
                if (!CollectionUtils.isEmpty(shopProductDescriptionImgVOS)) {
                    for (ShopProductDescriptionImgVO shopProductDescriptionImgVO : shopProductDescriptionImgVOS) {
                        if (shopProductDescriptionImgVO.getType() == 2
                                && shopProductDescriptionImgVO.getProductSkuId() != null
                                && productSkuVO.getId().longValue() == shopProductDescriptionImgVO.getProductSkuId().longValue()) {
                            productSkuVO.setDescriptionImgFilePath(shopProductDescriptionImgVO.getFilePath());
                            break;
                        }
                    }
                }
            }
            resultObjectVO.setData(productSkuVO);
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("查询失败!");
        }

        return resultObjectVO;
    }

    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryList(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            ProductSkuVO productSkuVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), ProductSkuVO.class);
            //防止查询库中全部数据 在这里做个必填校验
            Check.notNull(productSkuVO.getShopProductId(), ResultVO.FAILD, "店铺商品ID不能为空!");
            List<ProductSkuVO> productSkuVOS = productSkuService.queryList(productSkuVO);
            if (!CollectionUtils.isEmpty(productSkuVOS)) {
                if (productSkuVOS.get(0).getShopProductId() != null) {
                    ShopProductDescription shopProductDescription = shopProductDescriptionService.queryByShopProductId(productSkuVOS.get(0).getShopProductId());
                    if (shopProductDescription != null) {
                        List<ShopProductDescriptionImgVO> shopProductDescriptionImgVOS = shopProductDescriptionImgService.queryVOListByProductIdAndDescriptionIdOrderBySortDesc(productSkuVOS.get(0).getShopProductId(), shopProductDescription.getId());
                        if (!CollectionUtils.isEmpty(shopProductDescriptionImgVOS)) {
                            for (ProductSkuVO psv : productSkuVOS) {
                                for (ShopProductDescriptionImgVO shopProductDescriptionImgVO : shopProductDescriptionImgVOS) {
                                    if (shopProductDescriptionImgVO.getType() == 2
                                            && shopProductDescriptionImgVO.getProductSkuId() != null
                                            && psv.getId().longValue() == shopProductDescriptionImgVO.getProductSkuId().longValue()) {
                                        psv.setDescriptionImgFilePath(shopProductDescriptionImgVO.getFilePath());
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            resultObjectVO.setData(productSkuVOS);
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("查询失败!");
        }

        return resultObjectVO;
    }

    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryByIdList(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            List<ProductSkuVO> productSkus = JSONArray.parseArray(requestJsonVO.getEntityJson(), ProductSkuVO.class);
            if (!CollectionUtils.isEmpty(productSkus)) {
                List<ProductSku> productSkuList = new ArrayList<ProductSku>();
                for (ProductSkuVO productSku : productSkus) {
                    if (productSku.getId() != null) {
                        ProductSkuVO productSkuEntity = queryProductSkuByCacheOrDB(productSku.getId(), -1);
                        if (productSkuEntity != null) {
                            productSkuList.add(productSkuEntity);
                        }
                    } else {
                        logger.warn("exists product sku id is null {}", requestJsonVO.getEntityJson());
                    }
                }
                resultObjectVO.setData(productSkuList);
            }
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("查询失败!");
        }

        return resultObjectVO;
    }

    @RequestCheck(requireEntity = true)
    public ResultObjectVO inventoryReduction(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        List<InventoryReductionVO> inventoryReductions = requestJsonVO.formatEntityList(InventoryReductionVO.class);

        logger.info("扣库存: param: {} ", requestJsonVO.getEntityJson());
        if (CollectionUtils.isEmpty(inventoryReductions)) {
            logger.info("没有找到扣除库存的商品: param:{} ", requestJsonVO.getEntityJson());
            return ResultObjectVO.fail(ResultVO.FAILD, "没有找到商品!");
        }

        for (InventoryReductionVO inventoryReductionVO : inventoryReductions) {
            if (StringUtils.isEmpty(inventoryReductionVO.getUserId())) {
                logger.info("没有找到用户: param: {} ", JSONObject.toJSONString(inventoryReductionVO));
                return ResultObjectVO.fail(ResultVO.FAILD, "没有找到用户");
            }
            if (inventoryReductionVO.getProductSkuId() == null) {
                logger.info("没有找到商品ID: param: {} ", JSONObject.toJSONString(inventoryReductionVO));
                return ResultObjectVO.fail(ResultVO.FAILD, "没有找到商品ID");
            }
            if (inventoryReductionVO.getStockNum() == null || inventoryReductionVO.getStockNum().intValue() <= 0) {
                logger.info("扣库存数量不能为0: param: {} ", JSONObject.toJSONString(inventoryReductionVO));
                return ResultObjectVO.fail(ResultVO.FAILD, "扣库存数量不能为0");
            }
        }

        List<InventoryReductionVO> restoreInventoryReductions = new LinkedList<>();
        try {
            //校验库存数量是否足够
            List<Long> productSkuIds = inventoryReductions.stream().map(InventoryReductionVO::getProductSkuId).distinct().collect(Collectors.toList());
            List<ProductSku> productSkus = productSkuService.queryShelvesListByIdList(productSkuIds);
            boolean isFind = false;
            for (InventoryReductionVO inventoryReductionVO : inventoryReductions) {
                isFind = false;
                for (ProductSku productSku : productSkus) {
                    if (productSku.getId().longValue() == inventoryReductionVO.getProductSkuId().longValue()) {
                        if (productSku.getStockNum().intValue() < inventoryReductionVO.getStockNum().intValue()) {
                            return ResultObjectVO.fail(ResultVO.FAILD, "没有库存了", inventoryReductionVO);
                        }
                        isFind = true;
                        break;
                    }
                }
                if (!isFind) {
                    return ResultObjectVO.fail(ResultVO.FAILD, "商品已下架", inventoryReductionVO);
                }
            }

            for (InventoryReductionVO inventoryReductionVO : inventoryReductions) {
                logger.info("扣库存: skuId:{} stockNum:{} userId:{} ", inventoryReductionVO.getProductSkuId(), inventoryReductionVO.getStockNum(), inventoryReductionVO.getUserId());
                int row = productSkuService.inventoryReduction(inventoryReductionVO.getProductSkuId(), inventoryReductionVO.getStockNum());
                if (row <= 0) {
                    logger.warn("没有库存了 skuId:{} stockNum:{} userId:{} ", inventoryReductionVO.getProductSkuId(), inventoryReductionVO.getStockNum(), inventoryReductionVO.getUserId());
                    throw new IllegalArgumentException("没有库存了");
                } else { //清空商品缓存
                    restoreInventoryReductions.add(inventoryReductionVO);
                    productSkuRedisService.deleteCache(String.valueOf(inventoryReductionVO.getProductSkuId()));
                }
            }
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            //还原库存
            if (!CollectionUtils.isEmpty(restoreInventoryReductions)) {
                for (InventoryReductionVO inventoryReductionVO : restoreInventoryReductions) {
                    int row = productSkuService.restoreStock(inventoryReductionVO.getProductSkuId(), inventoryReductionVO.getStockNum());
                    if (row <= 0) {
                        logger.warn("还原扣库存失败 skuId:{} stockNum:{} userId:{} ", inventoryReductionVO.getProductSkuId(), inventoryReductionVO.getStockNum(), inventoryReductionVO.getUserId());
                    }
                }
            }
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("扣库存失败!");
        }
        return resultObjectVO;
    }

    @RequestCheck(requireEntity = true)
    public ResultObjectVO updateStock(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        String productSkuId = "-1";
        try {
            ProductSkuVO productSkuVO = requestJsonVO.formatEntity(ProductSkuVO.class);
            Check.notNull(productSkuVO.getId(), ResultVO.FAILD, "商品ID不能为空!");
            Check.notNull(productSkuVO.getStockNum(), ResultVO.FAILD, "库存数量不能为空!");
            productSkuId = String.valueOf(productSkuVO.getId());
            skylarkLock.lock(ShopProductRedisLockKey.getResaveProductLockKey(productSkuId), productSkuId);
            productSkuRedisService.deleteCache(String.valueOf(productSkuVO.getId()));
            productSkuService.updateStock(productSkuVO.getId(), productSkuVO.getStockNum());

            //延时双删
            Thread.sleep(ProductConstant.DELETE_REDIS_SLEEP);
            productSkuRedisService.deleteCache(String.valueOf(productSkuVO.getId()));
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("修改库存失败!");
        } finally {
            skylarkLock.unLock(ShopProductRedisLockKey.getResaveProductLockKey(productSkuId), productSkuId);
        }
        return resultObjectVO;
    }

    @RequestCheck(requireEntity = true)
    public ResultObjectVO updatePrice(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        String productSkuId = "-1";
        try {
            ProductSkuVO productSkuVO = requestJsonVO.formatEntity(ProductSkuVO.class);
            Check.notNull(productSkuVO.getId(), ResultVO.FAILD, "商品ID不能为空!");
            Check.notNull(productSkuVO.getPrice(), ResultVO.FAILD, "商品单价不能为空!");
            productSkuId = String.valueOf(productSkuVO.getId());
            skylarkLock.lock(ShopProductRedisLockKey.getResaveProductLockKey(productSkuId), productSkuId);
            productSkuRedisService.deleteCache(String.valueOf(productSkuVO.getId()));
            productSkuService.updatePrice(productSkuVO.getId(), productSkuVO.getPrice());

            //延时双删
            Thread.sleep(ProductConstant.DELETE_REDIS_SLEEP);
            productSkuRedisService.deleteCache(String.valueOf(productSkuVO.getId()));
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("修改库存失败!");
        } finally {
            skylarkLock.unLock(ShopProductRedisLockKey.getResaveProductLockKey(productSkuId), productSkuId);
        }
        return resultObjectVO;
    }

    @RequestCheck(requireEntity = true)
    public ResultObjectVO restoreStock(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        List<InventoryReductionVO> inventoryReductions = requestJsonVO.formatEntityList(InventoryReductionVO.class);

        logger.info("扣库存: param: {} ", requestJsonVO.getEntityJson());
        if (CollectionUtils.isEmpty(inventoryReductions)) {
            logger.info("没有找到扣除库存的商品: param:{} ", requestJsonVO.getEntityJson());
            return ResultObjectVO.fail(ResultVO.FAILD, "没有找到商品!");
        }

        for (InventoryReductionVO inventoryReductionVO : inventoryReductions) {
            if (inventoryReductionVO.getProductSkuId() == null) {
                logger.info("没有找到商品ID: param: {} ", JSONObject.toJSONString(inventoryReductionVO));
                return ResultObjectVO.fail(ResultVO.FAILD, "没有找到商品ID");
            }
            if (inventoryReductionVO.getStockNum() == null || inventoryReductionVO.getStockNum().intValue() <= 0) {
                logger.info("扣库存数量不能为0: param: {} ", JSONObject.toJSONString(inventoryReductionVO));
                return ResultObjectVO.fail(ResultVO.FAILD, "扣库存数量不能为0");
            }
        }

        List<InventoryReductionVO> restoreInventoryReductionFailds = new LinkedList<>();
        try {
            //还原库存
            if (!CollectionUtils.isEmpty(inventoryReductions)) {
                for (InventoryReductionVO inventoryReductionVO : inventoryReductions) {
                    int row = productSkuService.restoreStock(inventoryReductionVO.getProductSkuId(), inventoryReductionVO.getStockNum());
                    if (row <= 0) {
                        restoreInventoryReductionFailds.add(inventoryReductionVO);
                        logger.warn("还原扣库存失败 skuId:{} stockNum:{}  ", inventoryReductionVO.getProductSkuId(), inventoryReductionVO.getStockNum());
                        throw new IllegalArgumentException("还原扣库存失败");
                    } else {
                        productSkuRedisService.deleteCache(String.valueOf(inventoryReductionVO.getProductSkuId()));
                    }
                }
            }
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("还原扣库存失败!");
        }
        resultObjectVO.setData(restoreInventoryReductionFailds);
        return resultObjectVO;
    }

    @RequestCheck(requireEntity = true)
    public ResultObjectVO shelves(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        String productSkuId = "";
        try {
            logger.info("商品上架/下架 {} ", requestJsonVO.getEntityJson());
            ProductSkuVO productSkuVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), ProductSkuVO.class);
            Check.notNull(productSkuVO.getId(), ResultVO.FAILD, "商品ID不能为空!");
            Check.notNull(productSkuVO.getShopId(), ResultVO.FAILD, "店铺ID不能为空!");
            productSkuId = String.valueOf(productSkuVO.getId());
            skylarkLock.lock(ShopProductRedisLockKey.getResaveProductLockKey(productSkuId), productSkuId);

            ProductSku productSku = productSkuService.queryById(productSkuVO.getId());
            if (productSku == null) {
                return ResultObjectVO.fail(ResultVO.FAILD, "该商品不存在!");
            }

            //如果当前是上架状态
            if (productSku.getStatus() != null
                    && productSku.getStatus().intValue() == ProductConstant.SHELVES_UP.intValue()) {
                productSkuService.updateStatusById(productSku.getId(), productSku.getShopId(), ProductConstant.SHELVES_DOWN); //下架
            } else if (productSku.getStatus() != null
                    && productSku.getStatus().intValue() == ProductConstant.SHELVES_DOWN.intValue()) //如果当前是下架状态
            {
                productSkuService.updateStatusById(productSku.getId(), productSku.getShopId(), ProductConstant.SHELVES_UP); //上架
            }

            //查询店铺商品,判断下面所有SKU如果都下架的话,这个店铺商品直接下架
            Long shelvesCount = productSkuService.queryShelvesCountByShopProductId(productSku.getShopProductId());
            if (shelvesCount == 0) {
                shopProductService.updateStatus(productSku.getShopProductId(), productSku.getShopId(), ProductConstant.SHELVES_DOWN); //下架
            } else {
                shopProductService.updateStatus(productSku.getShopProductId(), productSku.getShopId(), ProductConstant.SHELVES_UP); //上架
            }

            //清空该商品下所有SKU缓存
            List<ProductSkuVO> productSkuVOS = productSkuService.queryProductSkuListByShopProductId(productSku.getShopProductId());
            if (!CollectionUtils.isEmpty(productSkuVOS)) {
                for (ProductSkuVO psv : productSkuVOS) {
                    if (psv.getId() != null) {
                        productSkuRedisService.deleteCache(String.valueOf(psv.getId()));
                    }
                }
            }
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("修改失败");
        } finally {
            skylarkLock.unLock(ShopProductRedisLockKey.getResaveProductLockKey(productSkuId), productSkuId);
        }
        return resultObjectVO;
    }

    @RequestCheck(requireEntity = true)
    public ResultObjectVO updatePreviewPhoto(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            ProductSkuVO productSkuVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), ProductSkuVO.class);
            ProductSku productSku = productSkuService.queryById(productSkuVO.getId());
            productSku.setProductPreviewPath(productSkuVO.getProductPreviewPath());
            int ret = productSkuService.update(productSku);
            if (ret != 1) {
                return ResultObjectVO.fail(ResultVO.FAILD, "修改失败");
            }
            productSkuRedisService.deleteCache(String.valueOf(productSkuVO.getId()));
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("修改失败");
        }
        return resultObjectVO;
    }

    @RequestCheck(requireEntity = true)
    public ResultObjectVO updateDescriptionPhoto(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            ProductSkuVO productSkuVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), ProductSkuVO.class);
            ProductSku productSku = productSkuService.queryById(productSkuVO.getId());
            ShopProductDescription shopProductDescription = shopProductDescriptionService.queryByShopProductId(productSku.getShopProductId());
            List<ShopProductDescriptionImgVO> shopProductDescriptionImgs = shopProductDescriptionImgService.queryVOListBySkuIdAndDescriptionIdOrderBySortDesc(productSku.getId(), shopProductDescription.getId());

            ShopProductDescriptionImgVO productSkuDescriptionImgVO = new ShopProductDescriptionImgVO();
            if (!CollectionUtils.isEmpty(shopProductDescriptionImgs)) {
                ShopProductDescriptionImgVO productDescriptionImgSkuVO = shopProductDescriptionImgs.get(0);
                BeanUtils.copyProperties(productSkuDescriptionImgVO, productDescriptionImgSkuVO);
                shopProductDescriptionImgService.deleteById(productDescriptionImgSkuVO.getId());
                productSkuDescriptionImgVO.setCreateDate(new Date());
            } else {
                productSkuDescriptionImgVO.setCreateDate(new Date());
                productSkuDescriptionImgVO.setShopProductId(productSku.getShopProductId()); //店铺商品ID
                productSkuDescriptionImgVO.setShopProductDescriptionId(shopProductDescription.getId());  //商品介绍主表ID
                productSkuDescriptionImgVO.setCreateDate(new Date());
                productSkuDescriptionImgVO.setType((short) 2);
                productSkuDescriptionImgVO.setProductSkuId(productSku.getId());
            }
            productSkuDescriptionImgVO.setId(idGenerator.id());
            productSkuDescriptionImgVO.setFilePath(productSkuVO.getDescriptionImgFilePath());
            shopProductDescriptionImgService.save(productSkuDescriptionImgVO);
            productSkuRedisService.deleteCache(String.valueOf(productSkuVO.getId()));
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("修改失败");
        }
        return resultObjectVO;
    }

    @RequestCheck(requireEntity = true)
    public ResultObjectVO removeDescriptionPhoto(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            ProductSkuVO productSkuVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), ProductSkuVO.class);
            ProductSku productSku = productSkuService.queryById(productSkuVO.getId());
            ShopProductDescription shopProductDescription = shopProductDescriptionService.queryByShopProductId(productSku.getShopProductId());
            List<ShopProductDescriptionImgVO> shopProductDescriptionImgs = shopProductDescriptionImgService.queryVOListBySkuIdAndDescriptionIdOrderBySortDesc(productSku.getId(), shopProductDescription.getId());

            if (!CollectionUtils.isEmpty(shopProductDescriptionImgs)) {
                ShopProductDescriptionImgVO productDescriptionImgSkuVO = shopProductDescriptionImgs.get(0);
                shopProductDescriptionImgService.deleteById(productDescriptionImgSkuVO.getId());
            }
            productSkuRedisService.deleteCache(String.valueOf(productSkuVO.getId()));
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("删除失败");
        }
        return resultObjectVO;
    }

    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryListByShopProductIdList(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            ProductSkuVO productSkuVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), ProductSkuVO.class);
            Check.isTrue(productSkuVO.getShopProductId() != null || !CollectionUtils.isEmpty(productSkuVO.getShopProductIdList()), ResultVO.FAILD, "店铺商品ID不能为空!");
            List<ProductSkuVO> productSkuVOS = productSkuService.queryList(productSkuVO);
            resultObjectVO.setData(productSkuVOS);
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("查询失败!");
        }

        return resultObjectVO;
    }

    @RequestCheck(requireEntity = true)
    public ResultTypeObjectVO<Long> queryShelvesCountByShopId(RequestJsonVO requestJsonVO) {
        ResultTypeObjectVO resultObjectVO = new ResultTypeObjectVO();

        try {
            ProductSkuVO productSkuVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), ProductSkuVO.class);
            Check.notNull(productSkuVO.getShopId(), ResultVO.FAILD, "店铺ID不能为空!");
            Long count = productSkuService.queryCount(productSkuVO);
            resultObjectVO.setData(count == null ? 0L : count);
        }catch(BusinessValidationException e){
            return ResultTypeObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("查询失败!");
        }
        return resultObjectVO;
    }

}
