package parsers;

public class Application {

    public static void main(String[] args) {
        CommodityPriceCrawlingService commodityPriceCrawlingService = new CommodityPriceCrawlingService();
        commodityPriceCrawlingService.crawlCommodityPrice(CommodityPriceSource.COMMODITY_ONLINE, System.currentTimeMillis()-DateUtils.DAY);

    }
}
