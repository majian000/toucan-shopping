package com.toucan.shopping.admin.auth.mapper;

import com.toucan.shopping.admin.auth.entity.App;
import com.toucan.shopping.admin.auth.page.AppPageInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface AppMapper {

    int insert(App app);

    int update(App app);

    List<App> findListByEntity(App app);

    List<App> queryListPage(AppPageInfo appPageInfo);

    Long queryListPageCount(AppPageInfo appPageInfo);

    int deleteById(Long id);
}
