package parsers.jindalpanther;

import okhttp3.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import parsers.*;


import java.util.*;

public class JindalPantherCrawlingService {
    public List<CrawlCommodityPriceDto> handleRequest() {
        HttpRequestDto httpRequestDto = buildRequest();
        HttpClientPool httpClientPool = new HttpClientPool();
        String csrfToken;
        Set<String> states;
        try {
            HttpResponseDto responseDto = httpClientPool.executeRequest(httpRequestDto);
            if(!responseDto.getSuccessful()){
                System.out.println("error while getting response for jindalPanther reqeuset [{}] , response [{}]" +
                        httpRequestDto + responseDto);
                return new ArrayList<>();
            }
            Document doc= Jsoup.parse(responseDto.getResponseString());
            System.out.println(doc);
            JindalPatherParser jindPatherParser=new JindalPatherParser();
            csrfToken=jindPatherParser.getCsrfToken(responseDto.getResponseString());
            System.out.println(csrfToken);
            states= jindPatherParser.getStates(responseDto.getResponseString());
            httpRequestDto = buildStateRequest(csrfToken);
            responseDto=httpClientPool.executeRequest(httpRequestDto);
            if(!responseDto.getSuccessful()){
                System.out.println("error while getting response for jindalPanther reqeuset [{}] , response [{}]" + httpRequestDto + responseDto);
                return new ArrayList<>();
            }
            System.out.println(responseDto.getResponseString());
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new ArrayList();
    }
    public HttpRequestDto buildRequest(){
        String url = CommodityPriceSource.JindalPanther.getUrl();
        Map<String, String> headers = HttpHeaderUtils.getApplicationFormURLEncodedHeaders();
        return HttpRequestDto.Builder.httpRequestDto()
                .withRequestType(RequestType.GET)
                .withUrl(url)
                .withHeaders(headers)
                .withPayload("")
                .build();
    }
    public HttpRequestDto buildStateRequest(String csrfToken){
        String url = CommodityPriceSource.JindalPanther.getUrl();
        Map<String, String> headers=HttpHeaderUtils.getApplicationFormURLEncodedHeaders();
        headers.put("x-csrf-token","SZYNZNjPxJ_y5LsANw3FGWV32Zt-2I9O6OhnnqTr1kckwzUqoqCC5pae0XJ-S6xUNzWM3iqB_jiQry7UydKcFA==");
        headers.put("x-requested-with","XMLHttpRequest");
        headers.put("Content-Type","application/x-www-form-urlencoded");
        headers.put("cookiee","_ga=GA1.1.629836338.1631602738; _csrf=d68f72db48916b2e85a610de5aca4839eecff18d12590ea8208948bc8c3940b1a%3A2%3A%7Bi%3A0%3Bs%3A5%3A%22_csrf%22%3Bi%3A1%3Bs%3A32%3A%22mU8NzoFydzjrIFiMRBUETYqvxGIJm9JS%22%3B%7D; _ga_N6R7BQ8W1X=GS1.1.1632995938.12.0.1632995938.0");
        String payload="statecode=Bihar&_csrf="+"SZYNZNjPxJ_y5LsANw3FGWV32Zt-2I9O6OhnnqTr1kckwzUqoqCC5pae0XJ-S6xUNzWM3iqB_jiQry7UydKcFA==";
        System.out.println(payload);
        return HttpRequestDto.Builder.httpRequestDto()
                .withRequestType(RequestType.POST)
                .withUrl("https://jindalpanther.com/app/get-district")
                .withHeaders(headers)
                .withPayload(payload)
                .build();
    }
}
