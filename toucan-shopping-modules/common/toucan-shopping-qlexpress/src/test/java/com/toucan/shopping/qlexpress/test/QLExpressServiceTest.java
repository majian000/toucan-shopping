package com.toucan.shopping.qlexpress.test;

import com.ql.util.express.DefaultContext;
import com.toucan.shopping.modules.qlexpress.service.QLExpressService;
import com.toucan.shopping.modules.qlexpress.service.impl.QLExpressServiceImpl;
import org.junit.Test;

public class QLExpressServiceTest {


    @Test
    public void execute() throws Exception {
        QLExpressService qlExpressService=new QLExpressServiceImpl();
        DefaultContext<String, Object> context = new DefaultContext<String, Object>();
        context.put("a", 1);
        context.put("b", 2);
        System.out.println(qlExpressService.execute("a+b",context));
    }

}
