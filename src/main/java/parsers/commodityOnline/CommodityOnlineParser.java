package parsers.commodityOnline;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import parsers.CommodityPriceSource;
import parsers.CrawlCommodityPriceDto;
import parsers.DateUtils;
import parsers.ICommodityPricingParser;
import parsers.ParserUtils;

/**
 * Created by Umesh. Date: 26/07/21
 */

public class CommodityOnlineParser implements ICommodityPricingParser {

    private static final String mandiParserDateFormat = "dd MMM yyyy";

    public List<CrawlCommodityPriceDto> parseCommodityPrice(String rawResponse) {

        List<CrawlCommodityPriceDto> priceDtos = new ArrayList<>();
        Document doc = Jsoup.parse(rawResponse);
        Elements items = doc.getElementsByClass("dt_ta_09");

        if (CollectionUtils.isEmpty(items)) {
            //log.info("No pricing available [{}]", rawResponse);
            return Collections.emptyList();
        }

        int n = items.size();

        if (n > 0) {
            for (Element element : items) {

                String commodity = getTextByClass(element, "dt_ta_10");
                String variety = getTextByClass(element, "dt_ta_12");

                if (!ParserUtils.equalsIgnoreCaseAndIngnoreSpace(commodity, variety)) {
                    commodity = commodity + " " + variety;
                }

                String state = getTextByClass(element, "dt_ta_17");
                String area = getTextByClass(element, "dt_ta_11").replace(state, "");
                Elements prices = element.getElementsByClass("dt_ta_14");
                String[] str3 = prices.get(1).text().split("/ ");
                Double minPrice = Double.parseDouble(str3[0]);
                Double maxPrice = Double.parseDouble(str3[1]);
                String date_str =
                    doc.getElementsByClass("la_update-style as_padd").select("span").text();
                Long date = DateUtils.parseToLong(mandiParserDateFormat, date_str);

                if (date == null) {
                    //log.error("error while parsing data [{}]", date_str);
                    continue;
                }

                priceDtos.add(CrawlCommodityPriceDto.Builder.crawlCommodityPriceDto()
                    .withProductName(commodity)
                    .withAreaName(area)
                    .withCity(area)
                    .withState(state)
                    .withMinPrice(minPrice)
                    .withMaxPrice(maxPrice)
                    .withDate(date)
                    .withSource(CommodityPriceSource.COMMODITY_ONLINE)
                    .build());
            }
        }
        return priceDtos;
    }

    private String getTextByClass(Element element, String cssClass) {
        String val = element.getElementsByClass(cssClass).text();
        if (StringUtils.isNotBlank(val)) {
            return val.trim();
        }
        return val;
    }
}
