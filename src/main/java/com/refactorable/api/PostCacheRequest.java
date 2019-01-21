package com.refactorable.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;

public class PostCacheRequest {

    @Min( value = 1L, message = "must be greater that 0" )
    private final Integer ttlInMinutes;

    @NotEmpty
    private final String url;

    @JsonCreator
    public PostCacheRequest(
            @JsonProperty( "ttlInMinutes" ) Integer ttlInMinutes,
            @JsonProperty( "url" ) String url ) {
        this.ttlInMinutes = ttlInMinutes;
        this.url = url;
    }

    public Integer getTtlInMinutes() {
        return ttlInMinutes;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return "PostCacheRequest{" +
                "ttlInMinutes=" + ttlInMinutes +
                ", url='" + url + '\'' +
                '}';
    }
}
