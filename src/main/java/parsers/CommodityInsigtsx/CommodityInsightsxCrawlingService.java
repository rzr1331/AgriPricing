package parsers.CommodityInsigtsx;
import org.apache.http.HttpHeaders;
import parsers.*;
import java.io.IOException;
import java.util.*;
public class CommodityInsightsxCrawlingService {
    public List<CrawlCommodityPriceDto> handleRequest(Long date) {
        HttpRequestDto httpRequestDto;
        HttpClientPool httpClientPool = new HttpClientPool();
        HashSet<String>marketurls=new HashSet<String>();
        List<CrawlCommodityPriceDto> crawlCommodityPriceDtoList = new ArrayList<>();
        HttpResponseDto responseDto;
        CommodityInsightsxParser commodityInsightsxParser=new CommodityInsightsxParser();
        int count=1;
        try {
            while(true){
                httpRequestDto = buildRequest(count);
                responseDto = httpClientPool.executeRequest(httpRequestDto);
                if (!responseDto.getSuccessful()) {
                    break;
                }
                marketurls.addAll(commodityInsightsxParser.getUrls(responseDto.getResponseString()));
                count++;
            }
            for(String url:marketurls){
                System.out.println(url);
                httpRequestDto = buildRequest(url);
                responseDto = httpClientPool.executeRequest(httpRequestDto);
                if (!responseDto.getSuccessful()) {
                    System.out.println("error while getting response for Commodity InsightSX reqeuset [{}] , response [{}]" + httpRequestDto + responseDto);
                    return new ArrayList<>();
                }
                crawlCommodityPriceDtoList.addAll(commodityInsightsxParser.parseCommodityPrice(responseDto.getResponseString(),date));
            }
            return crawlCommodityPriceDtoList;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("error while getting data  for domain {}" + CommodityPriceSource.COMMODITYINSIGHTSX);
        }
        return new ArrayList<>();
    }
    private HttpRequestDto buildRequest(int count){
        String url = "https://www.commodityinsightsx.com/markets/"+count;
        Map<String, String> headers = new HashMap<String,String>();
        headers.put(HttpHeaders.CONTENT_TYPE,"application/json");
        return HttpRequestDto.Builder.httpRequestDto()
                .withRequestType(RequestType.GET)
                .withUrl(url)
                .withHeaders(headers)
                .withPayload("")
                .build();
    }
    private HttpRequestDto buildRequest(String url){
        url = CommodityPriceSource.COMMODITYINSIGHTSX.getUrl()+url;
        Map<String, String> headers = new HashMap<String,String>();
        headers.put(HttpHeaders.CONTENT_TYPE,"application/json");
        System.out.println(url);
        return HttpRequestDto.Builder.httpRequestDto()
                .withRequestType(RequestType.GET)
                .withUrl(url    )
                .withHeaders(headers)
                .withPayload("")
                .build();
    }
}