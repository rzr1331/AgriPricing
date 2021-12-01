package parsers.kissanSuvidha;

import parsers.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class KissanSuvidhaCrawlingService {
    public List<CrawlCommodityPriceDto> handleRequest(Long date) {
        date =date-DateUtils.DAY;

        HttpRequestDto httpRequestDto = buildRequest();

        HttpClientPool httpClientPool = new HttpClientPool();
        List<CrawlCommodityPriceDto> crawlCommodityPriceDtoList = new ArrayList<>();    
        try {
            HttpResponseDto responseDto = httpClientPool.executeRequest(httpRequestDto);
            if (!responseDto.getSuccessful()) {
                System.out.println("error while getting response for enam reqeuset [{}] , response [{}]" +
                        httpRequestDto + responseDto);
                return new ArrayList<>();
            }
//            System.out.println(responseDto.getResponseString());
            KissanSuvidhaParser kissanSuvidhaParser=new KissanSuvidhaParser();
            for (String commodityName: kissanSuvidhaParser.getValues(responseDto.getResponseString(),"Commodity_Name")){
                httpRequestDto=buildRequest(commodityName);
                responseDto=httpClientPool.executeRequest(httpRequestDto);
                if (responseDto.getSuccessful()) {
                    for(String stateName: kissanSuvidhaParser.getValues(responseDto.getResponseString(), "State_Name")){
                        httpRequestDto =buildRequest(commodityName,stateName);
                        responseDto=httpClientPool.executeRequest(httpRequestDto);
                        if(responseDto.getSuccessful()){
                            for(String districtName: kissanSuvidhaParser.getValues(responseDto.getResponseString(),"District_Name")){
                                httpRequestDto =buildRequest(commodityName,stateName,districtName);
                                responseDto=httpClientPool.executeRequest(httpRequestDto);
                                if(responseDto.getSuccessful()){
                                    for(String marketName: kissanSuvidhaParser.getValues(responseDto.getResponseString(),"Market_Name")){
                                        httpRequestDto = buildRequest(commodityName,stateName,districtName,marketName);
                                        responseDto=httpClientPool.executeRequest(httpRequestDto);
                                        if(responseDto.getSuccessful()){
                                            crawlCommodityPriceDtoList.addAll(kissanSuvidhaParser.parseCommodityPrice(responseDto.getResponseString(),date));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return  crawlCommodityPriceDtoList;
        } catch (IOException e) {
            System.out.println("error while getting data  for domain {}" + CommodityPriceSource.ENAM);
        }

        return new ArrayList<>();
    }
    private HttpRequestDto buildRequest() {

        String url = CommodityPriceSource.KISSANSUVIDHA.getUrl()+"POST_Commodity_Rev_Kisan";

        String payload = "Pswd=abc^123";

        Map<String, String> headers = HttpHeaderUtils.getApplicationFormURLEncodedHeaders();

        return HttpRequestDto.Builder.httpRequestDto()
                .withRequestType(RequestType.POST)
                .withUrl(url)
                .withHeaders(headers)
                .withPayload(payload)
                .build();
    }
    private HttpRequestDto buildRequest(String commodityName) {

        String url = CommodityPriceSource.KISSANSUVIDHA.getUrl()+"POST_State_Rev_Kisan";

        String payload = "Pswd=abc^123&commodity="+commodityName;

        Map<String, String> headers = HttpHeaderUtils.getApplicationFormURLEncodedHeaders();

        return HttpRequestDto.Builder.httpRequestDto()
                .withRequestType(RequestType.POST)
                .withUrl(url)
                .withHeaders(headers)
                .withPayload(payload)
                .build();
    }
    private HttpRequestDto buildRequest(String commodityName,String stateName){
        String url = CommodityPriceSource.KISSANSUVIDHA.getUrl()+"POST_District_Rev_Kisan";

        String payload = "Pswd=abc^123&commodity="+commodityName+"&state="+stateName;

        Map<String, String> headers = HttpHeaderUtils.getApplicationFormURLEncodedHeaders();

        return HttpRequestDto.Builder.httpRequestDto()
                .withRequestType(RequestType.POST)
                .withUrl(url)
                .withHeaders(headers)
                .withPayload(payload)
                .build();
    }
    private HttpRequestDto buildRequest(String commodityName,String stateName,String districtName){
        String url = CommodityPriceSource.KISSANSUVIDHA.getUrl()+"POST_Market_Rev_Kisan";

        String payload = "Pswd=abc^123&commodity="+commodityName+"&state="+stateName+"&district="+districtName;

        Map<String, String> headers = HttpHeaderUtils.getApplicationFormURLEncodedHeaders();

        return HttpRequestDto.Builder.httpRequestDto()
                .withRequestType(RequestType.POST)
                .withUrl(url)
                .withHeaders(headers)
                .withPayload(payload)
                .build();
    }
    private HttpRequestDto buildRequest(String commodityName,String stateName,String districtName,String marketName){
        String url = CommodityPriceSource.KISSANSUVIDHA.getUrl()+"POST_Data_Kisan";

        String payload = "Pswd=abc^123&commodity="+commodityName+"&state="+stateName+"&district="+districtName+"&market="+marketName;

        Map<String, String> headers = HttpHeaderUtils.getApplicationFormURLEncodedHeaders();

        return HttpRequestDto.Builder.httpRequestDto()
                .withRequestType(RequestType.POST)
                .withUrl(url)
                .withHeaders(headers)
                .withPayload(payload)
                .build();
    }
}
