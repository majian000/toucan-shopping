package com.toucan.shopping.modules.product.business.service;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSON;
import com.toucan.shopping.modules.common.annotation.RequestCheck;
import com.toucan.shopping.modules.common.exception.BusinessValidationException;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.common.util.Check;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.product.entity.AttributeValue;
import com.toucan.shopping.modules.product.page.AttributeValuePageInfo;
import com.toucan.shopping.modules.product.service.AttributeValueService;
import com.toucan.shopping.modules.product.service.ProductSpuAttributeValueService;
import com.toucan.shopping.modules.product.vo.AttributeValueVO;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 属性值业务服务
 * @author majian
 */
@Service
public class AttributeValueBusinessService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private AttributeValueService attributeValueService;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private ProductSpuAttributeValueService productSpuAttributeValueService;


    /**
     * 查询列表
     * @param requestJsonVO
     * @return
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryListPage(RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            AttributeValuePageInfo queryPageInfo = JSONObject.parseObject(requestJsonVO.getEntityJson(), AttributeValuePageInfo.class);
            PageInfo<AttributeValueVO> pageInfo =  attributeValueService.queryListPage(queryPageInfo);
            resultObjectVO.setData(pageInfo);
        }catch(BusinessValidationException e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(e.getCode());
            resultObjectVO.setMsg(e.getMessage());
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
     * @param requestVo
     * @return
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO findById(RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            AttributeValueVO attributeKeyVO = JSONObject.parseObject(requestVo.getEntityJson(),AttributeValueVO.class);
            Check.notNull(attributeKeyVO.getId(), ResultVO.FAILD, "没有找到ID");

            //查询是否存在该对象
            AttributeValueVO query=new AttributeValueVO();
            query.setId(attributeKeyVO.getId());
            List<AttributeValueVO> attributeKeyVOS = attributeValueService.queryList(query);
            Check.notEmpty(attributeKeyVOS, ResultVO.FAILD, "不存在!");

            resultObjectVO.setData(attributeKeyVOS);

        }catch(BusinessValidationException e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(e.getCode());
            resultObjectVO.setMsg(e.getMessage());
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
    @RequestCheck(requireEntity = true)
    public ResultObjectVO save(RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            AttributeValueVO vo = JSONObject.parseObject(requestJsonVO.getEntityJson(), AttributeValueVO.class);

            AttributeValue entity = new AttributeValue();
            BeanUtils.copyProperties(entity,vo);
            entity.setId(idGenerator.id());
            entity.setCreateDate(new Date());
            entity.setDeleteStatus((short)0);
            int row = attributeValueService.save(entity);
            if (row <= 0) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请重试!");
                return resultObjectVO;
            }

        }catch(BusinessValidationException e)
        {
            resultObjectVO.setCode(e.getCode());
            resultObjectVO.setMsg(e.getMessage());
        }catch(Exception e)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请重试!");
            logger.warn(e.getMessage(),e);

        }
        return resultObjectVO;
    }


    /**
     * 删除指定
     * @param requestVo
     * @return
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO deleteById(RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            AttributeValue entity = JSONObject.parseObject(requestVo.getEntityJson(),AttributeValue.class);
            Check.notNull(entity.getId(), ResultVO.FAILD, "没有找到ID");

            int row = attributeValueService.deleteById(entity.getId());
            if (row < 1) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请重试!");
                return resultObjectVO;
            }


            resultObjectVO.setData(entity);

        }catch(BusinessValidationException e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(e.getCode());
            resultObjectVO.setMsg(e.getMessage());
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
    @RequestCheck(requireEntity = true)
    public ResultObjectVO deleteByIds(RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            List<AttributeValue> attributeKeys = JSON.parseArray(requestVo.getEntityJson(),AttributeValue.class);
            Check.notEmpty(attributeKeys, ResultVO.FAILD, "没有找到ID");

            List<ResultObjectVO> resultObjectVOList = new ArrayList<ResultObjectVO>();
            for(AttributeValue attributeKey:attributeKeys) {
                if(attributeKey.getId()!=null) {
                    ResultObjectVO appResultObjectVO = new ResultObjectVO();
                    appResultObjectVO.setData(attributeKey);

                    int row = attributeValueService.deleteById(attributeKey.getId());
                    if (row < 1) {
                        logger.warn("删除失败，id:{}",attributeKey.getId());
                        resultObjectVO.setCode(ResultVO.FAILD);
                        resultObjectVO.setMsg("请重试!");
                        continue;
                    }

                }
            }
            resultObjectVO.setData(resultObjectVOList);

        }catch(BusinessValidationException e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(e.getCode());
            resultObjectVO.setMsg(e.getMessage());
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
    @RequestCheck(requireEntity = true)
    public ResultObjectVO update(RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            AttributeValueVO entity = JSONObject.parseObject(requestVo.getEntityJson(),AttributeValueVO.class);

            Check.notEmpty(entity.getAttributeValue(), ResultVO.FAILD, "属性名不能为空!");

            Check.notNull(entity.getId(), ResultVO.FAILD, "请传入ID");

            entity.setUpdateDate(new Date());
            int row = attributeValueService.update(entity);
            if (row < 1) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请重试!");
                return resultObjectVO;
            }

            productSpuAttributeValueService.updateShowStatusAndSearchStatus(entity.getId(),entity.getAttributeValue(),entity.getShowStatus(),entity.getQueryStatus());

            resultObjectVO.setData(entity);

        }catch(BusinessValidationException e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(e.getCode());
            resultObjectVO.setMsg(e.getMessage());
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }



}
