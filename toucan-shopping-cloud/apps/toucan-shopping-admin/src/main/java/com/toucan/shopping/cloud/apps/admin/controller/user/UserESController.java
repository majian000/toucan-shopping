package com.toucan.shopping.cloud.apps.admin.controller.user;


import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.cloud.admin.auth.api.feign.service.FeignFunctionService;
import com.toucan.shopping.cloud.apps.admin.auth.web.controller.base.UIController;
import com.toucan.shopping.cloud.user.api.feign.service.FeignUserService;
import com.toucan.shopping.modules.auth.admin.AdminAuth;
import com.toucan.shopping.modules.common.generator.RequestJsonVOGenerator;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.util.SignUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.layui.vo.TableVO;
import com.toucan.shopping.modules.user.entity.User;
import com.toucan.shopping.modules.user.es.service.UserElasticSearchService;
import com.toucan.shopping.modules.user.es.vo.SearchAfterPage;
import com.toucan.shopping.modules.user.page.UserPageInfo;
import com.toucan.shopping.modules.user.vo.UserElasticSearchVO;
import com.toucan.shopping.modules.user.vo.UserVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/user/es")
public class UserESController  extends UIController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;

    @Autowired
    private FeignUserService feignUserService;

    @Autowired
    private UserElasticSearchService userElasticSearchService;

    @Autowired
    private FeignFunctionService feignFunctionService;

    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/listPage",method = RequestMethod.GET)
    public String page(HttpServletRequest request)
    {
        //初始化工具条按钮、操作按钮
        super.initButtons(request,toucan,"/user/es/listPage",feignFunctionService);
        return "pages/user/es/list.html";
    }




    /**
     * 查询列表
     * @param userPageInfo
     * @return
     */
    @AdminAuth
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    @ResponseBody
    public TableVO list(HttpServletRequest request, UserPageInfo userPageInfo)
    {
        TableVO tableVO = new TableVO();
        try {

            UserElasticSearchVO query = new UserElasticSearchVO();
            //userElasticSearchVO.setKeyword(userPageInfo.getKeyword());
            query.setMobilePhone(userPageInfo.getMobilePhone());
            query.setNickName(userPageInfo.getNickName());
            query.setEmail(userPageInfo.getEmail());
            query.setUserMainId(userPageInfo.getUserMainId());

            if(StringUtils.isNotEmpty(userPageInfo.getSortValuesString()))
            {
                userPageInfo.setSortValues(userPageInfo.getSortValuesString().split(","));
            }

            SearchAfterPage searchAfterPage = userElasticSearchService.queryListForSearchAfter(query,userPageInfo.getLimit(),userPageInfo.getSortValues());
            if(CollectionUtils.isNotEmpty(searchAfterPage.getUserElasticSearchVOS()))
            {
                tableVO.setCount(userElasticSearchService.queryCount(query));
                if(tableVO.getCount()>0) {
                    //将long类型的ID转成字符串,解决前端丢失精度问题
                    for(UserElasticSearchVO userElasticSearchVO:searchAfterPage.getUserElasticSearchVOS())
                    {
                        userElasticSearchVO.setUserId(String.valueOf(userElasticSearchVO.getId()));
                    }
                    tableVO.setData((List) searchAfterPage.getUserElasticSearchVOS());
                    tableVO.setSortValues(searchAfterPage.getSortValues());
                }
            }
        }catch(Exception e)
        {
            tableVO.setMsg("请求失败,请重试");
            tableVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return tableVO;
    }




    /**
     * 查看详情页
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/detail/page/{userMainId}",method = RequestMethod.GET)
    public String detailPage(HttpServletRequest request,@PathVariable String userMainId)
    {
        try {
            List<UserElasticSearchVO> userElasticSearchVOS = userElasticSearchService.queryByUserMainId(Long.parseLong(userMainId));
            if(CollectionUtils.isNotEmpty(userElasticSearchVOS)) {
                request.setAttribute("model", userElasticSearchVOS.get(0));
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);

            request.setAttribute("model",  new UserElasticSearchVO());
        }
        return "pages/user/es/detail.html";
    }



    /**
     * 刷新最新数据到缓存
     * @param request
     * @return
     */
    @AdminAuth(verifyMethod = AdminAuth.VERIFYMETHOD_ADMIN_AUTH,requestType = AdminAuth.REQUEST_FORM)
    @RequestMapping(value = "/flush/{id}",method = RequestMethod.DELETE)
    @ResponseBody
    public ResultObjectVO flushById(HttpServletRequest request, @PathVariable String id)
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        try {
            if(StringUtils.isEmpty(id))
            {
                resultObjectVO.setMsg("请求失败,请传入ID");
                resultObjectVO.setCode(ResultObjectVO.FAILD);
                return resultObjectVO;
            }

            Long userMainId = Long.parseLong(id);
            //从缓存中查询用户对象
            List<UserElasticSearchVO> userElasticSearchVOS = userElasticSearchService.queryByUserMainId(userMainId);
            UserElasticSearchVO userElasticSearchVO = null;
            if(CollectionUtils.isNotEmpty(userElasticSearchVOS))
            {
                userElasticSearchVO = userElasticSearchVOS.get(0);
            }else{ //如果缓存不存在将重新推到缓存中
                userElasticSearchVO = new UserElasticSearchVO();

            }

            UserVO queryUser = new UserVO();
            queryUser.setUserMainId(userMainId);
            RequestJsonVO requestJsonVO = RequestJsonVOGenerator.generator(toucan.getAppCode(),queryUser);
            resultObjectVO = feignUserService.findByUserMainId(requestJsonVO.sign(),requestJsonVO);
            if(resultObjectVO.isSuccess())
            {
                UserVO userVO = (UserVO) resultObjectVO.formatData(UserVO.class);
                BeanUtils.copyProperties(userVO,userElasticSearchVO);
                userElasticSearchService.update(userElasticSearchVO);
            }

        }catch(Exception e)
        {
            resultObjectVO.setMsg("请求失败,请重试");
            resultObjectVO.setCode(TableVO.FAILD);
            logger.warn(e.getMessage(),e);
        }
        return resultObjectVO;
    }


}

