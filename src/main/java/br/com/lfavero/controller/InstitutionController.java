package br.com.lfavero.controller;

import br.com.lfavero.entity.InstitutionEntity;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/institution")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class InstitutionController {

    @GET
    public Response findInstitution (){
        return Response.ok(InstitutionEntity.findAll().list()).build();
    }
}
