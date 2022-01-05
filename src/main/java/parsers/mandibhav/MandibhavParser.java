package parsers.mandibhav;

//import javax.swing.text.Document;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import parsers.CommodityPriceSource;
import parsers.CrawlCommodityPriceDto;
import parsers.ParserUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class MandibhavParser {
    public HashSet<String> getStates(String responseString) {
//        System.out.println(responseString);
        HashSet<String> states = new HashSet<String>();
        Document doc = Jsoup.parse(responseString);
        Element statesdiv = doc.getElementById("wrapper");
//        System.out.println(statesdiv.html());
        for(Element statelement:statesdiv.getElementsByTag("li")){
            states.add(statelement.getElementsByTag("a").first().attr("href"));
        }
        return states;
    }
    public List<CrawlCommodityPriceDto> parseCommodityPrice(String responseString,long date) {
        List<CrawlCommodityPriceDto> priceDtos = new ArrayList<>();
        Document doc = Jsoup.parse(responseString);
        String state = doc.getElementsByClass("text-center").first().text().replace("Mandi Bhav Today | आज का मंडी भाव | Commodity Market Rate","");
        Element table = doc.getElementsByTag("tbody").first();
        for(Element row :table.getElementsByTag("tr")){
            System.out.println(row.html());
            Elements cols = row.getElementsByTag("td");
            String commodity = cols.get(0).getElementsByTag("small").first().text();
            String minPriceText = cols.get(1).text().replace(cols.get(1).getElementsByTag("a").first().text(),"");
            String maxPriceText = cols.get(2).text().replace(cols.get(2).getElementsByTag("a").first().text(),"");
            Double maxPrice = ParserUtils.parseRawStringToDouble(minPriceText.substring(1).replace("-",""));
            Double minPrice = ParserUtils.parseRawStringToDouble(minPriceText.substring(2).replace("-",""));
            String minPriceCity = cols.get(1).getElementsByTag("a").first().text();
            System.out.println(commodity+minPrice+maxPrice);
            priceDtos.add(CrawlCommodityPriceDto.Builder.crawlCommodityPriceDto()
                    .withProductName(commodity)
                    .withAreaName(minPriceCity)
                    .withCity(minPriceCity)
                    .withState(state)
                    .withMinPrice(minPrice)
                    .withMaxPrice(maxPrice)
                    .withDate(date)
                    .withSource(CommodityPriceSource.MANDIBHAV)
                    .build());
        }
//        System.out.println(responseString);
        return priceDtos;
    }
}
