package com.toucan.shopping.modules.product.business.service;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSON;
import com.toucan.shopping.modules.common.annotation.RequestCheck;
import com.toucan.shopping.modules.common.exception.BusinessValidationException;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.common.util.Check;
import com.toucan.shopping.modules.common.util.LetterFirstUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.product.entity.Brand;
import com.toucan.shopping.modules.product.entity.BrandCategory;
import com.toucan.shopping.modules.product.page.BrandPageInfo;
import com.toucan.shopping.modules.product.service.BrandCategoryService;
import com.toucan.shopping.modules.product.service.BrandService;
import com.toucan.shopping.modules.product.vo.BrandVO;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

@Service
public class BrandBusinessService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private BrandService brandService;

    @Autowired
    private BrandCategoryService brandCategoryService;

    @Autowired
    private IdGenerator idGenerator;

    @RequestCheck(requireEntity = true)
    public ResultObjectVO save(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            BrandVO brandVo = JSONObject.parseObject(requestJsonVO.getEntityJson(), BrandVO.class);
            Check.isTrue(StringUtils.isNotEmpty(brandVo.getChineseName()) || StringUtils.isNotEmpty(brandVo.getEnglishName()), ResultVO.FAILD, "品牌名称不能为空");
            Check.notEmpty(brandVo.getCategoryIdCache(), ResultVO.FAILD, "品牌关联分类不能为空");

            String[] categotyIdArray = brandVo.getCategoryIdCache().split(",");
            if (categotyIdArray.length > 30) {
                return ResultObjectVO.fail(ResultVO.FAILD, "品牌最多只能关联30个分类");
            }

            BrandVO queryBrand = new BrandVO();
            queryBrand.setChineseName(brandVo.getChineseName());
            queryBrand.setDeleteStatus(0);

            List<Brand> brandList = brandService.queryList(queryBrand);
            if (!CollectionUtils.isEmpty(brandList)) {
                return ResultObjectVO.fail(ResultVO.FAILD, brandVo.getChineseName() + "名称已存在!");
            }

            if (StringUtils.isNotEmpty(brandVo.getEnglishName())) {
                queryBrand = new BrandVO();
                queryBrand.setEnglishName(brandVo.getEnglishName());
                queryBrand.setDeleteStatus(0);

                brandList = brandService.queryList(queryBrand);
                if (!CollectionUtils.isEmpty(brandList)) {
                    return ResultObjectVO.fail(ResultVO.FAILD, brandVo.getEnglishName() + "名称已存在!");
                }
            }

            Brand entity = new Brand();
            BeanUtils.copyProperties(entity, brandVo);
            entity.setId(idGenerator.id());
            entity.setCreateDate(new Date());
            entity.setDeleteStatus(0);
            int row = brandService.save(entity);
            if (row <= 0) {
                return ResultObjectVO.fail(ResultVO.FAILD, "请重试!");
            }

            for (String categoryId : categotyIdArray) {
                BrandCategory brandCategory = new BrandCategory();
                brandCategory.setId(idGenerator.id());
                brandCategory.setCategoryId(Long.parseLong(categoryId));
                brandCategory.setBrandId(entity.getId());
                brandCategory.setCreateDate(new Date());
                brandCategory.setDeleteStatus(0);
                brandCategoryService.save(brandCategory);
            }

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO = ResultObjectVO.fail(ResultVO.FAILD, "请重试!");
        }
        return resultObjectVO;
    }

    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryListByCategoryId(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            BrandVO queryBrand = JSONObject.parseObject(requestJsonVO.getEntityJson(), BrandVO.class);
            Check.notNull(queryBrand.getCategoryId(), ResultVO.FAILD, "分类ID不能为空!");

            List<Brand> brands = brandService.queryList(queryBrand);
            resultObjectVO.setData(brands);

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO = ResultObjectVO.fail(ResultVO.FAILD, "查询失败!");
        }
        return resultObjectVO;
    }

    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryListPage(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            BrandPageInfo queryPageInfo = JSONObject.parseObject(requestJsonVO.getEntityJson(), BrandPageInfo.class);
            PageInfo<BrandVO> pageInfo = brandService.queryListPage(queryPageInfo);
            if (CollectionUtils.isNotEmpty(pageInfo.getList())) {
                for (BrandVO brandVO : pageInfo.getList()) {
                    if (brandVO != null && StringUtils.isNotEmpty(brandVO.getCategoryIdCache())) {
                        String[] categoryIdArray = brandVO.getCategoryIdCache().split(",");
                        brandVO.setCategoryIdCacheArray(categoryIdArray);
                    }
                }
            }
            resultObjectVO.setData(pageInfo);
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO = ResultObjectVO.fail(ResultVO.FAILD, "查询失败!");
        }

        return resultObjectVO;
    }

    @RequestCheck(requireEntity = true)
    public ResultObjectVO update(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            BrandVO entity = JSONObject.parseObject(requestJsonVO.getEntityJson(), BrandVO.class);

            Check.notNull(entity.getId(), ResultVO.FAILD, "ID不能为空!");
            Check.isTrue(StringUtils.isNotEmpty(entity.getChineseName()) || StringUtils.isNotEmpty(entity.getEnglishName()), ResultVO.FAILD, "品牌名称不能为空");
            Check.notEmpty(entity.getCategoryIdCache(), ResultVO.FAILD, "品牌分类不能为空");

            String[] categotyIdArray = entity.getCategoryIdCache().split(",");
            if (categotyIdArray.length > 30) {
                return ResultObjectVO.fail(ResultVO.FAILD, "品牌最多只能关联30个分类");
            }

            BrandVO queryBrand = new BrandVO();
            List<Brand> brandList = null;
            if (StringUtils.isNotEmpty(entity.getChineseName())) {
                queryBrand.setChineseName(entity.getChineseName());
                queryBrand.setDeleteStatus(0);

                brandList = brandService.queryList(queryBrand);
                if (!CollectionUtils.isEmpty(brandList)) {
                    if (entity.getId().longValue() != brandList.get(0).getId().longValue()) {
                        return ResultObjectVO.fail(ResultVO.FAILD, entity.getChineseName() + "名称已存在!");
                    }
                }
            }

            if (StringUtils.isNotEmpty(entity.getEnglishName())) {
                queryBrand = new BrandVO();
                queryBrand.setEnglishName(entity.getEnglishName());
                queryBrand.setDeleteStatus(0);
                brandList = brandService.queryList(queryBrand);
                if (!CollectionUtils.isEmpty(brandList)) {
                    if (entity.getId().longValue() != brandList.get(0).getId().longValue()) {
                        return ResultObjectVO.fail(ResultVO.FAILD, entity.getEnglishName() + "名称已存在!");
                    }
                }
            }

            entity.setUpdateDate(new Date());
            int row = brandService.update(entity);
            if (row != 1) {
                return ResultObjectVO.fail(ResultVO.FAILD, "请重试!");
            }

            //更新关联
            brandCategoryService.deleteByBrandId(entity.getId());

            for (String categoryId : categotyIdArray) {
                BrandCategory brandCategory = new BrandCategory();
                brandCategory.setId(idGenerator.id());
                brandCategory.setCategoryId(Long.parseLong(categoryId));
                brandCategory.setBrandId(entity.getId());
                brandCategory.setCreateDate(new Date());
                brandCategory.setDeleteStatus(0);
                brandCategoryService.save(brandCategory);
            }

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO = ResultObjectVO.fail(ResultVO.FAILD, "请重试!");
        }
        return resultObjectVO;
    }

    @RequestCheck(requireEntity = true)
    public ResultObjectVO findById(RequestJsonVO requestVo) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            BrandVO entity = JSONObject.parseObject(requestVo.getEntityJson(), BrandVO.class);
            Check.notNull(entity.getId(), ResultVO.FAILD, "没有找到ID");

            //查询是否存
            BrandVO query = new BrandVO();
            query.setId(entity.getId());
            List<Brand> entityList = brandService.queryList(query);
            if (CollectionUtils.isEmpty(entityList)) {
                return ResultObjectVO.fail(ResultVO.FAILD, "对象不存在!");
            }
            List<BrandVO> brandVOS = new ArrayList<BrandVO>();
            for (Brand brand : entityList) {
                BrandVO brandVO = new BrandVO();
                BeanUtils.copyProperties(brandVO, brand);
                if (StringUtils.isNotEmpty(brandVO.getCategoryIdCache())) {
                    brandVO.setCategoryIdCacheArray(brandVO.getCategoryIdCache().split(","));
                }
                brandVOS.add(brandVO);
            }

            resultObjectVO.setData(brandVOS);

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);

            resultObjectVO = ResultObjectVO.fail(ResultVO.FAILD, "请稍后重试");
        }
        return resultObjectVO;
    }

    @RequestCheck(requireEntity = true)
    public ResultObjectVO findByIdList(RequestJsonVO requestVo) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            BrandVO query = JSONObject.parseObject(requestVo.getEntityJson(), BrandVO.class);
            Check.isTrue(query.getIdList() != null && query.getIdList().size() > 0, ResultVO.FAILD, "没有找到ID集合");

            List<Brand> brands = brandService.queryList(query);
            if (CollectionUtils.isEmpty(brands)) {
                return ResultObjectVO.fail(ResultVO.FAILD, "品牌列表为空");
            }

            resultObjectVO.setData(brands);

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);

            resultObjectVO = ResultObjectVO.fail(ResultVO.FAILD, "请稍后重试");
        }
        return resultObjectVO;
    }

    @RequestCheck(requireEntity = true)
    public ResultObjectVO findListByNameAndCategoryIdAndEnabled(RequestJsonVO requestVo) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            BrandVO query = JSONObject.parseObject(requestVo.getEntityJson(), BrandVO.class);
            Check.notEmpty(query.getName(), ResultVO.FAILD, "名称不能为空");

            List<Brand> brands = brandService.queryList(query);
            if (CollectionUtils.isEmpty(brands)) {
                return ResultObjectVO.fail(ResultVO.FAILD, "品牌列表为空");
            }

            resultObjectVO.setData(brands);

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);

            resultObjectVO = ResultObjectVO.fail(ResultVO.FAILD, "请稍后重试");
        }
        return resultObjectVO;
    }

    @RequestCheck(requireEntity = true)
    public ResultObjectVO deleteById(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            Brand brand = JSONObject.parseObject(requestJsonVO.getEntityJson(), Brand.class);

            Check.notNull(brand.getId(), ResultVO.FAILD, "ID不能为空!");

            BrandVO queryBrand = new BrandVO();
            queryBrand.setId(brand.getId());

            List<Brand> brandList = brandService.queryList(queryBrand);
            if (CollectionUtils.isEmpty(brandList)) {
                return ResultObjectVO.fail(ResultVO.FAILD, "不存在该品牌!");
            }

            brand = brandList.get(0);
            int row = brandService.deleteById(brand.getId());
            if (row <= 0) {
                return ResultObjectVO.fail(ResultVO.FAILD, "请重试!");
            }

            //删除品牌与类目关联
            brandCategoryService.deleteByBrandId(brand.getId());

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO = ResultObjectVO.fail(ResultVO.FAILD, "请重试!");
        }
        return resultObjectVO;
    }

    @RequestCheck(requireEntity = true)
    public ResultObjectVO deleteByIds(RequestJsonVO requestVo) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            List<BrandVO> brandVOS = JSON.parseArray(requestVo.getEntityJson(), BrandVO.class);
            if (CollectionUtils.isEmpty(brandVOS)) {
                return ResultObjectVO.fail(ResultVO.FAILD, "没有找ID");
            }
            List<ResultObjectVO> resultObjectVOList = new ArrayList<ResultObjectVO>();
            for (BrandVO brandVO : brandVOS) {
                if (brandVO.getId() != null) {
                    int row = brandService.deleteById(brandVO.getId());
                    if (row < 1) {
                        logger.warn("删除品牌失败 {} ", JSONObject.toJSONString(brandVO));
                        resultObjectVO = ResultObjectVO.fail(ResultVO.FAILD, "请重试!");

                        ResultObjectVO resultObjectRowVO = ResultObjectVO.fail(ResultVO.FAILD, "请重试!");
                        resultObjectRowVO.setData(brandVO.getId());
                        resultObjectVOList.add(resultObjectRowVO);

                        continue;
                    }

                    //只要品牌主表删除,关联如果删除失败也没什么影响
                    brandCategoryService.deleteByBrandId(brandVO.getId());

                }
            }
            resultObjectVO.setData(resultObjectVOList);

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);

            resultObjectVO = ResultObjectVO.fail(ResultVO.FAILD, "请稍后重试");
        }
        return resultObjectVO;
    }

    public ResultObjectVO saveByDisk() {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            File file = new File("D:\\mj\\2021-10-28\\布鞋品牌.json");
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            StringBuffer buffer = new StringBuffer();
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                buffer.append(line);
            }
            List<Map> rows = JSON.parseArray(buffer.toString(), Map.class);
            int size = rows.size();
            for (int i = 0; i < size; i++) {
                logger.info("遍历到{} 总数{}", i, size);
                Map row = rows.get(i);
                try {
                    BrandVO brand = new BrandVO();
                    brand.setCreateAdminId(-1L);
                    brand.setCreateDate(new Date());
                    String text = String.valueOf(row.get("text"));
                    if (text.indexOf("/") != -1) {
                        String[] texts = text.split("/");
                        brand.setEnglishName(texts[0]);
                        brand.setChineseName(texts[1]);
                    } else {
                        if (LetterFirstUtil.isLetterFirst(text)) {
                            brand.setEnglishName(text); //英文名称
                        } else {
                            brand.setChineseName(text);
                        }
                    }
                    List<Brand> brands = brandService.queryList(brand);
                    Long brandId = -1L;
                    if (CollectionUtils.isEmpty(brands)) {
                        brandId = idGenerator.id();
                        brand.setId(brandId);
                        brand.setTrademarkAreaType(1);
                        brand.setDeleteStatus(0);
                        brand.setEnabledStatus(1);
                        brandService.save(brand);
                    } else {
                        brandId = brands.get(0).getId();
                        Brand brand1 = brands.get(0);
                        brand = new BrandVO();
                        BeanUtils.copyProperties(brand, brand1);
                    }

                    BrandCategory brandCategory = new BrandCategory();
                    brandCategory.setCategoryId(889589266122801213L);
                    brandCategory.setBrandId(brandId);

                    List<BrandCategory> brandCategories = brandCategoryService.queryList(brandCategory);
                    if (CollectionUtils.isEmpty(brandCategories)) {
                        brandCategory.setId(idGenerator.id());
                        brandCategory.setCreateDate(new Date());
                        brandCategory.setDeleteStatus(0);
                        brandCategory.setBrandSort(999);

                        brandCategoryService.save(brandCategory);

                        brand.setCategoryIdCache(String.valueOf(brandCategory.getCategoryId()));
                    }

                    //查询出关联的所有类目
                    brandCategory.setCategoryId(null);
                    brandCategories = brandCategoryService.queryList(brandCategory);
                    if (CollectionUtils.isNotEmpty(brandCategories)) {
                        String categoryIdString = "";
                        for (int j = 0; j < brandCategories.size(); j++) {
                            categoryIdString += String.valueOf(brandCategories.get(j).getCategoryId());
                            if (j + 1 < brandCategories.size()) {
                                categoryIdString += ",";
                            }
                        }
                        brand.setCategoryIdCache(categoryIdString);
                    }
                    brandService.update(brand);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultObjectVO;
    }

}
