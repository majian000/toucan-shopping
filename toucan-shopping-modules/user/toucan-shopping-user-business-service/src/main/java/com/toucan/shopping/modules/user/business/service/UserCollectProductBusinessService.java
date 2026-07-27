package com.toucan.shopping.modules.user.business.service;


import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.common.annotation.RequestCheck;
import com.toucan.shopping.modules.common.exception.BusinessValidationException;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.common.util.Check;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.skylark.lock.service.SkylarkLock;
import com.toucan.shopping.modules.user.constant.UserCollectProductConstant;
import com.toucan.shopping.modules.user.entity.UserCollectProduct;
import com.toucan.shopping.modules.user.page.UserCollectProductPageInfo;
import com.toucan.shopping.modules.user.redis.UserCollectProductKey;
import com.toucan.shopping.modules.user.service.UserCollectProductService;
import com.toucan.shopping.modules.user.vo.UserCollectProductVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserCollectProductBusinessService {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private SkylarkLock skylarkLock;

    @Autowired
    private UserCollectProductService userCollectProductService;


    @RequestCheck(requireEntity = true)
    public ResultObjectVO save(RequestJsonVO requestJsonVO){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        UserCollectProductVO userCollectProductVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), UserCollectProductVO.class);
        Check.notNull(userCollectProductVO.getProductSkuId(), ResultObjectVO.FAILD, "商品ID不能为空");
        Check.notEmpty(userCollectProductVO.getAppCode(), ResultObjectVO.FAILD, "应用编码不能为空");
        String userMainId = String.valueOf(userCollectProductVO.getUserMainId());
        try {
            boolean lockStatus = skylarkLock.lock(UserCollectProductKey.getCollectKey(userMainId), userMainId);
            if (!lockStatus) {
                return ResultObjectVO.fail(ResultObjectVO.FAILD, "请稍后重试");
            }
            UserCollectProductVO queryUserCollectProduct = new UserCollectProductVO();
            queryUserCollectProduct.setUserMainId(userCollectProductVO.getUserMainId());
            queryUserCollectProduct.setAppCode(userCollectProductVO.getAppCode());
            List<UserCollectProduct> userCollectProducts = userCollectProductService.findListByEntity(queryUserCollectProduct);
            if(!CollectionUtils.isEmpty(userCollectProducts)&&userCollectProducts.size()>= UserCollectProductConstant.MAX_COUNT)
            {
                return ResultObjectVO.fail(ResultObjectVO.FAILD, "收藏商品数量达到"+ UserCollectProductConstant.MAX_COUNT+"个上限");
            }

            queryUserCollectProduct.setProductSkuId(userCollectProductVO.getProductSkuId());
            List<UserCollectProduct> userCollectProductList = userCollectProductService.findListByEntity(queryUserCollectProduct);
            if(!CollectionUtils.isEmpty(userCollectProductList))
            {
                return ResultObjectVO.fail(ResultVO.FAILD, "已存在该商品收藏");
            }

            userCollectProductVO.setId(idGenerator.id());
            userCollectProductVO.setDeleteStatus((short)0);
            userCollectProductVO.setCreateDate(new Date());
            int ret = userCollectProductService.save(userCollectProductVO);
            if(ret<=0)
            {
                logger.warn("保存收藏商品失败 requestJson{} id{}",requestJsonVO.getEntityJson(),userCollectProductVO.getId());
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请稍后重试");
            }
            resultObjectVO.setData(userCollectProductVO);
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            return ResultObjectVO.fail(ResultVO.FAILD, "请稍后重试");
        }finally{
            skylarkLock.unLock(UserCollectProductKey.getCollectKey(userMainId), userMainId);
        }
        return resultObjectVO;
    }


    /**
     * 删除指定
     * @param requestVo
     * @return
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO deleteBySkuIdAndUserMainIdAndAppCode(RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        UserCollectProduct entity = JSONObject.parseObject(requestVo.getEntityJson(),UserCollectProduct.class);
        Check.notNull(entity.getUserMainId(), ResultVO.FAILD, "用户ID不能为空");
        Check.notNull(entity.getProductSkuId(), ResultVO.FAILD, "商品ID不能为空");
        Check.notEmpty(entity.getAppCode(), ResultVO.FAILD, "应用编码不能为空");

        String userMainId = String.valueOf(entity.getUserMainId());
        try {
            boolean lockStatus = skylarkLock.lock(UserCollectProductKey.getCollectKey(userMainId), userMainId);
            if (!lockStatus) {
                return ResultObjectVO.fail(ResultObjectVO.FAILD, "请稍后重试");
            }
            int row = userCollectProductService.deleteBySkuIdAndUserMainIdAndAppCode(entity.getProductSkuId(),entity.getUserMainId(),entity.getAppCode());
            if (row < 1) {
                return ResultObjectVO.fail(ResultVO.FAILD, "请重试!");
            }
            resultObjectVO.setData(entity);

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            return ResultObjectVO.fail(ResultVO.FAILD, "请稍后重试");
        }finally{
            skylarkLock.unLock(UserCollectProductKey.getCollectKey(userMainId), userMainId);
        }
        return resultObjectVO;
    }


    /**
     * 查询收藏列表
     * @param requestVo
     * @return
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryCollectProducts(RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            UserCollectProductVO userCollectProductVO = JSONObject.parseObject(requestVo.getEntityJson(),UserCollectProductVO.class);

            Check.notNull(userCollectProductVO.getUserMainId(), ResultVO.FAILD, "用户ID不能为空");
            Check.notEmpty(userCollectProductVO.getAppCode(), ResultVO.FAILD, "应用编码不能为空");

            if(CollectionUtils.isEmpty(userCollectProductVO.getProductSkuIds()))
            {
                return ResultObjectVO.fail(ResultVO.FAILD, "商品SKU ID不能为空");
            }

            resultObjectVO.setData(userCollectProductService.findListByEntity(userCollectProductVO));

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            return ResultObjectVO.fail(ResultVO.FAILD, "请稍后重试");
        }
        return resultObjectVO;
    }


    /**
     * 查询列表页
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryListPage(RequestJsonVO requestJsonVO){

        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            UserCollectProductPageInfo pageInfo = JSONObject.parseObject(requestJsonVO.getEntityJson(),UserCollectProductPageInfo.class);
            PageInfo<UserCollectProductVO> listPage = userCollectProductService.queryListPage(pageInfo);
            resultObjectVO.setData(listPage);
            resultObjectVO.setCode(ResultObjectVO.SUCCESS);
            resultObjectVO.setMsg("请求完成");
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            return ResultObjectVO.fail(ResultObjectVO.FAILD, "请求失败");
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
            UserCollectProduct entity = JSONObject.parseObject(requestVo.getEntityJson(),UserCollectProduct.class);
            Check.notNull(entity.getId(), ResultVO.FAILD, "没有找到ID");

            //查询是否存在该数据
            UserCollectProductVO query=new UserCollectProductVO();
            query.setId(entity.getId());
            List<UserCollectProduct> list = userCollectProductService.findListByEntity(query);
            if(CollectionUtils.isEmpty(list))
            {
                return ResultObjectVO.fail(ResultVO.FAILD, "该数据不存在!");
            }

            int row = userCollectProductService.deleteById(entity.getId());
            if (row < 1) {
                return ResultObjectVO.fail(ResultVO.FAILD, "请重试!");
            }

            resultObjectVO.setData(entity);

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            return ResultObjectVO.fail(ResultVO.FAILD, "请稍后重试");
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
            List<UserCollectProduct> entitys = JSONObject.parseArray(requestVo.getEntityJson(),UserCollectProduct.class);
            if(CollectionUtils.isEmpty(entitys))
            {
                return ResultObjectVO.fail(ResultVO.FAILD, "没有找到ID");
            }
            List<ResultObjectVO> resultObjectVOList = new ArrayList<ResultObjectVO>();
            for(UserCollectProduct entity:entitys) {
                if(entity.getId()!=null) {
                    ResultObjectVO appResultObjectVO = new ResultObjectVO();
                    appResultObjectVO.setData(entity);

                    int row = userCollectProductService.deleteById(entity.getId());
                    if (row < 1) {
                        logger.warn("删除失败，id:{}",entity.getId());
                        resultObjectVO.setCode(ResultVO.FAILD);
                        resultObjectVO.setMsg("请重试!");
                        continue;
                    }

                }
            }
            resultObjectVO.setData(resultObjectVOList);

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            return ResultObjectVO.fail(ResultVO.FAILD, "请稍后重试");
        }
        return resultObjectVO;
    }

}
