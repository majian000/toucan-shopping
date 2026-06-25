package com.toucan.shopping.cloud.content.api.single;

import com.toucan.shopping.cloud.content.api.ColumnTypeServiceAPI;
import com.toucan.shopping.modules.column.business.service.ColumnTypeBusinessService;
import com.toucan.shopping.modules.column.vo.ColumnTypeVO;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultTypeObjectVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ColumnTypeServiceSingleImpl implements ColumnTypeServiceAPI {

    @Autowired
    private ColumnTypeBusinessService columnTypeBusinessService;

    @Override
    public ResultObjectVO save(RequestJsonVO requestJsonVO) {
        return columnTypeBusinessService.save(requestJsonVO);
    }

    @Override
    public ResultObjectVO deleteById(RequestJsonVO requestJsonVO) {
        return columnTypeBusinessService.deleteById(requestJsonVO);
    }

    @Override
    public ResultObjectVO deleteByIds(RequestJsonVO requestVo) {
        return columnTypeBusinessService.deleteByIds(requestVo);
    }

    @Override
    public ResultObjectVO update(RequestJsonVO requestVo) {
        return columnTypeBusinessService.update(requestVo);
    }

    @Override
    public ResultObjectVO queryListPage(RequestJsonVO requestJsonVO) {
        return columnTypeBusinessService.queryListPage(requestJsonVO);
    }

    @Override
    public ResultObjectVO findById(RequestJsonVO requestJsonVO) {
        return columnTypeBusinessService.findById(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryList(RequestJsonVO requestJsonVO) {
        return columnTypeBusinessService.queryList(requestJsonVO);
    }

    @Override
    public ResultTypeObjectVO<ColumnTypeVO> findOneByCode(RequestJsonVO requestVo) {
        return columnTypeBusinessService.findOneByCode(requestVo);
    }

}
