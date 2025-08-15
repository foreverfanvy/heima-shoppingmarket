package com.hmall.gateway.filters;


import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class MyGlobalFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //登录校验逻辑
        //1. 获取登录凭证
        HttpHeaders headers = exchange.getRequest().getHeaders();
        System.out.printf("请求头信息: %s%n", headers);
        //调用下一个filter，进行放行
        return chain.filter(exchange);
    }


    @Override
    public int getOrder() {
        // LOWEST_PRECEDENCE = 2147483647 的NettyRoutingFilter的优先级，其他只要比这个小就可以了，这里0是合适的
        return 0;
    }
}

