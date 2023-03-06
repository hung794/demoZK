package org.example.utils;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class ApiUtil {
    private static final Client client = Client.create();
    public static ClientResponse callApi(String url, Object body, String method) {
        WebResource webResource = client
                .resource(url);
        ClientResponse response = webResource.accept("application/json")
                .type("application/json")
                .method(method, ClientResponse.class, body);
        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + response.getStatus());
        }
        return response;
    }
}