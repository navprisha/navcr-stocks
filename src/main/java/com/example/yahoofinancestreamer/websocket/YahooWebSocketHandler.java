package com.example.yahoofinancestreamer.websocket;

import com.example.yahoofinancestreamer.controller.TickerController;
import com.example.yahoofinancestreamer.service.SubscriptionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
public class YahooWebSocketHandler extends TextWebSocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(YahooWebSocketHandler.class);
    private final SubscriptionService subscriptionService;
    private final TickerController tickerController;
    private WebSocketSession session;
    private final ObjectMapper objectMapper = new ObjectMapper();


    public YahooWebSocketHandler(SubscriptionService subscriptionService, TickerController tickerController) {
        this.subscriptionService = subscriptionService;
        this.tickerController = tickerController;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        this.session = session;
        logger.info("Connection established with session: {}", session.getId());
        subscribe(subscriptionService.getSubscriptions());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        try {
            byte[] decodedPayload = Base64.getDecoder().decode(message.getPayload());
            com.example.yahoofinancestreamer.proto.TickerProto.Ticker ticker = com.example.yahoofinancestreamer.proto.TickerProto.Ticker.parseFrom(decodedPayload);
            tickerController.sendTicker(ticker);
        } catch (Exception e) {
            logger.error("Error processing message: " + message.getPayload(), e);
        }
    }


    public void subscribe(List<String> symbols) {
        if (session != null && session.isOpen()) {
            try {
                String subscriptionMsg = objectMapper.writeValueAsString(Map.of("subscribe", symbols));
                session.sendMessage(new TextMessage(subscriptionMsg));
                logger.info("Subscribed to {}", symbols);
            } catch (IOException e) {
                logger.error("Error sending subscription message", e);
            }
        }
    }

    public void unsubscribe(List<String> symbols) {
        if (session != null && session.isOpen()) {
            try {
                String unsubscriptionMsg = objectMapper.writeValueAsString(Map.of("unsubscribe", symbols));
                session.sendMessage(new TextMessage(unsubscriptionMsg));
                logger.info("Unsubscribed from {}", symbols);
            } catch (IOException e) {
                logger.error("Error sending unsubscription message", e);
            }
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        logger.error("Transport error", exception);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) throws Exception {
        logger.info("Connection closed with session: {}, status: {}", session.getId(), status);
    }
}
