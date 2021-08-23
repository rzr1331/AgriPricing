package parsers.agmart;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import parsers.CommodityPriceSource;
import parsers.CrawlCommodityPriceDto;
import parsers.ICommodityPricingParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class AgmartParser implements ICommodityPricingParser {
    private static final String agmartDateFormat = "yyy y-MM-dd";

    public static void setDate(long date) {
        AgmartParser.date = date;
    }

    private static long date;
    private static HashMap<String,String>city_state=new HashMap<String,String>();
    private static HashMap<String,String>id_state=new HashMap<String,String>();
    public HashSet<String> getState_ids(String rawResponse) {
        HashSet<String>state_ids=new HashSet<String>();
        Document doc = Jsoup.parse(rawResponse);
        Elements items=doc.getElementById("selected_location").getElementsByTag("option");
        for(Element element:items){
            String state_id=element.attr("value");
            String state=element.text();
            //checking it has valid state_id
            if(!state_id.equalsIgnoreCase("")) {
                state_ids.add(state_id);
                id_state.put(state_id, state);
            }
        }
        return state_ids;
    }
    public  HashSet<String> getCityids(String rawResponse,String state_id){
        HashSet<String>city_ids=new HashSet<String>();
        Document doc = Jsoup.parse(rawResponse);
        Elements items=doc.getElementsByTag("option");
        for(Element element:items){
            String city_id=element.attr("value");
            if(!city_id.equalsIgnoreCase("")) {
                city_ids.add(city_id);
                city_state.put(element.text(),id_state.get(state_id));
            }
        }
        return city_ids;
    }

    @Override
    public List<CrawlCommodityPriceDto> parseCommodityPrice(String rawResponse) {
        List<CrawlCommodityPriceDto> priceDtos = new ArrayList<>();
        Document doc=Jsoup.parse(rawResponse);
        Elements elements=doc.getElementsByClass("row");
        for(Element element:elements){
            try{
                Elements items = element.getElementsByClass("col-lg-4 col-xs-4 col-sm-4");
                Element commodity=items.first();
                String commodity_text=commodity.getElementsByTag("a").first().text();
                String city_text= commodity.getElementsByTag("small").first().text();
                String min_max=commodity.nextElementSibling().nextElementSibling().getElementsByTag("small").first().text();
                Double min=Double.parseDouble(min_max.split("-")[0]);
                Double max=Double.parseDouble(min_max.replace(min_max.split("-")[0],""));
                String state=city_state.get(city_text);
                priceDtos.add(CrawlCommodityPriceDto.Builder.crawlCommodityPriceDto()
                        .withProductName(commodity_text)
                        .withAreaName(city_text)
                        .withCity(city_text)
                        .withState(state)
                        .withMinPrice(min)
                        .withMaxPrice(max)
                        .withDate(date)
                        .withSource(CommodityPriceSource.AGMART)
                        .build());
            }
            catch (Exception e){
                System.out.println("No valid Element");
            }
        }
        return priceDtos;
    }
}
