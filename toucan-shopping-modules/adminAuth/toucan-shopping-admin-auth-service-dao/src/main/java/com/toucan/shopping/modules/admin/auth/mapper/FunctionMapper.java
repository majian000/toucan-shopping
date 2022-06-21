package com.toucan.shopping.modules.admin.auth.mapper;

import com.toucan.shopping.modules.admin.auth.entity.Function;
import com.toucan.shopping.modules.admin.auth.page.FunctionTreeInfo;
import com.toucan.shopping.modules.admin.auth.vo.FunctionTreeVO;
import com.toucan.shopping.modules.admin.auth.vo.FunctionVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface FunctionMapper {

    /**
     * 保存
     * @param function
     * @return
     */
    int insert(Function function);

    /**
     * 更新
     * @param function
     * @return
     */
    int update(Function function);

    /**
     * 查询列表
     * @param function
     * @return
     */
    List<Function> findListByEntity(Function function);

    /**
     * 查询列表
     * @param function
     * @return
     */
    List<Function> findListByEntityFieldLike(Function function);
    /**
     * 查询列表页
     * @param FunctionTreeInfo
     * @return
     */
    List<Function> queryListPage(FunctionTreeInfo FunctionTreeInfo);

    /**
     * 查询列表页数量
     * @param FunctionTreeInfo
     * @return
     */
    Long queryListPageCount(FunctionTreeInfo FunctionTreeInfo);

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
    List<FunctionVO> queryListByAppCode(String appCode);

    List<FunctionVO> findListByPid(Long pid);


    List<FunctionTreeVO> findFunctionTreeListByPid(Long pid);

    List<FunctionVO> findListByPidAndAppCode(Long pid,String appCode);

    Long queryOneLevelChildrenCountByIdAndAppCode(Long pid,String appCode);

    List<FunctionVO> findById(Long id);

    List<Function> findTreeTableByPageInfo(FunctionTreeInfo functionTreeInfo);

    List<FunctionVO> queryListByRoleIdArray(String[] roleIds);

    List<FunctionVO> queryListByRoleIdArrayAndParentId(String[] roleIds,String parentId);

    List<FunctionVO> queryListByUrlsAndAppCodes(List<String> urls, List<String> appCodes);

}
