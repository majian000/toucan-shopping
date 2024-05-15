package com.toucan.shopping.modules.content.vo;

import com.toucan.shopping.modules.content.entity.Article;
import com.toucan.shopping.modules.content.entity.ArticleContent;
import lombok.Data;

/**
 * 文章内容
 *
 * @author majian
 */
@Data
public class ArticleContentVO extends ArticleContent {



    private String createAdminName; //创建人姓名
    private String updateAdminName; //修改人姓名

}
