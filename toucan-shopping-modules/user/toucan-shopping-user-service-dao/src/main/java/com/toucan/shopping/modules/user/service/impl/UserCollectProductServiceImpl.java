package com.toucan.shopping.modules.user.service.impl;

import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.user.entity.ConsigneeAddress;
import com.toucan.shopping.modules.user.entity.UserCollectProduct;
import com.toucan.shopping.modules.user.mapper.UserCollectProductMapper;
import com.toucan.shopping.modules.user.page.UserCollectProductPageInfo;
import com.toucan.shopping.modules.user.service.UserCollectProductService;
import com.toucan.shopping.modules.user.vo.UserCollectProductVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户收藏商品
 */
@Service
public class UserCollectProductServiceImpl implements UserCollectProductService {

    @Autowired
    private UserCollectProductMapper userCollectProductMapper;

    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Override
    public int save(UserCollectProduct entity) {
        return userCollectProductMapper.insert(entity);
    }



    @Override
    public int deleteById(Long id) {
        return userCollectProductMapper.deleteById(id);
    }

    @Override
    public PageInfo<UserCollectProductVO> queryListPage(UserCollectProductPageInfo pageInfo) {
        PageInfo<UserCollectProductVO> pageResult = new PageInfo();
        pageInfo.setStart(pageInfo.getPage()*pageInfo.getLimit()-pageInfo.getLimit());
        pageResult.setList(userCollectProductMapper.queryListPage(pageInfo));
        pageResult.setTotal(userCollectProductMapper.queryListPageCount(pageInfo));

        pageResult.setSize(pageInfo.getSize());
        pageResult.setLimit(pageInfo.getLimit());
        pageResult.setPage(pageInfo.getPage());
        return pageResult;
    }

    @Override
    public List<UserCollectProduct> findListByEntity(UserCollectProductVO query) {
        return userCollectProductMapper.findListByEntity(query);
    }

    @Override
    public int deleteByIdAndUserMainIdAndAppCode(Long id, Long userMainId, String appCode) {
        return userCollectProductMapper.deleteByIdAndUserMainIdAndAppCode(id,userMainId,appCode);
    }

    @Override
    public int deleteBySkuIdAndUserMainIdAndAppCode(Long skuId, Long userMainId, String appCode) {
        return userCollectProductMapper.deleteBySkuIdAndUserMainIdAndAppCode(skuId,userMainId,appCode);
    }

}
