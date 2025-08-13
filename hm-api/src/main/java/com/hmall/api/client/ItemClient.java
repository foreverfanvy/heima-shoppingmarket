package com.hmall.api.client;


import com.hmall.api.domain.dto.ItemDTO;
import com.hmall.api.domain.dto.OrderDetailDTO;
import com.hmall.api.domain.vo.CartVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;



import java.util.Collection;
import java.util.List;

@FeignClient(name = "item-service")
public interface ItemClient {
    @GetMapping("/items/{id}")
    List<ItemDTO> getItemById(@RequestParam Collection<Long> id);
    @PutMapping("/items/stock/deduct")
    void deductStock(@RequestBody List<OrderDetailDTO> items);
    @GetMapping
    List<ItemDTO> queryItemByIds(@RequestParam("ids") List<Long> ids);
}
