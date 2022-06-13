package com.toucan.shopping.modules.admin.auth.log.mapper;

import com.toucan.shopping.modules.admin.auth.log.vo.OperateLogVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface OperateLogMapper {


    int inserts(List<OperateLogVO> entitys);

}
