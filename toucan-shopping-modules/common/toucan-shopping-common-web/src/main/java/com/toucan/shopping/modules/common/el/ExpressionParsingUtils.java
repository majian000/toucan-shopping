package com.toucan.shopping.modules.common.el;


import org.apache.commons.lang3.StringUtils;
import org.springframework.context.expression.MapAccessor;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.*;

/**
 * EL表达式处理
 * @author majian
 * @date 2022-12-22 11:16:09
 */
public class ExpressionParsingUtils {

    public static Boolean expressionParsing(boolean isTrim,String skipExpress, Map map,String expressionPrefix,String expressionSuffix){

        if (StringUtils.isBlank(skipExpress) && map.isEmpty()){
            return false;
        }
        if(isTrim){
            skipExpress = skipExpress.trim();
        }
        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext();

        TemplateParserContext templateParserContext = new TemplateParserContext(expressionPrefix, expressionSuffix);
        MapAccessor propertyAccessor = new MapAccessor();
        context.setVariables(map);
        context.setPropertyAccessors(Arrays.asList(propertyAccessor));
        try {
            SpelExpression expression = (SpelExpression) parser.parseExpression(skipExpress, templateParserContext);
            expression.setEvaluationContext(context);
            return expression.getValue(map, boolean.class);
        } catch (Exception e) {
            throw e;
        }
    }


    public static void main(String[] args)
    {
        Map<String,Object> parms = new HashMap<>();
        parms.put("status","true");
        System.out.println(ExpressionParsingUtils.expressionParsing(true," ${status=='true'}        ",parms,"${","}"));


        List<String> users=new LinkedList<>();
        users.add("a");
        users.add("b");
        users.add("c");
        parms.put("users",users);
        parms.put("u","c");
        System.out.println(ExpressionParsingUtils.expressionParsing(false,"${#users.contains(#u)}",parms,"${","}"));
    }
}
