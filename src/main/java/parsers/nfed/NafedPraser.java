package parsers.nfed;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import parsers.CrawlCommodityPriceDto;

import java.util.ArrayList;
import java.util.List;

public class NafedPraser {

    public List<CrawlCommodityPriceDto> parseCommodityPrice(String responseString) {
//        System.out.println(responseString);
        Document doc= Jsoup.parse(responseString);
        Elements commodities =doc.getElementsByClass("msp-g");
        Elements prices=doc.getElementsByClass("msp-o");
        System.out.println(prices.size());
        for(int i=0;i<commodities.size()&&i<prices.size();i++){
            String commodity=commodities.get(i).text().replace("^","");
            String price = prices.get(i).text().replace("₹ ","");
            System.out.println(commodity+" "+price);
        }
        return new ArrayList<>();
    }
}
