package parsers.pfTirumalatmtSteelPrice;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import parsers.CrawlCommodityPriceDto;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class PfTirumalaSteelPriceParser {
    public List<CrawlCommodityPriceDto> parseCommodityPrice(String responseString) {
        Document doc= Jsoup.parse(responseString);
        Element table = doc.getElementById("tblprice");
        Elements rows=table.getElementsByTag("tbody").first().getElementsByTag("tr");
        String district =doc.getElementById("table-price").getElementsByTag("p").first().text().replace("CONSUMER PRICE-- ","");
        for(Element row:rows){
            Elements columns=row.getElementsByTag("td");
            if(columns.size()==3){
                String type=columns.get(1).text();
                String priceStr=columns.get(2).text().replace(" ","");
                Double price= Double.parseDouble(priceStr);
                System.out.println("District: "+district+",Type: "+type+",Price: "+price);
            }
        }
        return new ArrayList();
    }
    public HashSet<String> getDistricts(String responseString){
        HashSet<String> districts=new HashSet<String>();
        Document doc = Jsoup.parse(responseString);
        Element option = doc.getElementById("districtt");
        for(Element element:option.getElementsByAttribute("value")){
            String value = element.attr("value");
            if(!value.isEmpty()){
                districts.add(value);
            }
        }
        return districts;
    }
}
