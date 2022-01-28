package com.toucan.shopping.modules.category.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.category.entity.CategoryHot;
import com.toucan.shopping.modules.category.page.CategoryHotTreeInfo;
import com.toucan.shopping.modules.category.service.CategoryHotService;
import com.toucan.shopping.modules.category.vo.CategoryHotTreeVO;
import com.toucan.shopping.modules.category.vo.CategoryHotVO;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;


/**
 * 热门类别控制器
 */
@RestController
@RequestMapping("/category/hot")
public class CategoryHotController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private CategoryHotService categoryHotService;


    @Autowired
    private Toucan toucan;

    @Autowired
    private IdGenerator idGenerator;


    /**
     * 保存类别
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/save",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO save(@RequestHeader(value = "toucan-sign-header",defaultValue = "-1") String signHeader, @RequestBody RequestJsonVO requestJsonVO)
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
            CategoryHotVO categoryHot = JSONObject.parseObject(requestJsonVO.getEntityJson(), CategoryHotVO.class);



            if(categoryHot.getCategoryId()==null)
            {
                logger.info("类别名称为空 param:"+ JSONObject.toJSONString(categoryHot));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("类别名称不能为空!");
                return resultObjectVO;
            }

            CategoryHotVO queryCategory = new CategoryHotVO();
            queryCategory.setName(categoryHot.getName());
            queryCategory.setDeleteStatus((short)0);

            if(!CollectionUtils.isEmpty(categoryHotService.queryList(queryCategory)))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("已存在该类别!");
                return resultObjectVO;
            }

            categoryHot.setId(idGenerator.id());
            categoryHot.setCreateDate(new Date());
            int row = categoryHotService.save(categoryHot);
            if (row != 1) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请重试!");
                return resultObjectVO;
            }
        }catch(Exception e)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请重试!");
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }


    public boolean existsAdminId(List<String> adminIds,String adminId)
    {
        for (String aid : adminIds) {
            if (aid != null && aid.equals(adminId)) {
                return true;
            }
        }
        return false;
    }



    /**
     * 查询树表格
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/tree/table/by/pid",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryTreeTableByPid(@RequestBody RequestJsonVO requestJsonVO){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null||requestJsonVO.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }

        try {
            CategoryHotTreeInfo queryPageInfo = JSONObject.parseObject(requestJsonVO.getEntityJson(), CategoryHotTreeInfo.class);

            List<CategoryHotTreeVO> categoryTreeVOS = new ArrayList<CategoryHotTreeVO>();
            //按指定条件查询
            if(StringUtils.isNotEmpty(queryPageInfo.getName()))
            {
                CategoryHotVO queryCategoryHot = new CategoryHotVO();
                queryCategoryHot.setName(queryPageInfo.getName());
                List<CategoryHotVO> categories = categoryHotService.queryList(queryCategoryHot);
                for (int i = 0; i < categories.size(); i++) {
                    CategoryHot category = categories.get(i);
                    CategoryHotTreeVO categoryTreeVO = new CategoryHotTreeVO();
                    BeanUtils.copyProperties(categoryTreeVO, category);
                    categoryTreeVOS.add(categoryTreeVO);
                }
            }else {
                //查询当前节点下的所有子节点
                CategoryHotVO queryCategoryHot = new CategoryHotVO();
                if(queryPageInfo.getParentId()!=null) {
                    queryCategoryHot.setParentId(queryPageInfo.getParentId());
                }else{
                    queryCategoryHot.setParentId(-1L);
                }
                List<CategoryHotVO> categories = categoryHotService.queryList(queryCategoryHot);
                for (int i = 0; i < categories.size(); i++) {
                    CategoryHot category = categories.get(i);
                    CategoryHotTreeVO categoryTreeVO = new CategoryHotTreeVO();
                    BeanUtils.copyProperties(categoryTreeVO, category);

                    queryCategoryHot = new CategoryHotVO();
                    queryCategoryHot.setParentId(category.getId());
                    Long childCount = categoryHotService.queryCount(queryCategoryHot);
                    if (childCount > 0) {
                        categoryTreeVO.setHaveChild(true);
                    }
                    categoryTreeVOS.add(categoryTreeVO);
                }
            }

            List<String> adminIds = new ArrayList<String>();
            //拿到树节点中所有创建人和修改人
            if(!CollectionUtils.isEmpty(categoryTreeVOS)) {
                for (CategoryHotVO categoryVO : categoryTreeVOS) {
                    if (categoryVO.getCreateAdminId() != null&&!"-1".equals(categoryVO.getCreateAdminId())&&!existsAdminId(adminIds,categoryVO.getCreateAdminId())) {
                        adminIds.add(categoryVO.getCreateAdminId());
                    }
                    if (categoryVO.getUpdateAdminId() != null&&!"-1".equals(categoryVO.getUpdateAdminId())&&!existsAdminId(adminIds,categoryVO.getUpdateAdminId())) {
                        adminIds.add(categoryVO.getUpdateAdminId());
                    }
                }
            }


            //将查询的这个节点设置为顶级节点
            if(StringUtils.isNotEmpty(queryPageInfo.getName())) {
                if(!CollectionUtils.isEmpty(categoryTreeVOS)) {
                    for (CategoryHot category : categoryTreeVOS) {
                        category.setParentId(-1L);
                    }
                }
            }

            resultObjectVO.setData(categoryTreeVOS);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }





}
