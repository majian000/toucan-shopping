package com.toucan.shopping.modules.column.service.impl;

import com.toucan.shopping.modules.column.entity.HotProduct;
import com.toucan.shopping.modules.column.mapper.HotProductMapper;
import com.toucan.shopping.modules.column.page.HotProductPageInfo;
import com.toucan.shopping.modules.column.service.HotProductService;
import com.toucan.shopping.modules.column.vo.HotProductVO;
import com.toucan.shopping.modules.common.page.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HotProductServiceImpl implements HotProductService {

    @Autowired
    private HotProductMapper hotProductMapper;

    @Override
    public int saves(List<HotProduct> entitys) {
        return hotProductMapper.inserts(entitys);
    }


    @Override
    public PageInfo<HotProductVO> queryListPage(HotProductPageInfo queryPageInfo) {
        queryPageInfo.setStart(queryPageInfo.getPage()*queryPageInfo.getLimit()-queryPageInfo.getLimit());
        PageInfo<HotProductVO> pageInfo = new PageInfo();
        pageInfo.setList(hotProductMapper.queryListPage(queryPageInfo));
        pageInfo.setTotal(hotProductMapper.queryListPageCount(queryPageInfo));
        return pageInfo;
    }

    @Override
    public int save(HotProduct hotProduct) {
        return hotProductMapper.insert(hotProduct);
    }

    @Override
    public List<HotProductVO> queryList(HotProductVO query) {
        return hotProductMapper.queryList(query);
    }

    @Override
    public HotProduct findById(Long id) {
        return hotProductMapper.findById(id);
    }

    @Override
    public int update(HotProduct hotProduct) {
        return hotProductMapper.update(hotProduct);
    }

    @Override
    public int deleteById(Long id) {
        return hotProductMapper.deleteById(id);
    }

    @Override
    public List<HotProductVO> queryPcIndexHotProducts(HotProductVO hotProductVO) {
        return hotProductMapper.queryPcIndexHotProducts(hotProductVO);
    }
}
