package parsers.CommodityInsigtsx;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import parsers.CommodityPriceSource;
import parsers.CrawlCommodityPriceDto;
import parsers.DateUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class CommodityInsightsxParser {
    public List<CrawlCommodityPriceDto> parseCommodityPrice(String responseString,long datereq) {
        List<CrawlCommodityPriceDto> priceDtos = new ArrayList<>();
        Document doc=Jsoup.parse(responseString);
        try{
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String dateReq = df.format(datereq);
            Elements elements = doc.getElementsByClass("row-card");
            Elements table=doc.getElementsByClass("table table-striped table-bordered").first().getElementsByTag("td");
            String state = table.get(1).text();
            String district  = table.get(3).text();
            for(Element element:elements){
                Elements columns = element.getElementsByTag("td");
                if(columns.size()==6){
                    String commodity= columns.get(0).text();
                    String minPriceText=columns.get(3).getElementsByTag("span").first().text();
                    String maxPriceText=columns.get(4).getElementsByTag("span").first().text();
                    Double minPrice=Double.parseDouble(minPriceText);
                    Double maxPrice=Double.parseDouble(maxPriceText);
                    String dateText=columns.get(2).text();
                    Long date=DateUtils.parseToLong("yyyy-mm-dd",dateText);
                    if(!dateText.equalsIgnoreCase(dateReq)){
                        continue;
                    }
//                    System.out.println(dateReq+" "+dateText);
//                    System.out.println(commodity+minPrice+maxPrice+" "+date);
                    priceDtos.add(CrawlCommodityPriceDto.Builder.crawlCommodityPriceDto()
                            .withProductName(commodity)
                            .withAreaName(district)
                            .withCity(district)
                            .withState(state)
                            .withMinPrice(minPrice)
                            .withMaxPrice(maxPrice)
                            .withDate(date)
                            .withSource(CommodityPriceSource.COMMODITYINSIGHTSX)
                            .build());
                }
            }

        }
        catch(Exception e){
            e.printStackTrace();
        }
        return priceDtos;
    }
    public HashSet<String>getUrls(String responseString){
        Document doc = Jsoup.parse(responseString);
        HashSet<String>urls=new HashSet<String>();
        Elements elements=doc.getElementsByClass("boxContent card");
        for(Element element:elements){
            urls.add(element.getElementsByTag("a").first().attr("href"));
        }
        return urls;
    }
}
