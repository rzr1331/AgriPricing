package parsers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import okhttp3.Headers;

/**
 * Created by Umesh.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HttpResponseDto {

    private Boolean successful;

    private Integer responseCode;

    private Headers responseHeaders;

    private String responseString;

    private Headers headers;

    public static interface SuccessfulStep {
        ResponseCodeStep withSuccessful(Boolean successful);
    }

    public static interface ResponseCodeStep {
        ResponseHeadersStep withResponseCode(Integer responseCode);
    }

    public static interface ResponseHeadersStep {
        ResponseStringStep withResponseHeaders(Headers responseHeaders);
    }

    public static interface ResponseStringStep {
        HeadersStep withResponseString(String responseString);
    }

    public static interface HeadersStep {
        BuildStep withHeaders(Headers headers);
    }

    public static interface BuildStep {
        HttpResponseDto build();
    }

    public static class Builder
        implements SuccessfulStep, ResponseCodeStep, ResponseHeadersStep, ResponseStringStep,
        HeadersStep, BuildStep {
        private Boolean successful;

        private Integer responseCode;

        private Headers responseHeaders;

        private String responseString;

        private Headers headers;

        private Builder() {
        }

        public static SuccessfulStep httpResponseDto() {
            return new Builder();
        }

        @Override
        public ResponseCodeStep withSuccessful(Boolean successful) {
            this.successful = successful;
            return this;
        }

        @Override
        public ResponseHeadersStep withResponseCode(Integer responseCode) {
            this.responseCode = responseCode;
            return this;
        }

        @Override
        public ResponseStringStep withResponseHeaders(Headers responseHeaders) {
            this.responseHeaders = responseHeaders;
            return this;
        }

        @Override
        public HeadersStep withResponseString(String responseString) {
            this.responseString = responseString;
            return this;
        }

        @Override
        public BuildStep withHeaders(Headers headers) {
            this.headers = headers;
            return this;
        }

        @Override
        public HttpResponseDto build() {
            return new HttpResponseDto(
                this.successful,
                this.responseCode,
                this.responseHeaders,
                this.responseString,
                this.headers
            );
        }
    }

    @Override public String toString() {
        return "HttpResponseDto{" +
            "successful=" + successful +
            ", responseCode=" + responseCode +
            ", responseHeaders=" + responseHeaders +
            ", responseString='" + responseString + '\'' +
            ", headers=" + headers +
            '}';
    }
}
