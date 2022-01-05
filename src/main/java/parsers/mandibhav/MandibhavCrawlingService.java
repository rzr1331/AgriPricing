package parsers.mandibhav;

import parsers.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class MandibhavCrawlingService {
    public List<CrawlCommodityPriceDto> handleRequest(long date) {

        List<CrawlCommodityPriceDto> crawlCommodityPriceDtoList = new ArrayList<>();

        HttpRequestDto httpRequestDto = buildRequest();

        HttpClientPool httpClientPool = new HttpClientPool();

        try {
            HttpResponseDto responseDto = httpClientPool.executeRequest(httpRequestDto);
            MandibhavParser mandibhavParser = new MandibhavParser();
            HashSet<String> states = mandibhavParser.getStates(responseDto.getResponseString());

            for (String state : states){
                httpRequestDto = buildRequest(state);
                responseDto = httpClientPool.executeRequest(httpRequestDto);
                if (!responseDto.getSuccessful()) {
                    System.out.println("error while getting response for "+state);
                    continue;
                }
                crawlCommodityPriceDtoList.addAll(mandibhavParser.parseCommodityPrice(responseDto.getResponseString(),date));

            }
        } catch (IOException e) {
            System.out.println("error while getting data  for domain {}" + CommodityPriceSource.MANDIBHAV);
        }

        return crawlCommodityPriceDtoList;
    }
    private HttpRequestDto buildRequest() {
        String url = CommodityPriceSource.MANDIBHAV.getUrl();
        Map<String, String> headers = HttpHeaderUtils.getApplicationFormURLEncodedHeaders();
        return HttpRequestDto.Builder.httpRequestDto()
                .withRequestType(RequestType.GET)
                .withUrl(url)
                .withHeaders(headers)
                .withPayload("")
                .build();
    }
    private HttpRequestDto buildRequest(String state) {
        String url = CommodityPriceSource.MANDIBHAV.getUrl()+state;
        Map<String, String> headers = HttpHeaderUtils.getApplicationFormURLEncodedHeaders();
        return HttpRequestDto.Builder.httpRequestDto()
                .withRequestType(RequestType.GET)
                .withUrl(url)
                .withHeaders(headers)
                .withPayload("")
                .build();
    }
}
