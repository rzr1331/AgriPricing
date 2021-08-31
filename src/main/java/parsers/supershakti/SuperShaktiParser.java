package parsers.supershakti;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import parsers.CrawlCommodityPriceDto;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class SuperShaktiParser {

    public List<CrawlCommodityPriceDto> parseCommodityPrice(String responseString) {
        System.out.println(responseString);
        return new ArrayList<>();
    }
    public HashSet<String> getStates(String responseString){
        HashSet<String>statList = new HashSet<String>();
        Document doc = Jsoup.parse(responseString);
        Element states = doc.getElementById("fro-state");
        for(Element state:states.getAllElements()){
            String stateStr=state.attr("value");
            if(!stateStr.isEmpty())
//            System.out.println(state.attr("value"));
            statList.add(stateStr);
        }
        return statList;
    }

    public HashSet<String> getTypes(String responseString) {
        HashSet<String> typeslist =new HashSet<String>();
        Document doc=Jsoup.parse(responseString);
        for(Element type:doc.getElementsByAttribute("value")){
            String typeStr=type.attr("value");
            typeslist.add(typeStr);
        }
        return typeslist;
    }
}
