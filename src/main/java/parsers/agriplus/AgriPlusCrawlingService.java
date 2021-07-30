package parsers.agriplus;

import parsers.*;
import parsers.commodityOnline.CommodityOnlineParser;
import java.io.IOException;
import java.util.*;

public class AgriPlusCrawlingService {
    private static HashSet<String> states_list;
    private static HashSet<String> coms_list;
    public List<CrawlCommodityPriceDto> handleRequest(long date) {
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
//        List<CrawlCommodityPriceDto> crawlCommodityPriceDtoList=null;
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
