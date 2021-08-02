package parsers.agriplus;

import parsers.*;
import parsers.commodityOnline.CommodityOnlineParser;
import java.io.IOException;
import java.util.*;

public class AgriPlusCrawlingService {
    private static HashSet<String> states_list;
    private static HashSet<String> coms_list;
    public List<CrawlCommodityPriceDto> handleRequest(long date) {

        //Building all urls for states and commodities
        HttpRequestDto httpRequestDto = buildRequest("https://agriplus.in/price/","all","");
        HttpClientPool httpClientPool = new HttpClientPool();
        AgriPlusParser agriPlusParser1=new AgriPlusParser();
        try {
            HttpResponseDto responseDto= httpClientPool.executeRequest(httpRequestDto);
            if(!responseDto.getSuccessful()){
                System.out.println("error while getting response for enam reqeuset [{}] , response [{}]" +
                        httpRequestDto + responseDto);
            }
            states_list = agriPlusParser1.getStates(responseDto.getResponseString());
            coms_list=agriPlusParser1.get_coms(responseDto.getResponseString());
        } catch (IOException e) {
            states_list=new HashSet<String>();
            coms_list=new HashSet<String>();
            e.printStackTrace();
        }


        List<HttpRequestDto> httpRequestDtosList = new ArrayList<>();
        ParserCallableTaskExecutorService parserCallableTaskExecutorService =
                new ParserCallableTaskExecutorService();
        List<CrawlCommodityPriceDto> crawlCommodityPriceDtoList =
                null;
        try {
            for(String com:coms_list){
                for (String state:states_list){
                    httpRequestDtosList.add(buildRequest(CommodityPriceSource.AGRIPLUS.getUrl(),com,state));
                }
            }
            crawlCommodityPriceDtoList = parserCallableTaskExecutorService.executeTask(httpRequestDtosList,
                    new AgriPlusParser());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(crawlCommodityPriceDtoList==null){
            return new ArrayList();
        }
        return crawlCommodityPriceDtoList;
    }

    private HttpRequestDto buildRequest(String url,String commodity,String state) {
        url=url+commodity+"/"+state;
        return HttpRequestDto.Builder.httpRequestDto()
                .withRequestType(RequestType.GET)
                .withUrl(url)
                .withHeaders(Collections.emptyMap())
                .withPayload(null)
                .build();
    }
}
