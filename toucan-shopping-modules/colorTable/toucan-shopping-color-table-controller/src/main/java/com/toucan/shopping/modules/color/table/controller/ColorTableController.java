package com.toucan.shopping.modules.color.table.controller;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.color.table.entity.ColorTable;
import com.toucan.shopping.modules.color.table.page.ColorTablePageInfo;
import com.toucan.shopping.modules.color.table.service.ColorTableService;
import com.toucan.shopping.modules.color.table.vo.ColorTableVO;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 颜色表操作
 */
@RestController
@RequestMapping("/colorTable")
public class ColorTableController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ColorTableService colorTableService;


    @Autowired
    private IdGenerator idGenerator;


    /**
     * 保存颜色表
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/save",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO save(@RequestBody RequestJsonVO requestJsonVO)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestJsonVO==null)
        {
            logger.warn("请求参数为空");
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请重试!");
            return resultObjectVO;
        }
        if(requestJsonVO.getAppCode()==null)
        {
            logger.warn("没有找到对象编码: param:"+ JSONObject.toJSONString(requestJsonVO));
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到对象编码!");
            return resultObjectVO;
        }

        Long entityId = -1L;
        try {
            entityId = idGenerator.id();
            ColorTableVO colorTableVO = JSONObject.parseObject(requestJsonVO.getEntityJson(),ColorTableVO.class);
            if(StringUtils.isEmpty(colorTableVO.getName()))
            {
                logger.warn("名称不能为空: param:"+ JSONObject.toJSONString(requestJsonVO));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("名称不能为空!");
                return resultObjectVO;
            }

            ColorTableVO queryColorTable = new ColorTableVO();
            queryColorTable.setName(colorTableVO.getName());
            List<ColorTableVO> colorTableVOS = colorTableService.queryList(queryColorTable);
            if(CollectionUtils.isNotEmpty(colorTableVOS))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("该名称已存在!");
                return resultObjectVO;
            }


            ColorTable colorTable = new ColorTable();
            BeanUtils.copyProperties(colorTable,colorTableVO);
            colorTable.setId(entityId);
            colorTable.setCreateDate(new Date());
            int row = colorTableService.save(colorTable);
            if (row <= 0) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请重试!");
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
            ColorTableVO entityVO = JSONObject.parseObject(requestVo.getEntityJson(),ColorTableVO.class);
            if(entityVO.getId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到ID");
                return resultObjectVO;
            }

            //查询是否存在该对象
            ColorTableVO query=new ColorTableVO();
            query.setId(entityVO.getId());
            List<ColorTableVO> colorTableVOS = colorTableService.queryList(query);
            if(CollectionUtils.isEmpty(colorTableVOS))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("不存在!");
                return resultObjectVO;
            }
            resultObjectVO.setData(colorTableVOS);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }



    /**
     * 編輯
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/update",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO update(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }

        try {
            ColorTableVO entity = JSONObject.parseObject(requestVo.getEntityJson(),ColorTableVO.class);

            if(StringUtils.isEmpty(entity.getName()))
            {
                logger.info("名称为空 param:"+ JSONObject.toJSONString(entity));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("名称不能为空!");
                return resultObjectVO;
            }
            if(StringUtils.isEmpty(entity.getRgbColor()))
            {
                logger.info("颜色值为空 param:"+ JSONObject.toJSONString(entity));
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("颜色值不能为空!");
                return resultObjectVO;
            }

            if(entity.getId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请传入ID");
                return resultObjectVO;
            }

            ColorTableVO queryColorTable = new ColorTableVO();
            queryColorTable.setName(entity.getName());
            List<ColorTableVO> colorTableVOS = colorTableService.queryList(queryColorTable);
            if(CollectionUtils.isNotEmpty(colorTableVOS))
            {
                for(ColorTableVO colorTableVO:colorTableVOS)
                {
                    if(colorTableVO.getId()!=null&&colorTableVO.getId().intValue()!=entity.getId().intValue())
                    {
                        resultObjectVO.setCode(ResultVO.FAILD);
                        resultObjectVO.setMsg("该名称已存在!");
                        return resultObjectVO;
                    }
                }
            }



            entity.setUpdateDate(new Date());
            int row = colorTableService.update(entity);
            if (row < 1) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请重试!");
                return resultObjectVO;
            }


            resultObjectVO.setData(entity);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }



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
            ColorTablePageInfo queryPageInfo = JSONObject.parseObject(requestJsonVO.getEntityJson(),ColorTablePageInfo.class);
            PageInfo<ColorTableVO> pageInfo =  colorTableService.queryListPage(queryPageInfo);
            resultObjectVO.setData(pageInfo);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("查询失败!");
        }

        return resultObjectVO;
    }



    void saveColorTable(Long id,String value,String extend1)
    {
        ColorTable colorTable = new ColorTable();
        colorTable.setId(id);
        colorTable.setName(value);
        colorTable.setCreateDate(new Date());
        colorTable.setDeleteStatus((short)0);
        String[] rgbColors = extend1.split(",");
        if(rgbColors!=null&&rgbColors.length==3) {
            String rhex = Integer.toHexString(Integer.parseInt(rgbColors[0]));
            String ghex = Integer.toHexString(Integer.parseInt(rgbColors[1]));
            String bhex = Integer.toHexString(Integer.parseInt(rgbColors[2]));
            if("0".equals(rhex))
            {
                rhex="00";
            }
            if("0".equals(ghex))
            {
                ghex="00";
            }
            if("0".equals(bhex))
            {
                bhex="00";
            }
            colorTable.setRgbColor("#" + rhex + ghex + bhex);
            colorTable.setRedColor(rgbColors[0]);
            colorTable.setGreenColor(rgbColors[1]);
            colorTable.setBlueColor(rgbColors[2]);
        }
        colorTableService.save(colorTable);

    }

    @RequestMapping(value="/init",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO init()
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        saveColorTable(idGenerator.id(),"乳白色","255,251,240");
        saveColorTable(idGenerator.id(),"白色","255,255,255");
        saveColorTable(idGenerator.id(),"米白色","238,222,176");
        saveColorTable(idGenerator.id(),"浅灰色","228,228,228");
        saveColorTable(idGenerator.id(),"深灰色","102,102,102");
        saveColorTable(idGenerator.id(),"灰色","128,128,128");
        saveColorTable(idGenerator.id(),"银色","192,192,192");
        saveColorTable(idGenerator.id(),"黑色","0,0,0");
        saveColorTable(idGenerator.id(),"桔红色","255,117,0");
        saveColorTable(idGenerator.id(),"玫红色","223,27,118");
        saveColorTable(idGenerator.id(),"粉红色","255,182,193");
        saveColorTable(idGenerator.id(),"红色","255,0,0");
        saveColorTable(idGenerator.id(),"藕色","238,208,216");
        saveColorTable(idGenerator.id(),"西瓜红","240,86,84");
        saveColorTable(idGenerator.id(),"酒红色","153,0,0");
        saveColorTable(idGenerator.id(),"卡其色","195,176,145");
        saveColorTable(idGenerator.id(),"姜黄色","255,199,115");
        saveColorTable(idGenerator.id(),"明黄色","255,255,1");
        saveColorTable(idGenerator.id(),"杏色","247,238,214");
        saveColorTable(idGenerator.id(),"柠檬黄","255,236,67");
        saveColorTable(idGenerator.id(),"桔色","255,165,0");
        saveColorTable(idGenerator.id(),"浅黄色","250,255,114");
        saveColorTable(idGenerator.id(),"荧光黄","234,255,86");
        saveColorTable(idGenerator.id(),"金色","255,215,0");
        saveColorTable(idGenerator.id(),"香槟色","240,218,171");
        saveColorTable(idGenerator.id(),"黄色","255,255,0");
        saveColorTable(idGenerator.id(),"军绿色","93,118,42");
        saveColorTable(idGenerator.id(),"墨绿色","5,119,72");
        saveColorTable(idGenerator.id(),"浅绿色","152,251,152");
        saveColorTable(idGenerator.id(),"绿色","0,128,0");
        saveColorTable(idGenerator.id(),"翠绿色","10,163,68");
        saveColorTable(idGenerator.id(),"荧光绿","35,250,7");
        saveColorTable(idGenerator.id(),"青色","0,224,158");
        saveColorTable(idGenerator.id(),"天蓝色","68,206,246");
        saveColorTable(idGenerator.id(),"孔雀蓝","0,164,197");
        saveColorTable(idGenerator.id(),"宝蓝色","75,92,196");
        saveColorTable(idGenerator.id(),"浅蓝色","210,240,244");
        saveColorTable(idGenerator.id(),"深蓝色","4,22,144");
        saveColorTable(idGenerator.id(),"湖蓝色","48,223,243");
        saveColorTable(idGenerator.id(),"蓝色","0,0,254");
        saveColorTable(idGenerator.id(),"藏青色","46,78,126");
        saveColorTable(idGenerator.id(),"浅紫色","237,224,230");
        saveColorTable(idGenerator.id(),"深紫色","67,6,83");
        saveColorTable(idGenerator.id(),"紫红色","139,0,98");
        saveColorTable(idGenerator.id(),"紫罗兰","183,172,228");
        saveColorTable(idGenerator.id(),"紫色","128,0,128");
        saveColorTable(idGenerator.id(),"咖啡色","96,57,18");
        saveColorTable(idGenerator.id(),"巧克力色","210,105,30");
        saveColorTable(idGenerator.id(),"栗色","96,40,30");
        saveColorTable(idGenerator.id(),"浅棕色","179,92,68");
        saveColorTable(idGenerator.id(),"深卡其布色","189,183,107");
        saveColorTable(idGenerator.id(),"深棕色","124,75,0");
        saveColorTable(idGenerator.id(),"褐色","133,91,0");
        saveColorTable(idGenerator.id(),"驼色","168,132,98");
        saveColorTable(idGenerator.id(),"花色","");
        saveColorTable(idGenerator.id(),"透明","");


        return resultObjectVO;
    }


    /**
     * 查询列表
     * @param requestJsonVO
     * @return
     */
    @RequestMapping(value="/query/list",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultObjectVO queryList(@RequestBody RequestJsonVO requestJsonVO)
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
            ColorTableVO entityVO = JSONObject.parseObject(requestJsonVO.getEntityJson(),ColorTableVO.class);
            List<ColorTableVO> entityVOS = colorTableService.queryList(entityVO);
            resultObjectVO.setData(entityVOS);
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("查询失败!");
        }

        return resultObjectVO;
    }



    /**
     * 删除指定
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/delete/id",produces = "application/json;charset=UTF-8",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO deleteById(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }

        try {
            ColorTable entity = JSONObject.parseObject(requestVo.getEntityJson(),ColorTable.class);
            if(entity.getId()==null)
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到ID");
                return resultObjectVO;
            }

            //查询是否存在该数据
            ColorTableVO query=new ColorTableVO();
            query.setId(entity.getId());
            List<ColorTableVO> adminList = colorTableService.queryList(query);
            if(CollectionUtils.isEmpty(adminList))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("颜色表不存在!");
                return resultObjectVO;
            }

            int row = colorTableService.deleteById(entity.getId());
            if (row < 1) {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("请重试!");
                return resultObjectVO;
            }



            resultObjectVO.setData(entity);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }


    /**
     * 批量删除
     * @param requestVo
     * @return
     */
    @RequestMapping(value="/delete/ids",produces = "application/json;charset=UTF-8",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO deleteByIds(@RequestBody RequestJsonVO requestVo){
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if(requestVo==null||requestVo.getEntityJson()==null)
        {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("没有找到实体对象");
            return resultObjectVO;
        }

        try {
            List<ColorTable> entitys = JSONObject.parseArray(requestVo.getEntityJson(),ColorTable.class);
            if(CollectionUtils.isEmpty(entitys))
            {
                resultObjectVO.setCode(ResultVO.FAILD);
                resultObjectVO.setMsg("没有找到ID");
                return resultObjectVO;
            }
            List<ResultObjectVO> resultObjectVOList = new ArrayList<ResultObjectVO>();
            for(ColorTable entity:entitys) {
                if(entity.getId()!=null) {
                    ResultObjectVO appResultObjectVO = new ResultObjectVO();
                    appResultObjectVO.setData(entity);

                    int row = colorTableService.deleteById(entity.getId());
                    if (row < 1) {
                        logger.warn("删除颜色表失败，id:{}",entity.getId());
                        resultObjectVO.setCode(ResultVO.FAILD);
                        resultObjectVO.setMsg("请重试!");
                        continue;
                    }

                }
            }
            resultObjectVO.setData(resultObjectVOList);

        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }



}
