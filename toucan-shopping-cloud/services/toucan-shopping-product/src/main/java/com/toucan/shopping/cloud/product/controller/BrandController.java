package com.toucan.shopping.cloud.product.controller;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.common.util.LetterFirstUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.product.entity.AttributeKey;
import com.toucan.shopping.modules.product.entity.Brand;
import com.toucan.shopping.modules.product.entity.BrandCategory;
import com.toucan.shopping.modules.product.page.AttributeKeyPageInfo;
import com.toucan.shopping.modules.product.service.AttributeKeyService;
import com.toucan.shopping.modules.product.service.BrandCategoryService;
import com.toucan.shopping.modules.product.service.BrandService;
import com.toucan.shopping.modules.product.vo.AttributeKeyVO;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


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
            logger.warn("没有找到对象编码: param:"+ JSONObject.toJSONString(requestJsonVO));
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到对象编码!");
            return resultObjectVO;
        }

        try {
            BrandVO vo = JSONObject.parseObject(requestJsonVO.getEntityJson(), BrandVO.class);

            Brand entity = new Brand();
            BeanUtils.copyProperties(entity,vo);
            entity.setId(idGenerator.id());
            entity.setCreateDate(new Date());
            int row = brandService.save(entity);
            if (row <= 0) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("保存失败,请重试!");
                return resultObjectVO;
            }

        }catch(Exception e)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("保存失败,请重试!");
            logger.warn(e.getMessage(),e);

        }
        return resultObjectVO;
    }






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
            for(Map row:rows)
            {
                try {
                    Brand brand = new Brand();
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
                    }

                    BrandCategory brandCategory = new BrandCategory();
                    brandCategory.setCategoryId(889589266118606872L);
                    brandCategory.setBrandId(brandId);

                    List<BrandCategory> brandCategories = brandCategoryService.queryList(brandCategory);
                    if(CollectionUtils.isEmpty(brandCategories)) {
                        brandCategory.setId(idGenerator.id());
                        brandCategory.setCreateDate(new Date());
                        brandCategory.setDeleteStatus(0);
                        brandCategory.setBrandSort(999);

                        brandCategoryService.save(brandCategory);
                    }
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
