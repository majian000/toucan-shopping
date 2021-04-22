package com.toucan.shopping.modules.area.service.impl;

import com.toucan.shopping.modules.area.entity.Banner;
import com.toucan.shopping.modules.area.entity.SecondKillColumn;
import com.toucan.shopping.modules.area.mapper.BannerMapper;
import com.toucan.shopping.modules.area.mapper.SecondKillColumnMapper;
import com.toucan.shopping.modules.area.service.BannerService;
import com.toucan.shopping.modules.area.service.SecondKillColumnService;
import com.toucan.shopping.modules.area.vo.BannerVO;
import com.toucan.shopping.modules.area.vo.SecondKillColumnVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SecondKillColumnServiceImpl implements SecondKillColumnService {

    @Autowired
    private SecondKillColumnMapper secondKillColumnMapper;


    @Override
    public List<SecondKillColumn> queryList(SecondKillColumnVO secondKillColumnVO) {
        return secondKillColumnMapper.queryList(secondKillColumnVO);
    }

    @Override
    public int save(SecondKillColumn entity) {
        return secondKillColumnMapper.insert(entity);
    }

    @Override
    public int deleteById(Long id) {
        return secondKillColumnMapper.deleteById(id);
    }
}
