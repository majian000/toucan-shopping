package com.toucan.shopping.modules.seller.business.service;


import com.alibaba.fastjson2.JSONObject;
import com.toucan.shopping.modules.common.annotation.RequestCheck;
import com.toucan.shopping.modules.common.exception.BusinessValidationException;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.util.Check;
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



    @RequestCheck(requireEntity = true)
    public ResultObjectVO save(RequestJsonVO requestJsonVO){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        SellerLoginHistoryVO sellerShopLoginHistoryVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), SellerLoginHistoryVO.class);
        String userMainId = String.valueOf(sellerShopLoginHistoryVO.getUserMainId());
        try {
            boolean lockStatus = skylarkLock.lock(SellerLoginHistoryKey.getSaveLockKey(userMainId), userMainId);
            if (!lockStatus) {
                return ResultObjectVO.fail(ResultObjectVO.FAILD, "请稍后重试");
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
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            return ResultObjectVO.fail(ResultVO.FAILD, "请稍后重试");
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
    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryListPage(RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            SellerLoginHistoryPageInfo queryPageInfo = JSONObject.parseObject(requestVo.getEntityJson(), SellerLoginHistoryPageInfo.class);

            //查询列表页
            resultObjectVO.setData(sellerLoginHistoryService.queryListPage(queryPageInfo));

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
     * 查询10条最近登录的记录
     * @param requestVo
     * @return
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryListByLatest10(RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            SellerLoginHistoryVO querySellerLoginHistoryVO = JSONObject.parseObject(requestVo.getEntityJson(), SellerLoginHistoryVO.class);

            Check.notNull(querySellerLoginHistoryVO.getUserMainId(), ResultVO.FAILD, "没有找到用户ID");
            querySellerLoginHistoryVO.setSize(10);
            resultObjectVO.setData(sellerLoginHistoryService.queryListByCreateDateDesc(querySellerLoginHistoryVO));


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
