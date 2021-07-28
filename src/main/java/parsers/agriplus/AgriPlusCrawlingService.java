package parsers.agriplus;

import parsers.*;
import parsers.enam.EnamParser;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AgriPlusCrawlingService {
    private static String[] commodities={"ajwan","alasande-gram","alsandikai","amaranthus","amla-nelli-kai","amphophalus","antawala","anthorium","apple","apricot-jardalu-khumani","arecanut-betelnut-supari","arhar-tur-red-gram-whole","arhar-dal-tur-dal","ashgourd","avare-dal","bajra-pearl-millet-cumbu","bamboo","banana","banana-green","barley-jau","bay-leaf-tejpatta","beans","beaten-rice","beetroot","bengal-gram-dal-chana-dal","bengal-gram-gram-whole","betal-leaves","bhindi-ladies-finger","bitter-gourd","black-gram-urd-beans-whole","black-gram-dal-urd-dal","black-pepper","bop","bottle-gourd","bran","brinjal","broken-rice","broomstick-flower-broom","bull","bunch-beans","butter","cabbage","capsicum","cardamoms","carnation","carrot","cashewnuts","castor-seed","cauliflower","chapparad-avare","chennangi-dal","cherry","chikoos-sapota","chili-red","chilly-capsicum","chow-chow","chrysanthemum-loose","cloves","cluster-beans","cock","cocoa","coconut","coconut-oil","coconut-seed","coffee","colacasia","copra","coriander-leaves","corriander-seed","cotton","cow","cowpea-lobia-karamani","cowpea-veg","cucumbar-kheera","cummin-seed-jeera","dalda","dhaincha","drumstick","dry-chillies","dry-grapes","duck","duster-beans","egg","elephant-yam-suran","field-pea","fish","foxtail-millet-navane","french-beans-frasbean","garlic","ghee","gingelly-oil","ginger-dry","ginger-green","gladiolus-cut-flower","goat","gram-raw-chholia","grapes","green-avare-w","green-chilli","green-gram-moong-whole","green-gram-dal-moong-dal","green-peas","ground-nut-seed","groundnut","groundnut-split","groundnut-pods-raw","guar","guar-seed-cluster-beans-seed","guava","gur-jaggery","gurellu","he-buffalo","hen","hippe-seed","honge-seed","indian-beans-seam","isabgul-psyllium","jack-fruit","jaffri","jamun-narale-hannu","jarbara","jowar-sorghum","jute","kabuli-chana-chickpeas-white","karbuja-musk-melon","kartali-kantola","knool-khol","kodo-millet-varagu","kulthi-horse-gram","lak-teora","leafy-vegetable","lemon","lentil-masur-whole","lilly","lime","linseed","lint","litchi","little-gourd-kundru","long-melon-kakri","lotus","lotus-sticks","mace","mahedi","mahua","mahua-seed-hippe-seed","maida-atta","maize","mango","mango-raw-ripe","marget","marigold-calcutta","mashrooms","masur-dal","mataki","methi-seeds","methi-leaves","millets","mint-pudina","moath-dal","mousambi-sweet-lime","mustard","mustard-oil","neem-seed","nutmeg","onion","onion-green","orange","orchid","ox","paddy-dhan-basmati","paddy-dhan-common","papaya","papaya-raw","patti-calcutta","peach","pear-marasebu","peas-cod","peas-wet","peas-dry","pegeon-pea-arhar-fali","pepper-garbled","pepper-ungarbled","pigs","pineapple","plum","pointed-gourd-parval","pomegranate","potato","pumpkin","raddish","ragi-finger-millet","raibel","rajgir","ram","resinwood","rice","ridge-gourd(tori)","rose-local","rose-loose","round-gourd","rubber","sabu-dana","safflower","same-savi","seemebadnekai","seetafal","sesamum-sesame-gingelly-til","she-buffalo","she-goat","sheep","snake-gourd","soanf","soapnut-antawala-retha","soji","soyabean","spinach","sponge-gourd","squash-chappal-kadoo","sugar","sunflower","surat-beans-papadi","suva-dill-seed","suvarna-gadde","sweet-potato","sweet-pumpkin","t-v-cumbu","tamarind-fruit","tamarind-seed","tapioca","taramira","tender-coconut","thogrikai","thondekai","tinda","tobacco","tomato","toria","tube-rose-double","tube-rose-single","turmeric","turmeric-raw","turnip","water-melon","wheat","wheat-atta","white-peas","white-pumpkin","wood","yam-ratalu"};
    private static String[] states={"andhra-pradesh","chandigarh","chhattisgarh","gujarat","haryana","himachal-pradesh","jammu-and-kashmir","jharkhand","karnataka","kerala","madhya-pradesh","maharashtra","odisha","puducherry","punjab","rajasthan","tamil-nadu","telangana","uttar-pradesh","uttarakhand","west-bengal",};
    public List<CrawlCommodityPriceDto> handleRequest() {
        List<CrawlCommodityPriceDto> crawlCommodityPriceDtoList=null;
            for(String com:commodities){
                for (String state:states){
                    HttpRequestDto httpRequestDto = buildRequest(com,state);
                    HttpClientPool httpClientPool = new HttpClientPool();
                    try {
                        HttpResponseDto responseDto = httpClientPool.executeRequest(httpRequestDto);
                        if (!responseDto.getSuccessful()) {
                            System.out.println("error while getting response for enam reqeuset [{}] , response [{}]" +
                                    httpRequestDto + responseDto);
                            return new ArrayList<>();
                        }
                        AgriPlusParser agriPlusParser = new AgriPlusParser();
//                        System.out.println(responseDto.getResponseString());
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

    private HttpRequestDto buildRequest(String commodity,String state) {
        String url = CommodityPriceSource.AGRIPLUS.getUrl();
        url=CommodityPriceSource.AGRIPLUS.getUrl()+commodity+"/"+state;
        System.out.println(url);
        Map<String, String> headers = HttpHeaderUtils.getApplicationFormURLEncodedHeaders();
        return HttpRequestDto.Builder.httpRequestDto()
                .withRequestType(RequestType.GET)
                .withUrl(url)
                .withHeaders(headers)
                .withPayload("")
                .build();
    }
}
