package parsers;

/**
 * Created by Umesh.
 * Date: 12/07/21
 */
public enum CommodityPriceSource {
    ENAM("https://enam.gov.in/web/Ajax_ctrl/trade_data_list"),
    COMMODITY_ONLINE("https://www.commodityonline.com/mandiprices/"),
    AGRIPLUSSELLLEADS("https://agriplus.in/trade/sell-leads/"),
    KISSANSUVIDHA("https://agrionline.nic.in/api/PriceTrend/");


    private String url;

    CommodityPriceSource(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

}

