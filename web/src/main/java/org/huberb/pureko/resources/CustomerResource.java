/*
 * Copyright 2022 berni3.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.huberb.pureko.resources;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.huberb.pureko.application.customer.CustomerCommands;
import org.huberb.pureko.application.customer.CustomerData;
import org.huberb.pureko.application.customer.CustomerJsonConverter;

/**
 *
 * @author berni3
 */
@Path("customer")
@ApplicationScoped
public class CustomerResource {

    @Inject
    private CustomerCommands.ReadDefaultCustomerCommand createDefaultCustomerCommand;
    @Inject
    private CustomerCommands.ReadSingleCustomersCommand readSingleCustomersCommand;
    @Inject
    private CustomerCommands.CreateNewCustomerCommand createNewCustomerCommand;
    @Inject
    private CustomerCommands.UpdateCustomerCommand updateCustomerCommand;

    @Inject
    private CustomerJsonConverter customerJsonConverter;

    /**
     *
     * @return
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response customer() {
        final CustomerData customer = createDefaultCustomerCommand.createDefaultCustomerData();
        final String s = customerJsonConverter.createJsonObjectFrom(customer);
        return Response
                .ok(s)
                .build();
    }

    @GET
    @Path("read/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response read(@PathParam("id") Long id) {
        return readById(id);
    }

    @GET
    @Path("read/id/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response readById(@PathParam("id") Long id) {
        final CustomerData customer = readSingleCustomersCommand.readCustomerById(id);
        final String s = customerJsonConverter.createJsonObjectFrom(customer);
        return Response
                .ok(s)
                .build();
    }

    @GET
    @Path("read/customerid/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response readByCustomerId(@PathParam("id") String customerId) {
        final CustomerData customer = readSingleCustomersCommand.readCustomerByCustomerId(customerId);
        final String s = customerJsonConverter.createJsonObjectFrom(customer);
        return Response
                .ok(s)
                .build();
    }

    /**
     *
     * @param customer
     * @return
     */
    @POST
    @Path("create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(CustomerData customer) {
        final CustomerData createdCustomerData = createCustomer(customer);
        final String s = customerJsonConverter.createJsonObjectFrom(createdCustomerData);
        return Response
                .ok(s)
                .build();
    }

    private CustomerData createCustomer(CustomerData customer) {
        CustomerData createdCustomerData = createNewCustomerCommand.createCustomer(customer);
        return createdCustomerData;
    }

    /**
     *
     * @param customer
     * @return
     */
    @POST
    @Path("update")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(CustomerData customer) {
        final CustomerData createdCustomerData = updateCustomer(customer);
        final String s2 = customerJsonConverter.createJsonObjectFrom(createdCustomerData);
        return Response
                .ok(s2)
                .build();
    }

    private CustomerData updateCustomer(CustomerData customer) {
        CustomerData createdCustomerData = updateCustomerCommand.updateCustomer(customer);
        return createdCustomerData;
    }

}
