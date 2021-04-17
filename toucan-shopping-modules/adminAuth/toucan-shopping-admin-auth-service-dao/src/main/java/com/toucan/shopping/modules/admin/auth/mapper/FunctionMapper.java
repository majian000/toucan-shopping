package com.toucan.shopping.modules.admin.auth.mapper;

import com.toucan.shopping.modules.admin.auth.entity.Function;
import com.toucan.shopping.modules.admin.auth.page.FunctionPageInfo;
import com.toucan.shopping.modules.admin.auth.vo.FunctionTreeVO;
import com.toucan.shopping.modules.admin.auth.vo.FunctionVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface FunctionMapper {

    int insert(Function function);

    int update(Function function);

    List<Function> findListByEntity(Function function);

    List<Function> queryListPage(FunctionPageInfo FunctionPageInfo);

    Long queryListPageCount(FunctionPageInfo FunctionPageInfo);

    int deleteById(Long id);

    List<FunctionVO> queryListByAppCode(String appCode);
}
