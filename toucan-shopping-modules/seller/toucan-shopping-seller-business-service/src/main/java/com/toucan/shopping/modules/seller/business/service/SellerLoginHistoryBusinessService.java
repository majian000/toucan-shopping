package com.toucan.shopping.modules.seller.business.service;


import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.seller.page.SellerLoginHistoryPageInfo;
import com.toucan.shopping.modules.seller.redis.SellerLoginHistoryKey;
import com.toucan.shopping.modules.seller.service.SellerLoginHistoryService;
import com.toucan.shopping.modules.seller.vo.SellerLoginHistoryVO;
import com.toucan.shopping.modules.skylark.lock.service.SkylarkLock;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class SellerLoginHistoryBusinessService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private SkylarkLock skylarkLock;

    @Autowired
    private SellerLoginHistoryService sellerLoginHistoryService;



    public ResultObjectVO save(RequestJsonVO requestJsonVO){
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
        SellerLoginHistoryVO sellerShopLoginHistoryVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), SellerLoginHistoryVO.class);
        String userMainId = String.valueOf(sellerShopLoginHistoryVO.getUserMainId());
        try {
            boolean lockStatus = skylarkLock.lock(SellerLoginHistoryKey.getSaveLockKey(userMainId), userMainId);
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请稍后重试");
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
                resultObjectVO.setMsg("请稍后重试");
            }
            resultObjectVO.setData(sellerShopLoginHistoryVO);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }finally{
            skylarkLock.unLock(SellerLoginHistoryKey.getSaveLockKey(userMainId), userMainId);
        }
        return resultObjectVO;
    }


    /**
     * 查询列表页
     * @param requestVo
     * @return
     */
    public ResultObjectVO queryListPage(RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }

        try {
            SellerLoginHistoryPageInfo queryPageInfo = JSONObject.parseObject(requestVo.getEntityJson(), SellerLoginHistoryPageInfo.class);

            if(StringUtils.isEmpty(requestVo.getAppCode()))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到应用编码");
                return resultObjectVO;
            }

            //查询列表页
            resultObjectVO.setData(sellerLoginHistoryService.queryListPage(queryPageInfo));

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }




    /**
     * 查询10条最近登录的记录
     * @param requestVo
     * @return
     */
    public ResultObjectVO queryListByLatest10(RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }

        try {
            SellerLoginHistoryVO querySellerLoginHistoryVO = JSONObject.parseObject(requestVo.getEntityJson(), SellerLoginHistoryVO.class);

            if(StringUtils.isEmpty(requestVo.getAppCode()))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到应用编码");
                return resultObjectVO;
            }
            if(querySellerLoginHistoryVO.getUserMainId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到用户ID");
                return resultObjectVO;
            }
            querySellerLoginHistoryVO.setSize(10);
            resultObjectVO.setData(sellerLoginHistoryService.queryListByCreateDateDesc(querySellerLoginHistoryVO));


        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }


}
