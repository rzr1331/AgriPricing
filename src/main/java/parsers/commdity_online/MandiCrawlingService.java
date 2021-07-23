package parsers.commdity_online;
import org.jsoup.Jsoup;
import parsers.CommodityPriceSource;
import parsers.CrawlCommodityPriceDto;
import java.io.IOException;
import java.util.List;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class MandiCrawlingService {
    private String base_url=CommodityPriceSource.COMMODITY_ONLINE.getUrl();
    public List<CrawlCommodityPriceDto> handleRequest() {
        int n=1;
        int count=0;
        List<CrawlCommodityPriceDto> crawlCommodityPriceDtoList=null;
        while(n>0){
            Document doc=BuildRequest(count);
            MandiParser mandiParser=new MandiParser();
            if(crawlCommodityPriceDtoList==null){
                crawlCommodityPriceDtoList=mandiParser.parseCommodityPrice(doc.toString());
            }
            else{
                crawlCommodityPriceDtoList.addAll(mandiParser.parseCommodityPrice(doc.html()));
            }
            Elements items =doc.getElementsByClass("mob_p_12");
            n=items.size();
            count+=n;
        }
        return crawlCommodityPriceDtoList;
    }
    private Document BuildRequest(int count){
        Document doc = null;
        try {
            doc = Jsoup.connect(base_url+count).get();
        } catch (IOException e) {
            System.out.println("error while getting data  for domain {}" + CommodityPriceSource.COMMODITY_ONLINE);
        }
//        System.out.println(base_url+count);
        return doc;
    }
}
