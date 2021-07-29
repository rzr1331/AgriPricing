package parsers.agriplus;

import parsers.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class AgriPlusCrawlingService {
    private static HashSet<String> states_list;
    private static HashSet<String> coms_list;
    public List<CrawlCommodityPriceDto> handleRequest() {
        HttpRequestDto httpRequestDtoComs = buildRequest("https://agriplus.in/mandi","","");
        HttpClientPool httpClientPoolComs = new HttpClientPool();
        try{
            HttpResponseDto responseDto = httpClientPoolComs.executeRequest(httpRequestDtoComs);
            if(!responseDto.getSuccessful()){
                System.out.println("error while getting response for enam reqeuset [{}] , response [{}]" +
                        httpRequestDtoComs + responseDto);
            }
            System.out.println("check");
            AgriPlusParser agriPlusParser = new AgriPlusParser();
            states_list= agriPlusParser.getStates(responseDto.getResponseString());
        }
        catch (Exception e){
            e.printStackTrace();
        }
        int count=1;
        coms_list=new HashSet<String>();
        while(count>0){
            HttpRequestDto httpRequestDto = buildRequest("https://agriplus.in/price/","all",count+"");
            HttpClientPool httpClientPool = new HttpClientPool();
            try{
                HttpResponseDto responseDto = httpClientPool.executeRequest(httpRequestDto);
                if(!responseDto.getSuccessful()){
                    System.out.println("error while getting response for enam reqeuset [{}] , response [{}]" +
                            httpRequestDtoComs + responseDto);
                }
                AgriPlusParser agriPlusParser = new AgriPlusParser();
                HashSet<String> coms=agriPlusParser.get_coms(responseDto.getResponseString());
                if(coms.size()==0)count=0;
                else{
                    count++;
                    coms_list.addAll(coms);
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        List<CrawlCommodityPriceDto> crawlCommodityPriceDtoList=null;
            for(String com:coms_list){
                for (String state:states_list){
                    HttpRequestDto httpRequestDto = buildRequest(CommodityPriceSource.AGRIPLUS.getUrl(),com,state);
                    HttpClientPool httpClientPool = new HttpClientPool();
                    try {
                        HttpResponseDto responseDto = httpClientPool.executeRequest(httpRequestDto);
                        if (!responseDto.getSuccessful()) {
                            System.out.println("error while getting response for enam reqeuset [{}] , response [{}]" +
                                    httpRequestDto + responseDto);
                            return new ArrayList<>();
                        }
                        AgriPlusParser agriPlusParser = new AgriPlusParser();
                        if(crawlCommodityPriceDtoList==null){
                            crawlCommodityPriceDtoList=agriPlusParser.parseCommodityPrice(responseDto.getResponseString());
                        }
                        else{
                            crawlCommodityPriceDtoList.addAll(agriPlusParser.parseCommodityPrice(responseDto.getResponseString()));
                        }
                } catch (IOException e) {
                        System.out.println("error while getting data  for domain {}" + CommodityPriceSource.ENAM);
                    }

            }
        }
        if(crawlCommodityPriceDtoList==null){
            return new ArrayList();
        }
        return crawlCommodityPriceDtoList;
    }

    private HttpRequestDto buildRequest(String url,String commodity,String state) {
        url=url+commodity+"/"+state;
        Map<String, String> headers = HttpHeaderUtils.getApplicationFormURLEncodedHeaders();
        return HttpRequestDto.Builder.httpRequestDto()
                .withRequestType(RequestType.GET)
                .withUrl(url)
                .withHeaders(headers)
                .withPayload("")
                .build();
    }
}
