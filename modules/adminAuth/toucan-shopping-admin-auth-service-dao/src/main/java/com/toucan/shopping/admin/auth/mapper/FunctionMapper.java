package com.toucan.shopping.admin.auth.mapper;

import com.toucan.shopping.admin.auth.entity.Function;
import com.toucan.shopping.admin.auth.page.FunctionPageInfo;
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
}
