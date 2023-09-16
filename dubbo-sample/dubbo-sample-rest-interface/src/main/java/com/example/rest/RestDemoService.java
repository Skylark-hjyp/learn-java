package com.example.rest;


import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

@Path("rest")
public interface RestDemoService {
    @GET
    @Path("hello")
    @Consumes({MediaType.APPLICATION_JSON})
    String sayHello(String name);

    @GET
    @Path("hi")
    @Consumes({MediaType.APPLICATION_JSON})
    String sayHi();

    @GET
    @Path("interface")
    @Consumes({MediaType.APPLICATION_JSON})
    TestInterface sayInterface();
}
