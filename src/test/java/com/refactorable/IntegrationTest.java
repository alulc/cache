package com.refactorable;

import com.refactorable.api.GetCacheResult;
import com.refactorable.mother.PostCacheRequestMother;
import com.refactorable.resources.CacheResource;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.ClassRule;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class IntegrationTest {

    @ClassRule
    public static final DropwizardAppRule<CacheConfiguration> RULE = new DropwizardAppRule<>(
            CacheApplication.class,
            ResourceHelpers.resourceFilePath( "inMemory.yml" ) );

    @Test
    public void get_invalidUUID_404() {
        WebTarget target = RULE.client().target(
                "http://localhost:"
                        + RULE.getLocalPort()
                        + CacheResource.PATH_BASE
                        + "/"
                        + UUID.randomUUID() );
        Response response = target.request().get();
        assertEquals( 404, response.getStatus() );
    }

    @Test
    public void get_validUUID_200AndValidResponse() {

        WebTarget target = RULE.client().target(
                "http://localhost:"
                        + RULE.getLocalPort()
                        + CacheResource.PATH_BASE );
        Response response = target.request().post( Entity.entity(
                PostCacheRequestMother.googleAndTtlOneMinute(),
                MediaType.APPLICATION_JSON_TYPE ) );

        target = RULE.client().target( response.getHeaderString( "location" ) );
        response = target.request().get();
        assertEquals( 200, response.getStatus() );
        GetCacheResult getCacheResult = response.readEntity( GetCacheResult.class );
        assertEquals(
                PostCacheRequestMother.googleAndTtlOneMinute().getUrl(),
                getCacheResult.getUrl() );
        assertNotNull( getCacheResult.getHeaders() );
        assertNotNull( getCacheResult.getBodyAsBase64Encoded() );
    }

    @Test
    public void get_validUUIDButFailsWhenTtlExpires_404() {

        WebTarget target = RULE.client().target(
                "http://localhost:"
                        + RULE.getLocalPort()
                        + CacheResource.PATH_BASE );
        Response response = target.request().post( Entity.entity(
                PostCacheRequestMother.googleAndTtlOneMinute(),
                MediaType.APPLICATION_JSON_TYPE ) );

        String header = response.getHeaderString( "location" );
        target = RULE.client().target( header );
        response = target.request().get();
        assertEquals( 200, response.getStatus() );

        // wait for ttl to expire
        LoggerFactory.getLogger( this.getClass() ).info( "waiting for ttl to expire..." );
        try {
            Thread.sleep( 61000 );
        } catch( Exception e ) {
        }

        target = RULE.client().target( header );
        response = target.request().get();
        assertEquals( 404, response.getStatus() );
    }

    @Test
    public void post_validTtlAndUrl_201AndLocationHeader() {

        WebTarget target = RULE.client().target(
                "http://localhost:"
                        + RULE.getLocalPort()
                        + CacheResource.PATH_BASE );
        Response response = target.request().post( Entity.entity(
                PostCacheRequestMother.googleAndTtlOneMinute(),
                MediaType.APPLICATION_JSON_TYPE ) );
        assertEquals( 201, response.getStatus() );
        assertNotNull( response.getHeaderString( "location" ) );
    }

    @Test
    public void post_badUrl_502() {

        WebTarget target = RULE.client().target(
                "http://localhost:"
                        + RULE.getLocalPort()
                        + CacheResource.PATH_BASE );
        Response response = target.request().post( Entity.entity(
                PostCacheRequestMother.badUrlAndTtlOneMinute(),
                MediaType.APPLICATION_JSON_TYPE ) );
        assertEquals( 502, response.getStatus() );
    }
}
