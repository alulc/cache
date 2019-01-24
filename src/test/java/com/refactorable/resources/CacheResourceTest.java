package com.refactorable.resources;

import com.refactorable.core.model.GenericGetResult;
import com.refactorable.core.service.CachingService;
import com.refactorable.core.service.GenericGetResultService;
import com.refactorable.mother.GenericGetResultMother;
import com.refactorable.mother.PostCacheRequestMother;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.ClassRule;
import org.junit.Test;
import org.mockito.Mockito;

import javax.ws.rs.ServiceUnavailableException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class CacheResourceTest {

    private static CachingService<GenericGetResult> cachingService = Mockito.mock( CachingService.class );
    private static GenericGetResultService genericGetResultService = Mockito.mock( GenericGetResultService.class );

    @ClassRule
    public static ResourceTestRule resources = ResourceTestRule.builder()
            .addResource( new CacheResource( cachingService, genericGetResultService ) )
            .build();

    @Test
    public void post_invalidUrl_422() {
        Response response = resources.client().target( "/cache" ).request().post( Entity.entity(
                PostCacheRequestMother.invalidUrlAndTtlOneMinute(),
                MediaType.APPLICATION_JSON_TYPE ) );
        assertEquals( 422, response.getStatus() );
    }

    @Test
    public void post_blankUrl_422() {
        Response response = resources.client().target( "/cache" ).request().post( Entity.entity(
                PostCacheRequestMother.blankUrlAndTtlOneMinute(),
                MediaType.APPLICATION_JSON_TYPE ) );
        assertEquals( 422, response.getStatus() );
    }

    @Test
    public void post_nullUrl_422() {
        Response response = resources.client().target( "/cache" ).request().post( Entity.entity(
                PostCacheRequestMother.nullUrlAndTtlOneMinute(),
                MediaType.APPLICATION_JSON_TYPE ) );
        assertEquals( 422, response.getStatus() );
    }

    @Test
    public void post_ttlLessThanOne_422() {
        Response response = resources.client().target( "/cache" ).request().post( Entity.entity(
                PostCacheRequestMother.googleAndTtllessThanOneMinute(),
                MediaType.APPLICATION_JSON_TYPE ) );
        assertEquals( 422, response.getStatus() );
    }

    @Test
    public void post_ttlGreaterThanOneYear_422() {
        Response response = resources.client().target( "/cache" ).request().post( Entity.entity(
                PostCacheRequestMother.googleAndTtlGreaterThanOneYear(),
                MediaType.APPLICATION_JSON_TYPE ) );
        assertEquals( 422, response.getStatus() );
    }

    @Test
    public void post_cacheIsDown_503() {
        Mockito.doReturn( GenericGetResultMother.google() ).when( genericGetResultService ).get( Mockito.any() );
        Mockito.doThrow( new ServiceUnavailableException() ).when( cachingService ).add( Mockito.anyInt(), Mockito.any( GenericGetResult.class ) );
        Response response = resources.client().target( "/cache" ).request().post( Entity.entity(
                PostCacheRequestMother.googleAndTtlOneMinute(),
                MediaType.APPLICATION_JSON_TYPE ) );
        assertEquals( 503, response.getStatus() );
    }

    @Test
    public void get_cacheIsDown_503() {
        Mockito.doReturn( GenericGetResultMother.google() ).when( genericGetResultService ).get( Mockito.any() );
        Mockito.doThrow( new ServiceUnavailableException() ).when( cachingService ).get( Mockito.any( UUID.class ), Mockito.any( Class.class ) );
        Response response = resources.client().target( "/cache/" + UUID.randomUUID().toString() ).request().get();
        assertEquals( 503, response.getStatus() );
    }
}