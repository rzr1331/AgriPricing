package parsers.commodityOnline;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import parsers.CrawlCommodityPriceDto;
import parsers.HttpClientPool;
import parsers.HttpRequestDto;
import parsers.HttpResponseDto;
import parsers.ParserCallableTaskExecutorService;
import parsers.ParserUtils;
import parsers.RequestType;

import static parsers.CommodityPriceSource.COMMODITY_ONLINE;

/**
 * Created by Umesh. Date: 26/07/21
 */
public class CommodityOnlineCrawlingService {

    public List<CrawlCommodityPriceDto> handleRequest(Long date) {
        HttpClientPool httpClientPool = new HttpClientPool();
        try {
            int count = 0;
            HttpRequestDto requestDto = buildRequest(count);
            HttpResponseDto responseDto = httpClientPool.executeRequest(requestDto);

            if (!responseDto.getSuccessful()) {
                //log.info("[{}] Error while getting response , Request [{}] , Response [{}]",
                //    COMMODITY_ONLINE, requestDto, responseDto);
                return new ArrayList<>();
            }

            Document document = Jsoup.parse(responseDto.getResponseString());
            Elements elements = document.getElementsByClass("pagination").select("li");
            String lastPageUrl = elements.last().select("a").attr("href");

            String[] split = lastPageUrl.split("/");

            Optional<Integer> totalPricingOpt = ParserUtils.valueOfString(split[split.length - 1]);

            //if not able to get total pricing then considering default pricing -> 5000
            Integer totalPricing = totalPricingOpt.orElse(5000);

            List<HttpRequestDto> httpRequestDtos = new ArrayList<>();

            /**
             * on every page there is 36 result , making urls for all pages
             */
            while (count < totalPricing) {
                count += 36;
                httpRequestDtos.add(buildRequest(count));
            }

            //log.info("Getting response for [{}]", httpRequestDtos);
            ParserCallableTaskExecutorService parserCallableTaskExecutorService =
                new ParserCallableTaskExecutorService();
            List<CrawlCommodityPriceDto> crawlCommodityPriceDtoList =
                parserCallableTaskExecutorService.executeTask(httpRequestDtos,
                    new CommodityOnlineParser());

            return crawlCommodityPriceDtoList;
        } catch (Exception e) {
            e.printStackTrace();
            //log.error("Error while crawling commodity online ", e);
        }

        return new ArrayList<>();
    }

    private HttpRequestDto buildRequest(int count) {

        return HttpRequestDto.Builder.httpRequestDto()
            .withRequestType(RequestType.GET)
            .withUrl(COMMODITY_ONLINE.getUrl() + count)
            .withHeaders(Collections.emptyMap())
            .withPayload(null)
            .build();
    }
}
