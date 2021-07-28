package parsers.agriplus;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import parsers.CommodityPriceSource;
import parsers.CrawlCommodityPriceDto;
import parsers.DateUtils;

import java.util.ArrayList;
import java.util.List;

public class AgriPlusParser {
    private static final String mandiParserDateFormat = "dd MMM";
    public List<CrawlCommodityPriceDto> parseCommodityPrice(String rawResponse) {
        List<CrawlCommodityPriceDto> priceDtos = new ArrayList<>();
        Document doc= Jsoup.parse(rawResponse);
        Element data = doc.getElementById("dataTable");
        if(data == null){
            return priceDtos;
        }
        Elements items =data.getElementsByTag("tr");
        int n=items.size();
//        System.out.println(n);
        if(n>0){
            items.remove(0);
//            System.out.println(items.text());
            for(Element element : items){
//                System.out.println(element);
                Elements rowitems=element.getElementsByTag("td");
//                System.out.println(rowitems.size());
                String commodity=rowitems.get(4).text();
                String state=rowitems.get(1).text();
                String area=rowitems.get(3).text();
                String city=rowitems.get(2).text();
                Double minPrice=Double.parseDouble(rowitems.get(6).text());
                Double maxPrice=Double.parseDouble(rowitems.get(7).text());
                String date_str=rowitems.get(9).text();
                Long date= DateUtils.parseToLong(mandiParserDateFormat,date_str);
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