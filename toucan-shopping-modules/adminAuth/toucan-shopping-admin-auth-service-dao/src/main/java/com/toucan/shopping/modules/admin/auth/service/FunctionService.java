package com.toucan.shopping.modules.admin.auth.service;


import com.toucan.shopping.modules.admin.auth.entity.Function;
import com.toucan.shopping.modules.admin.auth.page.FunctionTreeInfo;
import com.toucan.shopping.modules.admin.auth.vo.FunctionTreeVO;
import com.toucan.shopping.modules.admin.auth.vo.FunctionVO;
import com.toucan.shopping.modules.common.page.PageInfo;

import java.util.List;

public interface FunctionService {

    List<Function> findListByEntity(Function entity);

    List<FunctionTreeVO> queryTreeByAppCode(String appCode);

    int save(Function entity);

    int update(Function entity);

    boolean exists(String name);

    PageInfo<Function> queryListPage(FunctionTreeInfo FunctionTreeInfo);

    int deleteById(Long id);


    List<FunctionVO> queryListByAppCode(String appCode);


    void queryChildren(List<Function> children,Function query);

}
