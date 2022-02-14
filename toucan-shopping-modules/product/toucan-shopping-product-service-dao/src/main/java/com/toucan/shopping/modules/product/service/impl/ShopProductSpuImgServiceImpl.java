package com.toucan.shopping.modules.product.service.impl;

import com.toucan.shopping.modules.product.entity.ShopProductSpuApproveRecord;
import com.toucan.shopping.modules.product.entity.ShopProductSpuImg;
import com.toucan.shopping.modules.product.mapper.ShopProductSpuImgMapper;
import com.toucan.shopping.modules.product.service.ShopProductSpuApproveRecordService;
import com.toucan.shopping.modules.product.service.ShopProductSpuImgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopProductSpuImgServiceImpl implements ShopProductSpuImgService {

    @Autowired
    private ShopProductSpuImgMapper shopProductSpuImgMapper;

    @Override
    public List<ShopProductSpuImg> queryAllList(ShopProductSpuImg queryModel) {
        return shopProductSpuImgMapper.queryAllList(queryModel);
    }
}
