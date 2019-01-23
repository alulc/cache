package com.refactorable.core.model;

import com.refactorable.api.ResponseMother;
import com.refactorable.rs.BadGatewayException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import java.net.URI;
import java.util.HashMap;

public class CacheableGetResultTest {

    private static Client CLIENT;

    @Before
    public void setUp() {
        CLIENT = Mockito.mock( Client.class, Mockito.RETURNS_DEEP_STUBS );
        CacheableGetResult.CLIENT = CLIENT;
    }

    @After
    public void after(){
        CacheableGetResult.CLIENT = ClientBuilder.newClient();
    }

    @Test( expected = NullPointerException.class )
    public void constructor_nullUri_throwsNullPointerException() {
        new CacheableGetResult(
                null,
                new HashMap<>(),
                "" );
    }

    @Test( expected = NullPointerException.class )
    public void constructor_nullHeaders_throwsNullPointerException() {
        new CacheableGetResult(
                URI.create( "http://www.google.com" ),
                null,
                "" );
    }

    @Test( expected = NullPointerException.class )
    public void constructor_nullBody_throwsNullPointerException() {
        new CacheableGetResult(
                URI.create( "http://www.google.com" ),
                new HashMap<>(),
                null );
    }

    @Test( expected = NullPointerException.class )
    public void fromUri_nullUri_throwsNullPointerException() {
        CacheableGetResult.fromUri( null );
    }

    @Test( expected = BadGatewayException.class )
    public void fromUri_100_throwsBadGatewayException() {
        Mockito.when( CLIENT.target( Mockito.any( URI.class ) ).request().get() ).thenReturn( ResponseMother.statusCode100() );
        CacheableGetResult.fromUri( URI.create( "http://www.google.com" ) );
    }

    @Test( expected = BadGatewayException.class )
    public void fromUri_302_throwsBadGatewayException() {
        Mockito.when( CLIENT.target( Mockito.any( URI.class ) ).request().get() ).thenReturn( ResponseMother.statusCode302() );
        CacheableGetResult.fromUri( URI.create( "http://www.google.com" ) );
    }

    @Test( expected = BadGatewayException.class )
    public void fromUri_400_throwsBadGatewayException() {
        Mockito.when( CLIENT.target( Mockito.any( URI.class ) ).request().get() ).thenReturn( ResponseMother.statusCode400() );
        CacheableGetResult.fromUri( URI.create( "http://www.google.com" ) );
    }

    @Test( expected = BadGatewayException.class )
    public void fromUri_404_throwsBadGatewayException() {
        Mockito.when( CLIENT.target( Mockito.any( URI.class ) ).request().get() ).thenReturn( ResponseMother.statusCode404() );
        CacheableGetResult.fromUri( URI.create( "http://www.google.com" ) );
    }

    @Test( expected = BadGatewayException.class )
    public void fromUri_500_throwsBadGatewayException() {
        Mockito.when( CLIENT.target( Mockito.any( URI.class ) ).request().get() ).thenReturn( ResponseMother.statusCode500() );
        CacheableGetResult.fromUri( URI.create( "http://www.google.com" ) );
    }

    @Test( expected = BadGatewayException.class )
    public void fromUri_noConnection_throwsBadGatewayException() {
        Mockito.when( CLIENT.target( Mockito.any( URI.class ) ).request().get() ).thenThrow( new ProcessingException( "cannot connect" ) );
        CacheableGetResult.fromUri( URI.create( "http://www.google.com" ) );
    }

    @Test
    public void closeQuietly_nullResponse_ok() {
        CacheableGetResult.closeQuietly( null );
    }
}