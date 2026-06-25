package com.toucan.shopping.cloud.user.api.single;

import com.toucan.shopping.cloud.user.api.feign.service.FeignUserLoginHistoryService;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.stereotype.Service;

@Service
public class FeignUserLoginHistoryServiceSingleImpl implements FeignUserLoginHistoryService {

    @Override
    public ResultObjectVO queryListPage(String signHeader, RequestJsonVO requestVo) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        resultObjectVO.setCode(ResultObjectVO.FAILD);
        resultObjectVO.setMsg("请稍后重试!");
        return resultObjectVO;
    }

    @Override
    public ResultObjectVO queryListByLatest10(String signHeader, RequestJsonVO requestVo) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        resultObjectVO.setCode(ResultObjectVO.FAILD);
        resultObjectVO.setMsg("请稍后重试!");
        return resultObjectVO;
    }
}
