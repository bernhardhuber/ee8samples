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
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import org.huberb.pureko.application.customer.CustomerData;
import org.huberb.pureko.application.customer.CustomerDataFactory;
import org.huberb.pureko.application.customer.CustomerTransforming.TransformCustomerToJson;
import org.huberb.pureko.application.support.Transformers;

/**
 *
 * @author berni3
 */
@Path("customer")
@ApplicationScoped
public class CustomerResource {

    @Inject
    private CustomerDataFactory customerDataFactory;

    @Inject
    private Transformers transformers;

    @Inject
    private TransformCustomerToJson transformCustomerToJson;

    @GET
    @Produces("application/json")
    public Response customer() {
        final CustomerData customer = createDefaultCustomer();
        final String s2 = transformers.transformTo(customer, transformCustomerToJson);
        return Response
                .ok(s2)
                .build();
    }

    private CustomerData createDefaultCustomer() {
        final CustomerData customer = customerDataFactory
                .createDataFakerCustomerList(1)
                .get(0);
        return customer;
    }

}
