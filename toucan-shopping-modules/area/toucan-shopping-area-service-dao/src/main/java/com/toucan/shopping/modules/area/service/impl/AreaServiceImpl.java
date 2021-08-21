package com.toucan.shopping.modules.area.service.impl;

import com.toucan.shopping.modules.area.entity.Area;
import com.toucan.shopping.modules.area.mapper.AreaMapper;
import com.toucan.shopping.modules.area.page.AreaTreeInfo;
import com.toucan.shopping.modules.area.service.AreaService;
import com.toucan.shopping.modules.area.vo.AreaTreeVO;
import com.toucan.shopping.modules.area.vo.AreaVO;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@Service
public class AreaServiceImpl implements AreaService {

    @Autowired
    private AreaMapper areaMapper;


    @Override
    public List<Area> queryList(Area area) {
        return areaMapper.queryList(area);
    }


    @Override
    public Long queryCount(Area area) {
        return areaMapper.queryCount(area);
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
                setChildrenByParentCode(areaVo);
            }
        }
        return areaVoList;
    }


    @Override
    public void setChildrenByParentCode(AreaVO areaVO) throws InvocationTargetException, IllegalAccessException {
        List<Area> childrenList = this.findByParentCode(areaVO.getAppCode(),areaVO.getCode());
        if(CollectionUtils.isNotEmpty(childrenList))
        {
            List<AreaVO> childrenAreaList = new ArrayList<AreaVO>();
            for(Area category:childrenList)
            {
                AreaVO childAreaVo = new AreaVO();
                BeanUtils.copyProperties(childAreaVo,category);
                if(category!=null&&category.getId()!=null) {
                    setChildrenByParentCode(childAreaVo);
                }
                childrenAreaList.add(childAreaVo);
            }
            areaVO.setChildren((List<AreaVO>)childrenAreaList);
        }
    }




    public void setChildren(List<Area> areas, AreaTreeVO currentNode) throws InvocationTargetException, IllegalAccessException {
        for (Area area : areas) {
            //为当前参数的子节点
            if(area.getParentCode().equals(currentNode.getCode()))
            {
                AreaTreeVO areaTreeVO = new AreaTreeVO();
                BeanUtils.copyProperties(areaTreeVO, area);

                if(1==area.getType().shortValue())
                {
                    areaTreeVO.setTitle(area.getProvince());
                    areaTreeVO.setText(area.getProvince());
                }else if(2==area.getType().shortValue())
                {
                    areaTreeVO.setTitle(area.getCity());
                    areaTreeVO.setText(area.getCity());
                }else if(3==area.getType().shortValue())
                {
                    areaTreeVO.setTitle(area.getArea());
                    areaTreeVO.setText(area.getArea());
                }
                areaTreeVO.setChildren(new ArrayList<AreaVO>());
                currentNode.getChildren().add(areaTreeVO);

                //查找当前节点的子节点
                setChildren(areas,areaTreeVO);
            }
        }
    }


    @Override
    public void queryChildren(List<Area> children, Area query) {
        List<Area> areas = areaMapper.findListByPid(query.getId());
        children.addAll(areas);
        for(Area area:areas)
        {
            queryChildren(children,area);
        }
    }



    public boolean existsParent(List<AreaVO> nodes,AreaVO node)
    {
        if(!CollectionUtils.isEmpty(nodes))
        {
            for(AreaVO n:nodes)
            {
                if(node.getParentCode().equals(n.getCode()))
                {
                    return true;
                }
            }

        }
        return false;
    }


    @Override
    public List<AreaVO> findTreeTable(AreaTreeInfo areaTreeInfo) {
        List<AreaVO> nodes = areaMapper.findTreeTableByPageInfo(areaTreeInfo);
        if(!CollectionUtils.isEmpty(nodes))
        {
            for(int i =0 ;i<nodes.size();i++)
            {
                AreaVO node = nodes.get(i);
                if(node.getType()==1)
                {
                    node.setName(node.getProvince());
                }else if(node.getType()==2)
                {
                    node.setName(node.getCity());
                }else if(node.getType()==3)
                {
                    node.setName(node.getArea());
                }
            }
        }
        return nodes;
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
