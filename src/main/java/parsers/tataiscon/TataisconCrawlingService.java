package parsers.tataiscon;

import parsers.*;
import parsers.enam.EnamParser;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TataisconCrawlingService {
    public List<CrawlCommodityPriceDto> handleRequest() {
        HttpRequestDto httpRequestDto = buildRequest();
        HttpClientPool httpClientPool = new HttpClientPool();
        try {
            HttpResponseDto responseDto = httpClientPool.executeRequest(httpRequestDto);
            if (!responseDto.getSuccessful()) {
                System.out.println("error while getting response for enam reqeuset [{}] , response [{}]" +
                        httpRequestDto + responseDto);
                return new ArrayList<>();
            }
            TataisconParser tataisconParser = new TataisconParser();
            Set<String>states=tataisconParser.getStates(responseDto.getResponseString());
            for(String state:states){
                httpRequestDto=buildRequest(state);
                responseDto=httpClientPool.executeRequest(httpRequestDto);
                if (!responseDto.getSuccessful()) {
                    System.out.println("error while getting response for enam reqeuset [{}] , response [{}]" +
                            httpRequestDto + responseDto);
                    continue;
                }
                Set<String>districts=tataisconParser.getDistricts(responseDto.getResponseString());
                for(String district:districts){
                    httpRequestDto=buildRequest(state,district);
                    responseDto=httpClientPool.executeRequest(httpRequestDto);
                    if (!responseDto.getSuccessful()) {
                        System.out.println("error while getting response for enam reqeuset [{}] , response [{}]" +
                                httpRequestDto + responseDto);
                        continue;
                    }
                    tataisconParser.ParseCommodityPrice(responseDto.getResponseString(),state,district);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("error while getting data  for domain {}" + CommodityPriceSource.TATAISCON);
        }
        return new ArrayList<>();
    }
    public HttpRequestDto buildRequest(String state,String district){
        String url = "https://tatatiscon.co.in/services/service.php?pagename=rcp_data";
        String payload=String.format("rcp_state=%s&rcp_district=%s&rcp_product=TATA+TISCON+SUPER+DUCTILE+REBAR",state,district);
        Map<String, String> headers = HttpHeaderUtils.getApplicationFormURLEncodedHeaders();
        return HttpRequestDto.Builder.httpRequestDto()
                .withRequestType(RequestType.POST)
                .withUrl(url)
                .withHeaders(headers)
                .withPayload(payload)
                .build();
    }
    public HttpRequestDto buildRequest(String state){
        String url = "https://tatatiscon.co.in/services/service.php?pagename=rcp_state_change";
        String payload = "state="+state;
        Map<String, String> headers = HttpHeaderUtils.getApplicationFormURLEncodedHeaders();
        return HttpRequestDto.Builder.httpRequestDto()
                .withRequestType(RequestType.POST)
                .withUrl(url)
                .withHeaders(headers)
                .withPayload(payload)
                .build();
    }
    public HttpRequestDto buildRequest(){
        String url="https://tatatiscon.co.in/rcp.php";
        Map<String, String> headers = HttpHeaderUtils.getApplicationFormURLEncodedHeaders();
        return HttpRequestDto.Builder.httpRequestDto()
                .withRequestType(RequestType.GET)
                .withUrl(url)
                .withHeaders(headers)
                .withPayload(null)
                .build();
    }
}

