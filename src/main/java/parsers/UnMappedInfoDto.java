package parsers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Umesh. Date: 16/07/21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnMappedInfoDto {

    private UnMappedEntityType unMappedEntity;

    private String rawValue;


    public static interface UnMappedEntityStep {
        RawValueStep withUnMappedEntity(UnMappedEntityType unMappedEntity);
    }

    public static interface RawValueStep {
        BuildStep withRawValue(String rawValue);
    }

    public static interface BuildStep {
        UnMappedInfoDto build();
    }

    public static class Builder implements UnMappedEntityStep, RawValueStep, BuildStep {
        private UnMappedEntityType unMappedEntity;
        private String rawValue;

        private Builder() {
        }

        public static UnMappedEntityStep unMappedInfoDto() {
            return new Builder();
        }

        @Override
        public RawValueStep withUnMappedEntity(UnMappedEntityType unMappedEntity) {
            this.unMappedEntity = unMappedEntity;
            return this;
        }

        @Override
        public BuildStep withRawValue(String rawValue) {
            this.rawValue = rawValue;
            return this;
        }

        @Override
        public UnMappedInfoDto build() {
            return new UnMappedInfoDto(
                this.unMappedEntity,
                this.rawValue
            );
        }
    }

    @Override public String toString() {
        return "UnMappedInfoDto{" +
            "unMappedEntity=" + unMappedEntity +
            ", rawValue='" + rawValue + '\'' +
            '}';
    }
}
