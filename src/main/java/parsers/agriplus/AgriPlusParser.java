package parsers.agriplus;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import parsers.CommodityPriceSource;
import parsers.CrawlCommodityPriceDto;
import parsers.DateUtils;
import parsers.ICommodityPricingParser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class AgriPlusParser implements ICommodityPricingParser {
    private static final String mandiParserDateFormat = "dd MMM";
    public List<CrawlCommodityPriceDto> parseCommodityPrice(String rawResponse) {
        List<CrawlCommodityPriceDto> priceDtos = new ArrayList<>();
        Document doc= Jsoup.parse(rawResponse);
        Element data = doc.getElementById("dataTable");
        if(data == null){
            return priceDtos;
        }
        Elements items =data.getElementsByTag("tr");
        int n=items.size();
        if(n>0){
            items.remove(0);
            for(Element element : items){
                Elements rowitems=element.getElementsByTag("td");
                String commodity=rowitems.get(4).text();
                String state=rowitems.get(1).text();
                String area=rowitems.get(3).text();
                String city=rowitems.get(2).text();
                Double minPrice=Double.parseDouble(rowitems.get(6).text());
                Double maxPrice=Double.parseDouble(rowitems.get(7).text());
                //Validation of min and max price
                if(!(minPrice>0&&maxPrice>0)){
                    continue;
                }
                String date_str=rowitems.get(9).text();
                Long date= DateUtils.parseToLong(mandiParserDateFormat,date_str);
//                System.out.println("Commodity:"+commodity+",Area:"+area+",Minprice:"+minPrice+",MaxPrice:"+maxPrice+"Date:"+date+",State:"+state);
                priceDtos.add(CrawlCommodityPriceDto.Builder.crawlCommodityPriceDto()
                        .withProductName(commodity)
                        .withAreaName(area)
                        .withCity(area)
                        .withState(state)
                        .withMinPrice(minPrice)
                        .withMaxPrice(maxPrice)
                        .withDate(date)
                        .withSource(CommodityPriceSource.AGRIPLUS)
                        .build());
            }
        }
        return priceDtos;
    }
    public HashSet<String> getStates(String rawResponse){
        Document doc= Jsoup.parse(rawResponse);
        HashSet<String> states_list=new HashSet<String>();
        Elements items=doc.getElementById("state").getElementsByTag("option");
        for(Element element:items){
            String state=element.text();
            if(state.equalsIgnoreCase("select state")){
                continue;
            }

            state=state.replace(" ","-");
            state=state.replace(")","");
            state =state.replace("(","-");
            states_list.add(state.toLowerCase());
//            System.out.println(state.toLowerCase());
        }
        return states_list;

    }
    public HashSet<String> get_coms(String rawResponse){
        Document doc= Jsoup.parse(rawResponse);
        HashSet<String> coms_list=new HashSet<String>();
        Elements items=doc.getElementById("commodity").getElementsByTag("option");
        for(Element element:items){
            String commodity=element.text();
            if(commodity.equalsIgnoreCase("Any Commodity")){
                continue;
            }
//            System.out.print(commodity.toLowerCase()+":   ");
            commodity=commodity.replace(" ","-").replace("/","-");
            commodity=commodity.replace(")","").replace(",","-");
            commodity =commodity.replace("(","-").replace(".","-").replace("--","-");
            coms_list.add(commodity.toLowerCase());
        }
        return coms_list;
    }
}