package parsers;


public class CommodityPriceCsvConverter implements CsvConverter<CrawlCommodityPriceDto> {

    @Override
    public String[] convert(CrawlCommodityPriceDto item) {

        return new String[] {item.getState(), item.getCity(),
            item.getAreaName(), item.getProductName(),
            String.valueOf("pricingAreaId"), String.valueOf("productAreaId"),
            String.valueOf(item.getMaxPrice()), String.valueOf(item.getMinPrice()),
            String.valueOf(item.getDate()), item.getSource().name()
        };
    }
}
