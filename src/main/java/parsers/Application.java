package parsers;

public class Application {

    public static void main(String[] args) {

        CommodityPriceCrawlingService commodityOnlinePriceCrawlingService=new CommodityPriceCrawlingService();
        commodityOnlinePriceCrawlingService.crawlCommodityPrice(CommodityPriceSource.JSWNEOSTEEL,DateUtils.DAY );
    }
}
