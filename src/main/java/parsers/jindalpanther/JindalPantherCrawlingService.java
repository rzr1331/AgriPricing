package parsers.jindalpanther;

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
            JindalPatherParser jindalPatherParser = new JindalPatherParser();
            csrfToken = jindalPatherParser.getCsrfToken(responseDto.getResponseString());
            states = jindalPatherParser.getStates(responseDto.getResponseString());
            String cookie = responseDto.getResponseHeaders().value(4);
            for(String state:states){
                httpRequestDto = buildRequest(state,csrfToken,cookie);
                responseDto = httpClientPool.executeRequest(httpRequestDto);
                if (!responseDto.getSuccessful()) {
                    System.out.println(
                            "error while getting response for jindalPanther reqeuset [{}] , response [{}]"
                                    + httpRequestDto
                                    + responseDto);
                    continue;
                }
                HashSet<String>districts= jindalPatherParser.getDistricts(responseDto.getResponseString());
                for (String district:districts){
                    httpRequestDto = buildRequest(state,district);
                    responseDto = httpClientPool.executeRequest(httpRequestDto);
                    if (!responseDto.getSuccessful()) {
                        System.out.println(
                                "error while getting response for jindalPanther reqeuset [{}] , response [{}]"
                                        + httpRequestDto
                                        + responseDto);
                        continue;
                    }
                    jindalPatherParser.parseCommodityPrice(responseDto.getResponseString(),state,district);
                }
            }
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
    public HttpRequestDto buildRequest(String state,String district) {
        String url =CommodityPriceSource.JindalPanther.getUrl()+String.format("&state=%s&district=%s",state,district);
        return HttpRequestDto.Builder.httpRequestDto()
                .withRequestType(RequestType.GET)
                .withUrl(url)
                .withHeaders(Collections.emptyMap())
                .withPayload(null)
                .build();
    }
    public HttpRequestDto buildRequest(String state,String csrfToken, String cookie) {
        String url = CommodityPriceSource.JindalPanther.getUrl();
        Map<String, String> headers = HttpHeaderUtils.getApplicationFormURLEncodedHeaders();
        headers.put("x-csrf-token", csrfToken);
        headers.put("x-requested-with", "XMLHttpRequest");
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        headers.put("cookie", cookie);
        String payload = String.format("statecode=%s&_csrf=%s",state,csrfToken);
        return HttpRequestDto.Builder.httpRequestDto()
            .withRequestType(RequestType.POST)
            .withUrl("https://jindalpanther.com/app/get-district")
            .withHeaders(headers)
            .withPayload(payload)
            .build();
    }
}
