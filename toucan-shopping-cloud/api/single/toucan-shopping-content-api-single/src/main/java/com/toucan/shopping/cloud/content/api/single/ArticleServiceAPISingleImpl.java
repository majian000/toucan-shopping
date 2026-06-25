package com.toucan.shopping.cloud.content.api.single;

import com.toucan.shopping.cloud.content.api.ArticleServiceAPI;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultTypeObjectVO;
import com.toucan.shopping.modules.content.business.service.ArticleBusinessService;
import com.toucan.shopping.modules.content.vo.ArticleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArticleServiceAPISingleImpl implements ArticleServiceAPI {

    @Autowired
    private ArticleBusinessService articleBusinessService;

    @Override
    public ResultObjectVO queryListPage(RequestJsonVO requestJsonVO) {
        return articleBusinessService.queryListPage(requestJsonVO);
    }

    @Override
    public ResultObjectVO save(RequestJsonVO requestJsonVO) {
        return articleBusinessService.save(requestJsonVO);
    }

    @Override
    public ResultTypeObjectVO<Long> queryMaxSort(RequestJsonVO requestJsonVO) {
        return articleBusinessService.queryMaxSort(requestJsonVO);
    }

    @Override
    public ResultTypeObjectVO<ArticleVO> findById(RequestJsonVO requestJsonVO) {
        return articleBusinessService.findById(requestJsonVO);
    }

    @Override
    public ResultObjectVO update(RequestJsonVO requestJsonVO) {
        return articleBusinessService.update(requestJsonVO);
    }

    @Override
    public ResultObjectVO deleteById(RequestJsonVO requestJsonVO) {
        return articleBusinessService.deleteById(requestJsonVO);
    }

    @Override
    public ResultObjectVO deleteByIds(RequestJsonVO requestJsonVO) {
        return articleBusinessService.deleteByIds(requestJsonVO);
    }

}
