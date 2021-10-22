package parsers.JswNeoSteel;


import parsers.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;


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
                HashSet<String>districtValues=new HashSet<String>();
                districtValues = jswNeoSteelParser.getDistrictValues(responseDto.getResponseString());
                for(String districtValue:districtValues){
                    httpRequestDto = buildRequest("2","20");
                    responseDto = httpClientPool.executeRequest(httpRequestDto);
                    if (!responseDto.getSuccessful()) {
                        System.out.println("error while getting response for JSWNeoSteel reqeuset [{}] , response [{}]" +
                                httpRequestDto + responseDto);
                        continue;
                    }
                    System.out.println(responseDto.getResponseString());
                }
            }
        } catch (IOException e) {
            System.out.println("error while getting data  for domain {}" + CommodityPriceSource.ENAM);
        }
        return new ArrayList<>();
    }

    private HttpRequestDto buildRequest() {
        String url = "https://www.jswneosteel.in/build/know-existing-prices.aspx";
//        String payload = "ctl00%24ScriptManager1=ctl00%24ContentPlaceHolder1%24update1%7Cctl00%24ContentPlaceHolder1%24ddlstate&__EVENTTARGET=ctl00%24ContentPlaceHolder1%24ddlstate&__EVENTARGUMENT=&__LASTFOCUS=&__VIEWSTATE=%2FwEPDwULLTEwNjU1MjM2NjcPZBYCZg9kFgQCAQ9kFgQCBA8WAh4HY29udGVudAUpSlNXIE5lb1N0ZWVsIFRNVCBCYXIgUHJpY2VzIGluIHlvdXIgU3RhdGVkAgUPFgIfAAVTSlNXIE5lb1N0ZWVsIFRNVCBCYXIgUHJpY2VzIGluIHlvdXIgU3RhdGUsIFRNVCBCYXIgUHJpY2VzLCBTdGF0ZXdpc2UgVE1UIEJhciBQcmljZXNkAgMPZBYGAgEPZBYCAgcPFgIeBWNsYXNzBRdjdXJyZW50IGRyb3Bkb3duLXRvZ2dsZWQCBQ9kFgICAQ9kFgJmD2QWBgIBDxBkZBYBZmQCAw9kFgJmD2QWBAIBDxAPFgYeDURhdGFUZXh0RmllbGQFCnN0YXRlX25hbWUeDkRhdGFWYWx1ZUZpZWxkBQhzdGF0ZV9pZB4LXyFEYXRhQm91bmRnZBAVGhAtIFNlbGVjdCBTdGF0ZSAtDkFuZGhyYSBQcmFkZXNoBUFzc2FtBUJpaGFyCkNoYW5kaWdhcmgLQ2hhdHRpc2dhcmgFRGVsaGkDR29hB0d1amFyYXQHSGFyeWFuYRBIaW1hY2hhbCBQcmFkZXNoEUphbW11IGFuZCBLYXNobWlyCUpoYXJraGFuZAlLYXJuYXRha2EGS2VyYWxhDk1hZGh5YSBQcmFkZXNoC01haGFyYXNodHJhBk9kaXNoYQtQb25kaWNoZXJyeQZQdW5qYWIJUmFqYXN0aGFuClRhbWlsIE5hZHUJVGVsYW5nYW5hDVV0dGFyIFByYWRlc2gLVXR0YXJha2hhbmQLV2VzdCBCZW5nYWwVGgABMgE0ATUBNgE3AjEwAjExAjEyAjEzAjE0AjE1AjE2AjE3AjE4AjIwAjIxAjI2AjI3AjI4AjI5AjMxAjMyAjM0AjM1AjM2FCsDGmdnZ2dnZ2dnZ2dnZ2dnZ2dnZ2dnZ2dnZ2dnFgFmZAIFDxBkZBYAZAIFD2QWAmYPZBYEAgEPEGRkFgBkAgUPEGRkFgBkAgcPZBYEAgEPFgIeC18hSXRlbUNvdW50AgUWCmYPZBYCZg8VAilodHRwczovL3d3dy5mYWNlYm9vay5jb20vSlNXU3RlZWxPZmZpY2lhbAtmYS1mYWNlYm9va2QCAQ9kFgJmDxUCHGh0dHBzOi8vdHdpdHRlci5jb20vanN3c3RlZWwKZmEtdHdpdHRlcmQCAg9kFgJmDxUCJGh0dHBzOi8vd3d3LmxpbmtlZGluLmNvbS9jb21wYW55L2pzdwtmYS1saW5rZWRpbmQCAw9kFgJmDxUCM2h0dHBzOi8vaW5zdGFncmFtLmNvbS9qc3cuc3RlZWw%2FaWdzaGlkPWpyNHNscm1yNml4eAxmYS1pbnN0YWdyYW1kAgQPZBYCZg8VAjhodHRwczovL3d3dy55b3V0dWJlLmNvbS9jaGFubmVsL1VDcE5mT3JuRGFvVzZPOGxkeklTWWtGQQpmYS15b3V0dWJlZAIDDxYCHgRUZXh0BcYCPCEtLSBHbG9iYWwgc2l0ZSB0YWcgKGd0YWcuanMpIC0gR29vZ2xlIEFuYWx5dGljcyAtLT4NCjxzY3JpcHQgYXN5bmMgc3JjPSJodHRwczovL3d3dy5nb29nbGV0YWdtYW5hZ2VyLmNvbS9ndGFnL2pzP2lkPVVBLTEyNTMzNjc4OC0xIj48L3NjcmlwdD4NCjxzY3JpcHQ%2BDQogIHdpbmRvdy5kYXRhTGF5ZXIgPSB3aW5kb3cuZGF0YUxheWVyIHx8IFtdOw0KICBmdW5jdGlvbiBndGFnKCl7ZGF0YUxheWVyLnB1c2goYXJndW1lbnRzKTt9DQogIGd0YWcoJ2pzJywgbmV3IERhdGUoKSk7DQoNCiAgZ3RhZygnY29uZmlnJywgJ1VBLTEyNTMzNjc4OC0xJyk7DQo8L3NjcmlwdD4NCiBkZFaKUtD7XWIfiN1AqMQCPbuvCUs0VoDd7rknHPK9dTyF&__VIEWSTATEGENERATOR=C43C4B55&ctl00%24ContentPlaceHolder1%24ddlstate=2&ctl00%24ContentPlaceHolder1%24hdncluster=&__ASYNCPOST=true&";
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
    private HttpRequestDto buildRequest(String stateValue,String districtValue) {
        String url = "https://www.jswneosteel.in/build/know-existing-prices.aspx";
//        String payload="ctl00%24ScriptManager1=ctl00%24ContentPlaceHolder1%24update1%7Cctl00%24ContentPlaceHolder1%24btnsubmit&ctl00%24ContentPlaceHolder1%24ddlstate="+stateValue+"&ctl00%24ContentPlaceHolder1%24ddldistrict="+districtValue+"&ctl00%24ContentPlaceHolder1%24hdncluster=0&__EVENTTARGET=&__EVENTARGUMENT=&__LASTFOCUS=&__VIEWSTATE=%2FwEPDwULLTEwNjU1MjM2NjcPZBYCZg9kFgQCAQ9kFgQCBA8WAh4HY29udGVudAUpSlNXIE5lb1N0ZWVsIFRNVCBCYXIgUHJpY2VzIGluIHlvdXIgU3RhdGVkAgUPFgIfAAVTSlNXIE5lb1N0ZWVsIFRNVCBCYXIgUHJpY2VzIGluIHlvdXIgU3RhdGUsIFRNVCBCYXIgUHJpY2VzLCBTdGF0ZXdpc2UgVE1UIEJhciBQcmljZXNkAgMPZBYGAgEPZBYCAgcPFgIeBWNsYXNzBRdjdXJyZW50IGRyb3Bkb3duLXRvZ2dsZWQCBQ9kFgICAQ9kFgJmD2QWBgIBDxBkZBYBZmQCAw9kFgJmD2QWCAIBDxAPFgYeDURhdGFUZXh0RmllbGQFCnN0YXRlX25hbWUeDkRhdGFWYWx1ZUZpZWxkBQhzdGF0ZV9pZB4LXyFEYXRhQm91bmRnZBAVGhAtIFNlbGVjdCBTdGF0ZSAtDkFuZGhyYSBQcmFkZXNoBUFzc2FtBUJpaGFyCkNoYW5kaWdhcmgLQ2hhdHRpc2dhcmgFRGVsaGkDR29hB0d1amFyYXQHSGFyeWFuYRBIaW1hY2hhbCBQcmFkZXNoEUphbW11IGFuZCBLYXNobWlyCUpoYXJraGFuZAlLYXJuYXRha2EGS2VyYWxhDk1hZGh5YSBQcmFkZXNoC01haGFyYXNodHJhBk9kaXNoYQtQb25kaWNoZXJyeQZQdW5qYWIJUmFqYXN0aGFuClRhbWlsIE5hZHUJVGVsYW5nYW5hDVV0dGFyIFByYWRlc2gLVXR0YXJha2hhbmQLV2VzdCBCZW5nYWwVGgABMgE0ATUBNgE3AjEwAjExAjEyAjEzAjE0AjE1AjE2AjE3AjE4AjIwAjIxAjI2AjI3AjI4AjI5AjMxAjMyAjM0AjM1AjM2FCsDGmdnZ2dnZ2dnZ2dnZ2dnZ2dnZ2dnZ2dnZ2dnFgECAWQCBQ8QDxYIHwIFDWRpc3RyaWN0X25hbWUfAwULZGlzdHJpY3RfaWQfBGceB1Zpc2libGVnZBAVERMtIFNlbGVjdCBEaXN0cmljdCAtCUFuYW50YXB1cghDaGl0dG9vcghDdWRkYXBhaA1FYXN0IEdvZGF2YXJpBkd1bnR1cgZLYWRhcGEHS3Jpc2huYQdLdXJub29sB05hdXBhZGEHTmVsbG9yZQhQcmFrYXNhbQpTcmlrYWt1bGFtCFRpcnVwYXRpDVZpc2FraGFwYXRuYW0MVml6aWFuYWdhcmFtDVdlc3QgR29kYXZhcmkVEQACMjADMTE3AzEyNAMxNjcDMjEyAzYzNAMzMjgDMzMzAzY2NwM0MTcDNDU5AzU1NwM2MzUDNjEwAzYxMQM2MTkUKwMRZ2dnZ2dnZ2dnZ2dnZ2dnZ2dkZAIHDw8WAh8FZ2RkAg0PZBYCZg8WAh4EVGV4dAULMjEtT2N0LTIwMjFkAgUPZBYCZg9kFgQCAQ8QZGQWAGQCBQ8QZGQWAGQCBw9kFgQCAQ8WAh4LXyFJdGVtQ291bnQCBRYKZg9kFgJmDxUCKWh0dHBzOi8vd3d3LmZhY2Vib29rLmNvbS9KU1dTdGVlbE9mZmljaWFsC2ZhLWZhY2Vib29rZAIBD2QWAmYPFQIcaHR0cHM6Ly90d2l0dGVyLmNvbS9qc3dzdGVlbApmYS10d2l0dGVyZAICD2QWAmYPFQIkaHR0cHM6Ly93d3cubGlua2VkaW4uY29tL2NvbXBhbnkvanN3C2ZhLWxpbmtlZGluZAIDD2QWAmYPFQIzaHR0cHM6Ly9pbnN0YWdyYW0uY29tL2pzdy5zdGVlbD9pZ3NoaWQ9anI0c2xybXI2aXh4DGZhLWluc3RhZ3JhbWQCBA9kFgJmDxUCOGh0dHBzOi8vd3d3LnlvdXR1YmUuY29tL2NoYW5uZWwvVUNwTmZPcm5EYW9XNk84bGR6SVNZa0ZBCmZhLXlvdXR1YmVkAgMPFgIfBgXGAjwhLS0gR2xvYmFsIHNpdGUgdGFnIChndGFnLmpzKSAtIEdvb2dsZSBBbmFseXRpY3MgLS0%2BDQo8c2NyaXB0IGFzeW5jIHNyYz0iaHR0cHM6Ly93d3cuZ29vZ2xldGFnbWFuYWdlci5jb20vZ3RhZy9qcz9pZD1VQS0xMjUzMzY3ODgtMSI%2BPC9zY3JpcHQ%2BDQo8c2NyaXB0Pg0KICB3aW5kb3cuZGF0YUxheWVyID0gd2luZG93LmRhdGFMYXllciB8fCBbXTsNCiAgZnVuY3Rpb24gZ3RhZygpe2RhdGFMYXllci5wdXNoKGFyZ3VtZW50cyk7fQ0KICBndGFnKCdqcycsIG5ldyBEYXRlKCkpOw0KDQogIGd0YWcoJ2NvbmZpZycsICdVQS0xMjUzMzY3ODgtMScpOw0KPC9zY3JpcHQ%2BDQogZGQQTg8i1keSnNwTfAjx2LCP0AZKweIB8NkJ2G4R8BO0BA%3D%3D&__VIEWSTATEGENERATOR=C43C4B55&__ASYNCPOST=true&ctl00%24ContentPlaceHolder1%24btnsubmit=Submit";
        String payload="ctl00%24ScriptManager1=ctl00%24ContentPlaceHolder1%24update1%7Cctl00%24ContentPlaceHolder1%24btnsubmit&ctl00%24ContentPlaceHolder1%24ddlstate="+stateValue+"&ctl00%24ContentPlaceHolder1%24ddldistrict="+districtValue+"&ctl00%24ContentPlaceHolder1%24hdncluster=0&__EVENTTARGET=&__EVENTARGUMENT=&__LASTFOCUS=&__VIEWSTATE=%2FwEPDwULLTEwNjU1MjM2NjcPZBYCZg9kFgQCAQ9kFgQCBA8WAh4HY29udGVudAUpSlNXIE5lb1N0ZWVsIFRNVCBCYXIgUHJpY2VzIGluIHlvdXIgU3RhdGVkAgUPFgIfAAVTSlNXIE5lb1N0ZWVsIFRNVCBCYXIgUHJpY2VzIGluIHlvdXIgU3RhdGUsIFRNVCBCYXIgUHJpY2VzLCBTdGF0ZXdpc2UgVE1UIEJhciBQcmljZXNkAgMPZBYGAgEPZBYCAgcPFgIeBWNsYXNzBRdjdXJyZW50IGRyb3Bkb3duLXRvZ2dsZWQCBQ9kFgICAQ9kFgJmD2QWBgIBDxBkZBYBZmQCAw9kFgJmD2QWCAIBDxAPFgYeDURhdGFUZXh0RmllbGQFCnN0YXRlX25hbWUeDkRhdGFWYWx1ZUZpZWxkBQhzdGF0ZV9pZB4LXyFEYXRhQm91bmRnZBAVGhAtIFNlbGVjdCBTdGF0ZSAtDkFuZGhyYSBQcmFkZXNoBUFzc2FtBUJpaGFyCkNoYW5kaWdhcmgLQ2hhdHRpc2dhcmgFRGVsaGkDR29hB0d1amFyYXQHSGFyeWFuYRBIaW1hY2hhbCBQcmFkZXNoEUphbW11IGFuZCBLYXNobWlyCUpoYXJraGFuZAlLYXJuYXRha2EGS2VyYWxhDk1hZGh5YSBQcmFkZXNoC01haGFyYXNodHJhBk9kaXNoYQtQb25kaWNoZXJyeQZQdW5qYWIJUmFqYXN0aGFuClRhbWlsIE5hZHUJVGVsYW5nYW5hDVV0dGFyIFByYWRlc2gLVXR0YXJha2hhbmQLV2VzdCBCZW5nYWwVGgABMgE0ATUBNgE3AjEwAjExAjEyAjEzAjE0AjE1AjE2AjE3AjE4AjIwAjIxAjI2AjI3AjI4AjI5AjMxAjMyAjM0AjM1AjM2FCsDGmdnZ2dnZ2dnZ2dnZ2dnZ2dnZ2dnZ2dnZ2dnFgECAWQCBQ8QDxYIHwIFDWRpc3RyaWN0X25hbWUfAwULZGlzdHJpY3RfaWQfBGceB1Zpc2libGVnZBAVERMtIFNlbGVjdCBEaXN0cmljdCAtCUFuYW50YXB1cghDaGl0dG9vcghDdWRkYXBhaA1FYXN0IEdvZGF2YXJpBkd1bnR1cgZLYWRhcGEHS3Jpc2huYQdLdXJub29sB05hdXBhZGEHTmVsbG9yZQhQcmFrYXNhbQpTcmlrYWt1bGFtCFRpcnVwYXRpDVZpc2FraGFwYXRuYW0MVml6aWFuYWdhcmFtDVdlc3QgR29kYXZhcmkVEQACMjADMTE3AzEyNAMxNjcDMjEyAzYzNAMzMjgDMzMzAzY2NwM0MTcDNDU5AzU1NwM2MzUDNjEwAzYxMQM2MTkUKwMRZ2dnZ2dnZ2dnZ2dnZ2dnZ2dkZAIHDw8WAh8FZ2RkAg0PZBYCZg8WAh4EVGV4dAULMjEtT2N0LTIwMjFkAgUPZBYCZg9kFgQCAQ8QZGQWAGQCBQ8QZGQWAGQCBw9kFgQCAQ8WAh4LXyFJdGVtQ291bnQCBRYKZg9kFgJmDxUCKWh0dHBzOi8vd3d3LmZhY2Vib29rLmNvbS9KU1dTdGVlbE9mZmljaWFsC2ZhLWZhY2Vib29rZAIBD2QWAmYPFQIcaHR0cHM6Ly90d2l0dGVyLmNvbS9qc3dzdGVlbApmYS10d2l0dGVyZAICD2QWAmYPFQIkaHR0cHM6Ly93d3cubGlua2VkaW4uY29tL2NvbXBhbnkvanN3C2ZhLWxpbmtlZGluZAIDD2QWAmYPFQIzaHR0cHM6Ly9pbnN0YWdyYW0uY29tL2pzdy5zdGVlbD9pZ3NoaWQ9anI0c2xybXI2aXh4DGZhLWluc3RhZ3JhbWQCBA9kFgJmDxUCOGh0dHBzOi8vd3d3LnlvdXR1YmUuY29tL2NoYW5uZWwvVUNwTmZPcm5EYW9XNk84bGR6SVNZa0ZBCmZhLXlvdXR1YmVkAgMPFgIfBgXGAjwhLS0gR2xvYmFsIHNpdGUgdGFnIChndGFnLmpzKSAtIEdvb2dsZSBBbmFseXRpY3MgLS0%2BDQo8c2NyaXB0IGFzeW5jIHNyYz0iaHR0cHM6Ly93d3cuZ29vZ2xldGFnbWFuYWdlci5jb20vZ3RhZy9qcz9pZD1VQS0xMjUzMzY3ODgtMSI%2BPC9zY3JpcHQ%2BDQo8c2NyaXB0Pg0KICB3aW5kb3cuZGF0YUxheWVyID0gd2luZG93LmRhdGFMYXllciB8fCBbXTsNCiAgZnVuY3Rpb24gZ3RhZygpe2RhdGFMYXllci5wdXNoKGFyZ3VtZW50cyk7fQ0KICBndGFnKCdqcycsIG5ldyBEYXRlKCkpOw0KDQogIGd0YWcoJ2NvbmZpZycsICdVQS0xMjUzMzY3ODgtMScpOw0KPC9zY3JpcHQ%2BDQogZGQQTg8i1keSnNwTfAjx2LCP0AZKweIB8NkJ2G4R8BO0BA%3D%3D&__VIEWSTATEGENERATOR=C43C4B55&__ASYNCPOST=true&ctl00%24ContentPlaceHolder1%24btnsubmit=Submit";
        Map<String, String> headers = HttpHeaderUtils.getApplicationFormURLEncodedHeaders();
        headers.put("user-agent","Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/93.0.4577.82 Safari/537.36");
        return HttpRequestDto.Builder.httpRequestDto()
                .withRequestType(RequestType.POST)
                .withUrl(url)
                .withHeaders(headers)
                .withPayload(payload)
                .build();
    }
}
