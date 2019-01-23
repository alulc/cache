package com.refactorable.resources;

import com.refactorable.api.PostCacheRequestMother;
import com.refactorable.core.service.CachingService;
import com.refactorable.core.service.InMemoryCachingService;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.ClassRule;
import org.junit.Test;
import org.mockito.Mockito;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;

public class CacheResourceTest {

    private static CachingService cachingService = Mockito.mock( InMemoryCachingService.class );

    @ClassRule
    public static ResourceTestRule resources = ResourceTestRule.builder()
            .addResource( new CacheResource( cachingService ) )
            .build();

    @Test
    public void post_invalidUrl_422() {
        Response response = resources.client().target( "/cache" ).request().post(  Entity.entity(
                PostCacheRequestMother.invalidUrlAndTtlOneMinute(),
                MediaType.APPLICATION_JSON_TYPE ) );
        assertEquals( 422, response.getStatus() );
    }

    @Test
    public void post_blankUrl_422() {
        Response response = resources.client().target( "/cache" ).request().post(  Entity.entity(
                PostCacheRequestMother.blankUrlAndTtlOneMinute(),
                MediaType.APPLICATION_JSON_TYPE ) );
        assertEquals( 422, response.getStatus() );
    }

    @Test
    public void post_nullUrl_422() {
        Response response = resources.client().target( "/cache" ).request().post(  Entity.entity(
                PostCacheRequestMother.nullUrlAndTtlOneMinute(),
                MediaType.APPLICATION_JSON_TYPE ) );
        assertEquals( 422, response.getStatus() );
    }

    @Test
    public void post_ttlLessThanOne_422() {
        Response response = resources.client().target( "/cache" ).request().post(  Entity.entity(
                PostCacheRequestMother.googleAndTtllessThanOneMinute(),
                MediaType.APPLICATION_JSON_TYPE ) );
        assertEquals( 422, response.getStatus() );
    }

    @Test
    public void post_ttlGreaterThanOneYear_422() {
        Response response = resources.client().target( "/cache" ).request().post(  Entity.entity(
                PostCacheRequestMother.googleAndTtlGreaterThanOneYear(),
                MediaType.APPLICATION_JSON_TYPE ) );
        assertEquals( 422, response.getStatus() );
    }
}