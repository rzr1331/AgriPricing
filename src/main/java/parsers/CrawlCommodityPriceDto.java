package parsers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CrawlCommodityPriceDto {

    private String productName;

    private String areaName;

    private String city;

    private String state;

    private Double minPrice;

    private Double maxPrice;

    private Long date;

    private CommodityPriceSource source;

    public static interface ProductNameStep {
        AreaNameStep withProductName(String productName);
    }

    public static interface AreaNameStep {
        CityStep withAreaName(String areaName);
    }

    public static interface CityStep {
        StateStep withCity(String city);
    }

    public static interface StateStep {
        MinPriceStep withState(String state);
    }

    public static interface MinPriceStep {
        MaxPriceStep withMinPrice(Double minPrice);
    }

    public static interface MaxPriceStep {
        DateStep withMaxPrice(Double maxPrice);
    }

    public static interface DateStep {
        SourceStep withDate(Long date);
    }

    public static interface SourceStep {
        BuildStep withSource(CommodityPriceSource source);
    }

    public static interface BuildStep {
        CrawlCommodityPriceDto build();
    }

    public static class Builder
        implements ProductNameStep, AreaNameStep, CityStep, StateStep, MinPriceStep, MaxPriceStep,
        DateStep, SourceStep, BuildStep {
        private String productName;
        private String areaName;
        private String city;
        private String state;
        private Double minPrice;
        private Double maxPrice;
        private Long date;
        private CommodityPriceSource source;

        private Builder() {
        }

        public static ProductNameStep crawlCommodityPriceDto() {
            return new Builder();
        }

        @Override
        public AreaNameStep withProductName(String productName) {
            this.productName = productName;
            return this;
        }

        @Override
        public CityStep withAreaName(String areaName) {
            this.areaName = areaName;
            return this;
        }

        @Override
        public StateStep withCity(String city) {
            this.city = city;
            return this;
        }

        @Override
        public MinPriceStep withState(String state) {
            this.state = state;
            return this;
        }

        @Override
        public MaxPriceStep withMinPrice(Double minPrice) {
            this.minPrice = minPrice;
            return this;
        }

        @Override
        public DateStep withMaxPrice(Double maxPrice) {
            this.maxPrice = maxPrice;
            return this;
        }

        @Override
        public SourceStep withDate(Long date) {
            this.date = date;
            return this;
        }

        @Override
        public BuildStep withSource(CommodityPriceSource source) {
            this.source = source;
            return this;
        }

        @Override
        public CrawlCommodityPriceDto build() {
            return new CrawlCommodityPriceDto(
                this.productName,
                this.areaName,
                this.city,
                this.state,
                this.minPrice,
                this.maxPrice,
                this.date,
                this.source
            );
        }
    }
}
