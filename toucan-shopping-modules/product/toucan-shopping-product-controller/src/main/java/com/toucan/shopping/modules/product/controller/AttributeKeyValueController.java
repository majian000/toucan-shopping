package com.toucan.shopping.modules.product.controller;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.product.page.AttributeKeyPageInfo;
import com.toucan.shopping.modules.product.service.AttributeKeyService;
import com.toucan.shopping.modules.product.service.AttributeValueService;
import com.toucan.shopping.modules.product.service.ProductSpuAttributeKeyService;
import com.toucan.shopping.modules.product.service.ProductSpuAttributeValueService;
import com.toucan.shopping.modules.product.vo.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 属性键值管理
 * @author majian
 */
@RestController
@RequestMapping("/attributeKeyValue")
public class AttributeKeyValueController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private AttributeKeyService attributeKeyService;

    @Autowired
    private AttributeValueService attributeValueService;

    @Autowired
    private ProductSpuAttributeKeyService productSpuAttributeKeyService;

    @Autowired
    private ProductSpuAttributeValueService productSpuAttributeValueService;

    private SimplePropertyPreFilter attributeKeyPropertyFilter =  new SimplePropertyPreFilter(AttributeKeyVO.class, "id","attributeName","categoryId","parentId","children","attributeSort","values","showStatus","queryStatus");
    private SimplePropertyPreFilter attributeValueSimplePropertyFilter =  new SimplePropertyPreFilter(AttributeValueVO.class, "id","attributeValue","parentId","attributeValue","attributeSort","showStatus","queryStatus");
    private SimplePropertyPreFilter[] simplePropertyPreFilters ={attributeKeyPropertyFilter,attributeValueSimplePropertyFilter};


    /**
     * 根据分类ID查询
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/find/category/id",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO findByCategoryId(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }

        try {
            AttributeKeyVO queryAttributeKeyVO = JSONObject.parseObject(requestVo.getEntityJson(),AttributeKeyVO.class);
            if(queryAttributeKeyVO.getCategoryId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到分类ID");
                return resultObjectVO;
            }

            List<AttributeKeyVO> attributeKeyVOS = attributeKeyService.queryListBySortDesc(queryAttributeKeyVO);
            //查询该分类下的所有键列表
            if(CollectionUtils.isNotEmpty(attributeKeyVOS))
            {
                //一个分类下不会有太多的销售属性key,所以这里用in查询
                List<Long> attributeKeyIdList = new LinkedList<>();

                for(AttributeKeyVO attributeKeyVO:attributeKeyVOS)
                {
                    attributeKeyVO.setValues(new LinkedList<>());
                    //保存属性键ID
                    attributeKeyIdList.add(attributeKeyVO.getId());
                }

                //查询这些键的值列表
                if(CollectionUtils.isNotEmpty(attributeKeyIdList)) {
                    AttributeValueVO queryAttributeValueVO = new AttributeValueVO();
                    queryAttributeValueVO.setAttributeKeyIdList(attributeKeyIdList);
                    queryAttributeValueVO.setShowStatus((short)1);
                    List<AttributeValueVO> attributeValueVOS = attributeValueService.queryListBySortDesc(queryAttributeValueVO);
                    if(CollectionUtils.isNotEmpty(attributeValueVOS))
                    {
                        for(AttributeValueVO attributeValueVO:attributeValueVOS)
                        {
                            for(AttributeKeyVO attributeKeyVO:attributeKeyVOS)
                            {
                                if(attributeValueVO.getAttributeKeyId()!=null&&attributeKeyVO.getId()!=null&&
                                        attributeValueVO.getAttributeKeyId().longValue()==attributeKeyVO.getId().longValue())
                                {
                                    attributeKeyVO.getValues().add(attributeValueVO);
                                    break;
                                }
                            }
                        }
                    }
                }

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
     * 根据分类ID查询属性树
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/query/attribute/tree/page",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryAttributeTreePage(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }

        try {
            AttributeKeyPageInfo attributeKeyPageInfo = JSONObject.parseObject(requestVo.getEntityJson(), AttributeKeyPageInfo.class);

            if(attributeKeyPageInfo.getCategoryId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到分类ID");
                return resultObjectVO;
            }

            if(attributeKeyPageInfo.getParentId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到上级节点ID");
                return resultObjectVO;
            }
            PageInfo<AttributeKeyVO> attributeKeyPage = attributeKeyService.queryListPageBySortDesc(attributeKeyPageInfo);
            if(attributeKeyPage.getTotal()!=null&&attributeKeyPage.getTotal().longValue()>0)
            {
                List<AttributeKeyVO> attributeKeyVOS = attributeKeyPage.getList();
                for(AttributeKeyVO attributeKeyVO:attributeKeyVOS)
                {
                    //设置当前节点属性值
                    attributeKeyVO.setValues(attributeValueService.queryListByAttributeKeyIdSortDesc(attributeKeyVO.getId()));
                    attributeKeyService.setChildNodeAndChildNodeValue(attributeKeyVO);
                }
                attributeKeyPage.setList(attributeKeyVOS);
            }
            resultObjectVO.setData(JSONObject.toJSONString(attributeKeyPage,simplePropertyPreFilters));

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }




    /**
     * 根据SPUID和SKU中的分类ID以及属性 查询可被搜索的属性列表
     * 注:同一级分类下属性名称不允许重复
     * 例如：手机分类下有颜色属性，游戏手机分类下也允许有颜色属性
     * 结构:
     *      手机 颜色属性
     *      手机》游戏手机 颜色属性
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/query/search/attribute/list/spuId/categoryId/attributeName/AttributeValue",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO querySearchAttributeList(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }

        try {
            List<AttributeKeyVO> attributes = new LinkedList<>();
            AttributeKeyValueVO attributeKeyValueVO = requestVo.formatEntity(AttributeKeyValueVO.class);
            if(attributeKeyValueVO.getProductSpuId()!=null) {
                ProductSpuAttributeKeyVO queryPSAK = new ProductSpuAttributeKeyVO();
                queryPSAK.setProductSpuId(attributeKeyValueVO.getProductSpuId());
                queryPSAK.setShowStatus((short)1);
                queryPSAK.setQueryStatus((short)1);
                List<ProductSpuAttributeKeyVO> productSpuAttributeKeyVOS = productSpuAttributeKeyService.queryListBySortDesc(queryPSAK);

                ProductSpuAttributeValueVO queryPSV=new ProductSpuAttributeValueVO();
                queryPSV.setProductSpuId(attributeKeyValueVO.getProductSpuId());
                queryPSV.setShowStatus((short)1);
                queryPSV.setQueryStatus((short)1);
                List<ProductSpuAttributeValueVO> productSpuAttributeValueVOS = productSpuAttributeValueService.queryListBySortDesc(queryPSV);

                for(ProductSpuAttributeKeyVO productSpuAttributeKeyVO:productSpuAttributeKeyVOS)
                {
                    AttributeKeyVO attributeKeyVO=new AttributeKeyVO();
                    attributeKeyVO.setId(productSpuAttributeKeyVO.getAttributeKeyId());
                    attributeKeyVO.setAttributeName(productSpuAttributeKeyVO.getAttributeName());
                    attributeKeyVO.setValues(new LinkedList<>());
                    for(ProductSpuAttributeValueVO productSpuAttributeValueVO:productSpuAttributeValueVOS)
                    {
                        if(productSpuAttributeValueVO.getAttributeKeyId().longValue()==productSpuAttributeKeyVO.getAttributeKeyId().longValue())
                        {
                            AttributeValueVO attributeValueVO=new AttributeValueVO();
                            attributeValueVO.setAttributeValue(productSpuAttributeValueVO.getAttributeValue());
                            attributeValueVO.setId(productSpuAttributeValueVO.getAttributeValueId());
                            attributeKeyVO.getValues().add(attributeValueVO);
                        }
                    }
                    attributes.add(attributeKeyVO);
                }

            }



            //查询SKU商品中的属性,判断这些销售属性是否可被搜索、可被显示
            if(attributeKeyValueVO.getCategoryId()!=null
                    && CollectionUtils.isNotEmpty(attributeKeyValueVO.getAttributeKeyList())
                    && CollectionUtils.isNotEmpty(attributeKeyValueVO.getAttributeValueList())) {
                AttributeKeyVO queryAttributeKeyVO = new AttributeKeyVO();
                queryAttributeKeyVO.setCategoryId(attributeKeyValueVO.getCategoryId());
                queryAttributeKeyVO.setAttributeNameList(attributeKeyValueVO.getAttributeKeyList());
                queryAttributeKeyVO.setShowStatus((short)1);
                queryAttributeKeyVO.setQueryStatus((short)1);
                List<AttributeKeyVO> attributeKeyVOS = attributeKeyService.queryList(queryAttributeKeyVO);
                if(CollectionUtils.isNotEmpty(attributeKeyVOS)) {


                    AttributeValueVO queryAttributeValueVO = new AttributeValueVO();
                    queryAttributeValueVO.setAttributeKeyIdList(attributeKeyVOS.stream().map(AttributeKeyVO::getId).collect(Collectors.toList()));
                    queryAttributeValueVO.setAttributeValueList(attributeKeyValueVO.getAttributeValueList());
                    queryAttributeValueVO.setShowStatus((short) 1);
                    queryAttributeValueVO.setQueryStatus((short) 1);
                    List<AttributeValueVO> attributeValueVOS = attributeValueService.queryList(queryAttributeValueVO);

                    for(AttributeKeyVO ak:attributeKeyVOS)
                    {
                        AttributeKeyVO attributeKeyVO=new AttributeKeyVO();
                        attributeKeyVO.setId(ak.getId());
                        attributeKeyVO.setAttributeName(ak.getAttributeName());
                        attributeKeyVO.setValues(new LinkedList<>());
                        for(AttributeValueVO av:attributeValueVOS)
                        {
                            if(av.getAttributeKeyId().equals(ak.getId()))
                            {
                                AttributeValueVO attributeValueVO=new AttributeValueVO();
                                attributeValueVO.setAttributeValue(av.getAttributeValue());
                                attributeValueVO.setId(av.getAttributeValueId());
                                attributeKeyVO.getValues().add(attributeValueVO);
                            }
                        }
                        attributes.add(attributeKeyVO);
                    }
                }
            }
            resultObjectVO.setData(attributes);


        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }

}
