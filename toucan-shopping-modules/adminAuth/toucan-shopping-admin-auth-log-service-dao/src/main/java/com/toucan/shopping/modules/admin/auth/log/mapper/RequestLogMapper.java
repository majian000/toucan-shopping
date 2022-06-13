package com.toucan.shopping.modules.admin.auth.log.mapper;

import com.toucan.shopping.modules.admin.auth.entity.App;
import com.toucan.shopping.modules.admin.auth.log.entity.RequestLog;
import com.toucan.shopping.modules.admin.auth.log.vo.RequestLogVO;
import com.toucan.shopping.modules.admin.auth.page.AppPageInfo;
import com.toucan.shopping.modules.admin.auth.vo.AppVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface RequestLogMapper {


    int inserts(List<RequestLogVO> entitys);

}
