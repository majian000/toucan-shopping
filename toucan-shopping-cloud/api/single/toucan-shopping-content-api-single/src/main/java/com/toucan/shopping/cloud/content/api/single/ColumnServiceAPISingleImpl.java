package com.toucan.shopping.cloud.content.api.single;

import com.toucan.shopping.cloud.content.api.ColumnServiceAPI;
import com.toucan.shopping.modules.column.business.service.ColumnBusinessService;
import com.toucan.shopping.modules.column.vo.ColumnVO;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultTypeObjectVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ColumnServiceAPISingleImpl implements ColumnServiceAPI {

    @Autowired
    private ColumnBusinessService columnBusinessService;

    @Override
    public ResultObjectVO queryListPage(RequestJsonVO requestJsonVO) {
        return columnBusinessService.queryListPage(requestJsonVO);
    }

    @Override
    public ResultObjectVO save(RequestJsonVO requestJsonVO) {
        return columnBusinessService.save(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryTreeTableByPid(RequestJsonVO requestJsonVO) {
        return columnBusinessService.queryTreeTableByPid(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryColumnTreeByPid(RequestJsonVO requestJsonVO) {
        return columnBusinessService.queryColumnTreeByPid(requestJsonVO);
    }

    @Override
    public ResultObjectVO deleteById(RequestJsonVO requestVo) {
        return columnBusinessService.deleteById(requestVo);
    }

    @Override
    public ResultObjectVO deleteByIds(RequestJsonVO requestVo) {
        return columnBusinessService.deleteByIds(requestVo);
    }

    @Override
    public ResultTypeObjectVO<ColumnVO> findById(RequestJsonVO requestVo) {
        return columnBusinessService.findById(requestVo);
    }

    @Override
    public ResultObjectVO update(RequestJsonVO requestJsonVO) {
        return columnBusinessService.update(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryListByPid(RequestJsonVO requestJsonVO) {
        return columnBusinessService.queryListByPid(requestJsonVO);
    }

}
