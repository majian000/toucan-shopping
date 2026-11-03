package com.toucan.shopping.cloud.apps.h5.controller.index;

import com.toucan.shopping.cloud.apps.h5.service.IndexService;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * H5 首页控制器
 */
@RestController("h5IndexController")
@RequestMapping("/h5/index")
public class IndexApiController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IndexService indexService;

    @RequestMapping("/banners")
    public ResultObjectVO banners() {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            resultObjectVO.setData(indexService.queryBanners());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("请求失败");
        }
        return resultObjectVO;
    }

    @RequestMapping("/categorys")
    public ResultObjectVO categorys() {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            resultObjectVO.setData(indexService.queryCategorys());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("请求失败");
        }
        return resultObjectVO;
    }

    @RequestMapping("/navigation")
    public ResultObjectVO navigation() {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            resultObjectVO.setData(indexService.queryNavigationCategorys());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("请求失败");
        }
        return resultObjectVO;
    }

    @RequestMapping("/hot_product")
    public ResultObjectVO hotProduct() {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            resultObjectVO.setData(indexService.queryHotProduces());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("请求失败");
        }
        return resultObjectVO;
    }
}
