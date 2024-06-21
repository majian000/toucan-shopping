package com.toucan.shopping.modules.content.page;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.content.vo.ArticleImageVO;
import com.toucan.shopping.modules.content.vo.ArticleVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 列表查询页对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class ArticleImagePageInfo extends PageInfo<ArticleImageVO> {


    // ===============查询条件===================

    private Integer id;



    private Long articleId; //文章ID

    private Short fileDeleteStatus;

    private Long[] idArray; //ID数组

    //==============================================

}
