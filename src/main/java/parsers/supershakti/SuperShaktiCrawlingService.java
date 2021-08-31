package parsers.supershakti;

import parsers.*;
import parsers.enam.EnamParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class SuperShaktiCrawlingService {
    public List<CrawlCommodityPriceDto> handleRequest(){
        HttpRequestDto statesDto=getStates();
        HttpClientPool httpClientPool = new HttpClientPool();
        try {
            SuperShaktiParser  superShaktiParser = new SuperShaktiParser();
            HttpResponseDto responseDto = httpClientPool.executeRequest(statesDto);
            if (!responseDto.getSuccessful()) {
                System.out.println("error while getting response for SuperShakti states [{}] , response [{}]" +
                        statesDto + responseDto);
                return new ArrayList<>();
            }
            HashSet<String>states = superShaktiParser.getStates(responseDto.getResponseString());
            HashSet<String>types;
            for(String state:states){
                HttpRequestDto typesDto=getTypes(state);
                responseDto=httpClientPool.executeRequest(typesDto);
                if(!responseDto.getSuccessful()){
                    System.out.println("error while getting response for SuperShakti types [{}] , response [{}]" +
                            typesDto + responseDto);
                    continue;
                }
                types= superShaktiParser.getTypes(responseDto.getResponseString());
                for(String type:types){
                    HttpRequestDto httpRequestDto = buildRequest(state,type);
                    responseDto= httpClientPool.executeRequest(httpRequestDto);
                    if (!responseDto.getSuccessful()) {
                        System.out.println("error while getting response for SuperShakti requeset [{}] , response [{}]" +
                                statesDto + responseDto);
                        continue;
                    }
                    System.out.println("State: "+state+" Type: "+type+" Value: "+responseDto.getResponseString());
//                    superShaktiParser.parseCommodityPrice(responseDto.getResponseString());
                }
            }
        } catch (IOException e) {
            System.out.println("error while getting data  for domain {}" + CommodityPriceSource.ENAM);
        }
        return new ArrayList<>();
    }
    private HttpRequestDto buildRequest(String state, String type) {
        String productId=type.split("-")[0];
        String productType=type.split("-")[1];
        String payload = String.format("action=get_price&state=%s&area=%s&product_id=%s&size=%s",state,state,productId,productType);
        Map<String, String> headers = HttpHeaderUtils.getApplicationFormURLEncodedHeaders();
        return HttpRequestDto.Builder.httpRequestDto()
                .withRequestType(RequestType.POST)
                .withUrl(CommodityPriceSource.SUPERSHAKTI.getUrl())
                .withHeaders(headers)
                .withPayload(payload)
                .build();

    }
    public HttpRequestDto getStates(){
        Map<String, String> headers = HttpHeaderUtils.getApplicationFormURLEncodedHeaders();
        return HttpRequestDto.Builder.httpRequestDto()
                .withRequestType(RequestType.GET)
                .withUrl("https://supershakti.in/price-calculator/")
                .withHeaders(headers)
                .withPayload("")
                .build();
    }
    public HttpRequestDto getTypes(String state){
        String payload = String.format("action=get_product&state=%s&area=%s",state,state);
        Map<String, String> headers = HttpHeaderUtils.getApplicationFormURLEncodedHeaders();
        return HttpRequestDto.Builder.httpRequestDto()
                .withRequestType(RequestType.POST)
                .withUrl(CommodityPriceSource.SUPERSHAKTI.getUrl())
                .withHeaders(headers)
                .withPayload(payload)
                .build();

    }
}
