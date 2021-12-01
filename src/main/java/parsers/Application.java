package parsers;

public class Application {

    public static void main(String[] args) {
        CommodityPriceCrawlingService commodityPriceCrawlingService = new CommodityPriceCrawlingService();
        commodityPriceCrawlingService.crawlCommodityPrice(CommodityPriceSource.KISSANSUVIDHA, System.currentTimeMillis());

    }
}
