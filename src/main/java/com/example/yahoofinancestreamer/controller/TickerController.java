package com.example.yahoofinancestreamer.controller;

import com.example.yahoofinancestreamer.dto.TickerDto;
import com.example.yahoofinancestreamer.mapper.TickerMapper;
import com.example.yahoofinancestreamer.proto.TickerProto;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class TickerController {

    private final SimpMessagingTemplate template;

    public TickerController(SimpMessagingTemplate template) {
        this.template = template;
    }

    public void sendTicker(TickerProto.Ticker ticker) {
        TickerDto tickerDto = TickerMapper.toDto(ticker);
        this.template.convertAndSend("/topic/tickers", tickerDto);
    }
}
