package parsers.KiranInfraispat;

import parsers.*;
import parsers.enam.EnamParser;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class KiranInfraIspatCrawlingService {
    public List<CrawlCommodityPriceDto> handleRequest(Long date) {

        HttpRequestDto httpRequestDto = buildRequest(date);
        HttpClientPool httpClientPool = new HttpClientPool();
        try {
            HttpResponseDto responseDto = httpClientPool.executeRequest(httpRequestDto);
            if (!responseDto.getSuccessful()) {
                System.out.println("error while getting response for enam reqeuset [{}] , response [{}]" +
                        httpRequestDto + responseDto);
                return new ArrayList<>();
            }
            KiranInfraIspatParser kiranInfraIspatParser = new KiranInfraIspatParser();
            List<CrawlCommodityPriceDto> crawlCommodityPriceDtoList = kiranInfraIspatParser.parseCommodityPrice(responseDto.getResponseString());
            return crawlCommodityPriceDtoList;
        }
        catch (IOException e) {
            System.out.println("error while getting data  for domain {}" + CommodityPriceSource.KIRANINFRAISPAT);
        }
        return new ArrayList<>();
    }

    private HttpRequestDto buildRequest(Long date) {

        String url = CommodityPriceSource.KIRANINFRAISPAT.getUrl();
        Map<String, String> headers = HttpHeaderUtils.getApplicationFormURLEncodedHeaders();

        return HttpRequestDto.Builder.httpRequestDto()
                .withRequestType(RequestType.GET)
                .withUrl(url)
                .withHeaders(headers)
                .withPayload("")
                .build();
    }
}
