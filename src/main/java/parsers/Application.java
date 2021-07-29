package parsers;

public class Application {

    public static void main(String[] args) {
        CommodityPriceCrawlingService commodityPriceCrawlingService = new CommodityPriceCrawlingService();
        commodityPriceCrawlingService.crawlCommodityPrice(CommodityPriceSource.ENAM, System.currentTimeMillis()-DateUtils.DAY);
        CommodityPriceCrawlingService commodityOnlinePriceCrawlingService=new CommodityPriceCrawlingService();
        commodityOnlinePriceCrawlingService.crawlCommodityPrice(CommodityPriceSource.COMMODITY_ONLINE,System.currentTimeMillis()-DateUtils.DAY );
        CommodityPriceCrawlingService agriPlusPriceCrawlingService = new CommodityPriceCrawlingService();
        agriPlusPriceCrawlingService.crawlCommodityPrice(CommodityPriceSource.AGRIPLUS, System.currentTimeMillis()-DateUtils.DAY);
    }
}
