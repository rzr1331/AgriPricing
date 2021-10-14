package parsers.KiranInfraispat;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import parsers.CrawlCommodityPriceDto;

import java.util.List;

public class KiranInfraIspatParser {

    public List<CrawlCommodityPriceDto> parseCommodityPrice(String responseString) {
        Document doc = Jsoup.parse(responseString);
        Element tbody = doc.getElementsByTag("tbody").first();
        for(Element el:tbody.getElementsByTag("tr")){
            Elements columns = el.getElementsByTag("td");
            if(columns.size()==2) {
                String type = columns.get(0).text();
                String priceStr = columns.get(1).text().replace("Rs. ","");
                double price =  Double.parseDouble(priceStr);
                System.out.println("Type"+type+" , price "+price);
            }
        }
        return null;
    }
}
