package com.toucan.shopping.cloud.content.api.single;

import com.toucan.shopping.cloud.content.api.ArticleImageServiceAPI;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.content.business.service.ArticleImageBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArticleImageServiceSingleImpl implements ArticleImageServiceAPI {

    @Autowired
    private ArticleImageBusinessService articleImageBusinessService;

    @Override
    public ResultObjectVO save(RequestJsonVO requestJsonVO) {
        return articleImageBusinessService.save(requestJsonVO);
    }

    @Override
    public ResultObjectVO deleteInvalidData(RequestJsonVO requestJsonVO) {
        return articleImageBusinessService.deleteInvalidData(requestJsonVO);
    }

    @Override
    public ResultObjectVO queryListPage(RequestJsonVO requestJsonVO) {
        return articleImageBusinessService.queryListPage(requestJsonVO);
    }

    @Override
    public ResultObjectVO deleteById(RequestJsonVO requestVo) {
        return articleImageBusinessService.deleteById(requestVo);
    }

    @Override
    public ResultObjectVO deleteByIds(RequestJsonVO requestVo) {
        return articleImageBusinessService.deleteByIds(requestVo);
    }

}
