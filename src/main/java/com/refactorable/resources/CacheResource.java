package com.refactorable.resources;

import com.refactorable.api.GetCacheResult;
import com.refactorable.api.Header;
import com.refactorable.api.PostCacheRequest;
import com.refactorable.core.CacheableGetResult;
import com.refactorable.service.CachingService;
import com.refactorable.service.RedisCachingService;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Path( "/cache" )
public class CacheResource {

    private static final Logger LOGGER = LoggerFactory.getLogger( CacheResource.class );

    private final CachingService cachingService;

    /**
     *
     * @param cachingService cannot be null
     */
    public CacheResource( CachingService cachingService ) {
        LOGGER.info( "creating {}!", this.getClass().getSimpleName() );
        this.cachingService = Validate.notNull( cachingService );
    }

    @GET
    @Path( "/{id}" )
    @Produces( MediaType.APPLICATION_JSON )
    public GetCacheResult get( @PathParam( "id" ) UUID id ) {

        Optional<CacheableGetResult> optionalCacheableGetResult = cachingService.get( id );

        if( !optionalCacheableGetResult.isPresent() ) throw new NotFoundException();

        CacheableGetResult cacheableGetResult = optionalCacheableGetResult.get();
        GetCacheResult getCacheResult = new GetCacheResult(
                cacheableGetResult.getUri().toString(),
                cacheableGetResult.getHeaders()
                        .entrySet()
                        .stream()
                        .map( e -> new Header( e.getKey(), e.getValue() ) )
                        .collect( Collectors.toList() ),
                cacheableGetResult.getBody() );
        return getCacheResult;
    }

    @POST
    @Produces( MediaType.APPLICATION_JSON )
    @Consumes( MediaType.APPLICATION_JSON )
    public Response post(
            @Valid PostCacheRequest postCacheRequest,
            @Context UriInfo uriInfo ) {

        UUID id = cachingService.add(
                postCacheRequest.getTtlInMinutes(),
                CacheableGetResult.fromUri( URI.create( postCacheRequest.getUrl() ) ) );
        UriBuilder builder = uriInfo.getAbsolutePathBuilder();
        builder.path( id.toString() );
        return Response.created( builder.build() ).build();
    }
}
