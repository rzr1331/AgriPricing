package parsers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.apache.http.HttpHeaders;
import org.apache.http.entity.ContentType;

public class HttpClientPool {

    public static OkHttpClient client = new OkHttpClient();
    
    public void init() {
        //TODO: need to check for optimal time out
        client = new OkHttpClient().newBuilder()
                .readTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();
        //TODO: remove
        System.out.println(client.toString());
    }

    public HttpResponseDto executeRequest(HttpRequestDto httpRequestDto) throws IOException {
        if (RequestType.GET == httpRequestDto.getRequestType()) {
            return getHttpGetResponseDto(httpRequestDto.getUrl(), httpRequestDto.getHeaders());
        } else {
            return getHttpPostResponseDto(httpRequestDto.getUrl(), httpRequestDto.getPayload(), httpRequestDto.getHeaders());
        }
    }

    public HttpResponseDto getHttpGetResponseDto(String url, Map<String, String> headers) throws IOException {

        Response response = getHttpGetResponse(url, headers);
        ResponseBody responseBody = response.body();

        String responseString = null;

        if (responseBody != null) {
            responseString = responseBody.string();
        }
        response.close();
        return HttpResponseDto.Builder.httpResponseDto()
                .withSuccessful(response.isSuccessful())
                .withResponseCode(response.code())
                .withResponseHeaders(response.headers())
                .withResponseString(responseString)
                .build();
    }

    public Response getHttpGetResponse(String url) throws IOException {
        return getHttpGetResponse(url, new HashMap<String, String>());
    }

    public Response getHttpGetResponse(String url, Map<String, String> headers) throws IOException {

        Request request = new Request.Builder()
                .headers(Headers.of(headers))
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        return response;
    }

    public Response getHttpPostResponse(String url, String payload, Map<String, String> headers) throws IOException {

        String contentType = headers.get(HttpHeaders.CONTENT_TYPE);
        MediaType mediaType = MediaType.parse(contentType);

        if (mediaType == null) {
            throw new UnsupportedOperationException(String.format("Unsupported media type %s", contentType));
        }

        RequestBody body = buildRequestBody(mediaType, payload);
        Request request = new Request.Builder()
                .url(url)
                .headers(Headers.of(headers))
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response;
    }

    private RequestBody buildRequestBody(MediaType mediaType, String payload) throws
        UnsupportedEncodingException {

        if (!mediaType.toString().equals(ContentType.APPLICATION_FORM_URLENCODED.getMimeType())) {
            return RequestBody.create(mediaType, payload);
        }

        String[] queryParams = payload.split("&");
        FormBody.Builder formBody = new FormBody.Builder();
        for (String queryParam : queryParams) {
            String[] keyValuePair = queryParam.split("=");
            String key = URLDecoder.decode(keyValuePair[0], "UTF-8");
            String value = "";
            if (keyValuePair.length == 2) {
                value = URLDecoder.decode(keyValuePair[1], "UTF-8");
            }
            formBody.add(key, value);
        }
        return formBody.build();
    }

    public HttpResponseDto getHttpPostResponseDto(String url, String payload, Map<String, String> headers)
            throws IOException {

        Response response = getHttpPostResponse(url, payload, headers);
        ResponseBody responseBody = response.body();
        String responseString = null;
        if (responseBody != null) {
            responseString = responseBody.string();
        }
        response.close();
        return HttpResponseDto.Builder.httpResponseDto()
                .withSuccessful(response.isSuccessful())
                .withResponseCode(response.code())
                .withResponseHeaders(response.headers())
                .withResponseString(responseString)
                .build();

    }
}
