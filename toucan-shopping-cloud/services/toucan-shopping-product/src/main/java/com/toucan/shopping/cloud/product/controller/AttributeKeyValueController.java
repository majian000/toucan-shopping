package com.toucan.shopping.cloud.product.controller;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.product.service.AttributeKeyService;
import com.toucan.shopping.modules.product.service.AttributeValueService;
import com.toucan.shopping.modules.product.vo.AttributeKeyVO;
import com.toucan.shopping.modules.product.vo.AttributeValueVO;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;


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
                //一个分类下不会有太多的属性key,所以这里用in查询
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





}
