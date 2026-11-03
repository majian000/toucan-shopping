package com.toucan.shopping.cloud.apps.h5.controller.category;

import com.toucan.shopping.cloud.apps.h5.service.IndexService;
import com.toucan.shopping.modules.category.vo.CategoryVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * H5 分类导航
 */
@RestController("h5CategoryNavigationController")
@RequestMapping("/h5/category/navigation")
public class CategoryNavigationApiController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IndexService indexService;

    @RequestMapping("/list")
    public ResultObjectVO queryList() {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            List<CategoryVO> categoryVOS = indexService.queryCategorys();
            resultObjectVO.setData(categoryVOS);
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("请求失败");
        }
        return resultObjectVO;
    }
}
