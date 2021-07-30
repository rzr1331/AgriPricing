package parsers;


import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;


/**
 * Created by Umesh. Date: 27/07/21
 */

public class ParserCallableTask implements Callable<List<CrawlCommodityPriceDto>> {

    private ICommodityPricingParser commodityPricingParser;

    private HttpClientPool httpClientPool ;

    private HttpRequestDto httpRequestDto;

    public ParserCallableTask(ICommodityPricingParser commodityPricingParser,
        HttpClientPool httpClientPool, HttpRequestDto httpRequestDto) {
        this.commodityPricingParser = commodityPricingParser;
        this.httpClientPool = httpClientPool;
        this.httpRequestDto = httpRequestDto;
    }

    @Override
    public List<CrawlCommodityPriceDto> call() throws Exception {

        HttpResponseDto responseDto = httpClientPool.executeRequest(httpRequestDto);

        if (!responseDto.getSuccessful()) {
            //log.info("Error while getting response for Enam Request [{}] , response [{}]",
            //    httpRequestDto, responseDto);
            return Collections.emptyList();
        }

        return commodityPricingParser.parseCommodityPrice(responseDto.getResponseString());
    }
}
