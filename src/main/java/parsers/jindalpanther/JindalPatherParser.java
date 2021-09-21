package parsers.jindalpanther;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashSet;
import java.util.Set;

public class JindalPatherParser {
    String getCsrfToken(String rawResponse){
        Document doc = Jsoup.parse(rawResponse);
        Elements elements = doc.getElementsByAttributeValueMatching("name","csrf-token");
        String csrfToken= elements.attr("content");
        return csrfToken;
    }
    Set<String>getStates(String rawResponse){
        Set<String>states=new HashSet<String>();
        Document doc =Jsoup.parse(rawResponse);
        Elements elements= doc.getElementsByClass("chosen-select js-pantherState").first().getElementsByTag("option");
        Elements stateElements=elements.last().getAllElements();
        for(Element element : elements){
            if(element.attr("value")!=""){
                states.add(element.attr("value"));
//                System.out.println(element.attr("value"));
            }
        }
        return states;
    }
}
