package com.example.yahoofinancestreamer.controller;

import com.example.yahoofinancestreamer.service.SubscriptionService;
import com.example.yahoofinancestreamer.websocket.YahooWebSocketHandler;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/stocks")
public class StockController {

    private final SubscriptionService subscriptionService;
    private final YahooWebSocketHandler webSocketHandler;

    public StockController(SubscriptionService subscriptionService, YahooWebSocketHandler webSocketHandler) {
        this.subscriptionService = subscriptionService;
        this.webSocketHandler = webSocketHandler;
    }

    @GetMapping
    public List<String> getSubscriptions() {
        return subscriptionService.getSubscriptions();
    }

    @PostMapping
    public void addSubscription(@RequestBody Map<String, String> body) {
        String symbol = body.get("symbol");
        subscriptionService.addSubscription(symbol);
        webSocketHandler.subscribe(Collections.singletonList(symbol));
    }

    @DeleteMapping
    public void removeSubscription(@RequestBody Map<String, String> body) {
        String symbol = body.get("symbol");
        subscriptionService.removeSubscription(symbol);
        webSocketHandler.unsubscribe(Collections.singletonList(symbol));
    }
}
