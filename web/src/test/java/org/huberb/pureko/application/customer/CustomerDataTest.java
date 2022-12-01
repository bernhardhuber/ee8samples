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

import org.huberb.pureko.application.customer.CustomerData;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 *
 * @author berni3
 */
public class CustomerDataTest {

    /**
     * Test of equals method, of class CustomerData.
     */
    @Test
    public void testEquals() {
        CustomerData customer1_1 = CustomerData.builder().companyName("companyName_1").customerID("customerID_1").build();
        CustomerData customer1_2 = CustomerData.builder().companyName("companyName_1").customerID("customerID_1").build();
        CustomerData customer2_1 = CustomerData.builder().companyName("companyName_2").customerID("customerID_2").build();

        assertTrue(customer1_1.equals(customer1_1));
        assertTrue(customer1_1.equals(customer1_2));
        assertTrue(customer1_2.equals(customer1_1));
        assertTrue(customer1_2.equals(customer1_2));

        assertFalse(customer1_1.equals(customer2_1));
        assertFalse(customer2_1.equals(customer1_1));
    }

    /**
     * Test of hashCode method, of class CustomerData.
     */
    @Test
    public void testHashCode() {
        CustomerData customer1_1 = CustomerData.builder().companyName("companyName_1").customerID("customerID_1").build();
        CustomerData customer1_2 = CustomerData.builder().companyName("companyName_1").customerID("customerID_1").build();
        CustomerData customer2_1 = CustomerData.builder().companyName("companyName_2").customerID("customerID_2").build();

        assertTrue(customer1_1.hashCode() == customer1_1.hashCode());
        assertTrue(customer1_1.hashCode() == customer1_2.hashCode());
        assertFalse(customer1_1.hashCode() == customer2_1.hashCode());
    }

    /**
     * Test of toString method, of class CustomerData.
     */
    @Test
    public void testToString() {
        CustomerData customer1_1 = CustomerData.builder().companyName("companyName_1").customerID("customerID_1").build();

        assertNotNull(customer1_1.toString());
    }

}
