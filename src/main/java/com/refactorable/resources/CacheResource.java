package com.refactorable.resources;

import com.refactorable.api.GetCacheResult;
import com.refactorable.api.Header;
import com.refactorable.api.PostCacheRequest;
import com.refactorable.core.model.GenericGetResult;
import com.refactorable.core.service.CachingService;
import com.refactorable.core.service.GenericGetResultService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ResponseHeader;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.refactorable.resources.CacheResource.PATH_BASE;

@Path( PATH_BASE )
@Api( value = "Cache API" )
public class CacheResource {

    private static final Logger LOGGER = LoggerFactory.getLogger( CacheResource.class );

    public static final String PATH_BASE = "/cache";

    private final CachingService<GenericGetResult> cachingService;
    private final GenericGetResultService genericGetResultService;

    /**
     *
     * @param cachingService cannot be null
     * @param genericGetResultService cannot be null
     */
    public CacheResource(
            CachingService<GenericGetResult> cachingService,
            GenericGetResultService genericGetResultService ) {
        LOGGER.info( "creating {}!", this.getClass().getSimpleName() );
        this.cachingService = Validate.notNull( cachingService );
        this.genericGetResultService = Validate.notNull( genericGetResultService );
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

        Optional<GenericGetResult> optionalCacheableGetResult = cachingService.get( id, GenericGetResult.class );

        if( !optionalCacheableGetResult.isPresent() ) throw new NotFoundException();

        GenericGetResult genericGetResult = optionalCacheableGetResult.get();
        GetCacheResult getCacheResult = new GetCacheResult(
                genericGetResult.getUri().toString(),
                genericGetResult.getHeaders()
                        .entrySet()
                        .stream()
                        .map( e -> new Header( e.getKey(), e.getValue() ) )
                        .collect( Collectors.toList() ),
                genericGetResult.getBody() );
        return getCacheResult;
    }

    @POST
    @Produces( MediaType.APPLICATION_JSON )
    @Consumes( MediaType.APPLICATION_JSON )
    @ApiResponses( value = {
            @ApiResponse( code = 201, message = Status.CREATED, responseHeaders = {
                    @ResponseHeader(name = "Location", description = "https://tools.ietf.org/html/rfc7231#section-7.1.2", response = String.class ) } ),
            @ApiResponse( code = 400, message = Status.BAD_REQUEST ),
            @ApiResponse( code = 422, message = Status.UNPROCESSABLE_ENTITY ),
            @ApiResponse( code = 500, message = Status.INTERNAL_ERROR ),
            @ApiResponse( code = 502, message = Status.BAD_GATEWAY ),
            @ApiResponse( code = 503, message = Status.SERVICE_UNAVAILABLE ) } )
    public Response post(
            @Valid @NotNull PostCacheRequest postCacheRequest,
            @Context UriInfo uriInfo ) {

        UUID id = cachingService.add(
                postCacheRequest.getTtlInMinutes(),
                genericGetResultService.get( URI.create( postCacheRequest.getUrl() ) ) );
        UriBuilder builder = uriInfo.getAbsolutePathBuilder();
        builder.path( id.toString() );
        return Response.created( builder.build() ).build();
    }
}
