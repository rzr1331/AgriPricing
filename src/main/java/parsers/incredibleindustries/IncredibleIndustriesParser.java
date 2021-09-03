package parsers.incredibleindustries;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import parsers.CrawlCommodityPriceDto;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class IncredibleIndustriesParser {
    public ArrayList<String> parseCommodityPrice(String rawResponse) {
        ArrayList<String>prices=new ArrayList<String>();
        Document doc=Jsoup.parse(rawResponse);
        Elements body=doc.getElementsByTag("tbody");
        for(Element row:body.first().getAllElements()){
            Elements rows=row.getElementsByTag("td");
            if(rows.size()==2) {
                String type= row.getElementsByTag("td").get(0).text();
                String price_str=row.getElementsByTag("td").get(1).text();
                price_str= price_str.replace("Rs.","").replace(" ","");
                if(price_str.length()>0&&!price_str.equalsIgnoreCase("0.00")){
                    prices.add(" Type:"+type+" price:"+price_str);
                }
            }
        }
        return prices;
    }
    public HashMap<String,String> getStates(String responseString) {
        HashMap<String,String>stateValues=new HashMap<String,String>();
        Document doc= Jsoup.parse(responseString);
        Elements states_list=doc.select("select.form-control[name=state_id]");
        for(Element stateId:states_list.first().getAllElements()){
            String val=stateId.val();
            String state=stateId.text();
            if(val.length()>0){
                stateValues.put(val,state);
            }
        }
        return stateValues;
    }


    public HashMap<String,String> getDistricts(String responseString) {
        HashMap<String,String> districts=new HashMap<String,String>();
        Document doc=Jsoup.parse(responseString);
        for(Element element:doc.getElementsByTag("option")){
            String val=element.val();
            String dis=element.text();
            if(val.length()>0){
                districts.put(val,dis);
            }
        }
        return districts;
    }
}
