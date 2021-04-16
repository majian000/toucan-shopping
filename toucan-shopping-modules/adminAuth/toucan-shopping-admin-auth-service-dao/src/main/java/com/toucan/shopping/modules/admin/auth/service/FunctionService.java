package com.toucan.shopping.modules.admin.auth.service;


import com.toucan.shopping.modules.admin.auth.entity.Function;
import com.toucan.shopping.modules.admin.auth.page.FunctionPageInfo;
import com.toucan.shopping.modules.common.page.PageInfo;

import java.util.List;

public interface FunctionService {

    List<Function> findListByEntity(Function entity);

    int save(Function entity);

    int update(Function entity);

    boolean exists(String name);

    PageInfo<Function> queryListPage(FunctionPageInfo FunctionPageInfo);

    int deleteById(Long id);
}
