package parsers;

import java.io.IOException;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import parsers.commdity_online.MandiCrawlingService;
import parsers.enam.EnamCrawlingService;
import parsers.incredibleindustries.IncredibleIndustriesCrawlingService;

public class CommodityPriceCrawlingService {

    
    private EnamCrawlingService enamCrawlingService = new EnamCrawlingService();
    private MandiCrawlingService mandiCrawlingService=new MandiCrawlingService();
    private IncredibleIndustriesCrawlingService incredibleIndustriesCrawlingService=new IncredibleIndustriesCrawlingService();
    private CommodityPricePublishingService commodityPricePublishingService = new CommodityPricePublishingService();

    public void crawlCommodityPrice(CommodityPriceSource commodityPriceSource, Long date) {
        List<CrawlCommodityPriceDto> crawlCommodityPriceDtos = null;

        switch (commodityPriceSource) {
            case ENAM:
                crawlCommodityPriceDtos = enamCrawlingService.handleRequest(date);
                break;
            case COMMODITY_ONLINE:
                crawlCommodityPriceDtos=mandiCrawlingService.handleRequest();
                break;
            case Incredible_Industries:
                crawlCommodityPriceDtos=incredibleIndustriesCrawlingService.handleRequest();

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

