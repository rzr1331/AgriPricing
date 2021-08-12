package parsers;

/**
 * Created by Umesh.
 * Date: 12/07/21
 */
public enum CommodityPriceSource {

    ENAM("https://enam.gov.in/web/Ajax_ctrl/trade_data_list"),
    AGRIPLUS("https://agriplus.in/prices/"),
    MPKRISHI("http://103.94.204.46:9080/Home/GetDashboard_DailyStatus_allMandi/"),
    AGMART("https://agmart.in/searchmarketquery"),
    COMMODITY_ONLINE("https://www.commodityonline.com/mandiprices/");;
    private String url;

    CommodityPriceSource(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

}
