package com.toucan.shopping.cloud.content.api.single;

import com.toucan.shopping.cloud.content.api.IndexRecommendColumnServiceAPI;
import com.toucan.shopping.modules.column.business.service.IndexRecommendColumnBusinessService;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IndexRecommendColumnServiceAPISingleImpl implements IndexRecommendColumnServiceAPI {

    @Autowired
    private IndexRecommendColumnBusinessService indexRecommendColumnBusinessService;

    @Override
    public ResultObjectVO queryListPage(RequestJsonVO requestJsonVO) {
        return indexRecommendColumnBusinessService.queryListPage(requestJsonVO);
    }

    @Override
    public ResultObjectVO save(RequestJsonVO requestJsonVO) {
        return indexRecommendColumnBusinessService.save(requestJsonVO);
    }

    @Override
    public ResultObjectVO update(RequestJsonVO requestJsonVO) {
        return indexRecommendColumnBusinessService.update(requestJsonVO);
    }

    @Override
    public ResultObjectVO deleteById(RequestJsonVO requestJsonVO) {
        return indexRecommendColumnBusinessService.deleteById(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryPcIndexColumns(RequestJsonVO requestVo) {
        return indexRecommendColumnBusinessService.queryPcIndexColumns(requestVo);
    }

    @Override
    public ResultObjectVO findById(RequestJsonVO requestVo) {
        return indexRecommendColumnBusinessService.findById(requestVo);
    }

}
