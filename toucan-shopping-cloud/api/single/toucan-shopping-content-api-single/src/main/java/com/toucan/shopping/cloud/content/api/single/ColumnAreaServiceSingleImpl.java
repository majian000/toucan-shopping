package com.toucan.shopping.cloud.content.api.single;

import com.toucan.shopping.cloud.content.api.feign.service.FeignColumnAreaService;
import com.toucan.shopping.modules.column.business.service.ColumnAreaBusinessService;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ColumnAreaServiceSingleImpl implements FeignColumnAreaService {

    @Autowired
    private ColumnAreaBusinessService columnAreaBusinessService;

    @Override
    public ResultObjectVO queryColumnAreaList(String signHeader, RequestJsonVO requestJsonVO) {
        return columnAreaBusinessService.queryColumnAreaList(requestJsonVO);
    }

}
