package parsers.enam;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import parsers.CommodityPriceSource;
import parsers.CrawlCommodityPriceDto;
import parsers.DateUtils;
import parsers.ParserUtils;

public class EnamParser {
    
    private static final String enamParserDateFormat = "yyyy-MM-dd";
    
    public List<CrawlCommodityPriceDto> parseCommodityPrice(String rawResponse) {

        JSONObject jsonObject = new JSONObject(rawResponse);
        JSONArray priceArray = jsonObject.getJSONArray("data");

        List<CrawlCommodityPriceDto> priceDtos = new ArrayList<>();
        for (int i = 0; i < priceArray.length(); i++) {

            JSONObject object = (JSONObject) priceArray.get(i);
            String state = object.getString("state");
            String area = object.getString("apmc");
            String commodity = object.getString("commodity");
            String maxPriceString = object.getString("max_price");
            String minPriceString = object.getString("min_price");

            Double maxPrice = ParserUtils.parseRawStringToDouble(maxPriceString);
            Double minPrice = ParserUtils.parseRawStringToDouble(minPriceString);

            if (maxPrice == null && minPrice == null) {
                System.out.println("unable to get min [{}] max [{}] price " + minPriceString +
                    maxPriceString);
                continue;
            }

            String dateString = object.getString("created_at");
            Long date = DateUtils.parseToLong(enamParserDateFormat, dateString);

            if (date == null) {
                System.out.println("Unable to get date value [{}]" + dateString);
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
                .withSource(CommodityPriceSource.ENAM)
                .build());
        }
        return priceDtos;
    }
}