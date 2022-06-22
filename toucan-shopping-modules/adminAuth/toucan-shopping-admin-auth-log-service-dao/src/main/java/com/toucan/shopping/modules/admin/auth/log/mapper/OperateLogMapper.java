package com.toucan.shopping.modules.admin.auth.log.mapper;

import com.toucan.shopping.modules.admin.auth.log.entity.OperateLog;
import com.toucan.shopping.modules.admin.auth.log.vo.OperateLogChartVO;
import com.toucan.shopping.modules.admin.auth.log.vo.OperateLogPageInfo;
import com.toucan.shopping.modules.admin.auth.log.vo.OperateLogVO;
import com.toucan.shopping.modules.admin.auth.page.AppPageInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;


@Mapper
public interface OperateLogMapper {


    int inserts(List<OperateLogVO> entitys);

    /**
     * 查询指定时间段 指定应用的操作数量
     * @param startDate
     * @param endDate
     * @param appCode
     * @return
     */
    List<OperateLogChartVO> queryOperateLogCountList(Date startDate, Date endDate, String appCode);



    List<OperateLogVO> queryListPage(OperateLogPageInfo pageInfo);

    Long queryListPageCount(OperateLogPageInfo pageInfo);


    List<OperateLog> findListByEntity(OperateLog query);

}
