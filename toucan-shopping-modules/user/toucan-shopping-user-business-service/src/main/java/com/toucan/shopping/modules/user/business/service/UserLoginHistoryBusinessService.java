package com.toucan.shopping.modules.user.business.service;


import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.common.annotation.RequestCheck;
import com.toucan.shopping.modules.common.exception.BusinessValidationException;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.util.Check;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.skylark.lock.service.SkylarkLock;
import com.toucan.shopping.modules.user.entity.UserLoginHistory;
import com.toucan.shopping.modules.user.page.UserLoginHistoryPageInfo;
import com.toucan.shopping.modules.user.redis.UserLoginHistoryKey;
import com.toucan.shopping.modules.user.service.UserLoginHistoryService;
import com.toucan.shopping.modules.user.vo.UserLoginHistoryVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserLoginHistoryBusinessService {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private SkylarkLock skylarkLock;

    @Autowired
    private UserLoginHistoryService userLoginHistoryService;


    /**
     * 查询列表页
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryListPage(RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            UserLoginHistoryPageInfo queryPageInfo = JSONObject.parseObject(requestVo.getEntityJson(), UserLoginHistoryPageInfo.class);
            resultObjectVO.setData(userLoginHistoryService.queryListPage(queryPageInfo));
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch(Exception e) {
            logger.warn(e.getMessage(),e);
            return ResultObjectVO.fail(ResultVO.FAILD, "请稍后重试");
        }
        return resultObjectVO;
    }


    /**
     * 查询10条最近登录的记录
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryListByLatest10(RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            UserLoginHistoryVO queryUserLoginHistoryVO = JSONObject.parseObject(requestVo.getEntityJson(), UserLoginHistoryVO.class);
            Check.notNull(queryUserLoginHistoryVO.getUserMainId(), ResultVO.FAILD, "没有找到用户ID");
            queryUserLoginHistoryVO.setSize(10);
            resultObjectVO.setData(userLoginHistoryService.queryListByCreateDateDesc(queryUserLoginHistoryVO));
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch(Exception e) {
            logger.warn(e.getMessage(),e);
            return ResultObjectVO.fail(ResultVO.FAILD, "请稍后重试");
        }
        return resultObjectVO;
    }


}
