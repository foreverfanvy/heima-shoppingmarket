package com.hmall.api.client;

import com.hmall.api.domain.vo.CartVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.List;

@FeignClient(name = "cart-service")
public interface CartClient {

    @DeleteMapping("/carts")
    void deleteCartItemByIds(@RequestParam("ids") Collection<Long> ids);
    @GetMapping("/carts")
    List<CartVO> queryMyCarts();
}
