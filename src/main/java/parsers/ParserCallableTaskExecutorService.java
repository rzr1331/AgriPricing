package parsers;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.apache.commons.collections.CollectionUtils;

import static java.util.stream.Collectors.toList;

/**
 * Created by Umesh. Date: 27/07/21
 *
 *
 */

public class ParserCallableTaskExecutorService {

    private ExecutorService threadPool = Executors.newFixedThreadPool(10);

    private HttpClientPool httpClientPool = new HttpClientPool();

    public List<CrawlCommodityPriceDto> executeTask(List<HttpRequestDto> requestDtos,
        ICommodityPricingParser commodityPricingParser) throws InterruptedException {

        if (CollectionUtils.isEmpty(requestDtos)) {
            return Collections.emptyList();
        }

        List<ParserCallableTask> crawlCommodityPriceDtos = requestDtos.stream()
            .map(x -> new ParserCallableTask(commodityPricingParser,
                httpClientPool, x)).collect(toList());

        List<Future<List<CrawlCommodityPriceDto>>> futures =
            threadPool.invokeAll(crawlCommodityPriceDtos);

        List<CrawlCommodityPriceDto> result = new ArrayList<>();

        for (Future<List<CrawlCommodityPriceDto>> future : futures) {

            try {
                List<CrawlCommodityPriceDto> priceDtoList = future.get();
                result.addAll(priceDtoList);
            } catch (ExecutionException e) {
                e.printStackTrace();
                //log.error("Error while getting result future result", e);
            }
        }
        return result;
    }
}
