package com.toucan.shopping.modules.product.business.service;

import com.alibaba.fastjson2.JSONObject;
import com.toucan.shopping.modules.common.annotation.RequestCheck;
import com.toucan.shopping.modules.common.exception.BusinessValidationException;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.util.Check;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.product.entity.BrandCategory;
import com.toucan.shopping.modules.product.service.BrandCategoryService;
import com.toucan.shopping.modules.product.vo.BrandCategoryVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandCategoryBusinessService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private BrandCategoryService brandCategoryService;

    @Autowired
    private IdGenerator idGenerator;

    @RequestCheck(requireEntity = true)
    public ResultObjectVO findByBrandId(RequestJsonVO requestVo) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            BrandCategoryVO entity = JSONObject.parseObject(requestVo.getEntityJson(), BrandCategoryVO.class);
            Check.notNull(entity.getBrandId(), ResultVO.FAILD, "没有找到品牌ID");

            //查询是否存在
            BrandCategoryVO query = new BrandCategoryVO();
            query.setBrandId(entity.getBrandId());
            List<BrandCategory> entityList = brandCategoryService.queryList(query);
            resultObjectVO.setData(entityList);

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }

}
