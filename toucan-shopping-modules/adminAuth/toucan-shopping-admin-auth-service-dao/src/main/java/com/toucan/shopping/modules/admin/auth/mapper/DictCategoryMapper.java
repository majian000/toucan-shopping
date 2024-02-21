package com.toucan.shopping.modules.admin.auth.mapper;

import com.toucan.shopping.modules.admin.auth.entity.DictCategory;
import com.toucan.shopping.modules.admin.auth.page.DictCategoryPageInfo;
import com.toucan.shopping.modules.admin.auth.vo.DictCategoryVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface DictCategoryMapper {


    /**
     * 保存
     * @param entity
     * @return
     */
    int insert(DictCategory entity);

    /**
     * 更新
     * @param entity
     * @return
     */
    int update(DictCategory entity);

    /**
     * 查询列表
     * @param entity
     * @return
     */
    List<DictCategoryVO> findListByEntity(DictCategory entity);

    /**
     * 查询列表页
     * @param pageInfo
     * @return
     */
    List<DictCategory> queryListPage(DictCategoryPageInfo pageInfo);

    /**
     * 查询列表页数量
     * @param pageInfo
     * @return
     */
    Long queryListPageCount(DictCategoryPageInfo pageInfo);

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
    List<DictCategoryVO> queryListByAppCode(String appCode);


    /**
     * 根据应用编码数组查询列表
     * @param appCodes
     * @return
     */
    List<DictCategoryVO> queryListByAppCodeArray(String[] appCodes);


    /**
     * 根据上级节点ID查询
     * @param pid
     * @return
     */
    List<DictCategoryVO> findListByPid(Long pid);

    /**
     * 根据指定ID查询
     * @param id
     * @return
     */
    List<DictCategoryVO> findById(Long id);


    /**
     * 根据指定ID查询
     * @param id
     * @return
     */
    List<DictCategoryVO> findByIdAndAppCode(Long id,String appCode);



    /**
     * 查询全部
     * @return
     */
    List<DictCategoryVO> queryAll();



}
