package com.toucan.shopping.modules.content.business.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.common.annotation.RequestCheck;
import com.toucan.shopping.modules.common.exception.BusinessValidationException;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.common.util.Check;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.content.cache.service.BannerRedisService;
import com.toucan.shopping.modules.content.entity.Banner;
import com.toucan.shopping.modules.content.entity.BannerArea;
import com.toucan.shopping.modules.content.page.BannerPageInfo;
import com.toucan.shopping.modules.content.service.BannerAreaService;
import com.toucan.shopping.modules.content.service.BannerService;
import com.toucan.shopping.modules.content.vo.BannerVO;
import com.toucan.shopping.modules.image.upload.service.ImageUploadService;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class BannerBusinessService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private BannerService bannerService;

    @Autowired
    private BannerAreaService bannerAreaService;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private BannerRedisService bannerRedisService;

    @Autowired
    private ImageUploadService imageUploadService;

    @RequestCheck(requireEntity = true)
    public ResultObjectVO save(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        Long bannerId = -1L;
        try {
            bannerId = idGenerator.id();
            BannerVO bannerVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), BannerVO.class);

            Check.isTrue(bannerVO.getAreaCodeArray() != null && bannerVO.getAreaCodeArray().length > 0, ResultVO.FAILD, "关联地区编码为空!");

            Banner banner = new Banner();
            BeanUtils.copyProperties(banner, bannerVO);
            banner.setId(bannerId);
            banner.setCreateDate(new Date());
            int row = bannerService.save(banner);
            if (row <= 0) {
                return ResultObjectVO.fail(ResultVO.FAILD, "请重试!");
            }

            BannerArea[] bannerAreas = new BannerArea[bannerVO.getAreaCodeArray().length];
            for (int i = 0; i < bannerVO.getAreaCodeArray().length; i++) {
                String areaCode = bannerVO.getAreaCodeArray()[i];
                BannerArea bannerArea = new BannerArea();
                bannerArea.setId(idGenerator.id());
                bannerArea.setAreaCode(areaCode);
                bannerArea.setBannerId(banner.getId());
                bannerArea.setCreateDate(new Date());
                bannerArea.setAppCode(banner.getAppCode());
                bannerAreas[i] = bannerArea;
            }
            //保存轮播图与地区关联
            row = bannerAreaService.saves(bannerAreas);
            if (row <= 0) {
                throw new IllegalArgumentException("保存地区轮播图关联失败");
            }
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请重试!");
            logger.warn(e.getMessage(), e);

            //地区与轮播图有一条关联回滚数据
            bannerService.deleteById(bannerId);
            bannerAreaService.deleteByBannerId(bannerId);
        }
        return resultObjectVO;
    }

    @RequestCheck(requireEntity = true)
    public ResultObjectVO flushWebIndexCache(RequestJsonVO requestVo) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            List<BannerVO> banners = JSONObject.parseArray(requestVo.getEntityJson(), BannerVO.class);
            if (CollectionUtils.isEmpty(banners)) {
                return ResultObjectVO.fail(ResultVO.FAILD, "没有找到ID");
            }
            List<ResultObjectVO> resultObjectVOList = new ArrayList<ResultObjectVO>();
            for (BannerVO banner : banners) {
                if (banner.getId() != null) {
                    ResultObjectVO appResultObjectVO = new ResultObjectVO();
                    appResultObjectVO.setData(banner);
                    try {
                        BannerVO query = new BannerVO();
                        query.setId(banner.getId());
                        List<BannerVO> bannerVOS = bannerService.queryList(query);
                        if (CollectionUtils.isNotEmpty(bannerVOS)) {
                            BannerVO bannerVO = bannerVOS.get(0);
                            //查询出所有轮播图地区关联
                            BannerArea queryBannerArea = new BannerArea();
                            queryBannerArea.setBannerId(bannerVO.getId());
                            List<BannerArea> bannerAreas = bannerAreaService.queryList(queryBannerArea);
                            String[] areaCodeArray = new String[bannerAreas.size()];
                            for (int i = 0; i < bannerAreas.size(); i++) {
                                areaCodeArray[i] = bannerAreas.get(i).getAreaCode();
                            }
                            //设置关联地区编码
                            bannerVO.setAreaCodeArray(areaCodeArray);
                            if (bannerVO.getImgPath() != null) {
                                bannerVO.setHttpImgPath(imageUploadService.getImageHttpPrefix() + bannerVO.getImgPath());
                            }
                            //刷新缓存
                            bannerRedisService.flushWebIndexCache(bannerVO);
                        }
                    } catch (Exception e) {
                        logger.warn("刷新轮播图缓存失败，id:{}", banner.getId());
                        logger.warn(e.getMessage(), e);
                        resultObjectVO.setCode(ResultVO.FAILD);
                        resultObjectVO.setMsg("请重试!");
                        continue;
                    }
                }
            }
            resultObjectVO.setData(resultObjectVOList);

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }

    @RequestCheck
    public ResultObjectVO clearWebIndexCache(RequestJsonVO requestVo) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            resultObjectVO.setData(bannerRedisService.clearWebIndexBanner());
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        }catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }

    @RequestCheck(requireEntity = true)
    public ResultObjectVO findById(RequestJsonVO requestVo) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            BannerVO bannerVO = JSONObject.parseObject(requestVo.getEntityJson(), BannerVO.class);
            Check.notNull(bannerVO.getId(), ResultVO.FAILD, "没有找到ID");

            //查询是否存在该对象
            BannerVO query = new BannerVO();
            query.setId(bannerVO.getId());
            List<BannerVO> banners = bannerService.queryList(query);
            if (CollectionUtils.isEmpty(banners)) {
                return ResultObjectVO.fail(ResultVO.FAILD, "不存在!");
            }

            //查询出所有轮播图地区关联
            BannerArea queryBannerArea = new BannerArea();
            queryBannerArea.setBannerId(bannerVO.getId());
            List<BannerArea> bannerAreaVOS = bannerAreaService.queryList(queryBannerArea);
            banners.get(0).setBannerAreas(bannerAreaVOS);
            resultObjectVO.setData(banners);

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }

    @RequestCheck(requireEntity = true)
    public ResultObjectVO update(RequestJsonVO requestVo) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            BannerVO entity = JSONObject.parseObject(requestVo.getEntityJson(), BannerVO.class);

            Check.notEmpty(entity.getTitle(), ResultVO.FAILD, "标题不能为空!");
            Check.notEmpty(entity.getClickPath(), ResultVO.FAILD, "点击连接不能为空!");
            Check.notNull(entity.getId(), ResultVO.FAILD, "请传入ID");

            entity.setUpdateDate(new Date());
            int row = bannerService.update(entity);
            if (row < 1) {
                return ResultObjectVO.fail(ResultVO.FAILD, "请重试!");
            }

            //删除轮播图地区关联
            bannerAreaService.deleteByBannerId(entity.getId());

            BannerArea[] bannerAreas = new BannerArea[entity.getAreaCodeArray().length];
            Banner perBanner = bannerService.findById(entity.getId());
            for (int i = 0; i < entity.getAreaCodeArray().length; i++) {
                String areaCode = entity.getAreaCodeArray()[i];
                BannerArea bannerArea = new BannerArea();
                bannerArea.setId(idGenerator.id());
                bannerArea.setAreaCode(areaCode);
                bannerArea.setBannerId(perBanner.getId());
                bannerArea.setAppCode(perBanner.getAppCode());
                bannerArea.setCreateDate(new Date());
                bannerAreas[i] = bannerArea;
            }
            //保存轮播图与地区关联
            row = bannerAreaService.saves(bannerAreas);
            if (row <= 0) {
                return ResultObjectVO.fail(ResultVO.FAILD, "保存地区轮播图关联失败");
            }

            resultObjectVO.setData(entity);

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }

    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryListPage(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            BannerPageInfo adminPageInfo = JSONObject.parseObject(requestJsonVO.getEntityJson(), BannerPageInfo.class);
            PageInfo<BannerVO> pageInfo = bannerService.queryListPage(adminPageInfo);
            resultObjectVO.setData(pageInfo);
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("查询失败!");
        }

        return resultObjectVO;
    }

    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryList(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            BannerVO bannerVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), BannerVO.class);
            List<BannerVO> bannerVOS = bannerService.queryList(bannerVO);
            if (CollectionUtils.isNotEmpty(bannerVOS)) {
                for (BannerVO bv : bannerVOS) {
                    //查询出所有轮播图地区关联
                    BannerArea queryBannerArea = new BannerArea();
                    queryBannerArea.setBannerId(bannerVO.getId());
                    List<BannerArea> bannerAreas = bannerAreaService.queryList(queryBannerArea);
                    if (CollectionUtils.isNotEmpty(bannerAreas)) {
                        String[] areaCodeArray = new String[bannerAreas.size()];
                        for (int i = 0; i < bannerAreas.size(); i++) {
                            areaCodeArray[i] = bannerAreas.get(i).getAreaCode();
                        }
                        bv.setAreaCodeArray(areaCodeArray);
                        ;
                    }
                }
            }
            resultObjectVO.setData(bannerVOS);
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("查询失败!");
        }

        return resultObjectVO;
    }

    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryIndexList(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            BannerVO bannerVO = JSONObject.parseObject(requestJsonVO.getEntityJson(), BannerVO.class);
            //查询首页轮播图
            List<BannerVO> bannerVOS = bannerService.queryIndexList(bannerVO);
            if (CollectionUtils.isNotEmpty(bannerVOS)) {
                for (BannerVO bv : bannerVOS) {
                    //查询出所有轮播图地区关联
                    BannerArea queryBannerArea = new BannerArea();
                    queryBannerArea.setBannerId(bannerVO.getId());
                    List<BannerArea> bannerAreas = bannerAreaService.queryList(queryBannerArea);
                    if (CollectionUtils.isNotEmpty(bannerAreas)) {
                        String[] areaCodeArray = new String[bannerAreas.size()];
                        for (int i = 0; i < bannerAreas.size(); i++) {
                            areaCodeArray[i] = bannerAreas.get(i).getAreaCode();
                        }
                        bv.setAreaCodeArray(areaCodeArray);
                        ;
                    }
                    bv.setHttpImgPath(imageUploadService.getImageHttpPrefix() + bv.getImgPath());
                }
            }
            resultObjectVO.setData(bannerVOS);
        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("查询失败!");
        }

        return resultObjectVO;
    }

    @RequestCheck(requireEntity = true)
    public ResultObjectVO deleteById(RequestJsonVO requestVo) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            Banner entity = JSONObject.parseObject(requestVo.getEntityJson(), Banner.class);
            Check.notNull(entity.getId(), ResultVO.FAILD, "没有找到ID");

            //查询是否存在该数据
            BannerVO query = new BannerVO();
            query.setId(entity.getId());
            List<BannerVO> adminList = bannerService.queryList(query);
            if (CollectionUtils.isEmpty(adminList)) {
                return ResultObjectVO.fail(ResultVO.FAILD, "轮播图不存在!");
            }

            int row = bannerService.deleteById(entity.getId());
            if (row < 1) {
                return ResultObjectVO.fail(ResultVO.FAILD, "请重试!");
            }

            //删除与地区关联
            bannerAreaService.deleteByBannerId(entity.getId());

            resultObjectVO.setData(entity);

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);

            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }

    @RequestCheck(requireEntity = true)
    public ResultObjectVO deleteByIds(RequestJsonVO requestVo) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            List<Banner> banners = JSONObject.parseArray(requestVo.getEntityJson(), Banner.class);
            if (CollectionUtils.isEmpty(banners)) {
                return ResultObjectVO.fail(ResultVO.FAILD, "没有找到ID");
            }
            List<ResultObjectVO> resultObjectVOList = new ArrayList<ResultObjectVO>();
            for (Banner banner : banners) {
                if (banner.getId() != null) {
                    ResultObjectVO appResultObjectVO = new ResultObjectVO();
                    appResultObjectVO.setData(banner);

                    int row = bannerService.deleteById(banner.getId());
                    if (row < 1) {
                        logger.warn("删除轮播图失败，id:{}", banner.getId());
                        resultObjectVO.setCode(ResultVO.FAILD);
                        resultObjectVO.setMsg("请重试!");
                        continue;
                    }
                    //删除与地区关联
                    bannerAreaService.deleteByBannerId(banner.getId());

                }
            }
            resultObjectVO.setData(resultObjectVOList);

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
