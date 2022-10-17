package com.toucan.shopping.cloud.seller.controller;


import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.redis.service.ToucanStringRedisService;
import com.toucan.shopping.modules.seller.constant.ShopConstant;
import com.toucan.shopping.modules.seller.entity.FreightTemplate;
import com.toucan.shopping.modules.seller.entity.SellerShop;
import com.toucan.shopping.modules.seller.page.FreightTemplatePageInfo;
import com.toucan.shopping.modules.seller.page.SellerShopPageInfo;
import com.toucan.shopping.modules.seller.redis.FreightTemplateKey;
import com.toucan.shopping.modules.seller.redis.SellerShopKey;
import com.toucan.shopping.modules.seller.service.FreightTemplateService;
import com.toucan.shopping.modules.seller.service.SellerLoginHistoryService;
import com.toucan.shopping.modules.seller.service.SellerShopService;
import com.toucan.shopping.modules.seller.vo.FreightTemplateVO;
import com.toucan.shopping.modules.seller.vo.SellerShopVO;
import com.toucan.shopping.modules.skylark.lock.service.SkylarkLock;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 运费模板
 * @author majian
 * @date 2022-9-21 14:13:19
 */
@RestController
@RequestMapping("/freightTemplate")
public class FreightTemplateController {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private SkylarkLock skylarkLock;

    @Autowired
    private FreightTemplateService freightTemplateService;


    @Autowired
    private Toucan toucan;






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
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }

        try {
            FreightTemplatePageInfo queryPageInfo = JSONObject.parseObject(requestVo.getEntityJson(), FreightTemplatePageInfo.class);

            if(StringUtils.isEmpty(requestVo.getAppCode()))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到应用编码");
                return resultObjectVO;
            }

            //查询列表页
            resultObjectVO.setData(freightTemplateService.queryListPage(queryPageInfo));

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
            logger.info("请求参数为空");
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请重试!");
            return resultObjectVO;
        }

        FreightTemplateVO freightTemplateVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), FreightTemplateVO.class);
        String userMainId = String.valueOf(freightTemplateVO.getUserMainId());
        try {

            boolean lockStatus = skylarkLock.lock(FreightTemplateKey.getSaveLockKey(userMainId), userMainId);
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请稍后重试");
                return resultObjectVO;
            }


            if(StringUtils.isEmpty(freightTemplateVO.getName()))
            {
                //释放锁
                skylarkLock.unLock(FreightTemplateKey.getSaveLockKey(userMainId), userMainId);

                logger.warn("名称为空 param:"+ JSONObject.toJSONString(freightTemplateVO));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("名称不能为空!");


                return resultObjectVO;
            }
            if(freightTemplateVO.getUserMainId()==null)
            {
                //释放锁
                skylarkLock.unLock(FreightTemplateKey.getSaveLockKey(userMainId), userMainId);

                logger.warn("用户ID为空 param:"+ JSONObject.toJSONString(freightTemplateVO));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("用户ID不能为空!");
                return resultObjectVO;
            }

            if(freightTemplateVO.getShopId()==null)
            {
                //释放锁
                skylarkLock.unLock(FreightTemplateKey.getSaveLockKey(userMainId), userMainId);

                logger.warn("店铺ID为空 param:"+ JSONObject.toJSONString(freightTemplateVO));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("店铺ID不能为空!");
                return resultObjectVO;
            }


            FreightTemplateVO queryCountVO = new FreightTemplateVO();
            queryCountVO.setUserMainId(freightTemplateVO.getUserMainId());
            queryCountVO.setShopId(freightTemplateVO.getShopId());
            Long count = freightTemplateService.queryCount(queryCountVO);
            if(count+1>toucan.getSeller().getFreightTemplateMaxCount())
            {
                //释放锁
                skylarkLock.unLock(FreightTemplateKey.getSaveLockKey(userMainId), userMainId);

                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("数量已达到上限");
                return resultObjectVO;
            }


            freightTemplateVO.setId(idGenerator.id());
            freightTemplateVO.setCreateDate(new Date());
            freightTemplateVO.setDeleteStatus((short)0);

            int row = freightTemplateService.save(freightTemplateVO);
            if (row < 1) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请重试!");
            }
        }catch(Exception e)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试!");
            logger.warn(e.getMessage(),e);
        }finally{
            skylarkLock.unLock(FreightTemplateKey.getSaveLockKey(userMainId), userMainId);
        }
        return resultObjectVO;
    }


}
