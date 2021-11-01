package parsers.JswNeoSteel;


import parsers.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;


public class JSWNeoSteelCrawlingService {
    public List<CrawlCommodityPriceDto> handleRequest() {
        HttpRequestDto httpRequestDto = buildRequest();
        HttpClientPool httpClientPool = new HttpClientPool();
        try {
            HttpResponseDto responseDto = httpClientPool.executeRequest(httpRequestDto);
            if (!responseDto.getSuccessful()) {
                System.out.println("error while getting response for enam reqeuset [{}] , response [{}]" +
                        httpRequestDto + responseDto);
                return new ArrayList<>();
            }

            JSWNeoSteelParser jswNeoSteelParser = new JSWNeoSteelParser();
            HashSet<String> stateValues = new HashSet<String>();
            stateValues = jswNeoSteelParser.getStateValues(responseDto.getResponseString());
            for(String stateValue:stateValues){
                httpRequestDto = buildRequest(stateValue);
                responseDto = httpClientPool.executeRequest(httpRequestDto);
                if (!responseDto.getSuccessful()) {
                    System.out.println("error while getting response for JSWNeoSteel reqeuset [{}] , response [{}]" +
                            httpRequestDto + responseDto);
                    continue;
                }
                HashMap<String,String[]>districtValues=new HashMap<String,String[]>();
                districtValues = jswNeoSteelParser.getDistrictValues(responseDto.getResponseString());
                for(String districtValue:districtValues.keySet()){
                    String[] load=districtValues.get(districtValue);
                    httpRequestDto = buildRequest(stateValue,districtValue,load[0],load[1]);
                    responseDto = httpClientPool.executeRequest(httpRequestDto);
                    if (!responseDto.getSuccessful()) {
                        System.out.println("error while getting response for JSWNeoSteel reqeuset [{}] , response [{}]" +
                                httpRequestDto + responseDto);
                        continue;
                    }
                    jswNeoSteelParser.parseCommodityPrice(responseDto.getResponseString());
                }
            }
        } catch (IOException e) {
            System.out.println("error while getting data  for domain {}" + CommodityPriceSource.ENAM);
        }
        return new ArrayList<>();
    }

