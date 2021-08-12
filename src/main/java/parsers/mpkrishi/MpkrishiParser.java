package parsers.mpkrishi;

import org.json.JSONArray;
import org.json.JSONObject;
import parsers.*;

import java.util.ArrayList;
import java.util.List;

public class MpkrishiParser implements ICommodityPricingParser {
    public static void setDate(long date) {
        MpkrishiParser.date = date;
    }
    private static long date;
    private static final String mpkrishiParserParserDateFormat = "dd-MM-yyyy";
    public List<CrawlCommodityPriceDto> parseCommodityPrice(String rawResponse){
        List<CrawlCommodityPriceDto> priceDtos = new ArrayList<>();
        System.out.println(rawResponse);
        JSONArray priceArray = new JSONArray(rawResponse);
        for(int i=1;i<priceArray.length();i++){
            JSONObject object = (JSONObject) priceArray.get(i);
            String state = "MadhyaPradesh";
            String city = object.getString("Division_name").split("-")[1];
            String area = object.getString("Mandi_Name").split("-")[1];
            String commodity = object.getString("Upaj_Name").split("-")[1];
            String maxPriceString = object.getString("MaximumRate");
            String minPriceString = object.getString("MinimumRate");
            Double maxPrice = ParserUtils.parseRawStringToDouble(maxPriceString);
            Double minPrice = ParserUtils.parseRawStringToDouble(minPriceString);
            if (maxPrice == null && minPrice == null) {
                System.out.println("unable to get min [{}] max [{}] price " + minPriceString +
                        maxPriceString);
                continue;
            }
            priceDtos.add(CrawlCommodityPriceDto.Builder.crawlCommodityPriceDto()
                    .withProductName(commodity.substring(1))
                    .withAreaName(area.substring(1))
                    .withCity(city)
                    .withState(state)
                    .withMinPrice(minPrice)
                    .withMaxPrice(maxPrice)
                    .withDate(date)
                    .withSource(CommodityPriceSource.MPKRISHI)
                    .build());
        }
        return priceDtos;
    }
}
