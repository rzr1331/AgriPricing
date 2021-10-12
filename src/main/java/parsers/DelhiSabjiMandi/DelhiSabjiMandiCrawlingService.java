package parsers.DelhiSabjiMandi;

import parsers.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Collections.addAll;

public class DelhiSabjiMandiCrawlingService {
    public List<CrawlCommodityPriceDto> handleRequest(Long date) {
        HttpRequestDto httpRequestDto = buildRequest("vegetables",date);
        HttpClientPool httpClientPool = new HttpClientPool();
        List<CrawlCommodityPriceDto> crawlCommodityPriceDtoList=new ArrayList<>();
        try {
            HttpResponseDto responseDto = httpClientPool.executeRequest(httpRequestDto);
            if (!responseDto.getSuccessful()) {
                System.out.println("error while getting response for DelhiMandiSabji reqeuset [{}] , response [{}]" +
                        httpRequestDto + responseDto);
                return new ArrayList<>();
            }
            DelhiSabjiMandiParserService delhiSabjiMandiParserService = new DelhiSabjiMandiParserService();
            crawlCommodityPriceDtoList.addAll(delhiSabjiMandiParserService.parseCommodityPrice(responseDto.getResponseString(),date));
            httpRequestDto = buildRequest("fruits",date);
            responseDto = httpClientPool.executeRequest(httpRequestDto);
            if (!responseDto.getSuccessful()) {
                System.out.println("error while getting response for DelhiMandiSabji reqeuset [{}] , response [{}]" +
                        httpRequestDto + responseDto);
                return new ArrayList<>();
            }
            crawlCommodityPriceDtoList.addAll(delhiSabjiMandiParserService.parseCommodityPrice(responseDto.getResponseString(),date));
            return crawlCommodityPriceDtoList;
        } catch (IOException e) {
            System.out.println("error while getting data  for domain {}" + CommodityPriceSource.DELHISABJIMANDI);
        }

        return new ArrayList<>();
    }
    public HttpRequestDto buildRequest(String type,long date){
        DateFormat df = new SimpleDateFormat("dd-MMMM-yyyy");
        String datestr =DateUtils.format(df,date);
        String url;
        if(type.equalsIgnoreCase("vegetables")) {
            url = CommodityPriceSource.DELHISABJIMANDI.getUrl() + "vegetable-prices-" + datestr.toLowerCase();
        }
        else{
            url= CommodityPriceSource.DELHISABJIMANDI.getUrl()+"fruit-price-"+datestr.toLowerCase();
        }
        System.out.println(url);
        Map<String, String> headers = HttpHeaderUtils.getApplicationFormURLEncodedHeaders();
        return HttpRequestDto.Builder.httpRequestDto()
                .withRequestType(RequestType.GET)
                .withUrl(url)
                .withHeaders(headers)
                .withPayload("")
                .build();
    }
}
