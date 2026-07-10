package com.toucan.shopping.modules.admin.auth.business.service;

import com.alibaba.fastjson.JSONObject;
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
    public ResultObjectVO listPage(RequestJsonVO requestVo) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if (requestVo == null || requestVo.getEntityJson() == null) {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }

        try {
            AdminLoginHistoryPageInfo pageInfo = JSONObject.parseObject(requestVo.getEntityJson(), AdminLoginHistoryPageInfo.class);
            PageInfo<AdminLoginHistoryVO> page = adminLoginHistoryService.queryListPage(pageInfo);
            resultObjectVO.setData(page);
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }

    /**
     * 根据ID查询
     * @param requestVo
     * @return
     */
    public ResultObjectVO findById(RequestJsonVO requestVo) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if (requestVo == null || requestVo.getEntityJson() == null) {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }

        try {
            AdminLoginHistory query = JSONObject.parseObject(requestVo.getEntityJson(), AdminLoginHistory.class);
            if (query.getId() == null) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到ID");
                return resultObjectVO;
            }

            List<AdminLoginHistory> list = adminLoginHistoryService.findListByEntity(query);
            if (CollectionUtils.isEmpty(list)) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("记录不存在!");
                return resultObjectVO;
            }
            resultObjectVO.setData(list);
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }
}
