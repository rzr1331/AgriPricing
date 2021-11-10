package parsers.kalingatmt;

import parsers.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class KalingatmtCrawlingService {

    public List<CrawlCommodityPriceDto> handleRequest() {
        HttpRequestDto httpRequestDto = buildRequest();
        HttpClientPool httpClientPool = new HttpClientPool();
        List<CrawlCommodityPriceDto> crawlCommodityPriceDtoList = new ArrayList<>();
        try {
            HttpResponseDto responseDto = httpClientPool.executeRequest(httpRequestDto);
            if (!responseDto.getSuccessful()) {
                System.out.println("error while getting response for Kalinga Parser reqeuset [{}] , response [{}]" +
                        httpRequestDto + responseDto);
                return new ArrayList<>();
            }
            KalingaParserService kalingaParser = new KalingaParserService();
            kalingaParser.parseCommodityPrice(responseDto.getResponseString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return crawlCommodityPriceDtoList;
    }
    private HttpRequestDto buildRequest() {
        String url = CommodityPriceSource.Kalingatmt.getUrl();
        Map<String, String> headers = HttpHeaderUtils.getApplicationFormURLEncodedHeaders();
        return HttpRequestDto.Builder.httpRequestDto()
                .withRequestType(RequestType.GET)
                .withUrl(url)
                .withHeaders(headers)
                .withPayload("")
                .build();
    }
}
