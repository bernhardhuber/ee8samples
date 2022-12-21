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

import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.bind.JsonbBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import org.huberb.pureko.application.order.OrderCommands;
import org.huberb.pureko.application.order.OrderData;
import org.huberb.pureko.application.support.JsonConvertersF;

/**
 *
 * @author berni3
 */
@Path("orders")
@ApplicationScoped
public class OrdersResource {

    /*
application/json
application/x-javascript
text/javascript
text/x-javascript
text/x-json

For JSON text:
application/json
The MIME media type for JSON text is application/json. The default encoding is UTF-8. (Source: RFC 4627)

For JSONP (runnable JavaScript) with callback:

application/javascript
     */
    @Inject
    private OrderCommands.ReadAllOrdersCommand readAllOrdersCommand;
    @Inject
    private OrderCommands.ReadDefaultOrderCommand readDefaultOrderCommand;
    @Inject
    private OrderCommands.SeedOrdersCommand seedOrdersCommand;
    @Inject
    private JsonConvertersF jsonConvertersF;

    @GET
    @Produces("application/json")
    public Response createSampleOrders() {
        final List<OrderData> orderDataList = readDefaultOrderCommand.createDefaultOrderData(10);
        final String s = jsonConvertersF.convertToString(orderDataList, JsonConvertersF.fromInstanceToJsonString(JsonbBuilder.create()));

        return Response
                .ok(s)
                .build();
    }

    @GET()
    @Path("read")
    @Produces("application/json")
    public Response orders() {
        final List<OrderData> orderDataList = readAllOrdersCommand.readOrders();
        final String s = jsonConvertersF.convertToString(orderDataList, JsonConvertersF.fromInstanceToJsonString(JsonbBuilder.create()));

        return Response
                .ok(s)
                .build();
    }

    @POST()
    @Path("seed")
    @Produces("application/json")
    public Response seedDataBase() {
        int createdCount = seedOrdersCommand.seedDataBase(5);
        final List<OrderData> orderDataList = readAllOrdersCommand.readOrders();
        final String s = jsonConvertersF.convertToString(orderDataList, JsonConvertersF.fromInstanceToJsonString(JsonbBuilder.create()));

        return Response
                .ok(s)
                .build();
    }

}
