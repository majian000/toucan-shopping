package com.toucan.shopping.cloud.apps.seller.web.service;

import javax.servlet.http.HttpServletRequest;

/**
 * 省市区静态文件生成
 */
public interface ProvinceCityAreaGeneratorService {

    void generateFile(HttpServletRequest httpServletRequest, String filePath) throws Exception;

}
