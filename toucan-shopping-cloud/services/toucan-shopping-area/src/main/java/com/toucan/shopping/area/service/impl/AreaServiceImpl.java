package com.toucan.shopping.area.service.impl;

import com.toucan.shopping.area.entity.Area;
import com.toucan.shopping.area.mapper.AreaMapper;
import com.toucan.shopping.area.service.AreaService;
import com.toucan.shopping.area.vo.AreaVO;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AreaServiceImpl implements AreaService {

    @Autowired
    private AreaMapper areaMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public List<Area> queryList(Area area) {
        return areaMapper.queryList(area);
    }



    @Transactional
    @Override
    public int save(Area area) {
        return areaMapper.insert(area);
    }

    @Override
    public int update(Area category) {
        return areaMapper.update(category);
    }

    @Override
    public Area queryById(Long id) {
        return areaMapper.queryById(id);
    }

    @Transactional
    @Override
    public int deleteById(String appCode,Long id) {
        return areaMapper.deleteById(appCode,id);
    }

    @Override
    public List<Area> findByParentCode(String appCode,String parentCode) {
        return areaMapper.queryByParentCode(appCode,parentCode);
    }

    /**
     * 拿到指定应用的地区树
     * @param appCode
     */
    public List<AreaVO> queryTree(String appCode) throws Exception {
        List<Area> areaList =this.findByParentCode(appCode,"-1");
        List<AreaVO> areaVoList=new ArrayList<AreaVO>();
        if(CollectionUtils.isNotEmpty(areaList))
        {
            for(Area area:areaList)
            {
                AreaVO areaVo = new AreaVO();
                BeanUtils.copyProperties(areaVo,area);
                areaVoList.add(areaVo);
                setChildrenByParentId(areaVo);
            }
        }
        return areaVoList;
    }


    @Override
    public void setChildrenByParentId(AreaVO areaVO) throws InvocationTargetException, IllegalAccessException {
        List<Area> childrenList = this.findByParentCode(areaVO.getAppCode(),areaVO.getCode());
        if(CollectionUtils.isNotEmpty(childrenList))
        {
            List<AreaVO> childrenCategoryVoList = new ArrayList<AreaVO>();
            for(Area category:childrenList)
            {
                AreaVO childAreaVo = new AreaVO();
                BeanUtils.copyProperties(childAreaVo,category);
                if(category!=null&&category.getId()!=null) {
                    setChildrenByParentId(childAreaVo);
                }
                childrenCategoryVoList.add(childAreaVo);
            }
            areaVO.setChildren(childrenCategoryVoList);
        }
    }

    @Override
    public void deleteChildrenByParentCode(String appCode,String parentCode) {
        List<Area> areaList = this.findByParentCode(appCode,parentCode);
        if(CollectionUtils.isNotEmpty(areaList))
        {
            for(Area area:areaList)
            {
                if(area!=null&&area.getId()!=null) {
                    deleteChildrenByParentCode(appCode,area.getCode());
                    this.deleteById(appCode,area.getId());
                }
            }
        }
    }

}
