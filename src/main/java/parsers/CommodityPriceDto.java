package parsers;

public class CommodityPriceDto {

    private String productId;

    private String productName;

    private String pricingAreaId;

    //it can be mandi name ,or any local area
    private String areaName;

    private String city;

    private String state;

    private Double minPrice;

    private Double maxPrice;

    private Long date;

    private CommodityPriceSource source;

    public CommodityPriceDto() {
    }

    public CommodityPriceDto(String productId, String productName, String pricingAreaId,
        String areaName, String city, String state, Double minPrice, Double maxPrice,
        Long date, CommodityPriceSource source) {
        this.productId = productId;
        this.productName = productName;
        this.pricingAreaId = pricingAreaId;
        this.areaName = areaName;
        this.city = city;
        this.state = state;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.date = date;
        this.source = source;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getPricingAreaId() {
        return pricingAreaId;
    }

    public void setPricingAreaId(String pricingAreaId) {
        this.pricingAreaId = pricingAreaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(Double minPrice) {
        this.minPrice = minPrice;
    }

    public Double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(Double maxPrice) {
        this.maxPrice = maxPrice;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public CommodityPriceSource getSource() {
        return source;
    }

    public void setSource(CommodityPriceSource source) {
        this.source = source;
    }

    public static interface ProductIdStep {
        ProductNameStep withProductId(String productId);
    }

    public static interface ProductNameStep {
        PricingAreaIdStep withProductName(String productName);
    }

    public static interface PricingAreaIdStep {
        AreaNameStep withPricingAreaId(String pricingAreaId);
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
        CommodityPriceDto build();
    }

    public static class Builder
        implements ProductIdStep, ProductNameStep, PricingAreaIdStep, AreaNameStep, CityStep,
        StateStep, MinPriceStep, MaxPriceStep, DateStep, SourceStep, BuildStep {
        private String productId;

        private String productName;

        private String pricingAreaId;

        private String areaName;

        private String city;

        private String state;

        private Double minPrice;

        private Double maxPrice;

        private Long date;

        private CommodityPriceSource source;

        private Builder() {
        }

        public static ProductIdStep commodityPriceDto() {
            return new Builder();
        }

        @Override
        public ProductNameStep withProductId(String productId) {
            this.productId = productId;
            return this;
        }

        @Override
        public PricingAreaIdStep withProductName(String productName) {
            this.productName = productName;
            return this;
        }

        @Override
        public AreaNameStep withPricingAreaId(String pricingAreaId) {
            this.pricingAreaId = pricingAreaId;
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
        public CommodityPriceDto build() {
            return new CommodityPriceDto(
                this.productId,
                this.productName,
                this.pricingAreaId,
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

