package com.refactorable.resources;

import com.refactorable.api.GetCacheResult;
import com.refactorable.api.Header;
import com.refactorable.api.PostCacheRequest;
import com.refactorable.core.CacheableGetResult;
import com.refactorable.service.CachingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Path( "/cache" )
@Api( value = "Cache API" )
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
    @ApiResponses( value = {
            @ApiResponse( code = 200, message = Status.OK ),
            @ApiResponse( code = 404, message = Status.NOT_FOUND ),
            @ApiResponse( code = 500, message = Status.INTERNAL_ERROR ),
            @ApiResponse( code = 503, message = Status.SERVICE_UNAVAILABLE ) } )
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
    @ApiResponses( value = {
            @ApiResponse( code = 201, message = Status.CREATED ),
            @ApiResponse( code = 400, message = Status.BAD_REQUEST ),
            @ApiResponse( code = 404, message = Status.NOT_FOUND ),
            @ApiResponse( code = 422, message = Status.UNPROCESSABLE_ENTITY ),
            @ApiResponse( code = 500, message = Status.INTERNAL_ERROR ),
            @ApiResponse( code = 502, message = Status.BAD_GATEWAY ),
            @ApiResponse( code = 503, message = Status.SERVICE_UNAVAILABLE ) } )
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
