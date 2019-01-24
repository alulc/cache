package com.refactorable.core.service;

import com.refactorable.core.model.GenericGetResult;
import com.refactorable.rs.BadGatewayException;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JerseyGenericGetResultService implements GenericGetResultService {

    private static final Logger LOGGER = LoggerFactory.getLogger( JerseyGenericGetResultService.class );

    private final Client client;

    /**
     *
     * @param client cannot be null
     */
    public JerseyGenericGetResultService( Client client ) {
        LOGGER.info( "creating {}!", this.getClass().getSimpleName() );
        this.client = Validate.notNull( client );
    }


    @Override
    public GenericGetResult get( URI uri ) {

        Validate.notNull( uri );

        Response response = null;
        try {
            response = client.target( uri ).request().get();
            if( response.getStatus() < 200 || response.getStatus() >= 300 ) {
                // treat any request that doesn't result in a 2XX (Successful) as an issue with the upstream server.
                LOGGER.warn( "received '{}' status GET '{}'", response.getStatus(), uri );
                throw new BadGatewayException();
            }
            return new GenericGetResult(
                    uri,
                    multivaluedMapToMap( response.getStringHeaders() ),
                    response.readEntity( String.class ) );
        } catch( ProcessingException pe ) {
            LOGGER.warn( "issue connecting to '{}'", uri, pe );
            throw new BadGatewayException();
        } finally {
            closeQuietly( response );
        }
    }

    /**
     *
     * {@link MultivaluedMap} is not serializable so we need to convert is to a {@link Map}.
     *
     * If during conversion a header has multiple values, they are combined into
     * a String that is comma delimited.
     *
     * @param multivaluedMap cannot be null
     *
     * @return {@link Map} representation of {@link MultivaluedMap}
     */
    static Map<String, String> multivaluedMapToMap( MultivaluedMap<String, String> multivaluedMap ) {

        Validate.notNull( multivaluedMap );

        Map<String, String> map = new HashMap<>( multivaluedMap.size() );
        for( Map.Entry<String, List<String>> entry : multivaluedMap.entrySet() ) {
            map.put( entry.getKey(), String.join( ",", entry.getValue() ) );
        }
        LOGGER.debug( "{} to {}", multivaluedMap, map );
        return map;
    }

    static void closeQuietly( Response response ) {
        if( response != null ) {
            LOGGER.debug( "closing response connection" );
            try {
                response.close();
                LOGGER.debug( "response connection closed" );
            } catch( Exception e ) {
                LOGGER.warn( "failed to close response connection!", e );
            }
        }
    }
}
