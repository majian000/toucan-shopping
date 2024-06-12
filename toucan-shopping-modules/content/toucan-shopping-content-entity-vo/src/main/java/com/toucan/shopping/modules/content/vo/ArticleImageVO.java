package com.toucan.shopping.modules.content.vo;

import com.toucan.shopping.modules.content.entity.ArticleContent;
import com.toucan.shopping.modules.content.entity.ArticleImage;
import lombok.Data;

/**
 * 文章内容
 *
 * @author majian
 */
@Data
public class ArticleImageVO extends ArticleImage {



    private String createAdminName; //创建人姓名
    private String updateAdminName; //修改人姓名

}
