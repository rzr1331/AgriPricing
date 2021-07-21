package parsers;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;

public class CommodityPricePublishingService {

    private static final String TEMPLATE_LOCAL_FILE_LOCATION = "/tmp/commodityPricing/";

    private CommodityPriceCsvConverter commodityPriceCsvConverter = new CommodityPriceCsvConverter();
    
    public void publishCommodityPrice(List<CrawlCommodityPriceDto> crawlCommodityPriceDtos,
        CommodityPriceSource priceSource) throws IOException {

        String fileName =
            TEMPLATE_LOCAL_FILE_LOCATION + "/" + priceSource + System.currentTimeMillis() + ".csv";
        File file = CsvUtils.build(fileName, crawlCommodityPriceDtos, commodityPriceCsvConverter);

    }
}
