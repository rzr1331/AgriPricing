package parsers.agmart;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import parsers.*;
import parsers.agriplus.AgriPlusParser;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class AgmartCrawlingService {
    public List<CrawlCommodityPriceDto> handleRequest(Long date) {
      HashSet<String>state_ids=new HashSet<String>();
        HashSet<String>city_ids=new HashSet<String>();
        HashMap<String,HashSet<String>> state_city=new HashMap<String,HashSet<String>>();
        HttpClientPool httpClientPool=new HttpClientPool();
        try {
            HttpRequestDto httpRequestDto=buildRequeStates();
            HttpResponseDto responseDto =httpClientPool.executeRequest(httpRequestDto);
            AgmartParser agmartParser=new AgmartParser();
            if(responseDto.getSuccessful())
                state_ids=agmartParser.getState_ids(responseDto.getResponseString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(String state_id:state_ids){
            HttpRequestDto httpRequestDto=buildRequestCity(state_id);
            AgmartParser agmartParser=new AgmartParser();
            try {
                HttpResponseDto responseDto=httpClientPool.executeRequest(httpRequestDto);
                if(responseDto.getSuccessful()){
                    state_city.put(state_id,agmartParser.getCityids(responseDto.getResponseString(),state_id));
                }
            } catch (IOException e) {
                System.out.println("error");
                e.printStackTrace();
            }
        }
        List<HttpRequestDto> httpRequestDtosList = new ArrayList<>();
        ParserCallableTaskExecutorService parserCallableTaskExecutorService =
                new ParserCallableTaskExecutorService();
        List<CrawlCommodityPriceDto> crawlCommodityPriceDtoList =
                null;
        try {
            AgmartParser.setDate(date);
            for(String state_id:state_city.keySet()){
                for (String city_id: state_city.get(state_id)){
                    httpRequestDtosList.add(buildRequest(date,state_id,city_id));
                }
            }
            crawlCommodityPriceDtoList = parserCallableTaskExecutorService.executeTask(httpRequestDtosList,
                    new AgmartParser());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(crawlCommodityPriceDtoList==null){
            return new ArrayList();
        }
        return crawlCommodityPriceDtoList;

    }
    public HttpRequestDto buildRequest(Long date,String state_id,String city_id){
        String url= CommodityPriceSource.AGMART.getUrl();
        String dateStr = DateUtils.format(new SimpleDateFormat("yyyy-MM-dd"), date);
        String payload =String.format("state_id=%s&market_id=%s&date=%s",state_id,city_id,"2021-08-04");
        Map<String, String> headers = HttpHeaderUtils.getApplicationFormURLEncodedHeaders();
        return HttpRequestDto.Builder.httpRequestDto()
                .withRequestType(RequestType.POST)
                .withUrl(url)
                .withHeaders(headers)
                .withPayload(payload)
                .build();
    }
    public HttpRequestDto buildRequestCity(String state_id){
        String url= "https://agmart.in/ajax";
        String payload ="location="+state_id;
        Map<String, String> headers = HttpHeaderUtils.getApplicationFormURLEncodedHeaders();
        return HttpRequestDto.Builder.httpRequestDto()
                .withRequestType(RequestType.POST)
                .withUrl(url)
                .withHeaders(headers)
                .withPayload(payload)
                .build();
    }
    public HttpRequestDto buildRequeStates(){
        String url ="https://agmart.in/commodity-market";
        return HttpRequestDto.Builder.httpRequestDto()
                .withRequestType(RequestType.GET)
                .withUrl(url)
                .withHeaders(Collections.emptyMap())
                .withPayload(null)
                .build();
    }
}
