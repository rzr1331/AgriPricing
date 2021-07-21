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
        BuildStep withResponseString(String responseString);
    }

    public static interface BuildStep {
        HttpResponseDto build();
    }


    public static class Builder implements SuccessfulStep, ResponseCodeStep, ResponseHeadersStep, ResponseStringStep, BuildStep {
        private Boolean successful;
        private Integer responseCode;
        private Headers responseHeaders;
        private String responseString;

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
        public BuildStep withResponseString(String responseString) {
            this.responseString = responseString;
            return this;
        }

        @Override
        public HttpResponseDto build() {
            HttpResponseDto
                httpResponseDto = new HttpResponseDto();
            httpResponseDto.setSuccessful(this.successful);
            httpResponseDto.setResponseCode(this.responseCode);
            httpResponseDto.setResponseHeaders(this.responseHeaders);
            httpResponseDto.setResponseString(this.responseString);
            return httpResponseDto;
        }
    }

    @Override
    public String toString() {
        return "HttpResponseDto{" +
                "successful=" + successful +
                ", responseCode=" + responseCode +
                ", responseHeaders=" + responseHeaders +
                ", responseString='" + responseString + '\'' +
                '}';
    }
}
