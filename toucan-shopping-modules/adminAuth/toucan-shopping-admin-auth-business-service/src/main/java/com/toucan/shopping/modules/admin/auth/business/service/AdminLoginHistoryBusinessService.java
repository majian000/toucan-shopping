package com.toucan.shopping.modules.admin.auth.business.service;

import com.alibaba.fastjson2.JSONObject;
import com.toucan.shopping.modules.admin.auth.entity.AdminLoginHistory;
import com.toucan.shopping.modules.admin.auth.page.AdminLoginHistoryPageInfo;
import com.toucan.shopping.modules.admin.auth.service.AdminLoginHistoryService;
import com.toucan.shopping.modules.admin.auth.vo.AdminLoginHistoryVO;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import com.toucan.shopping.modules.common.annotation.RequestCheck;
import com.toucan.shopping.modules.common.exception.BusinessValidationException;
import com.toucan.shopping.modules.common.util.Check;

import java.util.List;

/**
 * 登录历史管理
 */
@Service
public class AdminLoginHistoryBusinessService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private AdminLoginHistoryService adminLoginHistoryService;

    /**
     * 查询列表分页
     * @param requestVo
     * @return
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO listPage(RequestJsonVO requestVo) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            AdminLoginHistoryPageInfo pageInfo = JSONObject.parseObject(requestVo.getEntityJson(), AdminLoginHistoryPageInfo.class);
            PageInfo<AdminLoginHistoryVO> page = adminLoginHistoryService.queryListPage(pageInfo);
            resultObjectVO.setData(page);
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch (Exception e) {
            logger.warn(e.getMessage(), e);
            return ResultObjectVO.fail(ResultVO.FAILD, "请稍后重试");
        }
        return resultObjectVO;
    }

    /**
     * 根据ID查询
     * @param requestVo
     * @return
     */
    @RequestCheck(requireEntity = true)
    public ResultObjectVO findById(RequestJsonVO requestVo) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            AdminLoginHistory query = JSONObject.parseObject(requestVo.getEntityJson(), AdminLoginHistory.class);
            Check.notNull(query.getId(), ResultVO.FAILD, "没有找到ID");

            List<AdminLoginHistory> list = adminLoginHistoryService.findListByEntity(query);
            Check.isTrue(!CollectionUtils.isEmpty(list), ResultVO.FAILD, "记录不存在!");
            resultObjectVO.setData(list);
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch (Exception e) {
            logger.warn(e.getMessage(), e);
            return ResultObjectVO.fail(ResultVO.FAILD, "请稍后重试");
        }
        return resultObjectVO;
    }
}
