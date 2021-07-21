package parsers.enam;

import com.sun.tools.javac.util.Pair;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import parsers.CommodityPriceDto;
import parsers.CommodityPriceSource;
import parsers.CrawlCommodityPriceDto;
import parsers.DateUtils;
import parsers.HttpClientPool;
import parsers.HttpHeaderUtils;
import parsers.HttpRequestDto;
import parsers.HttpResponseDto;
import parsers.RequestType;
import parsers.UnMappedInfoDto;

public class EnamCrawlingService {
    
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

            EnamParser enamParser = new EnamParser();
            List<CrawlCommodityPriceDto> crawlCommodityPriceDtoList =
                enamParser.parseCommodityPrice(responseDto.getResponseString());
            //Pair<List<CommodityPriceDto>, List<UnMappedInfoDto>> mappedResult =
            //    enamMapper.mapCrawlCommodityDto(crawlCommodityPriceDtoList);
            return crawlCommodityPriceDtoList;
        } catch (IOException e) {
            System.out.println("error while getting data  for domain {}" + CommodityPriceSource.ENAM);
        }

        return new ArrayList<>();
    }

    private HttpRequestDto buildRequest(Long date) {

        String url = CommodityPriceSource.ENAM.getUrl();

        //Previous day
        //Long dateLong = System.currentTimeMillis() - DateUtils.DAY;
        String dateStr = DateUtils.format(new SimpleDateFormat("yyyy-MM-dd"), date);

        String payload = String.format(
            "language=en&stateName=--+All+--&apmcName=--+Select+APMCs+--&commodityName=--+Select+" +
                "Commodity+--&fromDate=%s&toDate=%s", dateStr, dateStr);

        Map<String, String> headers = HttpHeaderUtils.getApplicationFormURLEncodedHeaders();

        return HttpRequestDto.Builder.httpRequestDto()
            .withRequestType(RequestType.POST)
            .withUrl(url)
            .withHeaders(headers)
            .withPayload(payload)
            .build();
    }
}
