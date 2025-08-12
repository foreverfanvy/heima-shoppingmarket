package com.hmall.cart.client;

import com.hmall.cart.domain.dto.ItemDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.Collection;
import java.util.List;

@FeignClient(name = "item-service")
public interface ItemClient {
    @GetMapping("/items/{id}")
    List<ItemDTO> getItemById(@RequestParam Collection<Long> id);
}
