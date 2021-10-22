package parsers.JswNeoSteel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import parsers.CrawlCommodityPriceDto;

//import javax.swing.text.Document;
import java.util.HashSet;
import java.util.List;

public class JSWNeoSteelParser {

    public List<CrawlCommodityPriceDto> parseCommodityPrice(String responseString) {
        return null;
    }
    public HashSet<String>getStateValues(String responseString){
        HashSet<String>stateValues=new HashSet<String>();
        Document doc= Jsoup.parse(responseString);
        Element element = doc.getElementById("ContentPlaceHolder1_ddlstate");
        for(Element state:element.getElementsByTag("option")){
            String value=state.attr("value");
            if(value.length()>0){
                stateValues.add(value);
            }
        }
        return stateValues;
    }
    public HashSet<String>getDistrictValues(String responseString){
        HashSet<String>districtValues=new HashSet<String>();
        Document doc= Jsoup.parse(responseString);
        Element element = doc.getElementById("ContentPlaceHolder1_ddldistrict");
        for(Element district:element.getElementsByTag("option")){
            String value=district.attr("value");
            if(value.length()>0){
                districtValues.add(value);
            }
        }
        return districtValues;
    }
}
