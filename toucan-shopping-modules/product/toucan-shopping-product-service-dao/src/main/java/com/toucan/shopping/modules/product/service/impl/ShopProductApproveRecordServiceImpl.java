package com.toucan.shopping.modules.product.service.impl;

import com.toucan.shopping.modules.product.entity.ShopProductApproveRecord;
import com.toucan.shopping.modules.product.mapper.ShopProductApproveRecordMapper;
import com.toucan.shopping.modules.product.service.ShopProductApproveRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopProductApproveRecordServiceImpl implements ShopProductApproveRecordService {

    @Autowired
    private ShopProductApproveRecordMapper shopProductSpuApproveRecordMapper;

    @Override
    public List<ShopProductApproveRecord> queryAllList(ShopProductApproveRecord queryModel) {
        return shopProductSpuApproveRecordMapper.queryAllList(queryModel);
    }

    @Override
    public int save(ShopProductApproveRecord entity) {
        return shopProductSpuApproveRecordMapper.insert(entity);
    }
}
