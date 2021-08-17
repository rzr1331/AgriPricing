package parsers.kissanhelpline;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import parsers.CommodityPriceSource;
import parsers.CrawlCommodityPriceDto;
import parsers.DateUtils;
import parsers.ICommodityPricingParser;


import java.util.ArrayList;
import java.util.List;


public class KissanParser implements ICommodityPricingParser {
    private static final String kissanParserDateFormat = "yyyy-MM-dd";
    @Override
    public List<CrawlCommodityPriceDto> parseCommodityPrice(String rawResponse) {
        List<CrawlCommodityPriceDto> priceDtos = new ArrayList<>();
        Document doc = Jsoup.parse(rawResponse);
        Elements rows= doc.getElementsByTag("tr");
        for(Element row:rows){
            Elements columns =row.getElementsByTag("td");
            //checking if row has entries
            if(columns.size() == 10){
                String commodity = columns.get(4).text().replace("<\\/td>\\n","");
                String state=columns.get(1).text().replace("<\\/td>\\n","");
                String district=columns.get(2).text().replace("<\\/td>\\n","");
                String market=columns.get(3).text().replace("<\\/td>\\n","");
                String minPricestr=columns.get(6).text().replace("Rs\\/Quintal<\\/td>\\n","");
                String maxPricestr=columns.get(7).text().replace("Rs\\/Quintal<\\/td>\\n","");
                Double minPrice=Double.parseDouble(minPricestr);
                Double maxPrice=Double.parseDouble(maxPricestr);
                String dateStr=columns.get(9).text().replace("<\\/td>\\n <\\/tr>\\n","");
                Long date = DateUtils.parseToLong(kissanParserDateFormat, dateStr);
                if (date == null) {
                    continue;
                }

                priceDtos.add(CrawlCommodityPriceDto.Builder.crawlCommodityPriceDto()
                        .withProductName(commodity)
                        .withAreaName(market)
                        .withCity(district)
                        .withState(state)
                        .withMinPrice(minPrice)
                        .withMaxPrice(maxPrice)
                        .withDate(date)
                        .withSource(CommodityPriceSource.KISSANHELPLINE)
                        .build());
            }
        }
        return priceDtos;
    }
}
