package com.refactorable;

import com.bendb.dropwizard.redis.JedisFactory;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

@JsonIgnoreProperties( ignoreUnknown = true )
public class CacheConfiguration extends Configuration {

    @JsonProperty
    private JedisFactory redis;

    public JedisFactory getJedisFactory() {
        return redis;
    }
}
