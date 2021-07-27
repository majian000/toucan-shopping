package com.toucan.shopping.modules.common.persistence;


import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.interceptor.*;

import java.util.ArrayList;
import java.util.List;


@Aspect
@Configuration
public class TransactionaConfig {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private TransactionManager transactionManager;

    @Bean
    public TransactionInterceptor txAdvice() {

        logger.info("配置AOP全局事务处理.........");

        //开启事务
        RuleBasedTransactionAttribute writeTransaction = new RuleBasedTransactionAttribute();
        //如果当前没有事务就创建,如果已经存在就加入
        writeTransaction.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        //设置回滚异常类(Exception.class)
        RollbackRuleAttribute rollbackRuleAttribute = new RollbackRuleAttribute(Exception.class);
        List<RollbackRuleAttribute> rollbackRuleAttributeList = new ArrayList<RollbackRuleAttribute>();
        rollbackRuleAttributeList.add(rollbackRuleAttribute);
        writeTransaction.setRollbackRules(rollbackRuleAttributeList);


        //只读事务
        DefaultTransactionAttribute readOnlyTransaction = new DefaultTransactionAttribute();
        readOnlyTransaction.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        readOnlyTransaction.setReadOnly(true);

        NameMatchTransactionAttributeSource source = new NameMatchTransactionAttributeSource();

        source.addTransactionalMethod("save*", writeTransaction);
        source.addTransactionalMethod("update*", writeTransaction);
        source.addTransactionalMethod("edit*", writeTransaction);
        source.addTransactionalMethod("insert*", writeTransaction);
        source.addTransactionalMethod("delete*", writeTransaction);
        source.addTransactionalMethod("remove*", writeTransaction);



        source.addTransactionalMethod("find*", readOnlyTransaction);
        source.addTransactionalMethod("query*", readOnlyTransaction);

        return new TransactionInterceptor(transactionManager, source);
    }

    @Bean
    public Advisor txAdviceAdvisor() {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("execution(* com.toucan.shopping..*ServiceImpl.*(*) )");
        return new DefaultPointcutAdvisor(pointcut, txAdvice());
    }
}
