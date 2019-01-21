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

        CachingService cachingService = null;
        JedisFactory jedisFactory = configuration.getJedisFactory();
        if( jedisFactory == null ) {
            LOGGER.warn( "an in-memory cache is being used!" );
            cachingService = new InMemoryCachingService();
        } else {
            LOGGER.info( "redis is being used!" );
            cachingService = new RedisCachingService( jedisFactory.build( environment ) );
        }
        environment.jersey().register( new CacheResource( cachingService ) );
    }
}
