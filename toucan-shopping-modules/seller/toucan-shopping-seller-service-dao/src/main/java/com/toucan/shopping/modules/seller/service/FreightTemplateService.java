package com.toucan.shopping.modules.seller.service;

import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.seller.entity.FreightTemplate;
import com.toucan.shopping.modules.seller.page.FreightTemplatePageInfo;
import com.toucan.shopping.modules.seller.vo.FreightTemplateVO;

import java.util.List;

/**
 * 运费模板
 */
public interface FreightTemplateService {

    PageInfo<FreightTemplate> queryListPage(FreightTemplatePageInfo queryPageInfo);

    Long queryCount(FreightTemplateVO freightTemplateVO);

    List<FreightTemplate> queryListByVO(FreightTemplateVO query);

    int save(FreightTemplate entity);

    int deleteById(Long templateId);

    int deleteByIdAndUserMainId(Long templateId,Long userMainId);

    FreightTemplate findByIdAndUserMainId(Long id,Long userMainId);

    int update(FreightTemplate entity);

}
