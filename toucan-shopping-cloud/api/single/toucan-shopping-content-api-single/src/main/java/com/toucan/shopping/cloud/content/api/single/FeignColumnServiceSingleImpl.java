package com.toucan.shopping.cloud.content.api.single;

import com.toucan.shopping.cloud.content.api.feign.service.FeignColumnService;
import com.toucan.shopping.modules.column.vo.ColumnVO;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultTypeObjectVO;
import org.springframework.stereotype.Service;

@Service
public class FeignColumnServiceSingleImpl implements FeignColumnService {

    @Override
    public ResultObjectVO queryListPage(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        resultObjectVO.setCode(ResultObjectVO.FAILD);
        resultObjectVO.setMsg("单机模式暂不支持此服务");
        return resultObjectVO;
    }

    @Override
    public ResultObjectVO save(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        resultObjectVO.setCode(ResultObjectVO.FAILD);
        resultObjectVO.setMsg("单机模式暂不支持此服务");
        return resultObjectVO;
    }

    @Override
    public ResultObjectVO queryTreeTableByPid(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        resultObjectVO.setCode(ResultObjectVO.FAILD);
        resultObjectVO.setMsg("单机模式暂不支持此服务");
        return resultObjectVO;
    }

    @Override
    public ResultObjectVO queryColumnTreeByPid(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        resultObjectVO.setCode(ResultObjectVO.FAILD);
        resultObjectVO.setMsg("单机模式暂不支持此服务");
        return resultObjectVO;
    }

    @Override
    public ResultObjectVO deleteById(RequestJsonVO requestVo) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        resultObjectVO.setCode(ResultObjectVO.FAILD);
        resultObjectVO.setMsg("单机模式暂不支持此服务");
        return resultObjectVO;
    }

    @Override
    public ResultObjectVO deleteByIds(RequestJsonVO requestVo) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        resultObjectVO.setCode(ResultObjectVO.FAILD);
        resultObjectVO.setMsg("单机模式暂不支持此服务");
        return resultObjectVO;
    }

    @Override
    public ResultTypeObjectVO<ColumnVO> findById(RequestJsonVO requestVo) {
        ResultTypeObjectVO resultObjectVO = new ResultTypeObjectVO();
        resultObjectVO.setCode(ResultObjectVO.FAILD);
        resultObjectVO.setMsg("单机模式暂不支持此服务");
        return resultObjectVO;
    }

    @Override
    public ResultObjectVO update(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        resultObjectVO.setCode(ResultObjectVO.FAILD);
        resultObjectVO.setMsg("单机模式暂不支持此服务");
        return resultObjectVO;
    }

    @Override
    public ResultObjectVO queryListByPid(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        resultObjectVO.setCode(ResultObjectVO.FAILD);
        resultObjectVO.setMsg("单机模式暂不支持此服务");
        return resultObjectVO;
    }

}
