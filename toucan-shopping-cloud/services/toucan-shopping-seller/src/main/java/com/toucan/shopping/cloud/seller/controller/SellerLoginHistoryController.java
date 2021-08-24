package com.toucan.shopping.cloud.seller.controller;


import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.seller.redis.SellerShopKey;
import com.toucan.shopping.modules.seller.service.SellerLoginHistoryService;
import com.toucan.shopping.modules.seller.vo.SellerLoginHistoryVO;
import com.toucan.shopping.modules.skylark.lock.service.SkylarkLock;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * 卖家登录历史 增删改查
 */
@RestController
@RequestMapping("/seller/loginHistory")
public class SellerLoginHistoryController {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private SkylarkLock skylarkLock;

    @Autowired
    private SellerLoginHistoryService sellerLoginHistoryService;





    @RequestMapping(value="/save",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO save(@RequestBody RequestJsonVO requestJsonVO){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null)
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("请求失败,没有找到请求对象");
            return resultObjectVO;
        }
        if (StringUtils.isEmpty(requestJsonVO.getAppCode())) {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("请求失败,没有找到应用编码");
            return resultObjectVO;
        }
        SellerLoginHistoryVO sellerShopLoginHistoryVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), SellerLoginHistoryVO.class);
        String userMainId = String.valueOf(sellerShopLoginHistoryVO.getUserMainId());
        try {
            boolean lockStatus = skylarkLock.lock(SellerShopKey.getSaveLockKey(userMainId), userMainId);
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请求失败,请稍候重试");
                return resultObjectVO;
            }
            sellerShopLoginHistoryVO.setId(idGenerator.id());
            sellerShopLoginHistoryVO.setCreateDate(new Date());
            sellerShopLoginHistoryVO.setDeleteStatus((short)0);
            int ret = sellerLoginHistoryService.save(sellerShopLoginHistoryVO);
            if(ret<=0)
            {
                logger.warn("保存商户店铺失败 requestJson{} id{}",requestJsonVO.getEntityJson(),sellerShopLoginHistoryVO.getId());
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请求失败,请稍后重试");
            }
            resultObjectVO.setData(sellerShopLoginHistoryVO);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,请稍后重试");
        }finally{
            skylarkLock.unLock(SellerShopKey.getSaveLockKey(userMainId), userMainId);
        }
        return resultObjectVO;
    }



}
