package parsers;

public class Application {

    public static void main(String[] args) {
//        CommodityPriceCrawlingService commodityPriceCrawlingService = new CommodityPriceCrawlingService();
//        commodityPriceCrawlingService.crawlCommodityPrice(CommodityPriceSource.COMMODITY_ONLINE, System.currentTimeMillis()-DateUtils.DAY);
//        CommodityPriceCrawlingService agriPlusPriceCrawlingService = new CommodityPriceCrawlingService();
//        agriPlusPriceCrawlingService.crawlCommodityPrice(CommodityPriceSource.AGRIPLUS, System.currentTimeMillis()-DateUtils.DAY);
//         CommodityPriceCrawlingService commodityPriceCrawlingService =new CommodityPriceCrawlingService();
//         commodityPriceCrawlingService.crawlCommodityPrice(CommodityPriceSource.AGMART,System.currentTimeMillis()-DateUtils.DAY);
        CommodityPriceCrawlingService commodityPriceCrawlingService = new CommodityPriceCrawlingService();
        commodityPriceCrawlingService.crawlCommodityPrice(CommodityPriceSource.MPKRISHI,System.currentTimeMillis()-DateUtils.DAY);

    }
}
