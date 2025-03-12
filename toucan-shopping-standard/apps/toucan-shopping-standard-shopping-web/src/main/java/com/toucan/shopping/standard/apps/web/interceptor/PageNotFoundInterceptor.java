package com.toucan.shopping.standard.apps.web.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class PageNotFoundInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String contentType = request.getContentType();
        if (response.getStatus() == HttpStatus.NOT_FOUND.value()) {
            //只有404才做判断
            contentType = contentType==null ? "" :contentType.toLowerCase();
            if(StringUtils.isNotEmpty(contentType)&&contentType.indexOf("application/json")!=-1){
                ResultObjectVO resultVO = new ResultObjectVO(ResultVO.HTTPCODE_404,"API Not Found");
                responseWrite(response, JSONObject.toJSONString(resultVO));
                return false;
            }
            response.sendRedirect(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath()+"/index");
            return false;
        } else if (response.getStatus() == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
            //只有500才做判断
            contentType = contentType==null ? "" :contentType.toLowerCase();
            if(StringUtils.isNotEmpty(contentType)&&contentType.indexOf("application/json")!=-1){
                ResultObjectVO resultVO = new ResultObjectVO(ResultVO.HTTPCODE_404,"API Error");
                responseWrite(response, JSONObject.toJSONString(resultVO));
                return false;
            }
            response.sendRedirect(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath()+"/index");
            return false;
        }
        return true;
    }


    public void responseWrite(HttpServletResponse response, String content) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(content);
    }
}