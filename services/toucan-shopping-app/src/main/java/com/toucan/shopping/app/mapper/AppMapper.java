package com.toucan.shopping.app.mapper;

import com.toucan.shopping.app.entity.App;
import com.toucan.shopping.app.entity.App;
import com.toucan.shopping.app.entity.AppPageInfo;
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
