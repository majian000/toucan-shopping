package com.toucan.shopping.cloud.seller.controller;


import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.seller.entity.SellerShop;
import com.toucan.shopping.modules.seller.page.SellerShopPageInfo;
import com.toucan.shopping.modules.seller.redis.SellerShopKey;
import com.toucan.shopping.modules.seller.service.SellerShopService;
import com.toucan.shopping.modules.seller.vo.SellerShopVO;
import com.toucan.shopping.modules.skylark.lock.service.SkylarkLock;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 收货地址 增删改查
 */
@RestController
@RequestMapping("/sellerShop")
public class SellerShopController {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private SkylarkLock skylarkLock;

    @Autowired
    private SellerShopService sellerShopService;





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
        SellerShopVO sellerShopVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), SellerShopVO.class);
        if(StringUtils.isEmpty(sellerShopVO.getName()))
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("请求失败,店铺名称不能为空");
            return resultObjectVO;
        }
        if(sellerShopVO.getType()==null)
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("请求失败,类型不能为空");
            return resultObjectVO;
        }
        String userMainId = String.valueOf(sellerShopVO.getUserMainId());
        try {
            boolean lockStatus = skylarkLock.lock(SellerShopKey.getSaveLockKey(userMainId), userMainId);
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请求失败,请稍候重试");
                return resultObjectVO;
            }
            //查询关联店铺
            SellerShop querySellerShop = new SellerShop();
            querySellerShop.setUserMainId(sellerShopVO.getUserMainId());
            querySellerShop.setDeleteStatus((short)0);
            //个人申请
            if(sellerShopVO.getType().intValue()==1)
            {
                //查询该用户是否已存在店铺
                List<SellerShop> sellerShops = sellerShopService.findListByEntity(querySellerShop);
                if(!CollectionUtils.isEmpty(sellerShops))
                {
                    //释放锁
                    skylarkLock.unLock(SellerShopKey.getSaveLockKey(userMainId), userMainId);
                    resultObjectVO.setCode(ResultVO.FAILD);
                    resultObjectVO.setMsg("请求失败,该用户已有店铺");
                    return resultObjectVO;
                }

                //查询该店铺是否已被注册
                querySellerShop = new SellerShop();
                querySellerShop.setName(sellerShopVO.getName());
                querySellerShop.setDeleteStatus((short)0);
                sellerShops = sellerShopService.findListByEntity(querySellerShop);
                if(!CollectionUtils.isEmpty(sellerShops))
                {
                    //释放锁
                    skylarkLock.unLock(SellerShopKey.getSaveLockKey(userMainId), userMainId);
                    resultObjectVO.setCode(ResultVO.FAILD);
                    resultObjectVO.setMsg("请求失败,该店铺已注册");
                    return resultObjectVO;
                }

                sellerShopVO.setApproveStatus(2);  //个人店铺直接审核通过

            }else if(sellerShopVO.getType().intValue()==2) //企业申请
            {
                //查询企业相关信息,判断是否已注册

                sellerShopVO.setApproveStatus(1); //企业店铺需要审核资质等相关
            }

            sellerShopVO.setId(idGenerator.id());
            sellerShopVO.setPublicShopId(String.valueOf(idGenerator.id()));
            sellerShopVO.setCreateDate(new Date());
            sellerShopVO.setDeleteStatus((short)0);
            int ret = sellerShopService.save(sellerShopVO);
            if(ret<=0)
            {
                logger.warn("保存商户店铺失败 requestJson{} id{}",requestJsonVO.getEntityJson(),sellerShopVO.getId());
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请求失败,请稍后重试");
            }
            resultObjectVO.setData(sellerShopVO);
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


    /**
     * 查询指定用户的店铺
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/find/by/user",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO findByUser(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,没有找到实体对象");
            return resultObjectVO;
        }

        try {
            SellerShop querySellerShop = JSONObject.parseObject(requestVo.getEntityJson(), SellerShop.class);

            if(StringUtils.isEmpty(requestVo.getAppCode()))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到应用编码");
                return resultObjectVO;
            }
            if(querySellerShop.getUserMainId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到用户ID");
                return resultObjectVO;
            }

            List<SellerShop> sellerShops = sellerShopService.findListByEntity(querySellerShop);
            if(!CollectionUtils.isEmpty(sellerShops)) {
                resultObjectVO.setData(sellerShops.get(0));
            }


        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,请稍后重试");
        }
        return resultObjectVO;
    }





    /**
     * 查询列表页
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/list/page",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryListPage(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,没有找到实体对象");
            return resultObjectVO;
        }

        try {
            SellerShopPageInfo queryPageInfo = JSONObject.parseObject(requestVo.getEntityJson(), SellerShopPageInfo.class);

            if(StringUtils.isEmpty(requestVo.getAppCode()))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到应用编码");
                return resultObjectVO;
            }

            //查询列表页
            resultObjectVO.setData(sellerShopService.queryListPage(queryPageInfo));

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请求失败,请稍后重试");
        }
        return resultObjectVO;
    }




}
