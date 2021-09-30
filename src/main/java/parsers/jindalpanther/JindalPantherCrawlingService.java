package parsers.jindalpanther;

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
            if (!responseDto.getSuccessful()) {
                System.out.println(
                    "error while getting response for jindalPanther reqeuset [{}] , response [{}]" +
                        httpRequestDto + responseDto);
                return new ArrayList<>();
            }
            Document doc = Jsoup.parse(responseDto.getResponseString());
            System.out.println(doc);
            JindalPatherParser jindPatherParser = new JindalPatherParser();
            csrfToken = jindPatherParser.getCsrfToken(responseDto.getResponseString());
            System.out.println(csrfToken);
            states = jindPatherParser.getStates(responseDto.getResponseString());
            String cookie = responseDto.getResponseHeaders().value(5);
            httpRequestDto = buildDistrictRequest(csrfToken,cookie);
            responseDto = httpClientPool.executeRequest(httpRequestDto);
            if (!responseDto.getSuccessful()) {
                System.out.println(
                    "error while getting response for jindalPanther reqeuset [{}] , response [{}]"
                        + httpRequestDto
                        + responseDto);
                return new ArrayList<>();
            }
            System.out.println(responseDto.getResponseString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList();
    }

    public HttpRequestDto buildRequest() {
        String url = CommodityPriceSource.JindalPanther.getUrl();
        return HttpRequestDto.Builder.httpRequestDto()
            .withRequestType(RequestType.GET)
            .withUrl(url)
            .withHeaders(Collections.emptyMap())
            .withPayload(null)
            .build();
    }

    public HttpRequestDto buildDistrictRequest(String csrfToken, String cookie) {
        String url = CommodityPriceSource.JindalPanther.getUrl();
        Map<String, String> headers = HttpHeaderUtils.getApplicationFormURLEncodedHeaders();
        headers.put("x-csrf-token", csrfToken);
        headers.put("x-requested-with", "XMLHttpRequest");
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        headers.put("cookie", cookie);
        String payload = "statecode=Bihar&_csrf="
            + csrfToken;
        System.out.println(payload);
        return HttpRequestDto.Builder.httpRequestDto()
            .withRequestType(RequestType.POST)
            .withUrl("https://jindalpanther.com/app/get-district")
            .withHeaders(headers)
            .withPayload(payload)
            .build();
    }
}
