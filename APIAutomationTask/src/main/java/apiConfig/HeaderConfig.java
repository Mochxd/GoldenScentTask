package apiConfig;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * HTTP headers configuration for API requests
 * Provides standardized header configurations for different request types
 */
public class HeaderConfig {

    public Map<String, String> defaultHeaders(){
        Map<String, String> defaultHeaders = Collections.synchronizedMap(new HashMap<>());
        defaultHeaders.put("Content-Type", "application/json; charset=utf-8");
        defaultHeaders.put("accept", "application/json");
        return defaultHeaders;
    }
    public Map<String, String> defaultHeadersNoCharset(){
        Map<String, String> defaultHeaders = Collections.synchronizedMap(new HashMap<>());
        defaultHeaders.put("Content-Type", "application/json");
        defaultHeaders.put("accept", "application/json");
        return defaultHeaders;
    }
    // Headers with Bearer token authentication
    public Map<String, String> headersWithBearerToken(String token){
        Map<String, String> defaultHeaders = Collections.synchronizedMap(new HashMap<>());
        defaultHeaders.put("Content-Type", "application/json");
        defaultHeaders.put("Authorization", "Bearer "+token);
        return defaultHeaders;

    }

}
