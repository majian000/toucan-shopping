package com.toucan.shopping.modules.seller.business.service;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.common.annotation.RequestCheck;
import com.toucan.shopping.modules.common.exception.BusinessValidationException;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.common.util.Check;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import com.toucan.shopping.modules.seller.entity.ShopBanner;
import com.toucan.shopping.modules.seller.page.ShopBannerPageInfo;
import com.toucan.shopping.modules.seller.redis.ShopBannerKey;
import com.toucan.shopping.modules.seller.service.ShopBannerService;
import com.toucan.shopping.modules.seller.vo.ShopBannerVO;
import com.toucan.shopping.modules.skylark.lock.service.SkylarkLock;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ShopBannerBusinessService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ShopBannerService shopBannerService;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private SkylarkLock skylarkLock;

    @RequestCheck(requireEntity = true)
    public ResultObjectVO save(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        Long bannerId = -1L;
        try {
            bannerId = idGenerator.id();

            ShopBannerVO bannerVO = requestJsonVO.formatEntity(ShopBannerVO.class);
            ShopBanner shopBanner = new ShopBanner();
            BeanUtils.copyProperties(shopBanner, bannerVO);
            shopBanner.setId(bannerId);
            shopBanner.setCreateDate(new Date());
            shopBanner.setDeleteStatus((short) 0);
            int row = shopBannerService.save(shopBanner);
            if (row <= 0) {
                return ResultObjectVO.fail(ResultVO.FAILD, "请重试!");
            }

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请重试!");
            logger.warn(e.getMessage(), e);
        }
        return resultObjectVO;
    }

    @RequestCheck(requireEntity = true)
    public ResultObjectVO deleteById(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        ShopBanner shopBanner = JSONObject.parseObject(requestJsonVO.getEntityJson(), ShopBanner.class);

        Check.notNull(shopBanner.getId(), ResultVO.FAILD, "ID不能为空!");

        String shopId = String.valueOf(shopBanner.getShopId());
        try {

            boolean lockStatus = skylarkLock.lock(ShopBannerKey.getDeleteLockKey(shopId), shopId);
            if (!lockStatus) {
                return ResultObjectVO.fail(ResultObjectVO.FAILD, "请稍后重试");
            }


            if (shopBanner.getShopId() == null) {
                //释放锁
                skylarkLock.unLock(ShopBannerKey.getDeleteLockKey(shopId), shopId);

                logger.warn("店铺ID为空 param:" + JSONObject.toJSONString(shopBanner));
                return ResultObjectVO.fail(ResultVO.FAILD, "没有查询到关联店铺!");
            }


            int row = shopBannerService.deleteByIdAndShopId(shopBanner.getId(), shopBanner.getShopId());
            if (row <= 0) {
                //释放锁
                skylarkLock.unLock(ShopBannerKey.getDeleteLockKey(shopId), shopId);

                return ResultObjectVO.fail(ResultVO.FAILD, "请重试!");
            }

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请重试!");
            logger.warn(e.getMessage(), e);
        } finally {
            //释放锁
            skylarkLock.unLock(ShopBannerKey.getDeleteLockKey(shopId), shopId);
        }
        return resultObjectVO;
    }

    @RequestCheck(requireEntity = true)
    public ResultObjectVO deleteByIdForAdmin(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        ShopBanner shopBanner = JSONObject.parseObject(requestJsonVO.getEntityJson(), ShopBanner.class);

        Check.notNull(shopBanner.getId(), ResultVO.FAILD, "ID不能为空!");

        String shopId = String.valueOf(shopBanner.getShopId());
        try {

            boolean lockStatus = skylarkLock.lock(ShopBannerKey.getDeleteLockKey(shopId), shopId);
            if (!lockStatus) {
                return ResultObjectVO.fail(ResultObjectVO.FAILD, "请稍后重试");
            }


            if (shopBanner.getShopId() == null) {
                //释放锁
                skylarkLock.unLock(ShopBannerKey.getDeleteLockKey(shopId), shopId);

                logger.warn("店铺ID为空 param:" + JSONObject.toJSONString(shopBanner));
                return ResultObjectVO.fail(ResultVO.FAILD, "没有查询到关联店铺!");
            }


            int row = shopBannerService.deleteById(shopBanner.getId());
            if (row <= 0) {
                //释放锁
                skylarkLock.unLock(ShopBannerKey.getDeleteLockKey(shopId), shopId);

                return ResultObjectVO.fail(ResultVO.FAILD, "删除失败，请重试!");
            }

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("请重试!");
            logger.warn(e.getMessage(), e);
        } finally {
            //释放锁
            skylarkLock.unLock(ShopBannerKey.getDeleteLockKey(shopId), shopId);
        }
        return resultObjectVO;
    }

    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryListPage(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            ShopBannerPageInfo queryPageInfo = JSONObject.parseObject(requestJsonVO.getEntityJson(), ShopBannerPageInfo.class);
            PageInfo<ShopBannerVO> pageInfo = shopBannerService.queryListPage(queryPageInfo);
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
    public ResultObjectVO findById(RequestJsonVO requestVo) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            ShopBannerVO entity = JSONObject.parseObject(requestVo.getEntityJson(), ShopBannerVO.class);
            Check.notNull(entity.getId(), ResultVO.FAILD, "没有找到ID");

            resultObjectVO.setData(shopBannerService.findById(entity.getId()));

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
    public ResultObjectVO update(RequestJsonVO requestJsonVO) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            ShopBannerVO bannerVO = requestJsonVO.formatEntity(ShopBannerVO.class);
            Check.notNull(bannerVO.getId(), ResultVO.FAILD, "ID不能为空");
            ShopBanner shopBanner = new ShopBanner();
            BeanUtils.copyProperties(shopBanner, bannerVO);
            int row = shopBannerService.update(shopBanner);
            if (row <= 0) {
                return ResultObjectVO.fail(ResultVO.FAILD, "修改失败,请稍后请重试!");
            }

        }catch(BusinessValidationException e){
            return ResultObjectVO.fail(e.getCode(), e.getMessage());
        } catch (Exception e) {
            resultObjectVO.setCode(ResultVO.FAILD);
            resultObjectVO.setMsg("修改失败,请稍后请重试!");
            logger.warn(e.getMessage(), e);
        }
        return resultObjectVO;
    }

    @RequestCheck(requireEntity = true)
    public ResultObjectVO queryIndexList(RequestJsonVO requestVo) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();

        try {
            ShopBannerVO query = JSONObject.parseObject(requestVo.getEntityJson(), ShopBannerVO.class);
            Check.notNull(query.getShopId(), ResultVO.FAILD, "没有找到店铺ID");

            resultObjectVO.setData(shopBannerService.queryIndexList(query));

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
