package parsers.jindalpanther;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import parsers.CrawlCommodityPriceDto;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JindalPatherParser {
    String getCsrfToken(String rawResponse){
        Document doc = Jsoup.parse(rawResponse);
        Elements elements = doc.getElementsByAttributeValueMatching("name","csrf-token");
        String csrfToken= elements.attr("content");
        return csrfToken;
    }
    Set<String> getStates(String rawResponse){
        Set<String>states=new HashSet<String>();
        Document doc =Jsoup.parse(rawResponse);
        Elements elements= doc.getElementsByClass("chosen-select js-pantherState").first().getElementsByTag("option");
        for(Element element : elements){
            if(element.attr("value")!=""){
                states.add(element.attr("value"));
            }
        }
        return states;
    }

    public HashSet<String> getDistricts(String responseString) {
        HashSet<String>districts=new HashSet<String>();
        JSONObject jsonObject = new JSONObject(responseString);
        String htmlResponse= (String) jsonObject.get("template");
        Document doc=Jsoup.parse(htmlResponse);
        Elements elements = doc.getElementsByAttribute("value");
        for(Element element:elements){
            String district = element.attr("value");
            if(district.length()>0)
            districts.add(element.text());
        }
        return districts;
    }
    public List<CrawlCommodityPriceDto> parseCommodityPrice(String rawResponse,String state,String district) {
        Document doc= Jsoup.parse(rawResponse);
        Element tbody=doc.getElementById("js-sectionList");
        for(Element row:tbody.getElementsByTag("tr")){
            Elements columns = row.getElementsByTag("td");
            if(columns.size()==2){
                String type= columns.get(0).text();
                String price = columns.get(1).text();
                System.out.println("Type:"+type+" State:"+state+" District:"+district);
            }
        }
        return null;
    }
}
