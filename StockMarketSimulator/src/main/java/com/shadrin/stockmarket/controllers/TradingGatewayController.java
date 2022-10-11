package com.shadrin.stockmarket.controllers;

import com.shadrin.stockmarket.dto.OrderDto;
import com.shadrin.stockmarket.services.OrderRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TradingGatewayController {
    private final OrderRegistrationService orderRegistrationService;

    public TradingGatewayController(@Autowired OrderRegistrationService orderRegistrationService) {
        this.orderRegistrationService = orderRegistrationService;
    }

    @GetMapping(UrlTemplates.PING)
    public String ping() {
        return "Akc!";
    }

    @PostMapping(UrlTemplates.ORDER_ADD)
    public String createNewOrder(@RequestBody OrderDto orderDto) {
        var order = orderRegistrationService.addNewOrder(orderDto);
        return order.prettyPrint();
    }

    @PostMapping(UrlTemplates.ORDER_CANCEL)
    public void cancelOrder(@PathVariable Long id) {
        orderRegistrationService.cancelOrder(id);
    }

    @PostMapping(UrlTemplates.SYMBOL_ADD)
    public void createNewSymbol(@PathVariable String symbol) {
        orderRegistrationService.addNewSymbol(symbol);
    }
}
