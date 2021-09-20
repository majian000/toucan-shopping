package com.toucan.shopping.cloud.category.test;

import com.toucan.shopping.cloud.category.app.CloudCategoryServiceApplication;
import com.toucan.shopping.modules.category.entity.Category;
import com.toucan.shopping.modules.category.service.CategoryService;
import com.toucan.shopping.modules.category.vo.CategoryVO;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = CloudCategoryServiceApplication.class)
public class InitCategoryTest {

    private final Logger logger = LoggerFactory.getLogger(getClass());

//    @Autowired
//    private CategoryService categoryService;
//
//    @Autowired
//    private IdGenerator idGenerator;

//    @Test
//    public void initCategory()
//    {
//        List<String> lines = this.readFile("D:/JavaWorkspace/toucan-shopping/resources/京东类别树/根类别.txt");
//        List<Category> categories = new ArrayList<Category>();
//        for(String line:lines)
//        {
//            long sort=1000;
//            CategoryVO rootCategory = new CategoryVO();
//            rootCategory.setName(line.replace(" ",""));
//            rootCategory.setName(rootCategory.getName().trim());
//            rootCategory.setName(rootCategory.getName().replace("\uFEFF",""));
//            rootCategory.setName(rootCategory.getName().replace("\uE601",""));
//            rootCategory.setDeleteStatus((short)0);
//
//            rootCategory.setParentId(-1L);
//            rootCategory.setId(idGenerator.id());
//            rootCategory.setCategorySort(sort);
//            rootCategory.setCreateDate(new Date());
//            rootCategory.setIcon("/static/images/nav9.png");
//            rootCategory.setType(1);
//            rootCategory.setShowStatus(1);
//            if (rootCategory.getName().indexOf("/") != -1) {
//                String[] names = rootCategory.getName().split("/");
//                String hrefs = "";
//                for (int i = 0; i < names.length; i++) {
//                    if (i != 0 && i + 1 != hrefs.length()) {
//                        hrefs += "&toucan_spliter_2021&";
//                    }
//                    hrefs += "www.jd.com";
//                }
//                rootCategory.setHref(hrefs);
//            } else {
//                rootCategory.setHref("www.jd.com");
//            }
//            sort--;
//            categories.add(rootCategory);
//
//
//            String level2CategoryFileName="";
//            if(rootCategory.getName().indexOf("/")!=-1)
//            {
//                level2CategoryFileName = rootCategory.getName().replace("/","_");
//            }else{
//                level2CategoryFileName = rootCategory.getName();
//            }
//
//            List<String> line2s = this.readFile("D:/JavaWorkspace/toucan-shopping/resources/京东类别树/"+level2CategoryFileName+".txt");
//
//            long level2CategoryId = -1L;
//            for(String line2:line2s)
//            {
//                if(!"".equals(line2.replace(" ",""))) {
//                    if (!line2.startsWith(" ")) {  //2级分类
//                        long sort2 = 1000;
//                        CategoryVO childCategory = new CategoryVO();
//                        childCategory.setName(line2.replace(" ", ""));
//                        childCategory.setName(childCategory.getName().trim());
//                        childCategory.setName(childCategory.getName().replace("\uFEFF",""));
//                        childCategory.setName(childCategory.getName().replace("\uE601",""));
//                        childCategory.setDeleteStatus((short) 0);
//
//                        childCategory.setParentId(rootCategory.getId());
//                        childCategory.setId(idGenerator.id());
//                        childCategory.setCategorySort(sort);
//                        childCategory.setCreateDate(new Date());
//                        childCategory.setIcon("/static/images/nav9.png");
//                        childCategory.setType(1);
//                        childCategory.setShowStatus(1);
//                        childCategory.setHref("www.jd.com");
//                        sort2--;
//                        categories.add(childCategory);
//                        level2CategoryId = childCategory.getId();
//                    } else { //三级分类
//
//                        long sort3 = 1000;
//                        CategoryVO childCategory = new CategoryVO();
//                        childCategory.setName(line2.replace(" ", ""));
//                        childCategory.setName(childCategory.getName().trim());
//                        childCategory.setName(childCategory.getName().replace("\uFEFF",""));
//                        childCategory.setName(childCategory.getName().replace("\uE601",""));
//                        childCategory.setDeleteStatus((short) 0);
//
//                        childCategory.setParentId(level2CategoryId);
//                        childCategory.setId(idGenerator.id());
//                        childCategory.setCategorySort(sort);
//                        childCategory.setCreateDate(new Date());
//                        childCategory.setIcon("/static/images/nav9.png");
//                        childCategory.setType(1);
//                        childCategory.setShowStatus(1);
//                        childCategory.setHref("www.jd.com");
//                        sort3--;
//                        categories.add(childCategory);
//                    }
//                }
//            }
//        }
//
//        for(Category category:categories)
//        {
//            logger.info(" id:{} name:{} parentId:{} ",category.getId(),category.getName(),category.getParentId());
//        }
//        Category[] categoryArray = new Category[categories.size()];
//        for(int i=0;i<categories.size();i++)
//        {
//            categoryArray[i] = categories.get(i);
//        }
//        categoryService.saves(categoryArray);
//    }
//
//
//    private List<String> readFile(String path){
//        List<String> lines = new ArrayList<String>();
//        try {
//            File file = new File(path);
//            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
//            String line = null;
//            long sort=1000;
//            while((line = bufferedReader.readLine())!=null)
//            {
//                if(!"".equals(line.replace(" ",""))) {
//                    lines.add(line);
//                }
//            }
//            bufferedReader.close();
//        }catch(Exception e)
//        {
//            e.printStackTrace();
//        }
//        return lines;
//    }


}
