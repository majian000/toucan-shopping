package com.toucan.shopping.cloud.product.controller;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.product.entity.AttributeKey;
import com.toucan.shopping.modules.product.entity.AttributeValue;
import com.toucan.shopping.modules.product.page.AttributeKeyPageInfo;
import com.toucan.shopping.modules.product.service.AttributeKeyService;
import com.toucan.shopping.modules.product.service.AttributeValueService;
import com.toucan.shopping.modules.product.vo.AttributeKeyVO;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;


/**
 * 属性键管理
 * @author majian
 */
@RestController
@RequestMapping("/attributeKey")
public class AttributeKeyController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private AttributeKeyService attributeKeyService;

    @Autowired
    private AttributeValueService attributeValueService;

    @Autowired
    private IdGenerator idGenerator;



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
            AttributeKeyPageInfo queryPageInfo = JSONObject.parseObject(requestJsonVO.getEntityJson(), AttributeKeyPageInfo.class);

            List<AttributeKeyVO> attributeVOS = new ArrayList<AttributeKeyVO>();
            boolean queryCriteria=false;
            //按指定条件查询
            if(StringUtils.isNotEmpty(queryPageInfo.getAttributeName())
                    ||(queryPageInfo.getAttributeScope()!=null&&queryPageInfo.getAttributeScope().intValue()!=-1)
                    ||(queryPageInfo.getAttributeType()!=null&&queryPageInfo.getAttributeType().intValue()!=-1)
                    ||(queryPageInfo.getQueryStatus()!=null&&queryPageInfo.getQueryStatus().intValue()!=-1)
                    ||(queryPageInfo.getCategoryId()!=null&&queryPageInfo.getCategoryId().intValue()!=-1)
                    ||(queryPageInfo.getShowStatus()!=null&&queryPageInfo.getShowStatus().intValue()!=-1))
            {
                queryCriteria = true;
                AttributeKeyVO queryAttributeKey = new AttributeKeyVO();
                BeanUtils.copyProperties(queryAttributeKey,queryPageInfo);
                List<AttributeKeyVO> attributeKeyVOS = attributeKeyService.queryList(queryAttributeKey);
                for (int i = 0; i < attributeKeyVOS.size(); i++) {
                    attributeVOS.add(attributeKeyVOS.get(i));
                }
            }else {
                //查询当前节点下的所有子节点
                AttributeKeyVO queryAttributeKey = new AttributeKeyVO();
                if(queryPageInfo.getParentId()!=null) {
                    queryAttributeKey.setParentId(queryPageInfo.getParentId());
                }else{
                    queryAttributeKey.setParentId(-1L);
                }
                List<AttributeKeyVO> attributeKeyVOS = attributeKeyService.queryList(queryAttributeKey);
                for (int i = 0; i < attributeKeyVOS.size(); i++) {
                    AttributeKeyVO attributeKeyVO = attributeKeyVOS.get(i);

                    queryAttributeKey = new AttributeKeyVO();
                    queryAttributeKey.setParentId(attributeKeyVO.getId());
                    Long childCount = attributeKeyService.queryCount(queryAttributeKey);
                    if (childCount > 0) {
                        attributeKeyVO.setHaveChild(true);
                    }
                    attributeVOS.add(attributeKeyVO);
                }
            }

            //将查询的这个节点设置为顶级节点
            if(queryCriteria) {
                if(!CollectionUtils.isEmpty(attributeVOS)) {
                    for (AttributeKey attributeKey : attributeVOS) {
                        attributeKey.setParentId(-1L);
                    }
                }
            }

            resultObjectVO.setData(attributeVOS);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }


    /**
     * 查询树
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value = "/query/tree/category/id",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryTreeByCategoryId(@RequestBody RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            AttributeKeyVO query = JSONObject.parseObject(requestJsonVO.getEntityJson(), AttributeKeyVO.class);
            if(query.getCategoryId()==null||query.getCategoryId()==-1)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到分类ID或不支持查询顶级分类");
                return resultObjectVO;

            }

            List<AttributeKeyVO> attributeKeyVOS = attributeKeyService.queryList(query);

            AttributeKeyVO rootTreeVO = new AttributeKeyVO();
            rootTreeVO.setTitle("根属性");
            rootTreeVO.setParentId(-1L);
            rootTreeVO.setPid(-1L);
            rootTreeVO.setId(-1L);
            rootTreeVO.setText("根属性");

            if(!CollectionUtils.isEmpty(attributeKeyVOS))
            {
                List<AttributeKeyVO> attributeKeyTreeVOS = new ArrayList<AttributeKeyVO>();
                for(AttributeKeyVO attributeKeyVO : attributeKeyVOS)
                {
                    if(attributeKeyVO.getParentId().longValue()==-1) {
                        AttributeKeyVO treeVO = new AttributeKeyVO();
                        BeanUtils.copyProperties(treeVO, attributeKeyVO);

                        treeVO.setTitle(attributeKeyVO.getAttributeName());
                        treeVO.setText(attributeKeyVO.getAttributeName());
                        treeVO.setPid(attributeKeyVO.getParentId());

                        attributeKeyTreeVOS.add(treeVO);

                        treeVO.setChildren(new ArrayList<AttributeKeyVO>());
                        attributeKeyService.setChildren(attributeKeyVOS,treeVO);
                    }
                }

                rootTreeVO.setChildren(attributeKeyTreeVOS);
            }

            List<AttributeKeyVO> rootAttributeKeyTreeVOS = new ArrayList<AttributeKeyVO>();
            rootAttributeKeyTreeVOS.add(rootTreeVO);
            resultObjectVO.setData(rootAttributeKeyTreeVOS);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
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
            AttributeKeyVO attributeKeyVO = JSONObject.parseObject(requestVo.getEntityJson(),AttributeKeyVO.class);
            if(attributeKeyVO.getId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到ID");
                return resultObjectVO;
            }

            //查询是否存在该对象
            AttributeKeyVO query=new AttributeKeyVO();
            query.setId(attributeKeyVO.getId());
            List<AttributeKeyVO> attributeKeyVOS = attributeKeyService.queryList(query);
            if(CollectionUtils.isEmpty(attributeKeyVOS))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("不存在!");
                return resultObjectVO;
            }

            AttributeKeyVO parentAttributeKeyVO= attributeKeyService.queryById(attributeKeyVOS.get(0).getParentId());
            if(parentAttributeKeyVO!=null)
            {
                attributeKeyVOS.get(0).setParentName(parentAttributeKeyVO.getAttributeName());
            }else{
                attributeKeyVOS.get(0).setParentName("根节点");
            }
            resultObjectVO.setData(attributeKeyVOS);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }




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
            AttributeKeyVO vo = JSONObject.parseObject(requestJsonVO.getEntityJson(), AttributeKeyVO.class);

            if(vo.getCategoryId()==null)
            {
                logger.warn("关联类别为空 param:"+ requestJsonVO.getEntityJson());
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("关联类别为空!");
                return resultObjectVO;
            }

            AttributeKey entity = new AttributeKey();
            BeanUtils.copyProperties(entity,vo);
            entity.setId(idGenerator.id());
            entity.setCreateDate(new Date());
            entity.setDeleteStatus((short)0);
            int row = attributeKeyService.save(entity);
            if (row <= 0) {
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


//    void saveAttributeValue(Long keyId,String value,String extend1)
//    {
//        AttributeValue attributeValue = new AttributeValue();
//        attributeValue.setAttributeKeyId(keyId);
//        attributeValue.setId(idGenerator.id());
//        attributeValue.setCreateDate(new Date());
//        attributeValue.setDeleteStatus((short)0);
//        attributeValue.setAttributeValue(value);
//        attributeValue.setAttributeValueExtend1(extend1);
//        attributeValueService.save(attributeValue);
//
//    }


//    @RequestMapping(value="/init",produces = "application/json;charset=UTF-8")
//    @ResponseBody
//    public ResultObjectVO init(@RequestBody Long[] categoryIds)
//    {
//        ResultObjectVO resultObjectVO = new ResultObjectVO();
//        for(Long categoryId:categoryIds)
//        {
//            AttributeKey entity = new AttributeKey();
//            entity.setAttributeName("颜色");
//            entity.setCategoryId(categoryId);
//            entity.setDeleteStatus((short)0);
//            entity.setAttributeSort(999L);
//            entity.setId(idGenerator.id());
//            entity.setCreateDate(new Date());
//            int row = attributeKeyService.save(entity);
//            if(row<1)
//            {
//                throw new IllegalArgumentException("保存失败");
//            }
//
//            saveAttributeValue(entity.getId(),"乳白色","255, 251, 240");
//            saveAttributeValue(entity.getId(),"白色","255, 255, 255");
//            saveAttributeValue(entity.getId(),"米白色","238, 222, 176");
//            saveAttributeValue(entity.getId(),"浅灰色","228, 228, 228");
//            saveAttributeValue(entity.getId(),"深灰色","102, 102, 102");
//            saveAttributeValue(entity.getId(),"灰色","128, 128, 128");
//            saveAttributeValue(entity.getId(),"银色","192, 192, 192");
//            saveAttributeValue(entity.getId(),"黑色","0, 0, 0");
//            saveAttributeValue(entity.getId(),"桔红色","255, 117, 0");
//            saveAttributeValue(entity.getId(),"玫红色","223, 27, 118");
//            saveAttributeValue(entity.getId(),"粉红色","255, 182, 193");
//            saveAttributeValue(entity.getId(),"红色","255, 0, 0");
//            saveAttributeValue(entity.getId(),"藕色","238, 208, 216");
//            saveAttributeValue(entity.getId(),"西瓜红","240, 86, 84");
//            saveAttributeValue(entity.getId(),"酒红色","153, 0, 0");
//            saveAttributeValue(entity.getId(),"卡其色","195, 176, 145");
//            saveAttributeValue(entity.getId(),"姜黄色","255, 199, 115");
//            saveAttributeValue(entity.getId(),"明黄色","255, 255, 1");
//            saveAttributeValue(entity.getId(),"杏色","247, 238, 214");
//            saveAttributeValue(entity.getId(),"柠檬黄","255, 236, 67");
//            saveAttributeValue(entity.getId(),"桔色","255, 165, 0");
//            saveAttributeValue(entity.getId(),"浅黄色","250, 255, 114");
//            saveAttributeValue(entity.getId(),"荧光黄","234, 255, 86");
//            saveAttributeValue(entity.getId(),"金色","255, 215, 0");
//            saveAttributeValue(entity.getId(),"香槟色","240, 218, 171");
//            saveAttributeValue(entity.getId(),"黄色","255, 255, 0");
//            saveAttributeValue(entity.getId(),"军绿色","93, 118, 42");
//            saveAttributeValue(entity.getId(),"墨绿色","5, 119, 72");
//            saveAttributeValue(entity.getId(),"浅绿色","152, 251, 152");
//            saveAttributeValue(entity.getId(),"绿色","0, 128, 0");
//            saveAttributeValue(entity.getId(),"翠绿色","10, 163, 68");
//            saveAttributeValue(entity.getId(),"荧光绿","35, 250, 7");
//            saveAttributeValue(entity.getId(),"青色","0, 224, 158");
//            saveAttributeValue(entity.getId(),"天蓝色","68, 206, 246");
//            saveAttributeValue(entity.getId(),"孔雀蓝","0, 164, 197");
//            saveAttributeValue(entity.getId(),"宝蓝色","75, 92, 196");
//            saveAttributeValue(entity.getId(),"浅蓝色","210, 240, 244");
//            saveAttributeValue(entity.getId(),"深蓝色","4, 22, 144");
//            saveAttributeValue(entity.getId(),"湖蓝色","48, 223, 243");
//            saveAttributeValue(entity.getId(),"蓝色","0, 0, 254");
//            saveAttributeValue(entity.getId(),"藏青色","46, 78, 126");
//            saveAttributeValue(entity.getId(),"浅紫色","237, 224, 230");
//            saveAttributeValue(entity.getId(),"深紫色","67, 6, 83");
//            saveAttributeValue(entity.getId(),"紫红色","139, 0, 98");
//            saveAttributeValue(entity.getId(),"紫罗兰","183, 172, 228");
//            saveAttributeValue(entity.getId(),"紫色","128, 0, 128");
//            saveAttributeValue(entity.getId(),"咖啡色","96, 57, 18");
//            saveAttributeValue(entity.getId(),"巧克力色","210, 105, 30");
//            saveAttributeValue(entity.getId(),"栗色","96, 40, 30");
//            saveAttributeValue(entity.getId(),"浅棕色","179, 92, 68");
//            saveAttributeValue(entity.getId(),"深卡其布色","189, 183, 107");
//            saveAttributeValue(entity.getId(),"深棕色","124, 75, 0");
//            saveAttributeValue(entity.getId(),"褐色","133, 91, 0");
//            saveAttributeValue(entity.getId(),"驼色","168, 132, 98");
//            saveAttributeValue(entity.getId(),"花色","");
//            saveAttributeValue(entity.getId(),"透明","");
//
//
//        }
//        return resultObjectVO;
//    }

    /**
     * 删除指定
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/delete/id",produces = "application/json;charset=UTF-8",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO deleteById(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }

        try {
            AttributeKey entity = JSONObject.parseObject(requestVo.getEntityJson(),AttributeKey.class);
            if(entity.getId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到ID");
                return resultObjectVO;
            }

            int row = attributeKeyService.deleteById(entity.getId());
            if (row < 1) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请重试!");
                return resultObjectVO;
            }

            //删除属性值
            attributeValueService.deleteByAttributeKeyId(entity.getId());

            List<AttributeKeyVO> childList = new LinkedList<>();
            attributeKeyService.queryChildList(childList,entity.getId());
            if(CollectionUtils.isNotEmpty(childList))
            {
                for(AttributeKeyVO attributeKeyVO:childList)
                {
                    //删除属性名
                    attributeKeyService.deleteById(attributeKeyVO.getId());
                    //删除属性值
                    attributeValueService.deleteByAttributeKeyId(attributeKeyVO.getId());
                }

            }



            resultObjectVO.setData(entity);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
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
            List<AttributeKey> attributeKeys = JSONObject.parseArray(requestVo.getEntityJson(),AttributeKey.class);
            if(CollectionUtils.isEmpty(attributeKeys))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到ID");
                return resultObjectVO;
            }
            List<ResultObjectVO> resultObjectVOList = new ArrayList<ResultObjectVO>();
            for(AttributeKey attributeKey:attributeKeys) {
                if(attributeKey.getId()!=null) {
                    ResultObjectVO appResultObjectVO = new ResultObjectVO();
                    appResultObjectVO.setData(attributeKey);

                    int row = attributeKeyService.deleteById(attributeKey.getId());
                    if (row < 1) {
                        logger.warn("删除失败，id:{}",attributeKey.getId());
                        resultObjectVO.setCode(ResultVO.FAILD);
                        resultObjectVO.setMsg("请重试!");
                        continue;
                    }

                    //删除属性值
                    attributeValueService.deleteByAttributeKeyId(attributeKey.getId());


                    List<AttributeKeyVO> childList = new LinkedList<>();
                    attributeKeyService.queryChildList(childList,attributeKey.getId());
                    if(CollectionUtils.isNotEmpty(childList))
                    {
                        for(AttributeKeyVO attributeKeyVO:childList)
                        {
                            //删除属性名
                            attributeKeyService.deleteById(attributeKeyVO.getId());
                            //删除属性值
                            attributeValueService.deleteByAttributeKeyId(attributeKeyVO.getId());
                        }

                    }


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



    /**
     * 編輯
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/update",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO update(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }

        try {
            AttributeKeyVO entity = JSONObject.parseObject(requestVo.getEntityJson(),AttributeKeyVO.class);

            if(StringUtils.isEmpty(entity.getAttributeName()))
            {
                logger.info("属性名为空 param:"+ JSONObject.toJSONString(entity));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("属性名不能为空!");
                return resultObjectVO;
            }

            if(entity.getId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请传入ID");
                return resultObjectVO;
            }

            entity.setUpdateDate(new Date());
            int row = attributeKeyService.update(entity);
            if (row < 1) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请重试!");
                return resultObjectVO;
            }
            resultObjectVO.setData(entity);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }



}
