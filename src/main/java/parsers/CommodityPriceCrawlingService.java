package parsers;

import java.io.IOException;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import parsers.commodityOnline.CommodityOnlineCrawlingService;
import parsers.enam.EnamCrawlingService;

public class CommodityPriceCrawlingService {

    
    private EnamCrawlingService enamCrawlingService = new EnamCrawlingService();
    private CommodityOnlineCrawlingService commodityPriceCrawlingService = new CommodityOnlineCrawlingService();
    private CommodityPricePublishingService commodityPricePublishingService = new CommodityPricePublishingService();

    public void crawlCommodityPrice(CommodityPriceSource commodityPriceSource, Long date) {
        List<CrawlCommodityPriceDto> crawlCommodityPriceDtos = null;

        switch (commodityPriceSource) {
            case ENAM:
                crawlCommodityPriceDtos = enamCrawlingService.handleRequest(date);
                break;
            case COMMODITY_ONLINE:
                crawlCommodityPriceDtos = commodityPriceCrawlingService.handleRequest(date);

        }

        if (CollectionUtils.isNotEmpty(crawlCommodityPriceDtos)) {
            try {
                commodityPricePublishingService.publishCommodityPrice(crawlCommodityPriceDtos,
                    commodityPriceSource);
            } catch (IOException e) {
                System.out.println("error while uploading data to gcp [{}]" + commodityPriceSource + e);
            }
        }
    }
}

