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
        Document doc = Jsoup.parse(responseString);
        Element info = doc.getElementById("ContentPlaceHolder1_divprice").getElementsByTag("span").first();
        String state = info.getElementsByTag("b").text();
        String district = info.text().replace(state, "");
        Elements elements = doc.getElementsByClass("table table-responsive col-3-tble").first().getElementsByTag("tr");
        for(int i=1;i<elements.size();i++){
            Element element = elements.get(i);
            Elements columns=element.getElementsByTag("td");
            if(columns.size()==2){
                String type=columns.get(0).text();
                String price=columns.get(1).text();
                System.out.println(state.replace("-"," ").trim()+" "+district.replace("> Dist. ","")+" "+type+" "+price);
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
        String viewState="__VIEWSTATE";
        int viewStateIndex=responseString.indexOf("__VIEWSTATE")+viewState.length()+1;
        String viewStateValue=responseString.substring(viewStateIndex,responseString.indexOf("|",viewStateIndex));
        viewStateValue=viewStateValue.replace("/","%2F");
        viewStateValue=viewStateValue.replace("+","%2B");
        String viewStateGenarator="__VIEWSTATEGENERATOR";
        int stateGenaratorindex=responseString.indexOf(viewStateGenarator)+viewStateGenarator.length()+1;
        String viewStateGenaratorValue=responseString.substring(stateGenaratorindex,responseString.indexOf("|",stateGenaratorindex));
        if(element != null){
            for(Element district:element.getElementsByTag("option")){
                String value=district.attr("value");
                if(value.length()>0){
                    districtValues.put(value, new String[]{viewStateValue, viewStateGenaratorValue});
                }
            }
        }
        return districtValues;
    }
}
