package parsers;

/**
 * Created by Umesh.
 * Date: 12/07/21
 */
public enum CommodityPriceSource {

    ENAM("https://enam.gov.in/web/Ajax_ctrl/trade_data_list"),
    AGRIPLUS("https://agriplus.in/prices/"),
    AGMART("https://agmart.in/searchmarketquery"),
    KISSANHELPLINE("https://www.kisaanhelpline.com/crops/get_mandi_rate"),
    COMMODITY_ONLINE("https://www.commodityonline.com/mandiprices/");;
    private String url;

    CommodityPriceSource(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

}
