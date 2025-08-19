package com.example.yahoofinancestreamer.websocket;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class YahooWebSocketHandler extends TextWebSocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(YahooWebSocketHandler.class);

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        logger.info("Connection established with session: {}", session.getId());
        String subscriptionMsg = "{\"subscribe\":[\"TSLA\"]}";
        try {
            session.sendMessage(new TextMessage(subscriptionMsg));
            logger.info("Subscribed to TSLA");
        } catch (java.io.IOException e) {
            logger.error("Error sending subscription message", e);
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        try {
            byte[] decodedPayload = java.util.Base64.getDecoder().decode(message.getPayload());
            com.example.yahoofinancestreamer.proto.TickerProto.Ticker ticker = com.example.yahoofinancestreamer.proto.TickerProto.Ticker.parseFrom(decodedPayload);
            logger.info("Parsed Ticker: id={}, price={}", ticker.getId(), ticker.getPrice());
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
            logger.error("Error parsing protobuf message", e);
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
