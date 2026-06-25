package com.toucan.shopping.cloud.content.api.single;

import com.toucan.shopping.cloud.content.api.ColumnAreaServiceAPI;
import com.toucan.shopping.modules.column.business.service.ColumnAreaBusinessService;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ColumnAreaServiceSingleImpl implements ColumnAreaServiceAPI {

    @Autowired
    private ColumnAreaBusinessService columnAreaBusinessService;

    @Override
    public ResultObjectVO queryColumnAreaList(RequestJsonVO requestJsonVO) {
        return columnAreaBusinessService.queryColumnAreaList(requestJsonVO);
    }

}
