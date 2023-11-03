package com.toucan.shopping.modules.seller.controller;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.seller.entity.SellerDesignerPageModel;
import com.toucan.shopping.modules.seller.redis.SellerDesignerPageKey;
import com.toucan.shopping.modules.seller.service.SellerDesignerPageModelService;
import com.toucan.shopping.modules.seller.vo.SellerDesignerPageModelVO;
import com.toucan.shopping.modules.skylark.lock.service.SkylarkLock;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;


/**
 * 设计器页面操作
 */
@RestController
@RequestMapping("/seller/designer/page/model")
public class SellerDesignerPageModelController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SellerDesignerPageModelService sellerDesignerPageModelService;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private SkylarkLock skylarkLock;

    /**
     * 只保存1个
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/onlySaveOne",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO onlySaveOne(@RequestBody RequestJsonVO requestJsonVO)
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

        Long designerPageId = -1L;
        String userMainId = "-1";
        try {

            SellerDesignerPageModelVO sellerDesignerPageVO = requestJsonVO.formatEntity(SellerDesignerPageModelVO.class);
            userMainId = String.valueOf(sellerDesignerPageVO.getUserMainId());
            boolean lockStatus = skylarkLock.lock(SellerDesignerPageKey.getSaveUpdateLockKey(userMainId), userMainId);
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("保存失败,请稍后重试");
                return resultObjectVO;
            }
            SellerDesignerPageModel query = new SellerDesignerPageModel();
            query.setShopId(sellerDesignerPageVO.getShopId());
            query.setType(sellerDesignerPageVO.getType());
            query.setPosition(sellerDesignerPageVO.getPosition());
            SellerDesignerPageModel sellerDesignerPageModelRet = sellerDesignerPageModelService.queryLastOne(query);
            if(sellerDesignerPageModelRet ==null) {
                designerPageId = idGenerator.id();

                SellerDesignerPageModel sellerDesignerPageModel = new SellerDesignerPageModel();
                BeanUtils.copyProperties(sellerDesignerPageModel, sellerDesignerPageVO);
                sellerDesignerPageModel.setId(designerPageId);
                sellerDesignerPageModel.setAppCode(requestJsonVO.getAppCode());
                sellerDesignerPageModel.setCreateDate(new Date());
                sellerDesignerPageModel.setCreaterId(String.valueOf(sellerDesignerPageVO.getUserMainId()));
                sellerDesignerPageModel.setDeleteStatus((short) 0);
                int row = sellerDesignerPageModelService.save(sellerDesignerPageModel);
                if (row <= 0) {
                    resultObjectVO.setCode(ResultVO.FAILD);
                    resultObjectVO.setMsg("保存失败,请稍后重试!");
                    return resultObjectVO;
                }
            }else{ //覆盖该最新对象
                sellerDesignerPageModelRet.setUpdaterId(String.valueOf(sellerDesignerPageVO.getUserMainId()));
                sellerDesignerPageModelRet.setUpdateDate(new Date());
                sellerDesignerPageModelRet.setPageJson(sellerDesignerPageVO.getPageJson());
                sellerDesignerPageModelRet.setDesignerVersion(sellerDesignerPageVO.getDesignerVersion());
                int row = sellerDesignerPageModelService.update(sellerDesignerPageModelRet);
                if (row <= 0) {
                    resultObjectVO.setCode(ResultVO.FAILD);
                    resultObjectVO.setMsg("保存失败,请稍后重试!");
                    return resultObjectVO;
                }

            }

        }catch(Exception e)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("保存失败,请稍后重试!");
            logger.warn(e.getMessage(),e);
        }finally{
            skylarkLock.unLock(SellerDesignerPageKey.getSaveUpdateLockKey(userMainId), userMainId);
        }
        return resultObjectVO;
    }




    /**
     * 查询最后一个模型
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/queryLastOne",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryLastOne(@RequestBody RequestJsonVO requestJsonVO)
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

        String userMainId = "-1";
        try {
            SellerDesignerPageModelVO sellerDesignerPageVO = requestJsonVO.formatEntity(SellerDesignerPageModelVO.class);
            if(sellerDesignerPageVO.getShopId()==null){
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("店铺ID不能为空!");
                return resultObjectVO;
            }
            SellerDesignerPageModel query = new SellerDesignerPageModel();
            BeanUtils.copyProperties(query,sellerDesignerPageVO);
            resultObjectVO.setData(sellerDesignerPageModelService.queryLastOne(query));
        }catch(Exception e)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("查询失败,请稍后重试!");
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }


}