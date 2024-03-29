package com.toucan.shopping.modules.product.controller;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.common.util.LetterFirstUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.product.entity.Brand;
import com.toucan.shopping.modules.product.entity.BrandCategory;
import com.toucan.shopping.modules.product.page.BrandCategoryPageInfo;
import com.toucan.shopping.modules.product.page.BrandPageInfo;
import com.toucan.shopping.modules.product.service.BrandCategoryService;
import com.toucan.shopping.modules.product.service.BrandService;
import com.toucan.shopping.modules.product.vo.BrandCategoryVO;
import com.toucan.shopping.modules.product.vo.BrandVO;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;


/**
 * 品牌管理
 * @author majian
 *
 */
@RestController
@RequestMapping("/brand")
public class BrandController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private BrandService brandService;

    @Autowired
    private BrandCategoryService brandCategoryService;

    @Autowired
    private IdGenerator idGenerator;



    /**
     * 保存
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/save",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO save(@RequestBody RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null)
        {
            logger.warn("请求参数为空");
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请重试!");
            return resultObjectVO;
        }
        if(requestJsonVO.getAppCode()==null)
        {
            logger.warn("没有找到应用编码: param:"+ JSONObject.toJSONString(requestJsonVO));
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到应用编码!");
            return resultObjectVO;
        }

        try {
            BrandVO brandVo = JSONObject.parseObject(requestJsonVO.getEntityJson(), BrandVO.class);
            if(StringUtils.isEmpty(brandVo.getChineseName())&&StringUtils.isEmpty(brandVo.getEnglishName()))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("品牌名称不能为空");
                return resultObjectVO;
            }
            if(StringUtils.isEmpty(brandVo.getCategoryIdCache()))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("品牌关联分类不能为空");
                return resultObjectVO;
            }

            String[] categotyIdArray = brandVo.getCategoryIdCache().split(",");
            if(categotyIdArray.length>30)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("品牌最多只能关联30个分类");
                return resultObjectVO;
            }


            BrandVO queryBrand = new BrandVO();
            queryBrand.setChineseName(brandVo.getChineseName());
            queryBrand.setDeleteStatus(0);

            List<Brand> brandList = brandService.queryList(queryBrand);
            if(!CollectionUtils.isEmpty(brandList))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg(brandVo.getChineseName()+"名称已存在!");
                return resultObjectVO;
            }

            if(StringUtils.isNotEmpty(brandVo.getEnglishName())) {
                queryBrand = new BrandVO();
                queryBrand.setEnglishName(brandVo.getEnglishName());
                queryBrand.setDeleteStatus(0);

                brandList = brandService.queryList(queryBrand);
                if (!CollectionUtils.isEmpty(brandList)) {
                    resultObjectVO.setCode(ResultVO.FAILD);
                    resultObjectVO.setMsg(brandVo.getEnglishName() + "名称已存在!");
                    return resultObjectVO;
                }
            }

            Brand entity = new Brand();
            BeanUtils.copyProperties(entity,brandVo);
            entity.setId(idGenerator.id());
            entity.setCreateDate(new Date());
            entity.setDeleteStatus(0);
            int row = brandService.save(entity);
            if (row <= 0) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请重试!");
                return resultObjectVO;
            }


            for(String categoryId:categotyIdArray) {
                BrandCategory brandCategory = new BrandCategory();
                brandCategory.setId(idGenerator.id());
                brandCategory.setCategoryId(Long.parseLong(categoryId));
                brandCategory.setBrandId(entity.getId());
                brandCategory.setCreateDate(new Date());
                brandCategory.setDeleteStatus(0);
                brandCategoryService.save(brandCategory);
            }


        }catch(Exception e)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请重试!");
            logger.warn(e.getMessage(),e);

        }
        return resultObjectVO;
    }





    /**
     * 查询列表
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/list/by/categoryId",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryListByCategoryId(@RequestBody RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null)
        {
            logger.info("请求参数为空");
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请重试!");
            return resultObjectVO;
        }

        try {
            BrandVO queryBrand = JSONObject.parseObject(requestJsonVO.getEntityJson(), BrandVO.class);
            if(queryBrand.getCategoryId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("分类ID不能为空!");
                return resultObjectVO;

            }

            List<Brand> brands = brandService.queryList(queryBrand);
            resultObjectVO.setData(brands);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("查询失败!");
        }
        return resultObjectVO;
    }



    /**
     * 查询列表
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/list/page",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryListPage(@RequestBody RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null)
        {
            logger.info("请求参数为空");
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请重试!");
            return resultObjectVO;
        }
        if(requestJsonVO.getAppCode()==null)
        {
            logger.info("没有找到对象: param:"+ JSONObject.toJSONString(requestJsonVO));
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到对象!");
            return resultObjectVO;
        }
        try {
            BrandPageInfo queryPageInfo = JSONObject.parseObject(requestJsonVO.getEntityJson(), BrandPageInfo.class);
            PageInfo<BrandVO> pageInfo =  brandService.queryListPage(queryPageInfo);
            if(CollectionUtils.isNotEmpty(pageInfo.getList()))
            {
                for(BrandVO brandVO:pageInfo.getList())
                {
                    if(brandVO!=null&&StringUtils.isNotEmpty(brandVO.getCategoryIdCache()))
                    {
                        String[] categoryIdArray = brandVO.getCategoryIdCache().split(",");
                        brandVO.setCategoryIdCacheArray(categoryIdArray);
                    }
                }
            }
            resultObjectVO.setData(pageInfo);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("查询失败!");
        }

        return resultObjectVO;
    }







    /**
     * 更新
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/update",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO update(@RequestBody RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null)
        {
            logger.info("请求参数为空");
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请重试!");
            return resultObjectVO;
        }

        try {
            BrandVO entity = JSONObject.parseObject(requestJsonVO.getEntityJson(), BrandVO.class);

            if(entity.getId()==null)
            {
                logger.info("ID为空 param:"+ JSONObject.toJSONString(entity));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("ID不能为空!");
                return resultObjectVO;
            }

            if(StringUtils.isEmpty(entity.getChineseName())&&StringUtils.isEmpty(entity.getEnglishName()))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("品牌名称不能为空");
                return resultObjectVO;
            }

            if(StringUtils.isEmpty(entity.getCategoryIdCache()))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("品牌分类不能为空");
                return resultObjectVO;
            }

            String[] categotyIdArray = entity.getCategoryIdCache().split(",");
            if(categotyIdArray.length>30)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("品牌最多只能关联30个分类");
                return resultObjectVO;
            }



            BrandVO queryBrand = new BrandVO();
            List<Brand> brandList = null;
            if(StringUtils.isNotEmpty(entity.getChineseName())) {
                queryBrand.setChineseName(entity.getChineseName());
                queryBrand.setDeleteStatus(0);

                brandList = brandService.queryList(queryBrand);
                if (!CollectionUtils.isEmpty(brandList)) {
                    if (entity.getId().longValue() != brandList.get(0).getId().longValue()) {
                        resultObjectVO.setCode(ResultVO.FAILD);
                        resultObjectVO.setMsg(entity.getChineseName() + "名称已存在!");
                        return resultObjectVO;
                    }
                }
            }

            if(StringUtils.isNotEmpty(entity.getEnglishName())) {
                queryBrand = new BrandVO();
                queryBrand.setEnglishName(entity.getEnglishName());
                queryBrand.setDeleteStatus(0);
                brandList = brandService.queryList(queryBrand);
                if (!CollectionUtils.isEmpty(brandList)) {
                    if (entity.getId().longValue() != brandList.get(0).getId().longValue()) {
                        resultObjectVO.setCode(ResultVO.FAILD);
                        resultObjectVO.setMsg(entity.getEnglishName() + "名称已存在!");
                        return resultObjectVO;
                    }
                }
            }

            entity.setUpdateDate(new Date());
            int row = brandService.update(entity);
            if (row != 1) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请重试!");
                return resultObjectVO;
            }

            //更新关联
            brandCategoryService.deleteByBrandId(entity.getId());


            for(String categoryId:categotyIdArray) {
                BrandCategory brandCategory = new BrandCategory();
                brandCategory.setId(idGenerator.id());
                brandCategory.setCategoryId(Long.parseLong(categoryId));
                brandCategory.setBrandId(entity.getId());
                brandCategory.setCreateDate(new Date());
                brandCategory.setDeleteStatus(0);
                brandCategoryService.save(brandCategory);
            }

        }catch(Exception e)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请重试!");
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }






    /**
     * 根据ID查询
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/find/id",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO findById(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }

        try {
            BrandVO entity = JSONObject.parseObject(requestVo.getEntityJson(),BrandVO.class);
            if(entity.getId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到ID");
                return resultObjectVO;
            }

            //查询是否存
            BrandVO query=new BrandVO();
            query.setId(entity.getId());
            List<Brand> entityList = brandService.queryList(query);
            if(CollectionUtils.isEmpty(entityList))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("对象不存在!");
                return resultObjectVO;
            }
            List<BrandVO> brandVOS = new ArrayList<BrandVO>();
            for(Brand brand:entityList)
            {
                BrandVO brandVO = new BrandVO();
                BeanUtils.copyProperties(brandVO,brand);
                if(StringUtils.isNotEmpty(brandVO.getCategoryIdCache())) {
                    brandVO.setCategoryIdCacheArray(brandVO.getCategoryIdCache().split(","));
                }
                brandVOS.add(brandVO);
            }

            resultObjectVO.setData(brandVOS);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }





    /**
     * 根据ID集合查询
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/find/idList",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO findByIdList(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }

        try {
            BrandVO query = JSONObject.parseObject(requestVo.getEntityJson(),BrandVO.class);
            if(query.getIdList()==null||query.getIdList().size()<=0)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到ID集合");
                return resultObjectVO;
            }


            List<Brand> brands = brandService.queryList(query);
            if(CollectionUtils.isEmpty(brands))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("品牌列表为空");
                return resultObjectVO;
            }

            resultObjectVO.setData(brands);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }







    /**
     * 根据名称以及分类ID查询
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/find/name/categoryId/enabled",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO findListByNameAndCategoryIdAndEnabled(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }

        try {
            BrandVO query = JSONObject.parseObject(requestVo.getEntityJson(),BrandVO.class);
            if(StringUtils.isEmpty(query.getName()))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("名称不能为空");
                return resultObjectVO;
            }

            List<Brand> brands = brandService.queryList(query);
            if(CollectionUtils.isEmpty(brands))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("品牌列表为空");
                return resultObjectVO;
            }

            resultObjectVO.setData(brands);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }



    /**
     * 根据ID删除
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/delete/id",produces = "application/json;charset=UTF-8",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO deleteById(@RequestBody RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null)
        {
            logger.info("请求参数为空");
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请重试!");
            return resultObjectVO;
        }
        if(requestJsonVO.getAppCode()==null)
        {
            logger.info("没有找到应用编码: param:"+ JSONObject.toJSONString(requestJsonVO));
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到应用编码!");
            return resultObjectVO;
        }

        try {
            Brand brand = JSONObject.parseObject(requestJsonVO.getEntityJson(), Brand.class);



            if(brand.getId()==null)
            {
                logger.info("ID为空 param:"+ JSONObject.toJSONString(brand));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("ID不能为空!");
                return resultObjectVO;
            }


            BrandVO queryBrand = new BrandVO();
            queryBrand.setId(brand.getId());

            List<Brand> brandList = brandService.queryList(queryBrand);
            if(CollectionUtils.isEmpty(brandList))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("不存在该品牌!");
                return resultObjectVO;
            }

            brand = brandList.get(0);
            int row = brandService.deleteById(brand.getId());
            if (row <=0) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请重试!");
                return resultObjectVO;
            }

            //删除品牌与类目关联
            brandCategoryService.deleteByBrandId(brand.getId());


        }catch(Exception e)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请重试!");
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }




    /**
     * 批量删除
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/delete/ids",produces = "application/json;charset=UTF-8",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO deleteByIds(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }

        try {
            List<BrandVO> brandVOS = JSONObject.parseArray(requestVo.getEntityJson(),BrandVO.class);
            if(CollectionUtils.isEmpty(brandVOS))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找ID");
                return resultObjectVO;
            }
            List<ResultObjectVO> resultObjectVOList = new ArrayList<ResultObjectVO>();
            for(BrandVO brandVO:brandVOS) {
                if(brandVO.getId()!=null) {
                    int row = brandService.deleteById(brandVO.getId());
                    if (row < 1) {
                        logger.warn("删除品牌失败 {} ",JSONObject.toJSONString(brandVO));
                        resultObjectVO.setCode(ResultVO.FAILD);
                        resultObjectVO.setMsg("请重试!");

                        ResultObjectVO resultObjectRowVO = new ResultObjectVO();
                        resultObjectRowVO.setCode(ResultVO.FAILD);
                        resultObjectRowVO.setMsg("请重试!");
                        resultObjectRowVO.setData(brandVO.getId());
                        resultObjectVOList.add(resultObjectRowVO);

                        continue;
                    }

                    //只要品牌主表删除,关联如果删除失败也没什么影响
                    brandCategoryService.deleteByBrandId(brandVO.getId());

                }
            }
            resultObjectVO.setData(resultObjectVOList);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }


//
//    /**
//     * 刷新品牌主表的类别ID字段
//     * @param requestJsonVO
//     * @return
//     */
//    @RequestMapping(value="/flush/category/id/list",produces = "application/json;charset=UTF-8")
//    @ResponseBody
//    public ResultObjectVO flushCategoryIdList(@RequestBody RequestJsonVO requestJsonVO)
//    {
//        ResultObjectVO resultObjectVO = new ResultObjectVO();
//        if(requestJsonVO==null)
//        {
//            logger.info("请求参数为空");
//            resultObjectVO.setCode(ResultVO.FAILD);
//            resultObjectVO.setMsg("请重试!");
//            return resultObjectVO;
//        }
//        try {
//            BrandPageInfo brandPageInfo = new BrandPageInfo();
//            brandPageInfo.setLimit(500);
//            PageInfo<BrandVO> pageInfo = brandService.queryListPage(brandPageInfo);
//            while(CollectionUtils.isNotEmpty(pageInfo.getList()))
//            {
//                List<BrandVO> brandVOS = pageInfo.getList();
//                for(BrandVO brandVO:brandVOS)
//                {
//                    BrandCategory brandCategory = new BrandCategory();
//                    brandCategory.setBrandId(brandVO.getId());
//                    List<BrandCategory> brandCategories = brandCategoryService.queryList(brandCategory);
//                    if(CollectionUtils.isNotEmpty(brandCategories))
//                    {
//                        String categoryIdString="";
//                        for(int i=0;i<brandCategories.size();i++) {
//                            categoryIdString+=String.valueOf(brandCategories.get(i).getCategoryId());
//                            if(i+1<brandCategories.size())
//                            {
//                                categoryIdString+=",";
//                            }
//                        }
//                        brandVO.setCategoryIdCache(categoryIdString);
//                    }
//                    brandService.update(brandVO);
//                }
//                brandPageInfo.setPage(brandPageInfo.getPage()+1);
//                pageInfo = brandService.queryListPage(brandPageInfo);
//            }
//
//        }catch(Exception e)
//        {
//            logger.warn(e.getMessage(),e);
//            resultObjectVO.setCode(ResultVO.FAILD);
//            resultObjectVO.setMsg("查询失败!");
//        }
//
//        return resultObjectVO;
//    }
//
//
//
    /**
     * 保存类别
     * @return
     */
    @RequestMapping(value="/saveByDisk",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO saveByDisk()
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            File file = new File("D:\\mj\\2021-10-28\\布鞋品牌.json");
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            StringBuffer buffer = new StringBuffer();
            String line = null;
            while((line = bufferedReader.readLine())!=null)
            {
                buffer.append(line);
            }
            List<Map> rows = JSONObject.parseArray(buffer.toString(), Map.class);
            int size = rows.size();
            for(int i=0;i<size;i++)
            {
                logger.info("遍历到{} 总数{}",i,size);
                Map row=rows.get(i);
                try {
                    BrandVO brand = new BrandVO();
                    brand.setCreateAdminId(-1L);
                    brand.setCreateDate(new Date());
                    String text = String.valueOf(row.get("text"));
                    if(text.indexOf("/")!=-1)
                    {
                        String[] texts = text.split("/");
                        brand.setEnglishName(texts[0]);
                        brand.setChineseName(texts[1]);
                    }else{
                        if(LetterFirstUtil.isLetterFirst(text))
                        {
                            brand.setEnglishName(text); //英文名称
                        }else{
                            brand.setChineseName(text);
                        }
                    }
                    List<Brand> brands = brandService.queryList(brand);
                    Long brandId = -1L;
                    if(CollectionUtils.isEmpty(brands)) {
                        brandId = idGenerator.id();
                        brand.setId(brandId);
                        brand.setTrademarkAreaType(1);
                        brand.setDeleteStatus(0);
                        brand.setEnabledStatus(1);
                        brandService.save(brand);
                    }else {
                        brandId =brands.get(0).getId();
                        Brand brand1 = brands.get(0);
                        brand = new BrandVO();
                        BeanUtils.copyProperties(brand,brand1);
                    }

                    BrandCategory brandCategory = new BrandCategory();
                    brandCategory.setCategoryId(889589266122801213L);
                    brandCategory.setBrandId(brandId);

                    List<BrandCategory> brandCategories = brandCategoryService.queryList(brandCategory);
                    if(CollectionUtils.isEmpty(brandCategories)) {
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
                    if(CollectionUtils.isNotEmpty(brandCategories))
                    {
                        String categoryIdString="";
                        for(int j=0;j<brandCategories.size();j++) {
                            categoryIdString+=String.valueOf(brandCategories.get(j).getCategoryId());
                            if(j+1<brandCategories.size())
                            {
                                categoryIdString+=",";
                            }
                        }
                        brand.setCategoryIdCache(categoryIdString);
                    }
                    brandService.update(brand);
                }catch(Exception e)
                {
                    e.printStackTrace();
                }

            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        return resultObjectVO;
    }




}
