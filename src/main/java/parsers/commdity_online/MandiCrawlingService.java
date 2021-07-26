package parsers.commdity_online;
import org.jsoup.Jsoup;
import parsers.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class MandiCrawlingService {
    private String base_url=CommodityPriceSource.COMMODITY_ONLINE.getUrl();
    public List<CrawlCommodityPriceDto> handleRequest() {
        int n=1;
        int count=0;
        List<CrawlCommodityPriceDto> crawlCommodityPriceDtoList=null;
        while(n>0){
            HttpRequestDto httpRequestDto = buildRequest(count);
            HttpClientPool httpClientPool = new HttpClientPool();
            try {
                HttpResponseDto responseDto = httpClientPool.executeRequest(httpRequestDto);
                if (!responseDto.getSuccessful()) {
                    System.out.println("error while getting response for enam reqeuset [{}] , response [{}]" +
                            httpRequestDto + responseDto);
                    return new ArrayList<>();
                }
                MandiParser mandiParser = new MandiParser();
                Document doc=Jsoup.parse(responseDto.getResponseString());
                Elements items =doc.getElementsByClass("mob_p_12");
                n=items.size();
                count+=n;
                System.out.println(count);
                if(crawlCommodityPriceDtoList==null){
                    crawlCommodityPriceDtoList=mandiParser.parseCommodityPrice(responseDto.getResponseString());
                }
                else{
                    crawlCommodityPriceDtoList.addAll(mandiParser.parseCommodityPrice(responseDto.getResponseString()));
                }
            } catch (IOException e) {
                n=0;
                System.out.println("error while getting data  for domain {}" + CommodityPriceSource.ENAM);
            }
        }
        if(crawlCommodityPriceDtoList==null){
            return new ArrayList();
        }
        return crawlCommodityPriceDtoList;
    }
    private HttpRequestDto buildRequest(int count){
        String url = CommodityPriceSource.COMMODITY_ONLINE.getUrl()+count;
        Map<String, String> headers = HttpHeaderUtils.getApplicationFormURLEncodedHeaders();
        return HttpRequestDto.Builder.httpRequestDto()
                .withRequestType(RequestType.GET)
                .withUrl(url)
                .withHeaders(headers)
                .withPayload("")
                .build();
    }
}
