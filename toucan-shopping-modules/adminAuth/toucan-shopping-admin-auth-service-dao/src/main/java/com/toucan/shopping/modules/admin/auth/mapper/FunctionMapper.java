package com.toucan.shopping.modules.admin.auth.mapper;

import com.toucan.shopping.modules.admin.auth.entity.Function;
import com.toucan.shopping.modules.admin.auth.page.FunctionTreeInfo;
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

    List<FunctionVO> findById(Long id);

    List<Function> findTreeTableByPageInfo(FunctionTreeInfo functionTreeInfo);

    List<FunctionVO> queryListByAdminIdAndAppCode(String adminId, String appCode);
}
