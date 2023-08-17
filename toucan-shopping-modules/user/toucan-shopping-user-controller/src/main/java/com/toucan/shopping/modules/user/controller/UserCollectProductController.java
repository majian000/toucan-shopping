package com.toucan.shopping.modules.user.controller;


import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.skylark.lock.service.SkylarkLock;
import com.toucan.shopping.modules.user.constant.UserCollectProductConstant;
import com.toucan.shopping.modules.user.entity.UserCollectProduct;
import com.toucan.shopping.modules.user.service.UserCollectProductService;
import com.toucan.shopping.modules.user.service.UserCollectProductService;
import com.toucan.shopping.modules.user.vo.UserCollectProductVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 用户收藏商品
 */
@RestController
@RequestMapping("/user/collect/product")
public class UserCollectProductController {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private SkylarkLock skylarkLock;

    @Autowired
    private UserCollectProductService userCollectProductService;


    @RequestMapping(value="/save",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO save(@RequestBody RequestJsonVO requestJsonVO){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null)
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("没有找到请求对象");
            return resultObjectVO;
        }
        if (StringUtils.isEmpty(requestJsonVO.getAppCode())) {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("没有找到应用编码");
            return resultObjectVO;
        }
        UserCollectProductVO userCollectProductVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), UserCollectProductVO.class);
        if(userCollectProductVO.getProductSkuId()==null)
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("商品ID不能为空");
            return resultObjectVO;
        }
        if(StringUtils.isEmpty(userCollectProductVO.getAppCode()))
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("应用编码不能为空");
            return resultObjectVO;
        }
        String userMainId = String.valueOf(userCollectProductVO.getUserMainId());
        try {
            //查询收货人数量
            UserCollectProductVO queryUserCollectProduct = new UserCollectProductVO();
            queryUserCollectProduct.setUserMainId(userCollectProductVO.getUserMainId());
            queryUserCollectProduct.setAppCode(userCollectProductVO.getAppCode());
            List<UserCollectProduct> userCollectProducts = userCollectProductService.findListByEntity(queryUserCollectProduct);
            if(!CollectionUtils.isEmpty(userCollectProducts)&&userCollectProducts.size()>= UserCollectProductConstant.MAX_COUNT)
            {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("收藏商品数量达到"+ UserCollectProductConstant.MAX_COUNT+"个上限");
                return resultObjectVO;
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
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }finally{
        }
        return resultObjectVO;
    }






    /**
     * 删除指定
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/delete/id/userMainId/appCode",produces = "application/json;charset=UTF-8",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO deleteByIdAndUserMainIdAndAppCode(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }

        try {
            UserCollectProduct entity = JSONObject.parseObject(requestVo.getEntityJson(),UserCollectProduct.class);
            if(entity.getId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("ID不能为空");
                return resultObjectVO;
            }

            if(entity.getUserMainId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("用户ID不能为空");
                return resultObjectVO;
            }


            if(StringUtils.isEmpty(entity.getAppCode()))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("应用编码不能为空");
                return resultObjectVO;
            }
            int row = userCollectProductService.deleteByIdAndUserMainIdAndAppCode(entity.getId(),entity.getUserMainId(),entity.getAppCode());
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




    /**
     * 查询收藏列表
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/queryCollectProducts",produces = "application/json;charset=UTF-8",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO queryCollectProducts(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }

        try {
            UserCollectProductVO userCollectProductVO = JSONObject.parseObject(requestVo.getEntityJson(),UserCollectProductVO.class);

            if(userCollectProductVO.getUserMainId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("用户ID不能为空");
                return resultObjectVO;
            }

            if(StringUtils.isEmpty(userCollectProductVO.getAppCode()))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("应用编码不能为空");
                return resultObjectVO;
            }

            if(CollectionUtils.isEmpty(userCollectProductVO.getProductSkuIds()))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("商品SKU ID不能为空");
                return resultObjectVO;
            }

            resultObjectVO.setData(userCollectProductService.findListByEntity(userCollectProductVO));

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }


}
