package com.refactorable.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

public class PostCacheRequest {

    @Min( value = 1L, message = "must be greater that 0" )
    @Max( value = 525600L, message = "must be less than or equal to 525600" )
    @ApiModelProperty( required = true )
    private final Integer ttlInMinutes;

    @NotEmpty
    @ApiModelProperty( required = true )
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
