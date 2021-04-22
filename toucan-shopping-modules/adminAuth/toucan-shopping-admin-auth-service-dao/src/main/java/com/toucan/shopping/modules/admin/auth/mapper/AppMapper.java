package com.toucan.shopping.modules.admin.auth.mapper;

import com.toucan.shopping.modules.admin.auth.entity.App;
import com.toucan.shopping.modules.admin.auth.page.AppPageInfo;
import com.toucan.shopping.modules.admin.auth.vo.AppVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface AppMapper {

    int insert(App app);

    int update(App app);

    List<App> findListByEntity(App app);

    List<AppVO> queryListPage(AppPageInfo appPageInfo);

    Long queryListPageCount(AppPageInfo appPageInfo);

    int deleteById(Long id);
}
