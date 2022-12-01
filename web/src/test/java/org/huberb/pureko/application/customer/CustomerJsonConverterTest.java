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
package org.huberb.pureko.application.customer;

import org.huberb.pureko.application.customer.CustomerJsonConverter;
import org.huberb.pureko.application.customer.CustomerData;
import java.util.Arrays;
import java.util.List;
import javax.json.JsonObject;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author berni3
 */
public class CustomerJsonConverterTest {

    CustomerJsonConverter instance;

    @BeforeEach
    public void setUp() {
        instance = new CustomerJsonConverter();
    }

    /**
     * Test of createDefaultCustomerJson method, of class CustomerJsonConverter.
     */
    @Test
    public void given_a_customer_json_is_created() {
        final CustomerData customer = CustomerData.builder()
                .customerID("customerID_1")
                .companyName("companyName_1")
                .build();
        final String s = instance.createJsonObjectFrom(customer);
        assertEquals("{"
                + "\"companyName\":\"companyName_1\","
                + "\"customerID\":\"customerID_1\""
                + "}", s);
    }

    /**
     * Test of createDefaultCustomerJson method, of class CustomerJsonConverter.
     */
    @Test
    public void given_mulitple_customers_json_is_created() {
        final List<CustomerData> customerList = Arrays.asList(CustomerData.builder().customerID("customerID_1").companyName("companyName_1").build(),
                CustomerData.builder().customerID("customerID_2").companyName("companyName_2").build()
        );
        final String s = instance.createJsonArrayFrom(customerList);
        assertEquals("["
                + "{\"companyName\":\"companyName_1\",\"customerID\":\"customerID_1\"},"
                + "{\"companyName\":\"companyName_2\",\"customerID\":\"customerID_2\"}"
                + "]", s);
    }

    /**
     * Test of createInstanceFromJson method, of class CustomerJsonConverter.
     */
    @Test
    public void given_a_json_then_customer_is_created() {
        final CustomerData customer = CustomerData.builder()
                .customerID("customerID_1")
                .companyName("companyName_1")
                .build();
        final String s = instance.createJsonObjectFrom(customer);
        assertEquals("{\"companyName\":\"companyName_1\",\"customerID\":\"customerID_1\"}", s);
        CustomerData customerFromJson = instance.createCustomerFromJson(s);

        assertAll(
                () -> customer.equals(customerFromJson),
                () -> assertEquals("customerID_1", customerFromJson.getCustomerID()),
                () -> assertEquals("companyName_1", customerFromJson.getCompanyName())
        );
    }

    /**
     * Test of createJsonObjectFromJson method, of class OrderJsonConverter.
     */
    @Test
    public void testCreateJsonObjectFromJson() {
        final String s = "{\"companyName\":\"companyName_1\",\"customerID\":\"customerID_1\"}";
        final JsonObject jsonObject = instance.createJsonObjectFromJson(s);
        assertTrue(jsonObject.containsKey("companyName"), s);
        assertTrue(jsonObject.containsKey("customerID"), s);

        assertEquals("companyName_1", jsonObject.getString("companyName"));
        assertEquals("customerID_1", jsonObject.getString("customerID"));
    }
}
