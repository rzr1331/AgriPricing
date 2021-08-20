package parsers.agriplus;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import parsers.CommodityPriceSource;
import parsers.CrawlCommodityPriceDto;
import parsers.DateUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class AgriPlusParser {
    private static final String mandiParserDateFormat = "dd MMM YYYY";
    private static long dateRequired;

    public static void setDate(long date) {
        dateRequired = date;
    }

    public List<CrawlCommodityPriceDto> parseCommodityPrice(String rawResponse) {
        List<CrawlCommodityPriceDto> priceDtos = new ArrayList<>();
        Document doc= Jsoup.parse(rawResponse);
        Element data = doc.getElementById("dataTable");
        if(data == null){
            return priceDtos;
        }
        Elements items =data.getElementsByTag("tr");
        SimpleDateFormat df = new SimpleDateFormat(mandiParserDateFormat);
        String dateText = df.format(dateRequired);
        Long date= DateUtils.parseToLong(mandiParserDateFormat,dateText);
        int n=items.size();
        if(n>0){
            items.remove(0);
            for(Element element : items){
                Elements rowitems=element.getElementsByTag("td");
                String commodity=rowitems.get(4).text();
                String state=rowitems.get(1).text();
                String area=rowitems.get(3).text();
                String city=rowitems.get(2).text();
                Double minPrice=Double.parseDouble(rowitems.get(6).text());
                Double maxPrice=Double.parseDouble(rowitems.get(7).text());
                String date_str=rowitems.get(9).text();
                if(!date_str.equalsIgnoreCase(dateText.substring(0,6))){
                    continue;
                }
                System.out.println(date_str);
                System.out.println("Commodity:"+commodity+",Area:"+area+",Minprice:"+minPrice+",MaxPrice:"+maxPrice+"Date:"+date+",State:"+state+" dateReq:"+dateRequired);
                priceDtos.add(CrawlCommodityPriceDto.Builder.crawlCommodityPriceDto()
                        .withProductName(commodity)
                        .withAreaName(area)
                        .withCity(city)
                        .withState(state)
                        .withMinPrice(minPrice)
                        .withMaxPrice(maxPrice)
                        .withDate(date)
                        .withSource(CommodityPriceSource.AGRIPLUS)
                        .build());
            }
        }
        return priceDtos;
    }
    public HashSet<String> getStates(String rawResponse){
        Document doc= Jsoup.parse(rawResponse);
        HashSet<String> states_list=new HashSet<String>();
        Elements items = doc.getElementsByClass("card-wrapper");
        for(Element element:items){
            String link=element.select("a[href]").attr("href");
            states_list.add(link.split("/")[4]);
//            System.out.println(link.split("/")[4]);
        }
        return states_list;
    }
    public HashSet<String> get_coms(String rawResponse){
        Document doc= Jsoup.parse(rawResponse);
        HashSet<String> coms_list=new HashSet<String>();
        Elements items = doc.getElementsByClass("card-wrapper");
        for(Element element:items){
            String link=element.select("a[href]").attr("href");
            coms_list.add(link.split("/")[4]);
//            System.out.println(link);
        }
        return coms_list;
    }
}