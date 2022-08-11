package com.toucan.shopping.modules.column.controller;


import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.column.entity.HotProduct;
import com.toucan.shopping.modules.column.page.HotProductPageInfo;
import com.toucan.shopping.modules.column.redis.HotProductLockKey;
import com.toucan.shopping.modules.column.service.HotProductService;
import com.toucan.shopping.modules.column.vo.HotProductVO;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.skylark.lock.service.SkylarkLock;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 热门商品
 * @author majian
 */
@RestController
@RequestMapping("/hot/product")
public class HotProductController {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private HotProductService hotProductService;

    @Autowired
    private SkylarkLock skylarkLock;

    @Autowired
    private IdGenerator idGenerator;



    /**
     * 查询列表
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/list/page",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryListPage(@RequestBody RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null)
        {
            logger.info("请求参数为空");
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请重试!");
            return resultObjectVO;
        }
        if(requestJsonVO.getAppCode()==null)
        {
            logger.info("没有找到对象: param:"+ JSONObject.toJSONString(requestJsonVO));
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到对象!");
            return resultObjectVO;
        }
        try {
            HotProductPageInfo queryPageInfo = JSONObject.parseObject(requestJsonVO.getEntityJson(), HotProductPageInfo.class);
            PageInfo<HotProductVO> pageInfo =  hotProductService.queryListPage(queryPageInfo);
            resultObjectVO.setData(pageInfo);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("查询失败!");
        }

        return resultObjectVO;
    }



    @RequestMapping(value="/save",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO save(@RequestBody RequestJsonVO requestJsonVO){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null)
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("没有找到请求对象");
            return resultObjectVO;
        }
        if (StringUtils.isEmpty(requestJsonVO.getAppCode())) {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("没有找到应用编码");
            return resultObjectVO;
        }
        HotProductVO hotProductVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), HotProductVO.class);
        if(StringUtils.isEmpty(hotProductVO.getProductName()))
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("商品名称不能为空");
            return resultObjectVO;
        }
        if(StringUtils.isEmpty(hotProductVO.getAppCode()))
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("所属应用不能为空");
            return resultObjectVO;
        }
        String lockKey = hotProductVO.getAppCode();
        try {
            boolean lockStatus = skylarkLock.lock(HotProductLockKey.getSaveLockKey(lockKey), lockKey);
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请稍后重试");
                return resultObjectVO;
            }

            HotProductVO query = new HotProductVO();
            query.setProductName(hotProductVO.getProductName());
            query.setAppCode(hotProductVO.getAppCode());
            List<HotProductVO> columnVOS = hotProductService.queryList(query);
            if(!CollectionUtils.isEmpty(columnVOS))
            {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("该编码已存在");
                return resultObjectVO;
            }

            hotProductVO.setId(idGenerator.id());
            hotProductVO.setDeleteStatus((short)0);
            hotProductVO.setCreateDate(new Date());
            int ret = hotProductService.save(hotProductVO);
            if(ret<=0)
            {
                logger.warn("保存热门商品失败 requestJson{} id{}",requestJsonVO.getEntityJson(),hotProductVO.getId());
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请稍后重试");
            }
            resultObjectVO.setData(hotProductVO);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }finally{
            skylarkLock.unLock(HotProductLockKey.getSaveLockKey(lockKey), lockKey);
        }
        return resultObjectVO;
    }





    /**
     * 根据ID查询
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/find/id",produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
    @ResponseBody
    public ResultObjectVO findById(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }

        try {
            HotProduct hotProduct = JSONObject.parseObject(requestVo.getEntityJson(),HotProduct.class);
            if(hotProduct.getId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到ID");
                return resultObjectVO;
            }

            //查询是否存在该对象
            hotProduct = hotProductService.findById(hotProduct.getId());
            if(hotProduct==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("不存在!");
                return resultObjectVO;
            }

            resultObjectVO.setData(hotProduct);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }





    @RequestMapping(value="/update",produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional
    public ResultObjectVO update(@RequestBody RequestJsonVO requestJsonVO){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null)
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("没有找到请求对象");
            return resultObjectVO;
        }
        if (StringUtils.isEmpty(requestJsonVO.getAppCode())) {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("没有找到应用编码");
            return resultObjectVO;
        }
        HotProductVO hotProductVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), HotProductVO.class);
        if(StringUtils.isEmpty(hotProductVO.getProductName()))
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("商品名称不能为空");
            return resultObjectVO;
        }
        if(hotProductVO.getId()==null)
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("ID不能为空");
            return resultObjectVO;
        }
        if(StringUtils.isEmpty(hotProductVO.getAppCode()))
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("所属应用不能为空");
            return resultObjectVO;
        }
        String lockKey = hotProductVO.getAppCode();
        try {
            boolean lockStatus = skylarkLock.lock(HotProductLockKey.getUpdateLockKey(lockKey), lockKey);
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请稍后重试");
                return resultObjectVO;
            }

            HotProductVO query = new HotProductVO();
            query.setProductName(hotProductVO.getProductName());
            query.setAppCode(hotProductVO.getAppCode());
            List<HotProductVO> hotProductVOS = hotProductService.queryList(query);
            if(!CollectionUtils.isEmpty(hotProductVOS))
            {
                if(hotProductVOS.get(0).getId().longValue()!=hotProductVO.getId().longValue()) {
                    resultObjectVO.setCode(ResultObjectVO.FAILD);
                    resultObjectVO.setMsg("该商品已存在");
                    return resultObjectVO;
                }
            }

            hotProductVO.setUpdateDate(new Date());
            int ret = hotProductService.update(hotProductVO);
            if(ret<=0)
            {
                logger.warn("修改热门商品失败 requestJson{} id{}",requestJsonVO.getEntityJson(),hotProductVO.getId());
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请稍后重试");
            }

            resultObjectVO.setData(hotProductVO);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }finally{
            skylarkLock.unLock(HotProductLockKey.getUpdateLockKey(lockKey), lockKey);
        }
        return resultObjectVO;
    }





}
