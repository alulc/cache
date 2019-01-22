package com.refactorable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.refactorable.resources.CacheResource;
import com.refactorable.service.CachingService;
import com.refactorable.service.InMemoryCachingService;
import com.refactorable.service.RedisCachingService;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CacheApplication extends Application<CacheConfiguration> {

    private static final Logger LOGGER = LoggerFactory.getLogger( CacheApplication.class );

    public static void main( String[] args ) throws Exception {
        new CacheApplication().run( args );
    }

    @Override
    public void initialize( Bootstrap<CacheConfiguration> bootstrap ) {
        bootstrap.addBundle( new SwaggerBundle<CacheConfiguration>() {
            @Override
            protected SwaggerBundleConfiguration getSwaggerBundleConfiguration( CacheConfiguration configuration ) {
                return configuration.getSwaggerBundleConfiguration();
            }
        } );
    }

    @Override
    public void run(
            CacheConfiguration configuration,
            Environment environment ) {

        CachingService cachingService = null;
        if( configuration.getUseInMemoryCache() ) {
            LOGGER.warn( "an in-memory cache is being used!" );
            cachingService = new InMemoryCachingService();
        } else if( configuration.getJedisFactory() != null ) {
            LOGGER.info( "redis is being used!" );
            cachingService = new RedisCachingService( configuration.getJedisFactory().build( environment ) );
        } else {
            LOGGER.info( "redis configuration not defined!" );
            System.exit( 0 );
        }
        environment.jersey().register( new CacheResource( cachingService ) );
        environment.getObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }
}
