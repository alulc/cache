package com.refactorable.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotEmpty;

public class Header {

    @NotEmpty
    @ApiModelProperty( required = true )
    private final String name;

    @NotEmpty
    @ApiModelProperty( required = true )
    private final String value;

    @JsonCreator
    public Header(
            @JsonProperty("name") String name,
            @JsonProperty("value") String value ) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Header{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
