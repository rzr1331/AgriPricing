package parsers;

import java.util.HashMap;
import java.util.Map;
import org.apache.http.HttpHeaders;
import org.apache.http.entity.ContentType;

/**
 *
 */
public class HttpHeaderUtils {

    public static Map<String, String> getDefaultHttpHeaders() {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put(HttpHeaders.CONTENT_TYPE,
            ContentType.APPLICATION_JSON.getMimeType());

        return headers;
    }

    public static Map<String, String> getApplicationFormURLEncodedHeaders() {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put(HttpHeaders.CONTENT_TYPE,
                ContentType.APPLICATION_FORM_URLENCODED.getMimeType());
        return headers;
    }

    public static Map<String, String> getTextPlainHttpHeaders() {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put(HttpHeaders.CONTENT_TYPE,
            ContentType.TEXT_PLAIN.getMimeType());

        return headers;
    }

}

