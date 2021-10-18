package parsers.CommodityInsigtsx;

import parsers.*;
import parsers.commdity_online.MandiParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class CommodityInsightsxCrawlingService {
    public List<CrawlCommodityPriceDto> handleRequest(Long date) {
        HttpRequestDto httpRequestDto;

        HttpClientPool httpClientPool = new HttpClientPool();

        try {
            HttpRequestDto cook = getCookie();
            HttpResponseDto res = httpClientPool.executeRequest(cook);
            String set="";
            if(res.getSuccessful()){
                set=res.getResponseHeaders().get("Set-Cookie");
                System.out.println(res.getResponseString());
            }
            httpRequestDto = buildRequest(set);
            HttpResponseDto responseDto = httpClientPool.executeRequest(httpRequestDto);
            if (!responseDto.getSuccessful()) {
                System.out.println("error while getting response for Commodity InsightSX reqeuset [{}] , response [{}]" + httpRequestDto + responseDto);
                return new ArrayList<>();
            }
            CommodityInsightsxParser commodityInsightsxParser=new CommodityInsightsxParser();
            List<CrawlCommodityPriceDto> crawlCommodityPriceDtoList = commodityInsightsxParser.parseCommodityPrice(responseDto.getResponseString());
            return crawlCommodityPriceDtoList;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("error while getting data  for domain {}" + CommodityPriceSource.COMMODITYINSIGHTSX);
        }
        return new ArrayList<>();
    }
    private HttpRequestDto buildRequest(String set){
        String url = CommodityPriceSource.COMMODITYINSIGHTSX.getUrl();
        Map<String, String> headers = HttpHeaderUtils.getApplicationFormURLEncodedHeaders();
        headers.put("content-type", "application/json;charset=UTF-8");
        headers.put("Accept-Encoding","gzip, deflate, br");
        headers.put("cookie","_ga=GA1.2.2141299610.1634024906; _gid=GA1.2.1346718099.1634024906; JSESSIONID=1CEC1E08D82CAC775413CE434B3E43C8");
        return HttpRequestDto.Builder.httpRequestDto()
                .withRequestType(RequestType.POST)
                .withUrl("https://www.commodityinsightsx.com/api/search/market-commodities-names/states")
                .withHeaders(headers)
                .withPayload("commodity=Ajwan")
                .build();
    }
    public HttpRequestDto getCookie(){
        Map<String, String> headers = HttpHeaderUtils.getApplicationFormURLEncodedHeaders();
        return HttpRequestDto.Builder.httpRequestDto()
                .withRequestType(RequestType.GET)
                .withUrl("https://www.commodityinsightsx.com/commodities")
                .withHeaders(headers)
                .withPayload("")
                .build();
    }
}
