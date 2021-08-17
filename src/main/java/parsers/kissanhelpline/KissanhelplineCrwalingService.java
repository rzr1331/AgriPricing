package parsers.kissanhelpline;

import parsers.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static parsers.CommodityPriceSource.COMMODITY_ONLINE;
import static parsers.CommodityPriceSource.KISSANHELPLINE;

public class KissanhelplineCrwalingService {


    public List<CrawlCommodityPriceDto> handleRequest(Long date) {
        List<CrawlCommodityPriceDto> crawlCommodityPriceDtoList =null;
        HttpClientPool httpClientPool = new HttpClientPool();
        int ofset=0;
        while(true){
            try{
                HttpRequestDto httpRequestDto = buildRequest(date,ofset);
                HttpResponseDto responseDto = httpClientPool.executeRequest(httpRequestDto);
                if (!responseDto.getSuccessful()) {
                    System.out.println("error while getting response for kissanhelpline reqeuset [{}] , response [{}]" +
                            httpRequestDto + responseDto);
                    return new ArrayList<>();
                }
                KissanParser kissanParser=new KissanParser();
                List<CrawlCommodityPriceDto>ofsetlist = kissanParser.parseCommodityPrice(responseDto.getResponseString());
                if(ofsetlist.size()==0){
                    break;
                }
                else if(crawlCommodityPriceDtoList==null) {
                    crawlCommodityPriceDtoList = ofsetlist;
                }
                else{

                    crawlCommodityPriceDtoList.addAll(ofsetlist);
                }
            }
            catch (Exception e){
                System.out.println(e.getStackTrace());
            }
            ofset+=10;
        }
        return crawlCommodityPriceDtoList;
    }
    private HttpRequestDto buildRequest(long date,int offset) {
        String dateStr = DateUtils.format(new SimpleDateFormat("yyyy-MM-dd"), date);
        String payload = String.format("co=&st=&dt=&ofset=%s&country=1&date=%s",offset,dateStr);
        Map<String, String> headers = HttpHeaderUtils.getApplicationFormURLEncodedHeaders();
        return HttpRequestDto.Builder.httpRequestDto()
                .withRequestType(RequestType.POST)
                .withUrl(KISSANHELPLINE.getUrl())
                .withHeaders(headers)
                .withPayload(payload)
                .build();
    }
}
