package com.example.yahoofinancestreamer.mapper;

import com.example.yahoofinancestreamer.dto.TickerDto;
import com.example.yahoofinancestreamer.proto.TickerProto;

public class TickerMapper {

    public static TickerDto toDto(TickerProto.Ticker proto) {
        TickerDto dto = new TickerDto();
        dto.setId(proto.getId());
        dto.setPrice(proto.getPrice());
        dto.setTime(proto.getTime());
        dto.setCurrency(proto.getCurrency());
        dto.setExchange(proto.getExchange());
        dto.setQuoteType(proto.getQuoteType().name());
        dto.setMarketHours(proto.getMarketHours().name());
        dto.setChangePercent(proto.getChangePercent());
        dto.setDayVolume(proto.getDayVolume());
        dto.setDayHigh(proto.getDayHigh());
        dto.setDayLow(proto.getDayLow());
        dto.setChange(proto.getChange());
        dto.setShortName(proto.getShortName());
        dto.setExpireDate(proto.getExpireDate());
        dto.setOpenPrice(proto.getOpenPrice());
        dto.setPreviousClose(proto.getPreviousClose());
        dto.setStrikePrice(proto.getStrikePrice());
        dto.setUnderlyingSymbol(proto.getUnderlyingSymbol());
        dto.setOpenInterest(proto.getOpenInterest());
        dto.setOptionsType(proto.getOptionsType().name());
        dto.setMiniOption(proto.getMiniOption());
        dto.setLastSize(proto.getLastSize());
        dto.setBid(proto.getBid());
        dto.setBidSize(proto.getBidSize());
        dto.setAsk(proto.getAsk());
        dto.setAskSize(proto.getAskSize());
        dto.setPriceHint(proto.getPriceHint());
        dto.setVol_24hr(proto.getVol24Hr());
        dto.setVolAllCurrencies(proto.getVolAllCurrencies());
        dto.setFromcurrency(proto.getFromcurrency());
        dto.setLastMarket(proto.getLastMarket());
        dto.setCirculatingSupply(proto.getCirculatingSupply());
        dto.setMarketcap(proto.getMarketcap());
        return dto;
    }
}
