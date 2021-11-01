package parsers.JswNeoSteel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import parsers.CrawlCommodityPriceDto;

//import javax.swing.text.Document;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class JSWNeoSteelParser {

    public List<CrawlCommodityPriceDto> parseCommodityPrice(String responseString) {
        System.out.println(responseString);
        Document doc = Jsoup.parse(responseString);
        Elements elements = doc.getElementsByClass("table table-responsive col-3-tble").first().getElementsByTag("tr");
        for(int i=1;i<elements.size();i++){
            Element element = elements.get(i);
            Elements columns=element.getElementsByTag("td");
            if(columns.size()==2){
                String type=columns.get(0).text();
                String price=columns.get(1).text();
                System.out.println(type+" "+price);
            }
        }
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
    public HashMap<String,String[]> getDistrictValues(String responseString){
        HashMap<String, String[]> districtValues=new HashMap<>();
        Document doc= Jsoup.parse(responseString);
        Element element = doc.getElementById("ContentPlaceHolder1_ddldistrict");
        int start=responseString.indexOf("__VIEWSTATE");
        int end=responseString.indexOf("|",start+12);
        String substr=responseString.substring(start+12,end);
        substr=substr.replace("/","%2F");
        substr=substr.replace("+","%2B");
        String genarator="__VIEWSTATEGENERATOR";
        int stateGenaratorindex=responseString.indexOf(genarator)+genarator.length()+1;
        String viewStateGenarator=responseString.substring(stateGenaratorindex,responseString.indexOf("|",stateGenaratorindex));
        for(Element district:element.getElementsByTag("option")){
            String value=district.attr("value");
            if(value.length()>0){
                districtValues.put(value, new String[]{substr, viewStateGenarator});
            }
        }
        return districtValues;
    }

    public String getViewState(String responseString) {
        int start=responseString.indexOf("__VIEWSTATE");
        int end=responseString.indexOf("|",start+12);
        String substr=responseString.substring(start+12,end);
        substr=substr.replace("/","%2F");
        substr=substr.replace("+","%2B");
        System.out.println(substr);
        return substr;
    }
}
