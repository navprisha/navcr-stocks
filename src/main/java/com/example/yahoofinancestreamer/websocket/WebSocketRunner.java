package com.example.yahoofinancestreamer.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

@Component
public class WebSocketRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketRunner.class);
    private static final String YAHOO_FINANCE_URL = "wss://streamer.finance.yahoo.com/";
    private final YahooWebSocketHandler yahooWebSocketHandler;

    public WebSocketRunner(YahooWebSocketHandler yahooWebSocketHandler) {
        this.yahooWebSocketHandler = yahooWebSocketHandler;
    }

    @Override
    public void run(String... args) throws Exception {
        WebSocketClient client = new StandardWebSocketClient();
        logger.info("Connecting to Yahoo Finance WebSocket...");
        client.execute(yahooWebSocketHandler, YAHOO_FINANCE_URL);
        logger.info("WebSocket connection initiated. The application will keep running to listen for messages.");
    }
}
