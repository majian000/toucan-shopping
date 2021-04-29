package com.toucan.shopping.modules.admin.auth.mapper;

import com.toucan.shopping.modules.admin.auth.entity.Orgnazition;
import com.toucan.shopping.modules.admin.auth.page.OrgnazitionTreeInfo;
import com.toucan.shopping.modules.admin.auth.vo.OrgnazitionVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface OrgnazitionMapper {

    /**
     * 保存
     * @param entity
     * @return
     */
    int insert(Orgnazition entity);

    /**
     * 更新
     * @param entity
     * @return
     */
    int update(Orgnazition entity);

    /**
     * 查询列表
     * @param entity
     * @return
     */
    List<Orgnazition> findListByEntity(Orgnazition entity);

    /**
     * 查询列表页
     * @param orgnazitionTreeInfo
     * @return
     */
    List<Orgnazition> queryListPage(OrgnazitionTreeInfo orgnazitionTreeInfo);

    /**
     * 查询列表页数量
     * @param orgnazitionTreeInfo
     * @return
     */
    Long queryListPageCount(OrgnazitionTreeInfo orgnazitionTreeInfo);

    /**
     * 根据ID删除
     * @param id
     * @return
     */
    int deleteById(Long id);

    /**
     * 根据应用查询列表
     * @param appCode
     * @return
     */
    List<OrgnazitionVO> queryListByAppCode(String appCode);

    List<OrgnazitionVO> findListByPid(Long pid);

    List<OrgnazitionVO> findById(Long id);

    List<Orgnazition> findTreeTableByPageInfo(OrgnazitionTreeInfo OrgnazitionTreeInfo);



}
