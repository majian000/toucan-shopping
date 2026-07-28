package com.toucan.shopping.modules.content.business.service;

import com.alibaba.fastjson2.JSONObject;
import com.toucan.shopping.modules.common.annotation.RequestCheck;
import com.toucan.shopping.modules.common.exception.BusinessValidationException;
import com.toucan.shopping.modules.common.util.Check;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.annotation.RequestCheck;
import com.toucan.shopping.modules.common.exception.BusinessValidationException;
import com.toucan.shopping.modules.common.util.Check;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.annotation.RequestCheck;
import com.toucan.shopping.modules.common.exception.BusinessValidationException;
import com.toucan.shopping.modules.common.util.Check;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.content.entity.BannerArea;
import com.toucan.shopping.modules.content.service.BannerAreaService;
import com.toucan.shopping.modules.content.vo.BannerAreaVO;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BannerAreaBusinessService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private BannerAreaService bannerAreaService;

    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryBannerAreaList(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            BannerAreaVO query = JSONObject.parseObject(requestJsonVO.getEntityJson(), BannerAreaVO.class);

            if (query.getBannerId() == null) {
                query.setBannerId(-1L);
            }

            List<BannerArea> bannerAreas = bannerAreaService.queryList(query);
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
