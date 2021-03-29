package com.toucan.shopping.category.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.category.entity.Category;
import com.toucan.shopping.category.service.CategoryService;
import com.toucan.shopping.category.vo.CategoryVO;
import com.toucan.shopping.common.vo.RequestJsonVO;
import com.toucan.shopping.common.vo.ResultListVO;
import com.toucan.shopping.common.vo.ResultObjectVO;
import com.toucan.shopping.common.vo.ResultVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/category/user")
public class CategoryController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private CategoryService categoryService;




    /**
     * 查询指定应用下所有类别,并返回子节点
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/tree/area/code",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryCategoryTreeByAreaCode(@RequestBody RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null)
        {
            logger.warn("请求参数为空");
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请重试!");
            return resultObjectVO;
        }
        try {
            CategoryVO categoryVO = JSONObject.parseObject(requestJsonVO.getEntityJson(),CategoryVO.class);
            if(StringUtils.isEmpty(categoryVO.getAreaCode()))
            {
                logger.warn("地区编码为空 {} ",requestJsonVO.getEntityJson());
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("地区编码为空!");
                return resultObjectVO;
            }

            resultObjectVO.setData(categoryService.queryTree(categoryVO.getAreaCode()));
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("查询失败!");
        }

        return resultObjectVO;
    }



    /**
     * 根据ID查询
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/id",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryById(@RequestHeader("bbs-sign-header") String signHeader,@RequestBody RequestJsonVO requestJsonVO)
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
            Category Category = JSONObject.parseObject(requestJsonVO.getEntityJson(), Category.class);
            resultObjectVO.setData(categoryService.queryById(Category.getId()));
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("查询失败!");
        }

        return resultObjectVO;
    }


    /**
     * 根据ID查询
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/ids",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryByIdList(@RequestHeader("bbs-sign-header") String signHeader,@RequestBody RequestJsonVO requestJsonVO)
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
            List<Category> Categorys = JSONArray.parseArray(requestJsonVO.getEntityJson(),Category.class);
            if(!CollectionUtils.isEmpty(Categorys)) {
                List<Category> CategoryList = new ArrayList<Category>();
                for(Category Category:Categorys) {
                    Category CategoryEntity = categoryService.queryById(Category.getId());
                    if(CategoryEntity!=null) {
                        CategoryList.add(CategoryEntity);
                    }
                }
                resultObjectVO.setData(CategoryList);
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("查询失败!");
        }

        return resultObjectVO;
    }




}
