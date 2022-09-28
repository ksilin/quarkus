package org.jboss.resteasy.reactive.server.vertx.test.resource.basic.resource;

import java.net.URI;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.UriInfo;

@Path("/")
public class UriInfoResolveResource {
    @Produces("text/plain")
    @GET
    @Path("resolve")
    public String relativize(@Context UriInfo info, @QueryParam("to") String to) {
        return info.resolve(URI.create(to)).toString();
    }
}
