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

import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import org.huberb.pureko.model.Customer;
import org.huberb.pureko.model.Customer.FullAddress;
import org.huberb.pureko.model.CustomerModel;
import org.huberb.pureko.model.CustomerRepository;

/**
 *
 * @author berni3
 */
@Path("customers")
@ApplicationScoped
public class CustomersResource {

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
    private CustomerRepository customerRepository;

    @GET
    @Produces("application/json")
    public Response customers() {
        final List<Customer> customerList = customerRepository.loadCustomers(10);

        final String s = new CustomerModel().createJsonArrayFrom(customerList);

        return Response
                .ok(s)
                .build();
    }

    List<Customer> createFakeCustomerList(int n) {
        List<Customer> customerList = new ArrayList<>();
        for (int i = 0; i < n; i += 1) {
            final FullAddress fullAddress = FullAddress.builder()
                    .address("address-" + i)
                    .city("city-" + i)
                    .country("country-" + i)
                    .postalcode("postalcode-")
                    .region("region-" + i)
                    .build();
            final Customer customer = Customer.builder()
                    .customerID("customerID-" + i)
                    .companyName("companyName-" + i)
                    .contactName("contactName-" + i)
                    .contactTitle("contactTitle-" + i)
                    .fax("fax-" + i)
                    .phone("phone-" + i)
                    .fullAddress(fullAddress)
                    .build();
            customerList.add(customer);
        }
        return customerList;
    }
}
