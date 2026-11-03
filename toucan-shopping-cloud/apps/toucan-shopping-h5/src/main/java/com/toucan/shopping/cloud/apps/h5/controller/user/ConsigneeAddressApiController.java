package com.toucan.shopping.cloud.apps.h5.controller.user;

import com.toucan.shopping.cloud.apps.h5.controller.BaseController;
import com.toucan.shopping.cloud.user.api.ConsigneeAddressServiceAPI;
import com.toucan.shopping.modules.auth.user.UserAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.util.UserAuthHeaderUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.user.page.ConsigneeAddressPageInfo;
import com.toucan.shopping.modules.user.vo.ConsigneeAddressVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

/**
 * H5 收货地址
 */
@RestController("h5ConsigneeAddressController")
@RequestMapping("/h5/user/consigneeAddress")
public class ConsigneeAddressApiController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ConsigneeAddressServiceAPI consigneeAddressService;

    /** 新增或更新（id 为空则新增） */
    @UserAuth
    @RequestMapping(value = "/update/save")
    public ResultObjectVO updateOrSave(HttpServletRequest request, @RequestBody ConsigneeAddressVO vo) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        String msg = validate(vo);
        if (msg != null) {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg(msg);
            return resultObjectVO;
        }
        try {
            String userMainId = UserAuthHeaderUtil.getUserMainId(request.getHeader(this.getToucan().getUserAuth().getHttpToucanAuthHeader()));
            vo.setUserMainId(Long.parseLong(userMainId));
            vo.setAppCode(this.getAppCode());
            if (vo.getId() != null) {
                resultObjectVO = consigneeAddressService.update(RequestJsonVOGenerator.generator(this.getAppCode(), vo));
            } else {
                vo.setDeleteStatus((short) 0);
                resultObjectVO = consigneeAddressService.save(RequestJsonVOGenerator.generator(this.getAppCode(), vo));
            }
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }

    @UserAuth
    @RequestMapping("/list")
    public ResultObjectVO list(HttpServletRequest request, @RequestBody ConsigneeAddressPageInfo queryPageInfo) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if (queryPageInfo == null) {
                queryPageInfo = new ConsigneeAddressPageInfo();
            }
            String userMainId = UserAuthHeaderUtil.getUserMainId(request.getHeader(this.getToucan().getUserAuth().getHttpToucanAuthHeader()));
            queryPageInfo.setUserMainId(Long.parseLong(userMainId));
            queryPageInfo.setAppCode(this.getAppCode());
            resultObjectVO = consigneeAddressService.queryListPage(RequestJsonVOGenerator.generator(this.getAppCode(), queryPageInfo));
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("请稍后重试");
        }
        return resultObjectVO;
    }

    @UserAuth
    @RequestMapping(value = "/delete")
    public ResultObjectVO delete(HttpServletRequest request, @RequestBody ConsigneeAddressVO vo) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if (vo == null || vo.getId() == null) {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("删除失败,ID不能为空");
            return resultObjectVO;
        }
        try {
            String userMainId = UserAuthHeaderUtil.getUserMainId(request.getHeader(this.getToucan().getUserAuth().getHttpToucanAuthHeader()));
            vo.setUserMainId(Long.parseLong(userMainId));
            vo.setAppCode(this.getAppCode());
            resultObjectVO = consigneeAddressService.deleteByIdAndUserMainIdAndAppCode(RequestJsonVOGenerator.generator(this.getAppCode(), vo));
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
        return resultObjectVO;
    }

    @UserAuth
    @RequestMapping(value = "/set/default")
    public ResultObjectVO setDefault(HttpServletRequest request, @RequestBody ConsigneeAddressVO vo) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if (vo == null || vo.getId() == null) {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("设置失败,ID不能为空");
            return resultObjectVO;
        }
        try {
            String userMainId = UserAuthHeaderUtil.getUserMainId(request.getHeader(this.getToucan().getUserAuth().getHttpToucanAuthHeader()));
            vo.setUserMainId(Long.parseLong(userMainId));
            vo.setAppCode(this.getAppCode());
            resultObjectVO = consigneeAddressService.setDefaultByIdAndUserMainId(RequestJsonVOGenerator.generator(this.getAppCode(), vo));
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
        return resultObjectVO;
    }

    @UserAuth
    @RequestMapping(value = "/load")
    public ResultObjectVO load(HttpServletRequest request, @RequestBody ConsigneeAddressVO vo) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        if (vo == null || vo.getId() == null) {
            resultObjectVO.setCode(ResultObjectVO.FAILD);
            resultObjectVO.setMsg("加载失败,ID不能为空");
            return resultObjectVO;
        }
        try {
            String userMainId = UserAuthHeaderUtil.getUserMainId(request.getHeader(this.getToucan().getUserAuth().getHttpToucanAuthHeader()));
            vo.setUserMainId(Long.parseLong(userMainId));
            vo.setAppCode(this.getAppCode());
            resultObjectVO = consigneeAddressService.findByIdAndUserMainIdAndAppcode(RequestJsonVOGenerator.generator(this.getAppCode(), vo));
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
        return resultObjectVO;
    }

    /** 默认地址（无默认取最新一条） */
    @UserAuth
    @RequestMapping(value = "/find/default", produces = "application/json;charset=UTF-8")
    public ResultObjectVO findDefault(HttpServletRequest request) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            ConsigneeAddressVO vo = new ConsigneeAddressVO();
            String userMainId = UserAuthHeaderUtil.getUserMainId(request.getHeader(this.getToucan().getUserAuth().getHttpToucanAuthHeader()));
            vo.setUserMainId(Long.parseLong(userMainId));
            vo.setAppCode(this.getAppCode());
            resultObjectVO = consigneeAddressService.findDefaultByUserMainIdAndAppcode(RequestJsonVOGenerator.generator(this.getAppCode(), vo));
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
        return resultObjectVO;
    }

    private String validate(ConsigneeAddressVO vo) {
        if (vo == null) {
            return "提交失败,没有找到收货信息";
        }
        if (StringUtils.isEmpty(vo.getName())) {
            return "收货人不能为空";
        }
        if (StringUtils.isEmpty(vo.getAddress()) || StringUtils.isEmpty(vo.getAddress().replace(" ", ""))) {
            return "详细地址不能为空";
        }
        if (StringUtils.isEmpty(vo.getProvinceCode()) || StringUtils.isEmpty(vo.getProvinceName())) {
            return "省/直辖市不能为空";
        }
        if (StringUtils.isEmpty(vo.getAreaCode()) || StringUtils.isEmpty(vo.getAreaName())) {
            return "市区不能为空";
        }
        return null;
    }
}
