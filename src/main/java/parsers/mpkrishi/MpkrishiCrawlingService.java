package parsers.mpkrishi;

import parsers.*;
import parsers.enam.EnamParser;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MpkrishiCrawlingService {
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

            MpkrishiParser mpkrishiParser = new MpkrishiParser();
            MpkrishiParser.setDate(date);
            List<CrawlCommodityPriceDto> crawlCommodityPriceDtoList =
                    mpkrishiParser.parseCommodityPrice(responseDto.getResponseString());
            return crawlCommodityPriceDtoList;
        } catch (IOException e) {
            System.out.println("error while getting data  for domain {}" + CommodityPriceSource.MPKRISHI);
        }

        return new ArrayList<>();
    }
    private HttpRequestDto buildRequest(Long date) {
        //Previous day
        //Long dateLong = System.currentTimeMillis() - DateUtils.DAY;
        String dateStr = DateUtils.format(new SimpleDateFormat("dd-MM-YYYY"), date);
        System.out.println(dateStr);
        String[] dates=dateStr.split("-");
        String payload = "Dte="+dates[0]+"%2F"+dates[1]+"%2F"+dates[2];
        String url = CommodityPriceSource.MPKRISHI.getUrl()+"?"+payload;
        Map<String, String> headers = HttpHeaderUtils.getApplicationFormURLEncodedHeaders();

        return HttpRequestDto.Builder.httpRequestDto()
                .withRequestType(RequestType.POST)
                .withUrl(url)
                .withHeaders(headers)
                .withPayload(payload)
                .build();
    }
}
