package com.toucan.shopping.modules.product.service.impl;

import com.toucan.shopping.modules.product.entity.ShopProductSpuApproveRecord;
import com.toucan.shopping.modules.product.mapper.ShopProductSpuApproveRecordMapper;
import com.toucan.shopping.modules.product.service.ShopProductSpuApproveRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopProductSpuApproveRecordServiceImpl implements ShopProductSpuApproveRecordService {

    @Autowired
    private ShopProductSpuApproveRecordMapper shopProductSpuApproveRecordMapper;

    @Override
    public List<ShopProductSpuApproveRecord> queryAllList(ShopProductSpuApproveRecord queryModel) {
        return shopProductSpuApproveRecordMapper.queryAllList(queryModel);
    }
}
