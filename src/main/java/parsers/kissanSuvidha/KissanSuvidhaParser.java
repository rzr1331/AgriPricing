package parsers.kissanSuvidha;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import org.json.JSONArray;
import org.json.JSONObject;
import parsers.CommodityPriceSource;
import parsers.CrawlCommodityPriceDto;
import parsers.DateUtils;
import parsers.ParserUtils;

public class KissanSuvidhaParser {
    private static final String kissanSuvidhaParserDateFormat = "dd/MM/YYYY";
    private static final SimpleDateFormat formatter = new SimpleDateFormat(kissanSuvidhaParserDateFormat);
    public List<CrawlCommodityPriceDto> parseCommodityPrice(String rawResponse,long date) {
        JSONArray priceArray = new JSONArray(rawResponse);
        List<CrawlCommodityPriceDto> priceDtos = new ArrayList<>();
        String requiredDate=formatter.format(date);
        for (int i = 0; i < priceArray.length(); i++) {
            JSONObject object = (JSONObject) priceArray.get(i);
            String currentDateText=object.optString("date");
            if(!currentDateText.equalsIgnoreCase(requiredDate)){
                continue;
            }
            long currentDate=DateUtils.parseToLong(kissanSuvidhaParserDateFormat, currentDateText);
            Date required=DateUtils.parse(kissanSuvidhaParserDateFormat,object.optString("date"));
            System.out.println(object.getString("commodity")+" "+object.optString("market")+" "+object.optString("district")+" "+object.optString("date"));
            priceDtos.add(CrawlCommodityPriceDto.Builder.crawlCommodityPriceDto()
                    .withProductName(object.getString("commodity"))
                    .withAreaName(object.optString("market"))
                    .withCity(object.optString("district"))
                    .withState(object.optString("state"))
                    .withMinPrice(ParserUtils.parseRawStringToDouble(object.optString("Price")))
                    .withMaxPrice(ParserUtils.parseRawStringToDouble(object.optString("Price")))
                    .withDate(currentDate)
                    .withSource(CommodityPriceSource.KISSANSUVIDHA)
                    .build());
        }
        return priceDtos;
    }
    public Set<String>getValues(String rawResponse,String key){
        Set<String>vals=new HashSet<String>();
        JSONArray valuesArray = new JSONArray(rawResponse);
        for (int i = 0; i < valuesArray.length(); i++) {
            JSONObject object = (JSONObject) valuesArray.get(i);
            vals.add(object.getString(key));
//            System.out.println(object.getString(key));
        }
        return vals;
    }
}
