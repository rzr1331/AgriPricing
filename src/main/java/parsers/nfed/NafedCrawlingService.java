package parsers.nfed;

import parsers.*;
import parsers.enam.EnamParser;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NafedCrawlingService {


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
            NafedPraser nafedPraser = new NafedPraser();
            List<CrawlCommodityPriceDto> crawlCommodityPriceDtoList = nafedPraser.parseCommodityPrice(responseDto.getResponseString());
            return crawlCommodityPriceDtoList;
        } catch (IOException e) {
            System.out.println("error while getting data  for domain {}" + CommodityPriceSource.NAFED);
        }
        return new ArrayList<>();
    }
    private HttpRequestDto buildRequest() {

        String url = CommodityPriceSource.NAFED.getUrl();

        Map<String, String> headers = HttpHeaderUtils.getApplicationFormURLEncodedHeaders();

        return HttpRequestDto.Builder.httpRequestDto()
                .withRequestType(RequestType.GET)
                .withUrl(url)
                .withHeaders(headers)
                .withPayload("")
                .build();
    }
}
