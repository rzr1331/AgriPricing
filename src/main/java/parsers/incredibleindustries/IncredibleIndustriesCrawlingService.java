package parsers.incredibleindustries;


import parsers.*;
import java.io.IOException;
import java.util.*;

public class IncredibleIndustriesCrawlingService {
    public List<CrawlCommodityPriceDto> handleRequest(){
        HttpRequestDto statesDto=getStates();
        HttpClientPool httpClientPool = new HttpClientPool();
        try {
            IncredibleIndustriesParser incredibleindustriesParser = new IncredibleIndustriesParser();
            HttpResponseDto responseDto = httpClientPool.executeRequest(statesDto);
            if (!responseDto.getSuccessful()) {
                System.out.println("error while getting response for Incredible_Industries states [{}] , response [{}]" +
                        statesDto + responseDto);
                return new ArrayList<>();
            }
            HashMap<String,String> states = incredibleindustriesParser.getStates(responseDto.getResponseString());
            HashMap<String,String>types;
            for(String state:states.keySet()){
                HttpRequestDto typesDto=getDistricts(state);
                responseDto=httpClientPool.executeRequest(typesDto);
                if(!responseDto.getSuccessful()){
                    System.out.println("error while getting response for Incredible_Industries types [{}] , response [{}]" +
                            typesDto + responseDto);
                    continue;
                }
                types= incredibleindustriesParser.getDistricts(responseDto.getResponseString());
                for(String type:types.keySet()){
                    HttpRequestDto httpRequestDto = buildRequest(state,type);
                    responseDto= httpClientPool.executeRequest(httpRequestDto);
                    if (!responseDto.getSuccessful()) {
                        System.out.println("error while getting response for Incredible_Industries requeset [{}] , response [{}]" +
                                statesDto + responseDto);
                        continue;
                    }
                    ArrayList<String>prices= incredibleindustriesParser.parseCommodityPrice(responseDto.getResponseString());
                    for(String price:prices)
                    System.out.println("State:"+states.get(state)+" City:"+types.get(type)+price);
//                    incredibleindustriesParser.parseCommodityPrice(responseDto.getResponseString());
                }
            }
        } catch (IOException e) {
            System.out.println("error while getting data  for domain {}" + CommodityPriceSource.ENAM);
        }
        return new ArrayList<>();
    }
    private HttpRequestDto buildRequest(String state_id, String type) {
        String payload = String.format("chenge_showresult.php?state_id=%s&city_id=%s&product_id=1",state_id,type);
        Map<String, String> headers = HttpHeaderUtils.getApplicationFormURLEncodedHeaders();
        return HttpRequestDto.Builder.httpRequestDto()
                .withRequestType(RequestType.GET)
                .withUrl(CommodityPriceSource.Incredible_Industries.getUrl()+payload)
                .withHeaders(headers)
                .withPayload(payload)
                .build();

    }
    public HttpRequestDto getStates(){
        Map<String, String> headers = HttpHeaderUtils.getApplicationFormURLEncodedHeaders();
        return HttpRequestDto.Builder.httpRequestDto()
                .withRequestType(RequestType.GET)
                .withUrl("https://www.incredibleindustries.co.in/recommended-price#")
                .withHeaders(headers)
                .withPayload("")
                .build();
    }
    public HttpRequestDto getDistricts(String state){
        Map<String, String> headers = HttpHeaderUtils.getApplicationFormURLEncodedHeaders();
        return HttpRequestDto.Builder.httpRequestDto()
                .withRequestType(RequestType.GET)
                .withUrl(CommodityPriceSource.Incredible_Industries.getUrl()+"chenge_city.php?row_id="+state)
                .withHeaders(headers)
                .withPayload("")
                .build();

    }
}