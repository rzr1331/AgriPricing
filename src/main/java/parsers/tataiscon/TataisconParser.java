package parsers.tataiscon;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.util.HashSet;
import java.util.Set;

public class TataisconParser {
    public Set<String>getStates(String rawResonse){
        Document doc= Jsoup.parse(rawResonse);
        Element states=doc.getElementById("rcp_state");
        Set<String>statesStr=new HashSet<String>();
        for(Element state: states.getElementsByAttribute("value")){
            statesStr.add(state.attributes().get("value"));
        }
        return statesStr;
    }
    public Set<String>getDistricts(String rawResonse){
        Document doc=Jsoup.parse(rawResonse);
        Elements districts=doc.getElementsByAttribute("value");
        Set<String>districtsStr=new HashSet<String>();
        for(Element district:districts){
            districtsStr.add(district.attr("value"));
        }
        return districtsStr;
    }
    public void ParseCommodityPrice(String rawResonse,String state,String district){
        Document doc=Jsoup.parse(rawResonse);
        Elements cols =doc.getElementsByClass("col-6");
        if(cols.size()==2){
            Elements leftCol=cols.first().getElementsByTag("p");
            Elements rightCol=cols.get(1).getElementsByTag("p");
            for(int row=0;row<leftCol.size();row++){
                System.out.println("State: "+state+",District:"+district+",Type: "+leftCol.get(row).text()+",Price: "+rightCol.get(row).text());
            }
        }
        return;
    }
}
