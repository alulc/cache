package com.refactorable.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.List;

public class GetCacheResult {

    @NotEmpty
    private final String url;

    @NotNull
    private final List<Header> headers;

    @NotEmpty
    private final String body;

    @JsonCreator
    public GetCacheResult(
            @JsonProperty("url") String url,
            @JsonProperty("headers") List<Header> headers,
            @JsonProperty("body") String body ) {
        this.url = url;
        this.headers = headers;
        this.body = body;
    }

    public String getUrl() {
        return url;
    }

    public List<Header> getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "GetCacheResult{" +
                "url='" + url + '\'' +
                ", headers=" + headers +
                ", body='" + body + '\'' +
                '}';
    }
}
