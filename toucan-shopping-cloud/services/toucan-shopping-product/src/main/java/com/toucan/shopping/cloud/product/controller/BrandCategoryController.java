package com.toucan.shopping.cloud.product.controller;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.page.PageInfo;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 品牌分类管理
 * @author majian
 *
 */
@RestController
@RequestMapping("/brand/category")
public class BrandCategoryController {
    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Autowired
    private BrandCategoryService brandCategoryService;

    @Autowired
    private IdGenerator idGenerator;





    /**
     * 根据品牌ID查询
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/find/brand/id",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO findByBrandId(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }

        try {
            BrandCategoryVO entity = JSONObject.parseObject(requestVo.getEntityJson(),BrandCategoryVO.class);
            if(entity.getBrandId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到品牌ID");
                return resultObjectVO;
            }

            //查询是否存在
            BrandCategoryVO query=new BrandCategoryVO();
            query.setBrandId(entity.getBrandId());
            List<BrandCategory> entityList = brandCategoryService.queryList(query);
            resultObjectVO.setData(entityList);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }




}
