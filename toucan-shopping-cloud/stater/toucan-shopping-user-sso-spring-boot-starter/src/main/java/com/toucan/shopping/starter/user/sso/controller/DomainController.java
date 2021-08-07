package com.toucan.shopping.starter.user.sso.controller;


import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RestController("UserSSODomainController")
@RequestMapping("/api/sso")
public class DomainController {

    @Autowired
    private Toucan toucan;

    @RequestMapping(value="/query/domains")
    @ResponseBody
    public ResultObjectVO setCookie()
    {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        List<String> setCookieUrlList = new ArrayList<String>();
        if(StringUtils.isNotEmpty(toucan.getUserAuth().getSsoSetCookieUrlList())) {
            if (toucan.getUserAuth().getSsoSetCookieUrlList().indexOf(",") != -1) {
                String[] domainArray = toucan.getUserAuth().getSsoSetCookieUrlList().split(",");
                if(domainArray!=null) {
                    for (int i=0;i<domainArray.length;i++) {
                        setCookieUrlList.add(domainArray[i]);
                    }
                }
            } else {
                setCookieUrlList.add(toucan.getUserAuth().getSsoSetCookieUrlList());
            }
        }
        resultObjectVO.setData(setCookieUrlList);
        return resultObjectVO;
    }



}
