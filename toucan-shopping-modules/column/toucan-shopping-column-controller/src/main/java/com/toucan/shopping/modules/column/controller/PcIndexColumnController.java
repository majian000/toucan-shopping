package com.toucan.shopping.modules.column.controller;


import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.column.entity.ColumnBanner;
import com.toucan.shopping.modules.column.page.ColumnPageInfo;
import com.toucan.shopping.modules.column.redis.ColumnLockKey;
import com.toucan.shopping.modules.column.service.ColumnBannerService;
import com.toucan.shopping.modules.column.service.ColumnService;
import com.toucan.shopping.modules.column.vo.ColumnBannerVO;
import com.toucan.shopping.modules.column.vo.ColumnVO;
import com.toucan.shopping.modules.column.vo.PcIndexColumnVO;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.skylark.lock.service.SkylarkLock;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * 用户端栏目操作
 */
@RestController
@RequestMapping("/column/pc/index")
public class PcIndexColumnController {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ColumnService columnService;

    @Autowired
    private ColumnBannerService columnBannerService;


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
            ColumnPageInfo queryPageInfo = JSONObject.parseObject(requestJsonVO.getEntityJson(), ColumnPageInfo.class);
            PageInfo<ColumnVO> pageInfo =  columnService.queryListPage(queryPageInfo);
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
        PcIndexColumnVO pcIndexColumnVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), PcIndexColumnVO.class);
        if(StringUtils.isEmpty(pcIndexColumnVO.getTitle()))
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("栏目标题不能为空");
            return resultObjectVO;
        }
        if(StringUtils.isEmpty(pcIndexColumnVO.getColumnTypeCode()))
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("栏目类型编码不能为空");
            return resultObjectVO;
        }
        if(StringUtils.isEmpty(pcIndexColumnVO.getAppCode()))
        {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("所属应用不能为空");
            return resultObjectVO;
        }
        String lockKey = pcIndexColumnVO.getAppCode()+"_"+pcIndexColumnVO.getColumnTypeCode();
        try {
            boolean lockStatus = skylarkLock.lock(ColumnLockKey.getSaveLockKey(lockKey), lockKey);
            if (!lockStatus) {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("请稍后重试");
                return resultObjectVO;
            }

            ColumnVO query = new ColumnVO();
            query.setTitle(pcIndexColumnVO.getTitle());
            query.setColumnTypeCode(pcIndexColumnVO.getColumnTypeCode());
            query.setAppCode(pcIndexColumnVO.getAppCode());
            List<ColumnVO> columnVOS = columnService.queryList(query);
            if(!CollectionUtils.isEmpty(columnVOS))
            {
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                resultObjectVO.setMsg("该栏目已存在");
                return resultObjectVO;
            }

            pcIndexColumnVO.setId(idGenerator.id());
            pcIndexColumnVO.setDeleteStatus((short)0);
            pcIndexColumnVO.setCreateDate(new Date());
            int ret = columnService.save(pcIndexColumnVO);
            if(ret<=0)
            {
                logger.warn("保存栏目失败 requestJson{} id{}",requestJsonVO.getEntityJson(),pcIndexColumnVO.getId());
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请稍后重试");
            }


            List<ColumnBanner> columnBanners = new LinkedList<>();
            List<ColumnBannerVO> columnLeftBannerVOS = pcIndexColumnVO.getColumnLeftBannerVOS();
            //左侧顶部轮播图
            if(!CollectionUtils.isEmpty(columnLeftBannerVOS))
            {
                for(ColumnBannerVO columnBannerVO:columnLeftBannerVOS)
                {
                    ColumnBanner columnBanner = new ColumnBanner();
                    BeanUtils.copyProperties(columnBanner,columnBannerVO);
                    columnBanner.setId(idGenerator.id());
                    columnBanner.setColumnId(pcIndexColumnVO.getId());
                    columnBanner.setPosition(2);
                    columnBanner.setCreateDate(new Date());
                    columnBanner.setCreateAdminId(pcIndexColumnVO.getCreateAdminId());
                    columnBanner.setAppCode(pcIndexColumnVO.getAppCode());
                    columnBanner.setBannerSort(0);
                    columnBanners.add(columnBanner);
                }
            }

            //右侧顶部图片预览
            ColumnBanner rightTopBanner = new ColumnBanner();
            BeanUtils.copyProperties(rightTopBanner,pcIndexColumnVO.getRightTopBanner());
            rightTopBanner.setId(idGenerator.id());
            rightTopBanner.setColumnId(pcIndexColumnVO.getId());
            rightTopBanner.setPosition(3);
            rightTopBanner.setCreateDate(new Date());
            rightTopBanner.setCreateAdminId(pcIndexColumnVO.getCreateAdminId());
            rightTopBanner.setAppCode(pcIndexColumnVO.getAppCode());
            rightTopBanner.setBannerSort(0);
            columnBanners.add(rightTopBanner);


            //右侧底部图片预览
            ColumnBanner rightBottomBanner = new ColumnBanner();
            BeanUtils.copyProperties(rightBottomBanner,pcIndexColumnVO.getRightBottomBanner());
            rightBottomBanner.setId(idGenerator.id());
            rightBottomBanner.setColumnId(pcIndexColumnVO.getId());
            rightBottomBanner.setPosition(3);
            rightBottomBanner.setCreateDate(new Date());
            rightBottomBanner.setCreateAdminId(pcIndexColumnVO.getCreateAdminId());
            rightBottomBanner.setAppCode(pcIndexColumnVO.getAppCode());
            rightBottomBanner.setBannerSort(0);
            columnBanners.add(rightBottomBanner);

            //保存栏目轮播图
            columnBannerService.saves(columnBanners);






            resultObjectVO.setData(pcIndexColumnVO);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }finally{
            skylarkLock.unLock(ColumnLockKey.getSaveLockKey(lockKey), lockKey);
        }
        return resultObjectVO;
    }




    /**
     * 根据ID删除
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/delete/id",produces = "application/json;charset=UTF-8",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO deleteById(@RequestBody RequestJsonVO requestJsonVO)
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
            logger.info("没有找到应用编码: param:"+ JSONObject.toJSONString(requestJsonVO));
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到应用编码!");
            return resultObjectVO;
        }

        try {
            PcIndexColumnVO pcIndexColumnVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), PcIndexColumnVO.class);

            if(pcIndexColumnVO.getId()==null)
            {
                logger.info("ID为空 param:"+ JSONObject.toJSONString(pcIndexColumnVO));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("ID不能为空!");
                return resultObjectVO;
            }


            int ret = columnService.deleteById(pcIndexColumnVO.getId());
            if(ret<=0)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("不存在该栏目!");
                return resultObjectVO;
            }


        }catch(Exception e)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请重试!");
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }



}
