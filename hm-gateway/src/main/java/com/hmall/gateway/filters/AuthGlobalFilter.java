package com.hmall.gateway.filters;

import com.hmall.common.utils.CollUtils;
import com.hmall.gateway.config.AuthProperties;
import com.hmall.gateway.util.JwtTool;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(AuthProperties.class)
public class AuthGlobalFilter implements GlobalFilter, Ordered {
    private final AuthProperties authProperties;
    private final JwtTool jwtTool;
    private final AntPathMatcher matcher = new AntPathMatcher();

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //1.获取request对象
        ServerHttpRequest request = exchange.getRequest();
        //2.判断是不是需要进行拦截的
        if (isExclude(request.getPath().toString())) {
            return chain.filter(exchange);
        }
        //3.获取请求头中的token
        String token = null;
        List<String> heads = request.getHeaders().get("authorization");
        if (!CollUtils.isEmpty(heads)) {
            token = heads.get(0);
        }
        //4.校验解析Token
        Long userId = null;
        try {
            userId = jwtTool.parseToken(token);
        } catch (Exception e) {
            //抛出异常,会出现500的异常，但是不好理解，所以我们选择设定状态码的方式
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            //拦截终止
            return response.setComplete();
        }
        //5.保存到请求头中，像后面的微服务进行传递
        System.out.println("用户ID: " + userId);
        //6.放行
        return chain.filter(exchange);
    }

    private boolean isExclude(String path) {
        if (authProperties == null || authProperties.getExcludePaths() == null) {
            System.out.println("AuthProperties or excludePaths is null");
            return false;
        }
        for(String pattern : authProperties.getExcludePaths()){
            if(matcher.match(pattern,path)){
                return true;
            }
        }
        return false;
    }
}
