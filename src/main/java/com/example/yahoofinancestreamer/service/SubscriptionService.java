package com.example.yahoofinancestreamer.service;

import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class SubscriptionService {

    private final List<String> subscriptions = new CopyOnWriteArrayList<>();

    @PostConstruct
    public void init() {
        // Initialize with a default set of stock symbols
        subscriptions.addAll(Arrays.asList("TSLA", "AAPL", "GOOGL"));
    }

    public List<String> getSubscriptions() {
        return subscriptions;
    }

    public void addSubscription(String symbol) {
        subscriptions.add(symbol);
    }

    public void removeSubscription(String symbol) {
        subscriptions.remove(symbol);
    }
}
