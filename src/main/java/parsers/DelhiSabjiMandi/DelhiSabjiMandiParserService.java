package parsers.DelhiSabjiMandi;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import parsers.CommodityPriceSource;
import parsers.CrawlCommodityPriceDto;

import java.util.ArrayList;
import java.util.List;

public class DelhiSabjiMandiParserService {
    public List<CrawlCommodityPriceDto> parseCommodityPrice(String responseString,Long date) {
        List<CrawlCommodityPriceDto> priceDtos = new ArrayList<>();
        Document doc= Jsoup.parse(responseString);
        Elements tables=doc.getElementsByClass("wp-block-table is-style-stripes");
        for(Element table:tables){
//            System.out.println(row.text());
            Elements rows=table.getElementsByTag("tr");
            for(Element row:rows){
                Elements columns=row.getElementsByTag("td");
                if(columns.size()==3){
                    String quantity=columns.get(2).text();
                    String unit;
                    if(quantity.toLowerCase().contains("kg")){
                        quantity=quantity.toLowerCase().replace("kg","").replace(" ","");
                        unit="Kg";
                    }
                    else if(quantity.toLowerCase().contains("g")){
                        quantity=quantity.toLowerCase().replace("g","").replace(" ","");
                        unit="g";
                    }
                    else{
                        continue;
                    }
                    try{
                        double q=Double.parseDouble(quantity);
                        if(unit.equalsIgnoreCase("g")){
                            q=q/1000;
                        }
                        double price=(Double.parseDouble(columns.get(1).text())*100)/q;
                        String commodityName=columns.get(0).text();
                        int index=commodityName.indexOf('(');
                        if(index!=-1){
                            commodityName=commodityName.substring(0,index);
                        }
                        priceDtos.add(CrawlCommodityPriceDto.Builder.crawlCommodityPriceDto()
                                .withProductName(commodityName)
                                .withAreaName("Delhi")
                                .withCity("Delhi")
                                .withState("Delhi")
                                .withMinPrice(price)
                                .withMaxPrice(price)
                                .withDate(date)
                                .withSource(CommodityPriceSource.DELHISABJIMANDI)
                                .build());
                    }
                    catch(Exception e){
                        System.out.println("Not valid Element");
                    }
                }
            }
        }
        return priceDtos;
    }
}
