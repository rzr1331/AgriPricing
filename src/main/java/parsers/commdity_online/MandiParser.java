package parsers.commdity_online;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import parsers.CommodityPriceSource;
import parsers.CrawlCommodityPriceDto;
import parsers.DateUtils;
import java.util.ArrayList;
import java.util.List;

public class MandiParser {
    private static final String mandiParserDateFormat = "dd MMM yyyy";
    public List<CrawlCommodityPriceDto> parseCommodityPrice(String rawResponse) {
        List<CrawlCommodityPriceDto> priceDtos = new ArrayList<>();
        Document doc= Jsoup.parse(rawResponse);
        Elements items =doc.getElementsByClass("dt_ta_09");
        int n=items.size();
        if(n>0){
            for(Element element : items){
                String commodity=element.getElementsByClass("dt_ta_10").text();
                String state=element.getElementsByClass("dt_ta_17").text();
                String area=element.getElementsByClass("dt_ta_11").text().replace(state,"");
                Elements prices=element.getElementsByClass("dt_ta_14");
                String[] str3 = prices.get(1).text().split("/ ");
                Double minPrice=Double.parseDouble(str3[0]);
                Double maxPrice=Double.parseDouble(str3[1]);
                String date_str=doc.getElementsByClass("la_update-style as_padd").text().substring(15);
                Long date=DateUtils.parseToLong(mandiParserDateFormat,date_str);
                System.out.println("Commodity:"+commodity+",Area:"+area+",Minprice:"+minPrice+",MaxPrice:"+maxPrice+"Date:"+date+",State:"+state);
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
}
