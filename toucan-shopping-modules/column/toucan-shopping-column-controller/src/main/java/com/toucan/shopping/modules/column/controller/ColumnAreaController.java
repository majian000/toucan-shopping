package com.toucan.shopping.modules.column.controller;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.column.entity.ColumnArea;
import com.toucan.shopping.modules.column.service.ColumnAreaService;
import com.toucan.shopping.modules.column.vo.ColumnAreaVO;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 轮播图地区操作
 */
@RestController
@RequestMapping("/columnArea")
public class ColumnAreaController {

    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Autowired
    private ColumnAreaService columnAreaService;

    /**
     * 查询指定栏目下所有地区关联
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value = "/query/list",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO queryColumnAreaList(@RequestBody RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            ColumnAreaVO query = JSONObject.parseObject(requestJsonVO.getEntityJson(), ColumnAreaVO.class);

            if(query.getColumnId()==null)
            {
                query.setColumnId(-1L);
            }

            List<ColumnArea> bannerAreas = columnAreaService.queryList(query);
            if(!CollectionUtils.isEmpty(bannerAreas))
            {
                resultObjectVO.setData(bannerAreas);
            }

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }



}
