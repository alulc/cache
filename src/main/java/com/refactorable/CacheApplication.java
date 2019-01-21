package com.refactorable;

import com.bendb.dropwizard.redis.JedisFactory;
import com.refactorable.resources.CacheResource;
import com.refactorable.service.CachingService;
import com.refactorable.service.InMemoryCachingService;
import com.refactorable.service.RedisCachingService;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CacheApplication extends Application<CacheConfiguration> {

    private static final Logger LOGGER = LoggerFactory.getLogger( CacheApplication.class );

    public static void main( String[] args ) throws Exception {
        new CacheApplication().run( args );
    }

    @Override
    public void run(
            CacheConfiguration configuration,
            Environment environment ) {

        environment.jersey().register( new CacheResource() );
        environment.jersey().register( new AbstractBinder() {
            @Override
            protected void configure() {
                JedisFactory jedisFactory = configuration.getJedisFactory();
                if( jedisFactory == null ) {
                    LOGGER.warn( "an in-memory cache is being used!" );
                    bind( new InMemoryCachingService() ).to( CachingService.class );
                } else {
                    LOGGER.info( "redis is being used!" );
                    bind( new RedisCachingService( jedisFactory.build( environment ) ) ).to( CachingService.class );
                }
            }
        } );
    }
}
