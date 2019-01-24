package com.refactorable.core.service;

import com.refactorable.mother.ResponseMother;
import com.refactorable.rs.BadGatewayException;
import org.junit.Test;
import org.mockito.Mockito;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import java.net.URI;

public class JerseyGenericGetResultServiceTest {

    Client client = Mockito.mock( Client.class, Mockito.RETURNS_DEEP_STUBS );
    GenericGetResultService genericGetResultService = new JerseyGenericGetResultService( client );

    @Test( expected = NullPointerException.class )
    public void constructor_nullClient_throwsNullPointerException() {
        new JerseyGenericGetResultService( null );
    }

    @Test( expected = BadGatewayException.class )
    public void get_100_throwsBadGatewayException() {
        Mockito.when( client.target( Mockito.any( URI.class ) ).request().get() ).thenReturn( ResponseMother.statusCode100() );
        genericGetResultService.get( URI.create( "http://www.google.com" ) );
    }

    @Test( expected = BadGatewayException.class )
    public void get_302_throwsBadGatewayException() {
        Mockito.when( client.target( Mockito.any( URI.class ) ).request().get() ).thenReturn( ResponseMother.statusCode302() );
        genericGetResultService.get( URI.create( "http://www.google.com" ) );
    }

    @Test( expected = BadGatewayException.class )
    public void get_400_throwsBadGatewayException() {
        Mockito.when( client.target( Mockito.any( URI.class ) ).request().get() ).thenReturn( ResponseMother.statusCode400() );
        genericGetResultService.get( URI.create( "http://www.google.com" ) );
    }

    @Test( expected = BadGatewayException.class )
    public void get_404_throwsBadGatewayException() {
        Mockito.when( client.target( Mockito.any( URI.class ) ).request().get() ).thenReturn( ResponseMother.statusCode404() );
        genericGetResultService.get( URI.create( "http://www.google.com" ) );
    }

    @Test( expected = BadGatewayException.class )
    public void get_500_throwsBadGatewayException() {
        Mockito.when( client.target( Mockito.any( URI.class ) ).request().get() ).thenReturn( ResponseMother.statusCode500() );
        genericGetResultService.get( URI.create( "http://www.google.com" ) );
    }

    @Test( expected = BadGatewayException.class )
    public void get_noConnection_throwsBadGatewayException() {
        Mockito.when( client.target( Mockito.any( URI.class ) ).request().get() ).thenThrow( new ProcessingException( "cannot connect" ) );
        genericGetResultService.get( URI.create( "http://www.google.com" ) );
    }
    
    @Test
    public void closeQuietly_nullResponse_ok() {
        JerseyGenericGetResultService.closeQuietly( null );
    }

    @Test( expected = NullPointerException.class )
    public void multivaluedMapToMap_nullResponse_throwsNullPointerException() {
        JerseyGenericGetResultService.multivaluedMapToMap( null );
    }
}