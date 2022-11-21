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

import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author berni3
 */
public class CustomerModelTest {

    CustomerModel instance;

    @BeforeEach
    public void setUp() {
        instance = new CustomerModel();
    }

    /**
     * Test of createDefaultCustomer method, of class CustomerModel.
     */
    @Test
    public void then_createDefaultCustomer_returns_a_customer() {
        final Customer customer = instance.createDefaultCustomer();
        assertAll(() -> assertEquals("customerID1", customer.getCustomerID()),
                () -> assertEquals("companyName1", customer.getCompanyName())
        );
    }

    /**
     * Test of createDefaultCustomerJson method, of class CustomerModel.
     */
    @Test
    public void given_a_customer_json_is_created() {
        final Customer customer = instance.createDefaultCustomer();
        final String s = instance.createJsonObjectFrom(customer);
        assertEquals("{"
                + "\"companyName\":\"companyName1\","
                + "\"customerID\":\"customerID1\""
                + "}", s);
    }

    /**
     * Test of createDefaultCustomerJson method, of class CustomerModel.
     */
    @Test
    public void given_mulitple_customers_json_is_created() {
        final List<Customer> customerList = Arrays.asList(
                Customer.builder().customerID("customerID_1").companyName("companyName_1").build(),
                Customer.builder().customerID("customerID_2").companyName("companyName_2").build()
        );
        final String s = instance.createJsonArrayFrom(customerList);
        assertEquals("["
                + "{\"companyName\":\"companyName_1\",\"customerID\":\"customerID_1\"},"
                + "{\"companyName\":\"companyName_2\",\"customerID\":\"customerID_2\"}"
                + "]", s);
    }

    /**
     * Test of createCustomerFromJson method, of class CustomerModel.
     */
    @Test
    public void given_a_json_then_customer_is_created() {
        final Customer customer0 = instance.createDefaultCustomer();
        final String s = instance.createJsonObjectFrom(customer0);
        Customer customer = instance.createCustomerFromJson(s);

        assertAll(
                () -> assertEquals("customerID1", customer.getCustomerID()),
                () -> assertEquals("companyName1", customer.getCompanyName())
        );
    }

    /**
     * Test of createJsonObjectFromJson method, of class CustomerModel.
     */
    @Test
    public void testXxx4() {
        assertNotNull(instance);
    }

}
