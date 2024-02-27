package com.toucan.shopping.modules.admin.auth.mapper;

import com.toucan.shopping.modules.admin.auth.entity.Dict;
import com.toucan.shopping.modules.admin.auth.page.DictPageInfo;
import com.toucan.shopping.modules.admin.auth.vo.DictVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface DictMapper {


    /**
     * 保存
     * @param entity
     * @return
     */
    int insert(Dict entity);


    /**
     * 更新
     * @param entity
     * @return
     */
    int update(Dict entity);

    /**
     * 查询列表
     * @param entity
     * @return
     */
    List<DictVO> findListByEntity(Dict entity);

    /**
     * 查询列表
     * @param entity
     * @return
     */
    List<DictVO> findListByVO(DictVO entity);

    /**
     * 查询列表页
     * @param pageInfo
     * @return
     */
    List<DictVO> queryListPage(DictPageInfo pageInfo);

    /**
     * 查询列表页数量
     * @param pageInfo
     * @return
     */
    Long queryListPageCount(DictPageInfo pageInfo);

    /**
     * 根据ID删除
     * @param id
     * @return
     */
    int deleteById(Long id);

    /**
     * 根据ID集合删除
     * @param ids
     * @return
     */
    int deleteByIdList(List<Long> ids);

    /**
     * 根据应用查询列表
     * @param appCode
     * @return
     */
    List<DictVO> queryListByAppCode(String appCode);


    /**
     * 根据应用编码数组查询列表
     * @param appCodes
     * @return
     */
    List<DictVO> queryListByAppCodeArray(String[] appCodes);

    /**
     * 查询最大排序
     * @return
     */
    Integer queryMaxSort();

    /**
     * 根据上级节点ID查询
     * @param pid
     * @return
     */
    List<DictVO> findListByPid(Long pid);

    /**
     * 根据指定ID查询
     * @param id
     * @return
     */
    List<DictVO> findById(Long id);


    /**
     * 根据指定ID查询
     * @param id
     * @return
     */
    List<DictVO> findByIdAndAppCode(Long id,String appCode);



    /**
     * 查询全部
     * @return
     */
    List<DictVO> queryAll();

    /**
     * 根据分类ID删除
     * @param categoryId
     * @return
     */
    int deleteByCategoryId(Integer categoryId);


    List<DictVO> queryList(DictVO entity);

    Long queryListCount(DictVO entity);

    Long queryOneChildCountByPid(Long pid,String appCode);

    Long queryOneChildCountByPidAndCategoryId(Long pid,String appCode,Integer categoryId);

}
