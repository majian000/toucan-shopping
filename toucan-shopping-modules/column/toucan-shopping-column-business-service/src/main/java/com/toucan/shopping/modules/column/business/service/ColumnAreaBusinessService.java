package com.toucan.shopping.modules.column.business.service;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.column.entity.ColumnArea;
import com.toucan.shopping.modules.column.service.ColumnAreaService;
import com.toucan.shopping.modules.column.vo.ColumnAreaVO;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.skylark.lock.service.SkylarkLock;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ColumnAreaBusinessService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ColumnAreaService columnAreaService;

    @Autowired
    private SkylarkLock skylarkLock;

    public ResultObjectVO queryColumnAreaList(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            ColumnAreaVO query = JSONObject.parseObject(requestJsonVO.getEntityJson(), ColumnAreaVO.class);

            if (query.getColumnId() == null) {
                query.setColumnId(-1L);
            }

            List<ColumnArea> bannerAreas = columnAreaService.queryList(query);
            if (!CollectionUtils.isEmpty(bannerAreas)) {
                resultObjectVO.setData(bannerAreas);
            }

        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }

}
