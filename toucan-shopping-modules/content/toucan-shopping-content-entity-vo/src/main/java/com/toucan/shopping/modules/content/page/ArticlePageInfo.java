package com.toucan.shopping.modules.content.page;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.content.vo.ArticleVO;
import com.toucan.shopping.modules.content.vo.BannerVO;
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
public class ArticlePageInfo extends PageInfo<ArticleVO> {


    // ===============查询条件===================

    private Integer id;


    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date startShowDate; //开始展示时间

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date endShowDate; //结束展示时间


    private Long columnId; //栏目ID

    /**
     * 标题
     */
    private String title;

    /**
     * 作者
     */
    private String author;

    /**
     *seo标题
     */
    private String seoTitle;

    /**
     * seo关键字
     */
    private String seoKeywords;


    private Short showStatus;  //显示状态 0隐藏 1显示 -1全部

    private Short perpetualStatus; //永久 0:否 1:是


    private Long[] idArray; //ID数组

    //==============================================

}
