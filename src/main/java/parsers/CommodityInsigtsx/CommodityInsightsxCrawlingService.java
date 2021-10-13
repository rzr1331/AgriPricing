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
        HttpRequestDto httpRequestDto = buildRequest();

        HttpClientPool httpClientPool = new HttpClientPool();

        try {
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
    private HttpRequestDto buildRequest(){
        String url = CommodityPriceSource.COMMODITYINSIGHTSX.getUrl();
        Map<String, String> headers = HttpHeaderUtils.getApplicationFormURLEncodedHeaders();
        headers.put("content-type", "application/json");
        headers.put("Accept-Encoding","gzip, deflate, br");
        headers.put("Set-Cookie","JSESSIONID=DF3B2EF3C29A9C8DDC1D521BD6E4613B; Path=/; HttpOnly");
        return HttpRequestDto.Builder.httpRequestDto()
                .withRequestType(RequestType.POST)
                .withUrl("https://www.commodityinsightsx.com/api/search/market-commodities-names/")
                .withHeaders(headers)
                .withPayload("{:}")
                .build();
    }
}
