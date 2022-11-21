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
package org.huberb.pureko.model;

import java.io.StringReader;
import java.util.List;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.bind.JsonbBuilder;

/**
 *
 * @author berni3
 */
public class CustomerModel {

    public Customer createDefaultCustomer() {
        final Customer customer = Customer.builder()
                .customerID("customerID1")
                .companyName("companyName1")
                .build();
        return customer;
    }

    public String createJsonArrayFrom(List<Customer> customerList) {
        final String s = JsonbBuilder.create().toJson(customerList);
        return s;
    }

    public String createJsonObjectFrom(Customer customer) {
        final String s = JsonbBuilder.create().toJson(customer);
        return s;
    }

    public Customer createCustomerFromJson(String s) {
        final Customer customer = JsonbBuilder.create().fromJson(s, Customer.class);
        return customer;
    }

    public JsonObject createJsonObjectFromJson(String s) {
        StringReader sr = new StringReader(s);
        JsonObject jsonObject = Json.createParser(sr).getObject();
        return jsonObject;
    }

}
