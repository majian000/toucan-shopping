package com.toucan.shopping.modules.area.service.impl;

import com.toucan.shopping.modules.area.entity.SecondKillColumn;
import com.toucan.shopping.modules.area.entity.SecondKillColumnArea;
import com.toucan.shopping.modules.area.mapper.SecondKillColumnAreaMapper;
import com.toucan.shopping.modules.area.mapper.SecondKillColumnMapper;
import com.toucan.shopping.modules.area.service.SecondKillColumnAreaService;
import com.toucan.shopping.modules.area.service.SecondKillColumnService;
import com.toucan.shopping.modules.area.vo.SecondKillColumnAreaVO;
import com.toucan.shopping.modules.area.vo.SecondKillColumnVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SecondKillColumnAreaServiceImpl implements SecondKillColumnAreaService {

    @Autowired
    private SecondKillColumnAreaMapper secondKillColumnAreaMapper;


    @Override
    public List<SecondKillColumnArea> queryList(SecondKillColumnAreaVO entity) {
        return secondKillColumnAreaMapper.queryList(entity);
    }

    @Override
    public int save(SecondKillColumnArea entity) {
        return secondKillColumnAreaMapper.insert(entity);
    }

    @Override
    public int deleteById(Long id) {
        return secondKillColumnAreaMapper.deleteById(id);
    }
}
