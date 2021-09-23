package parsers.tataiscon;

import parsers.*;
import parsers.enam.EnamParser;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TataisconCrawlingService {
    public List<CrawlCommodityPriceDto> handleRequest() {
        HttpRequestDto httpRequestDto = buildRequest("");
        HttpClientPool httpClientPool = new HttpClientPool();
        try {
            HttpResponseDto responseDto = httpClientPool.executeRequest(httpRequestDto);
            if (!responseDto.getSuccessful()) {
                System.out.println("error while getting response for enam reqeuset [{}] , response [{}]" +
                        httpRequestDto + responseDto);
                return new ArrayList<>();
            }
            System.out.println(responseDto.getResponseString());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("error while getting data  for domain {}" + CommodityPriceSource.TATAISCON);
        }
        return new ArrayList<>();
    }
    public HttpRequestDto buildRequest(String state){
        String url = "https://tatatiscon.co.in/services/service.php?pagename=rcp_state_change";
        String payload = "state=Assam&";

        Map<String, String> headers = HttpHeaderUtils.getApplicationFormURLEncodedHeaders();
        headers.put("X-Requested-With","XMLHttpRequest");

        return HttpRequestDto.Builder.httpRequestDto()
                .withRequestType(RequestType.POST)
                .withUrl(url)
                .withHeaders(headers)
                .withPayload(payload)
                .build();
    }
}

