package parsers;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Umesh.
 * Date: 12/07/21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HttpRequestDto {

    private RequestType requestType;

    private String url;

    private Map<String, String> headers;

    private String payload;

    public static interface RequestTypeStep {
        UrlStep withRequestType(RequestType requestType);
    }

    public static interface UrlStep {
        HeadersStep withUrl(String url);
    }

    public static interface HeadersStep {
        PayloadStep withHeaders(Map<String, String> headers);
    }

    public static interface PayloadStep {
        BuildStep withPayload(String payload);
    }

    public static interface BuildStep {
        HttpRequestDto build();
    }


    public static class Builder implements RequestTypeStep, UrlStep, HeadersStep, PayloadStep, BuildStep {
        private RequestType requestType;
        private String url;
        private Map<String, String> headers;
        private String payload;

        private Builder() {
        }

        public static RequestTypeStep httpRequestDto() {
            return new Builder();
        }

        @Override
        public UrlStep withRequestType(RequestType requestType) {
            this.requestType = requestType;
            return this;
        }

        @Override
        public HeadersStep withUrl(String url) {
            this.url = url;
            return this;
        }

        @Override
        public PayloadStep withHeaders(Map<String, String> headers) {
            this.headers = headers;
            return this;
        }

        @Override
        public BuildStep withPayload(String payload) {
            this.payload = payload;
            return this;
        }

        @Override
        public HttpRequestDto build() {
            HttpRequestDto
                httpRequestDto = new HttpRequestDto();
            httpRequestDto.setRequestType(this.requestType);
            httpRequestDto.setUrl(this.url);
            httpRequestDto.setHeaders(this.headers);
            httpRequestDto.setPayload(this.payload);
            return httpRequestDto;
        }
    }
}
