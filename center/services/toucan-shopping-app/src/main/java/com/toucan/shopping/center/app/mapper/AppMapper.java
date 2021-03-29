package com.toucan.shopping.center.app.mapper;

import com.toucan.shopping.center.app.entity.App;
import com.toucan.shopping.center.app.entity.AppPageInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface AppMapper {

    int insert(App app);

    int update(App app);

    List<App> findListByEntity(App app);

    List<App> queryListPage(AppPageInfo appPageInfo);

    int deleteById(Integer id);
}
