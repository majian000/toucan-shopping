package com.toucan.shopping.cloud.base.gateway.filter;

import com.alibaba.fastjson.JSONObject;
import com.toucan.shopping.modules.common.util.SignUtil;
import com.toucan.shopping.modules.common.vo.RequestJsonVO;
import com.toucan.shopping.modules.common.vo.RequestVO;
import com.toucan.shopping.modules.common.vo.ResultObjectVO;
import com.toucan.shopping.modules.common.vo.ResultVO;
import io.netty.buffer.ByteBufAllocator;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.NettyDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;


/**
 * 签名过滤器
 */
@Component
public class SignFilter implements GlobalFilter, Ordered {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        logger.info(" 校验签名  {} ",exchange.getRequest().getPath());
        try {
            exchange.getResponse().getHeaders().add("Content-Type", "application/json;charset=UTF-8");
            exchange.getResponse().setStatusCode(HttpStatus.OK);

            ServerHttpRequest request = exchange.getRequest();
            String method = exchange.getRequest().getMethodValue();
            if (method == null) {
                logger.warn(" 请求方法为空  {} ", exchange.getRequest().getPath());
                ResultVO resultVO = new ResultObjectVO(ResultVO.FAILD,"method为空");
                DataBuffer bodyDataBuffer = exchange.getResponse().bufferFactory().wrap(JSONObject.toJSONString(resultVO).getBytes(StandardCharsets.UTF_8));
                return exchange.getResponse().writeWith(Mono.just(bodyDataBuffer));
            }
            HttpHeaders httpHeaders = request.getHeaders();
            List<String> signVlaues = httpHeaders.getValuesAsList("toucan-sign-header");
            if (CollectionUtils.isEmpty(signVlaues)) {
                logger.warn(" toucan-sign-header 签名请求头为空 ");
                ResultVO resultVO = new ResultObjectVO(ResultVO.FAILD,"签名请求头为空");
                DataBuffer bodyDataBuffer = exchange.getResponse().bufferFactory().wrap(JSONObject.toJSONString(resultVO).getBytes(StandardCharsets.UTF_8));
                return exchange.getResponse().writeWith(Mono.just(bodyDataBuffer));
            }
            //调用传进来的签名
            String requestSign = signVlaues.get(0);
            if (StringUtils.isEmpty(requestSign)) {
                logger.warn(" toucan-sign-header 签名请求头为空 ");
                ResultVO resultVO = new ResultObjectVO(ResultVO.FAILD,"签名请求头为空");
                DataBuffer bodyDataBuffer = exchange.getResponse().bufferFactory().wrap(JSONObject.toJSONString(resultVO).getBytes(StandardCharsets.UTF_8));
                return exchange.getResponse().writeWith(Mono.just(bodyDataBuffer));
            }

//        if(method.toUpperCase().equals("GET"))
//        {
//            Set<String> paramKeys = request.getQueryParams().keySet();
//            Iterator<String> paramKeyIt = paramKeys.iterator();
//            while(paramKeyIt.hasNext())
//            {
//                String paramKey = paramKeyIt.next();
//                request.getQueryParams().getFirst(paramKey);
//            }
//
//            MultiValueMap<String, String> queryParams =  request.getQueryParams();
//
//        }
            if (method.toUpperCase().equals("POST")) {
                MediaType mediaType = httpHeaders.getContentType();
                //JSON格式
                if (mediaType.toString().toUpperCase().indexOf("APPLICATION/JSON") != -1) {

                    //提取request body部分
                    Flux<DataBuffer> body = request.getBody();
                    CountDownLatch downLatch = new CountDownLatch(1);
                    StringBuilder builder = new StringBuilder();
                    //阻塞读取
                    body.subscribe(dataBuffer -> {
                        try {
                            logger.info(" 开始读取请求体 ");
                            CharBuffer charBuffer = StandardCharsets.UTF_8.decode(dataBuffer.asByteBuffer());
                            DataBufferUtils.release(dataBuffer);
                            builder.append(charBuffer.toString());

                            logger.info(" 结束读取请求体 {} ", builder);
                        }catch(Exception e) {
                            logger.warn(e.getMessage(),e);
                        }finally{
                            //进行-1
                            downLatch.countDown();
                        }
                    });
                    try {
                        //开始阻塞,判断如果不为0
                        downLatch.await();
                    } catch (InterruptedException e) {
                        logger.warn(e.getMessage(),e);
                    }
                    String requestBody = builder.toString();//获取request body
                    logger.info(" 请求体 {}", requestBody);

                    if (StringUtils.isEmpty(requestBody)) {
                        logger.warn(" 请求体为空 ");
                        ResultVO resultVO = new ResultObjectVO(ResultVO.FAILD,"请求体为空");
                        DataBuffer bodyDataBuffer = exchange.getResponse().bufferFactory().wrap(JSONObject.toJSONString(resultVO).getBytes(StandardCharsets.UTF_8));
                        return exchange.getResponse().writeWith(Mono.just(bodyDataBuffer));
                    }

                    RequestJsonVO requestJsonVO = JSONObject.parseObject(requestBody, RequestJsonVO.class);
                    String sign = SignUtil.sign(requestJsonVO.getAppCode(), requestJsonVO.getEntityJson());
                    logger.info("接收到签名为{} 正确签名为 sign {}",requestSign,sign);
                    if(!StringUtils.equals(requestSign,sign))
                    {
                        logger.warn("签名校验失败");
                        ResultVO resultVO = new ResultObjectVO(ResultVO.FAILD,"签名校验失败");
                        DataBuffer bodyDataBuffer = exchange.getResponse().bufferFactory().wrap(JSONObject.toJSONString(resultVO).getBytes(StandardCharsets.UTF_8));
                        return exchange.getResponse().writeWith(Mono.just(bodyDataBuffer));
                    }

                    DataBuffer bodyDataBuffer = convertDataBuffer(requestBody);
                    Flux<DataBuffer> bodyFlux = Flux.just(bodyDataBuffer);

                    //由于request的输入流只能读取一次 所以我们重新构造一份新对象
                    request = new ServerHttpRequestDecorator(request) {
                        @Override
                        public Flux<DataBuffer> getBody() {
                            return bodyFlux;
                        }
                    };
                }

            }
            //将构造的新对象往下层传递
            return chain.filter(exchange.mutate().request(request).build());
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }

        exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
        return exchange.getResponse().setComplete();
    }

    protected DataBuffer convertDataBuffer(String value) {
        byte[] bytes = value.getBytes(StandardCharsets.UTF_8);

        NettyDataBufferFactory nettyDataBufferFactory = new NettyDataBufferFactory(ByteBufAllocator.DEFAULT);
        DataBuffer buffer = nettyDataBufferFactory.allocateBuffer(bytes.length);
        buffer.write(bytes);
        return buffer;
    }


    @Override
    public int getOrder() {
        return -150;
    }
}
