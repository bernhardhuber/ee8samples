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
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.huberb.pureko.application.order.OrderCommands;
import org.huberb.pureko.application.order.OrderData;
import org.huberb.pureko.application.order.OrderJsonConverter;

/**
 *
 * @author berni3
 */
@Path("order")
@ApplicationScoped
public class OrderResource {

    @Inject
    private OrderCommands.ReadDefaultOrderCommand createDefaultOrderCommand;
    @Inject
    private OrderCommands.ReadSingleOrdersCommand readSingleOrdersCommand;
//    @Inject
//    private OrderCommands.CreateNewOrderCommand createNewOrderCommand;
//    @Inject
//    private OrderCommands.UpdateOrderCommand updateOrderCommand;

    @Inject
    private OrderJsonConverter orderJsonConverter;

    /**
     *
     * @return
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response order() {
        final OrderData order = createDefaultOrderCommand.createDefaultOrderData();
        final String s = orderJsonConverter.createJsonObjectFrom(order);
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
        final OrderData order = readSingleOrdersCommand.readOrderById(id);
        final String s = orderJsonConverter.createJsonObjectFrom(order);
        return Response
                .ok(s)
                .build();
    }

    @GET
    @Path("read/customerid/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response readByOrderId(@PathParam("id") String customerId) {
        final OrderData orderData = readSingleOrdersCommand.readOrderByOrderId(customerId);
        final String s = orderJsonConverter.createJsonObjectFrom(orderData);
        return Response
                .ok(s)
                .build();
    }

//    /**
//     *
//     * @param customer
//     * @return
//     */
//    @POST
//    @Path("create")
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response create(OrderData customer) {
//        final OrderData createdOrderData = createOrder(customer);
//        final String s = customerJsonConverter.createJsonObjectFrom(createdOrderData);
//        return Response
//                .ok(s)
//                .build();
//    }
//
//    private OrderData createOrder(OrderData customer) {
//        OrderData createdOrderData = createNewOrderCommand.createOrder(customer);
//        return createdOrderData;
//    }
//    /**
//     *
//     * @param customer
//     * @return
//     */
//    @POST
//    @Path("update")
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response update(OrderData customer) {
//        final OrderData createdOrderData = updateOrder(customer);
//        final String s2 = customerJsonConverter.createJsonObjectFrom(createdOrderData);
//        return Response
//                .ok(s2)
//                .build();
//    }
//
//    private OrderData updateOrder(OrderData customer) {
//        OrderData createdOrderData = updateOrderCommand.updateOrder(customer);
//        return createdOrderData;
//    }
}
