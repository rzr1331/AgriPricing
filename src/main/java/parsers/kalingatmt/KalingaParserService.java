package parsers.kalingatmt;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import parsers.CrawlCommodityPriceDto;

import java.util.List;

public class KalingaParserService {
    public List<CrawlCommodityPriceDto> parseCommodityPrice(String rawResponse) {
        Document doc = Jsoup.parse(rawResponse);
        Element table = doc.getElementsByClass("table table-striped price-list").first();
        for(Element rows:table.getElementsByTag("tr")){
            Elements columns = rows.getElementsByTag("td");
            if(columns.size()==3) {
                String type =columns.get(1).text();
                String priceStr=columns.get(2).text().substring(1);
                double price = Double.parseDouble(priceStr);
                System.out.println("Type: "+type+" price: "+price);
            }
        }
        return null;
    }
}