    private HttpRequestDto buildRequest() {
        String url = "https://www.jswneosteel.in/build/know-existing-prices.aspx";
        Map<String, String> headers = HttpHeaderUtils.getApplicationFormURLEncodedHeaders();
//        headers.put("user-agent","Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/93.0.4577.82 Safari/537.36");
        return HttpRequestDto.Builder.httpRequestDto()
                .withRequestType(RequestType.GET)
                .withUrl(url)
                .withHeaders(headers)
                .withPayload("")
                .build();
    }
    private HttpRequestDto buildRequest(String stateValue) {
        String url = "https://www.jswneosteel.in/build/know-existing-prices.aspx";
        String payload = "ctl00%24ScriptManager1=ctl00%24ContentPlaceHolder1%24update1%7Cctl00%24ContentPlaceHolder1%24ddlstate&__EVENTTARGET=ctl00%24ContentPlaceHolder1%24ddlstate&__EVENTARGUMENT=&__LASTFOCUS=&__VIEWSTATE=%2FwEPDwULLTEwNjU1MjM2NjcPZBYCZg9kFgQCAQ9kFgQCBA8WAh4HY29udGVudAUpSlNXIE5lb1N0ZWVsIFRNVCBCYXIgUHJpY2VzIGluIHlvdXIgU3RhdGVkAgUPFgIfAAVTSlNXIE5lb1N0ZWVsIFRNVCBCYXIgUHJpY2VzIGluIHlvdXIgU3RhdGUsIFRNVCBCYXIgUHJpY2VzLCBTdGF0ZXdpc2UgVE1UIEJhciBQcmljZXNkAgMPZBYGAgEPZBYCAgcPFgIeBWNsYXNzBRdjdXJyZW50IGRyb3Bkb3duLXRvZ2dsZWQCBQ9kFgICAQ9kFgJmD2QWBgIBDxBkZBYBZmQCAw9kFgJmD2QWBAIBDxAPFgYeDURhdGFUZXh0RmllbGQFCnN0YXRlX25hbWUeDkRhdGFWYWx1ZUZpZWxkBQhzdGF0ZV9pZB4LXyFEYXRhQm91bmRnZBAVGhAtIFNlbGVjdCBTdGF0ZSAtDkFuZGhyYSBQcmFkZXNoBUFzc2FtBUJpaGFyCkNoYW5kaWdhcmgLQ2hhdHRpc2dhcmgFRGVsaGkDR29hB0d1amFyYXQHSGFyeWFuYRBIaW1hY2hhbCBQcmFkZXNoEUphbW11IGFuZCBLYXNobWlyCUpoYXJraGFuZAlLYXJuYXRha2EGS2VyYWxhDk1hZGh5YSBQcmFkZXNoC01haGFyYXNodHJhBk9kaXNoYQtQb25kaWNoZXJyeQZQdW5qYWIJUmFqYXN0aGFuClRhbWlsIE5hZHUJVGVsYW5nYW5hDVV0dGFyIFByYWRlc2gLVXR0YXJha2hhbmQLV2VzdCBCZW5nYWwVGgABMgE0ATUBNgE3AjEwAjExAjEyAjEzAjE0AjE1AjE2AjE3AjE4AjIwAjIxAjI2AjI3AjI4AjI5AjMxAjMyAjM0AjM1AjM2FCsDGmdnZ2dnZ2dnZ2dnZ2dnZ2dnZ2dnZ2dnZ2dnFgFmZAIFDxBkZBYAZAIFD2QWAmYPZBYEAgEPEGRkFgBkAgUPEGRkFgBkAgcPZBYEAgEPFgIeC18hSXRlbUNvdW50AgUWCmYPZBYCZg8VAilodHRwczovL3d3dy5mYWNlYm9vay5jb20vSlNXU3RlZWxPZmZpY2lhbAtmYS1mYWNlYm9va2QCAQ9kFgJmDxUCHGh0dHBzOi8vdHdpdHRlci5jb20vanN3c3RlZWwKZmEtdHdpdHRlcmQCAg9kFgJmDxUCJGh0dHBzOi8vd3d3LmxpbmtlZGluLmNvbS9jb21wYW55L2pzdwtmYS1saW5rZWRpbmQCAw9kFgJmDxUCM2h0dHBzOi8vaW5zdGFncmFtLmNvbS9qc3cuc3RlZWw%2FaWdzaGlkPWpyNHNscm1yNml4eAxmYS1pbnN0YWdyYW1kAgQPZBYCZg8VAjhodHRwczovL3d3dy55b3V0dWJlLmNvbS9jaGFubmVsL1VDcE5mT3JuRGFvVzZPOGxkeklTWWtGQQpmYS15b3V0dWJlZAIDDxYCHgRUZXh0BcYCPCEtLSBHbG9iYWwgc2l0ZSB0YWcgKGd0YWcuanMpIC0gR29vZ2xlIEFuYWx5dGljcyAtLT4NCjxzY3JpcHQgYXN5bmMgc3JjPSJodHRwczovL3d3dy5nb29nbGV0YWdtYW5hZ2VyLmNvbS9ndGFnL2pzP2lkPVVBLTEyNTMzNjc4OC0xIj48L3NjcmlwdD4NCjxzY3JpcHQ%2BDQogIHdpbmRvdy5kYXRhTGF5ZXIgPSB3aW5kb3cuZGF0YUxheWVyIHx8IFtdOw0KICBmdW5jdGlvbiBndGFnKCl7ZGF0YUxheWVyLnB1c2goYXJndW1lbnRzKTt9DQogIGd0YWcoJ2pzJywgbmV3IERhdGUoKSk7DQoNCiAgZ3RhZygnY29uZmlnJywgJ1VBLTEyNTMzNjc4OC0xJyk7DQo8L3NjcmlwdD4NCiBkZFaKUtD7XWIfiN1AqMQCPbuvCUs0VoDd7rknHPK9dTyF&__VIEWSTATEGENERATOR=C43C4B55&ctl00%24ContentPlaceHolder1%24ddlstate="+stateValue+"&ctl00%24ContentPlaceHolder1%24hdncluster=&__ASYNCPOST=true&";
        Map<String, String> headers = HttpHeaderUtils.getApplicationFormURLEncodedHeaders();
        headers.put("user-agent","Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/93.0.4577.82 Safari/537.36");
        return HttpRequestDto.Builder.httpRequestDto()
                .withRequestType(RequestType.POST)
                .withUrl(url)
                .withHeaders(headers)
                .withPayload(payload)
                .build();
    }
//    private HttpRequestDto buildRequest(String stateValue,String districtValue) {
//        String url = "https://www.jswneosteel.in/build/know-existing-prices.aspx";
//        String payload ="ctl00%24ScriptManager1=ctl00%24ContentPlaceHolder1%24update1%7Cctl00%24ContentPlaceHolder1%24btnsubmit&ctl00%24ContentPlaceHolder1%24ddlstate="+stateValue+"&ctl00%24ContentPlaceHolder1%24ddldistrict="+districtValue+"&ctl00%24ContentPlaceHolder1%24hdncluster=0&__EVENTTARGET=&__EVENTARGUMENT=&__LASTFOCUS=&__VIEWSTATE=%2FwEPDwULLTEwNjU1MjM2NjcPZBYCZg9kFgQCAQ9kFgQCBA8WAh4HY29udGVudAUpSlNXIE5lb1N0ZWVsIFRNVCBCYXIgUHJpY2VzIGluIHlvdXIgU3RhdGVkAgUPFgIfAAVTSlNXIE5lb1N0ZWVsIFRNVCBCYXIgUHJpY2VzIGluIHlvdXIgU3RhdGUsIFRNVCBCYXIgUHJpY2VzLCBTdGF0ZXdpc2UgVE1UIEJhciBQcmljZXNkAgMPZBYGAgEPZBYCAgcPFgIeBWNsYXNzBRdjdXJyZW50IGRyb3Bkb3duLXRvZ2dsZWQCBQ9kFgICAQ9kFgJmD2QWBgIBDxBkZBYBZmQCAw9kFgJmD2QWCgIBDxAPFgYeDURhdGFUZXh0RmllbGQFCnN0YXRlX25hbWUeDkRhdGFWYWx1ZUZpZWxkBQhzdGF0ZV9pZB4LXyFEYXRhQm91bmRnZBAVGhAtIFNlbGVjdCBTdGF0ZSAtDkFuZGhyYSBQcmFkZXNoBUFzc2FtBUJpaGFyCkNoYW5kaWdhcmgLQ2hhdHRpc2dhcmgFRGVsaGkDR29hB0d1amFyYXQHSGFyeWFuYRBIaW1hY2hhbCBQcmFkZXNoEUphbW11IGFuZCBLYXNobWlyCUpoYXJraGFuZAlLYXJuYXRha2EGS2VyYWxhDk1hZGh5YSBQcmFkZXNoC01haGFyYXNodHJhBk9kaXNoYQtQb25kaWNoZXJyeQZQdW5qYWIJUmFqYXN0aGFuClRhbWlsIE5hZHUJVGVsYW5nYW5hDVV0dGFyIFByYWRlc2gLVXR0YXJha2hhbmQLV2VzdCBCZW5nYWwVGgABMgE0ATUBNgE3AjEwAjExAjEyAjEzAjE0AjE1AjE2AjE3AjE4AjIwAjIxAjI2AjI3AjI4AjI5AjMxAjMyAjM0AjM1AjM2FCsDGmdnZ2dnZ2dnZ2dnZ2dnZ2dnZ2dnZ2dnZ2dnFgECAWQCBQ8QDxYIHwIFDWRpc3RyaWN0X25hbWUfAwULZGlzdHJpY3RfaWQfBGceB1Zpc2libGVnZBAVERMtIFNlbGVjdCBEaXN0cmljdCAtCUFuYW50YXB1cghDaGl0dG9vcghDdWRkYXBhaA1FYXN0IEdvZGF2YXJpBkd1bnR1cgZLYWRhcGEHS3Jpc2huYQdLdXJub29sB05hdXBhZGEHTmVsbG9yZQhQcmFrYXNhbQpTcmlrYWt1bGFtCFRpcnVwYXRpDVZpc2FraGFwYXRuYW0MVml6aWFuYWdhcmFtDVdlc3QgR29kYXZhcmkVEQACMjADMTE3AzEyNAMxNjcDMjEyAzYzNAMzMjgDMzMzAzY2NwM0MTcDNDU5AzU1NwM2MzUDNjEwAzYxMQM2MTkUKwMRZ2dnZ2dnZ2dnZ2dnZ2dnZ2dkZAIHDw8WAh8FZ2RkAgsPFgIfBWcWCgIBDxYCHgRUZXh0BREgLSBBbmRocmEgUHJhZGVzaGQCAw8WAh8GBQogIENoaXR0b29yZAIHDxYCHgtfIUl0ZW1Db3VudAIBFgJmD2QWAgIBDxYCHwYFEkpTVyBOZW9zdGVlbCA1NTAgRGQCCQ8WAh8HAggWEGYPZBYGAgEPFgIfBgUENiBtbWQCAw8PFgIfBgUBOGRkAgUPFgIfBwIBFgJmD2QWBAIBDw8WAh8GBQExZGQCAg8VAQMyMjJkAgEPZBYGAgEPFgIfBgUEOCBtbWQCAw8PFgIfBgUBMWRkAgUPFgIfBwIBFgJmD2QWBAIBDw8WAh8GBQExZGQCAg8VAQMzOTFkAgIPZBYGAgEPFgIfBgUFMTAgbW1kAgMPDxYCHwYFATJkZAIFDxYCHwcCARYCZg9kFgQCAQ8PFgIfBgUBMWRkAgIPFQEDNjAwZAIDD2QWBgIBDxYCHwYFBTEyIG1tZAIDDw8WAh8GBQEzZGQCBQ8WAh8HAgEWAmYPZBYEAgEPDxYCHwYFATFkZAICDxUBAzg1M2QCBA9kFgYCAQ8WAh8GBQUxNiBtbWQCAw8PFgIfBgUBNGRkAgUPFgIfBwIBFgJmD2QWBAIBDw8WAh8GBQExZGQCAg8VAQQxNTE5ZAIFD2QWBgIBDxYCHwYFBTIwIG1tZAIDDw8WAh8GBQE1ZGQCBQ8WAh8HAgEWAmYPZBYEAgEPDxYCHwYFATFkZAICDxUBBDIzNzRkAgYPZBYGAgEPFgIfBgUFMjUgbW1kAgMPDxYCHwYFATZkZAIFDxYCHwcCARYCZg9kFgQCAQ8PFgIfBgUBMWRkAgIPFQEEMzcwMGQCBw9kFgYCAQ8WAh8GBQUzMiBtbWQCAw8PFgIfBgUBN2RkAgUPFgIfBwIBFgJmD2QWBAIBDw8WAh8GBQExZGQCAg8VAQQ2MDY1ZAINDxYCHwVoZAINDxYCHwVnFgJmDxYCHwYFCzIxLU9jdC0yMDIxZAIFD2QWAmYPZBYEAgEPEGRkFgBkAgUPEGRkFgBkAgcPZBYEAgEPFgIfBwIFFgpmD2QWAmYPFQIpaHR0cHM6Ly93d3cuZmFjZWJvb2suY29tL0pTV1N0ZWVsT2ZmaWNpYWwLZmEtZmFjZWJvb2tkAgEPZBYCZg8VAhxodHRwczovL3R3aXR0ZXIuY29tL2pzd3N0ZWVsCmZhLXR3aXR0ZXJkAgIPZBYCZg8VAiRodHRwczovL3d3dy5saW5rZWRpbi5jb20vY29tcGFueS9qc3cLZmEtbGlua2VkaW5kAgMPZBYCZg8VAjNodHRwczovL2luc3RhZ3JhbS5jb20vanN3LnN0ZWVsP2lnc2hpZD1qcjRzbHJtcjZpeHgMZmEtaW5zdGFncmFtZAIED2QWAmYPFQI4aHR0cHM6Ly93d3cueW91dHViZS5jb20vY2hhbm5lbC9VQ3BOZk9ybkRhb1c2TzhsZHpJU1lrRkEKZmEteW91dHViZWQCAw8WAh8GBcYCPCEtLSBHbG9iYWwgc2l0ZSB0YWcgKGd0YWcuanMpIC0gR29vZ2xlIEFuYWx5dGljcyAtLT4NCjxzY3JpcHQgYXN5bmMgc3JjPSJodHRwczovL3d3dy5nb29nbGV0YWdtYW5hZ2VyLmNvbS9ndGFnL2pzP2lkPVVBLTEyNTMzNjc4OC0xIj48L3NjcmlwdD4NCjxzY3JpcHQ%2BDQogIHdpbmRvdy5kYXRhTGF5ZXIgPSB3aW5kb3cuZGF0YUxheWVyIHx8IFtdOw0KICBmdW5jdGlvbiBndGFnKCl7ZGF0YUxheWVyLnB1c2goYXJndW1lbnRzKTt9DQogIGd0YWcoJ2pzJywgbmV3IERhdGUoKSk7DQoNCiAgZ3RhZygnY29uZmlnJywgJ1VBLTEyNTMzNjc4OC0xJyk7DQo8L3NjcmlwdD4NCiBkZOXjNVz%2BwrdN2HPL84Oxlxrtm%2ByXTSdeO2Kp2W7dHOzf&__VIEWSTATEGENERATOR=C43C4B55&__ASYNCPOST=true&ctl00%24ContentPlaceHolder1%24btnsubmit=Submit";
//       System.out.println(stateValue+" "+districtValue);
//       System.out.println(payload);
//        Map<String, String> headers = HttpHeaderUtils.getApplicationFormURLEncodedHeaders();
//        headers.put("user-agent","Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/93.0.4577.82 Safari/537.36");
//        return HttpRequestDto.Builder.httpRequestDto()
//                .withRequestType(RequestType.POST)
//                .withUrl(url)
//                .withHeaders(headers)
//                .withPayload(payload)
//                .build();
//    }
    private HttpRequestDto buildRequest(String stateValue,String districtValue,String viewState,String genarator) {
        String url = "https://www.jswneosteel.in/build/know-existing-prices.aspx";
        String payload="ctl00%24ScriptManager1=ctl00%24ContentPlaceHolder1%24update1%7Cctl00%24ContentPlaceHolder1%24btnsubmit&ctl00%24ContentPlaceHolder1%24ddlstate="+stateValue+"&ctl00%24ContentPlaceHolder1%24ddldistrict="+districtValue+"&ctl00%24ContentPlaceHolder1%24hdncluster=0&__EVENTTARGET=&__EVENTARGUMENT=&__LASTFOCUS=&__VIEWSTATE="+viewState+"&__VIEWSTATEGENERATOR="+genarator+"&__ASYNCPOST=true&ctl00%24ContentPlaceHolder1%24btnsubmit=Submit";
        System.out.println(stateValue+" "+districtValue);
        System.out.println(payload);
        Map<String, String> headers =HttpHeaderUtils.getApplicationFormURLEncodedHeaders();
        headers.put("content-type","application/x-www-form-urlencoded; charset=UTF-8");
        headers.put("user-agent","Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/93.0.4577.82 Safari/537.36");
        return HttpRequestDto.Builder.httpRequestDto()
                .withRequestType(RequestType.POST)
                .withUrl(url)
                .withHeaders(headers)
                .withPayload(payload)
                .build();
    }
}
