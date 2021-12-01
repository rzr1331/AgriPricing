package parsers;

import java.io.IOException;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import parsers.agriPlusSellLeads.AgriPlusSellLeadsCrawlingService;
import parsers.commdity_online.MandiCrawlingService;
import parsers.enam.EnamCrawlingService;
import parsers.kissanSuvidha.KissanSuvidhaCrawlingService;

public class CommodityPriceCrawlingService {

    
    private EnamCrawlingService enamCrawlingService = new EnamCrawlingService();
    private MandiCrawlingService mandiCrawlingService=new MandiCrawlingService();
    private AgriPlusSellLeadsCrawlingService agriPlusSellLeadsCrawlingService = new AgriPlusSellLeadsCrawlingService();
    private KissanSuvidhaCrawlingService kissanSuvidhaCrawlingService = new KissanSuvidhaCrawlingService();
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
            case AGRIPLUSSELLLEADS:
                crawlCommodityPriceDtos=agriPlusSellLeadsCrawlingService.handleRequest();
                break;
            case KISSANSUVIDHA:
                crawlCommodityPriceDtos = kissanSuvidhaCrawlingService.handleRequest(date);
                break;
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

