package com.toucan.shopping.user.web.controller.user;


import com.toucan.shopping.auth.admin.Auth;
import com.toucan.shopping.app.export.entity.AppPageInfo;
import com.toucan.shopping.user.api.feign.service.FeignUserService;
import com.toucan.shopping.user.export.page.UserPageInfo;
import com.toucan.shopping.user.export.vo.UserElasticSearchVO;
import com.toucan.shopping.user.service.UserElasticSearchService;
import com.toucan.shopping.user.vo.SearchAfterPage;
import com.toucan.shopping.common.properties.Toucan;
import com.toucan.shopping.user.web.vo.TableVO;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${toucan.app-code}")
    private String appCode;

    @Autowired
    private Toucan toucan;

    @Autowired
    private FeignUserService feignUserService;

    @Autowired
    private UserElasticSearchService userElasticSearchService;


    @Auth(verifyMethod = Auth.VERIFYMETHOD_USER_CENTER,requestType = Auth.REQUEST_FORM)
    @RequestMapping(value = "/listPage",method = RequestMethod.GET)
    public String page()
    {
        return "pages/user/list.html";
    }




    /**
     * 查询列表
     * @param userPageInfo
     * @return
     */
    @Auth
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    @ResponseBody
    public TableVO listPage(HttpServletRequest request,UserPageInfo userPageInfo)
    {
        TableVO tableVO = new TableVO();
        try {

            UserElasticSearchVO query = new UserElasticSearchVO();
            //userElasticSearchVO.setKeyword(userPageInfo.getKeyword());
            query.setMobilePhone(userPageInfo.getMobilePhone());
            query.setNickName(userPageInfo.getNickName());
            query.setEmail(userPageInfo.getEmail());
            query.setId(userPageInfo.getId());

            if(userPageInfo.getPage()<=1)
            {
                request.getSession().removeAttribute("sortValue");
            }
            if(request.getSession().getAttribute("sortValue")!=null) {
                userPageInfo.setSortValues(new String[]{String.valueOf(request.getSession().getAttribute("sortValue"))});
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
                    if(searchAfterPage.getSortValues()!=null&&searchAfterPage.getSortValues().length>0) {
                        request.getSession().setAttribute("sortValue", searchAfterPage.getSortValues()[0]);
                    }
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



}

