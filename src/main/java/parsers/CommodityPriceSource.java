package parsers;

/**
 * Created by Umesh.
 * Date: 12/07/21
 */
public enum CommodityPriceSource {
    ENAM("https://enam.gov.in/web/Ajax_ctrl/trade_data_list"),
    COMMODITY_ONLINE("https://www.commodityonline.com/mandiprices/"),
    SUPERSHAKTI("https://supershakti.in/wp-admin/admin-ajax.php");


    private String url;

    CommodityPriceSource(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

}

