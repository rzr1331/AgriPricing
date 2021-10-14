package parsers.pfTirumalatmtSteelPrice;

import parsers.*;
import parsers.enam.EnamParser;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class PfTirumalatmtSteelPriceCrawlingService {
    public List<CrawlCommodityPriceDto> handleRequest() {
        List<CrawlCommodityPriceDto> crawlCommodityPriceDtoList = new ArrayList<>();
        HashSet<String> districts = new HashSet<String>();
        HttpRequestDto httpRequestDto = buildRequest();
        HttpClientPool httpClientPool = new HttpClientPool();
        try {
            HttpResponseDto responseDto = httpClientPool.executeRequest(httpRequestDto);
            if (!responseDto.getSuccessful()) {
                System.out.println("error while getting response for fTirumalaSteelTmtPrice reqeuset [{}] , response [{}]" +
                        httpRequestDto + responseDto);
                return new ArrayList<>();
            }
            PfTirumalaSteelPriceParser pfTirumalaSteelPriceParser = new PfTirumalaSteelPriceParser();
            districts=pfTirumalaSteelPriceParser.getDistricts(responseDto.getResponseString());
            for(String district:districts){
                httpRequestDto = buildRequest(district);
                responseDto = httpClientPool.executeRequest(httpRequestDto);
                if (!responseDto.getSuccessful()) {
                    System.out.println("error while getting response for PfTirumalaSteelTmtPrice reqeuset [{}] , response [{}]" +
                            httpRequestDto + responseDto);
                    continue;
                }
                crawlCommodityPriceDtoList.addAll(pfTirumalaSteelPriceParser.parseCommodityPrice(responseDto.getResponseString()));
            }

        } catch (IOException e) {
            System.out.println("error while getting data  for domain {}" + CommodityPriceSource.PFTIRUMALATMTSTEELPRICE);
        }

        return new ArrayList<>();
    }

    private HttpRequestDto buildRequest() {

        Map<String, String> headers = HttpHeaderUtils.getApplicationFormURLEncodedHeaders();
        return HttpRequestDto.Builder.httpRequestDto()
                .withRequestType(RequestType.GET)
                .withUrl("http://pfitirumalatmtsteelpricelist.com/index.php")
                .withHeaders(headers)
                .withPayload("")
                .build();
    }
    public HttpRequestDto buildRequest(String district){
        Map<String, String> headers = HttpHeaderUtils.getApplicationFormURLEncodedHeaders();
        String payload=String.format("districtt=%s&submit=Submit",district);
        return HttpRequestDto.Builder.httpRequestDto()
                .withRequestType(RequestType.POST)
                .withUrl(CommodityPriceSource.PFTIRUMALATMTSTEELPRICE.getUrl())
                .withHeaders(headers)
                .withPayload(payload)
                .build();
    }
}
