package com.toucan.shopping.starter.user.sso.controller;


import com.toucan.shopping.modules.common.util.HttpCookieUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController("UserSSOSetCookieController")
@RequestMapping("/api/sso")
public class SetCookieController {


    @RequestMapping(value="/setCookie")
    public void setCookie(String cookies,HttpServletRequest request, HttpServletResponse response)
    {
        String tss_uid = "";
        String tss_lt="";
        if(cookies.indexOf(";")!=-1)
        {
            String[] cookieArray = cookies.split(";");
            for(String cookie:cookieArray)
            {
                if(cookie.indexOf("tss_uid=")!=-1)
                {
                    tss_uid = cookie.substring(cookie.indexOf("=")+1,cookie.length());
                }
                if(cookie.indexOf("tss_lt=")!=-1)
                {
                    tss_lt = cookie.substring(cookie.indexOf("=")+1,cookie.length());
                }
            }
        }
        String userAgent = request.getHeader("User-Agent");
        String sameSite="";
        if(HttpCookieUtil.disallowsSameSiteNone(userAgent))
        {
            sameSite="SameSite=None;";
        }


        response.addHeader("Set-Cookie","tss_uid="+tss_uid+"; Max-Age=2147483647; Path=/;Secure=True;"+sameSite);
        response.addHeader("Set-Cookie","tss_lt="+tss_lt+"; Max-Age=2147483647;  Path=/;Secure=True;"+sameSite);

    }



}
