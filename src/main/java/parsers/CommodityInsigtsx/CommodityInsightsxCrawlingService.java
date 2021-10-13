package parsers.CommodityInsigtsx;

import okhttp3.internal.http.HttpHeaders;
import parsers.*;

import java.util.List;

public class CommodityInsightsxCrawlingService {
    public List<CrawlCommodityPriceDto> handleRequest(Long date) {

        return null;
    }
    public HttpRequestDto buildRequest(){
        return HttpRequestDto.Builder.httpRequestDto()
                .withRequestType(RequestType.GET)
                .withUrl(CommodityPriceSource.COMMODITYINSIGHTSX.getUrl())
                .withHeaders(HttpHeaderUtils.getApplicationFormURLEncodedHeaders())
                .withPayload("")
                .build();
    }
    public HttpRequestDto buildRequest(String url){
        String payload=
        return HttpRequestDto.Builder.httpRequestDto()
                .withRequestType(RequestType.POST)
                .withUrl(CommodityPriceSource.COMMODITYINSIGHTSX.getUrl())
                .withHeaders(HttpHeaderUtils.getApplicationFormURLEncodedHeaders())
                .withPayload("")
                .build();
    }
}
