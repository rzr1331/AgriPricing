package parsers;

import java.util.List;

/**
 * Created by Umesh.
 * Date: 13/07/21
 */
public interface ICommodityPricingParser {

    List<CrawlCommodityPriceDto> parseCommodityPrice(String rawResponse);
}
