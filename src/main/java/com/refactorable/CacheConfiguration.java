package com.refactorable;

import com.bendb.dropwizard.redis.JedisFactory;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;

@JsonIgnoreProperties( ignoreUnknown = true )
public class CacheConfiguration extends Configuration {

    @JsonProperty
    private JedisFactory redis;

    @JsonProperty( "swagger" )
    private SwaggerBundleConfiguration swaggerBundleConfiguration;

    @JsonProperty
    private boolean useInMemoryCache;

    public JedisFactory getJedisFactory() {
        return redis;
    }

    public SwaggerBundleConfiguration getSwaggerBundleConfiguration() {
        return swaggerBundleConfiguration;
    }

    public Boolean getUseInMemoryCache() {
        return useInMemoryCache;
    }
}
