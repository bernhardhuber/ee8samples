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

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author berni3
 */
public class CustomerTransformingTest {

    CustomerTransforming instance;

    @BeforeEach
    public void setUp() {
        instance = new CustomerTransforming();
    }

    /**
     * Test of transformCustomerToExistingCustomerEntity method, of class
     * CustomerTransforming.
     */
    @Test
    public void testTransformCustomerToExistingCustomerEntity() {
        CustomerData from = CustomerData.builder()
                .companyName("companyName")
                .contactName("contactName")
                .contactTitle("contactTitle")
                .customerID("customerID")
                .fax("fax")
                .phone("phone")
                .build();
        CustomerEntity to = CustomerEntity.builder()
                .customerID("customerID_1")
                .companyName("companyName_1")
                .build();

        instance.transformCustomerToExistingCustomerEntity(from, to);

        assertEquals("companyName", to.getCompanyName());
        assertEquals("contactName", to.getContactName());
        assertEquals("contactTitle", to.getContactTitle());
        assertEquals("customerID", to.getCustomerID());
        assertEquals("fax", to.getFax());
        assertEquals("phone", to.getPhone());

        assertEquals(from.getCompanyName(), to.getCompanyName());
        assertEquals(from.getContactName(), to.getContactName());
        assertEquals(from.getContactTitle(), to.getContactTitle());
        assertEquals(from.getCustomerID(), to.getCustomerID());
        assertEquals(from.getFax(), to.getFax());
        assertEquals(from.getPhone(), to.getPhone());
    }

    /**
     * Test of transformCustomerToNewCustomerEntity method, of class
     * CustomerTransforming.
     */
    @Test
    public void testTransformCustomerToNewCustomerEntity() {
        CustomerData from = CustomerData.builder()
                .companyName("companyName")
                .contactName("contactName")
                .contactTitle("contactTitle")
                .customerID("customerID")
                .fax("fax")
                .phone("phone")
                .build();

        CustomerEntity to = instance.transformCustomerToNewCustomerEntity(from);

        assertEquals("companyName", to.getCompanyName());
        assertEquals("contactName", to.getContactName());
        assertEquals("contactTitle", to.getContactTitle());
        assertEquals("customerID", to.getCustomerID());
        assertEquals("fax", to.getFax());
        assertEquals("phone", to.getPhone());

        assertEquals(from.getCompanyName(), to.getCompanyName());
        assertEquals(from.getContactName(), to.getContactName());
        assertEquals(from.getContactTitle(), to.getContactTitle());
        assertEquals(from.getCustomerID(), to.getCustomerID());
        assertEquals(from.getFax(), to.getFax());
        assertEquals(from.getPhone(), to.getPhone());
    }

    /**
     * Test of transformCustomerEntityToNewCustomer method, of class
     * CustomerTransforming.
     */
    @Test
    public void testTransformCustomerEntityToNewCustomer() {
        CustomerEntity from = CustomerEntity.builder()
                .companyName("companyName")
                .contactName("contactName")
                .contactTitle("contactTitle")
                .customerID("customerID")
                .fax("fax")
                .phone("phone")
                .build();

        CustomerData to = instance.transformCustomerEntityToNewCustomer(from);

        assertEquals("companyName", to.getCompanyName());
        assertEquals("contactName", to.getContactName());
        assertEquals("contactTitle", to.getContactTitle());
        assertEquals("customerID", to.getCustomerID());
        assertEquals("fax", to.getFax());
        assertEquals("phone", to.getPhone());

        assertEquals(from.getCompanyName(), to.getCompanyName());
        assertEquals(from.getContactName(), to.getContactName());
        assertEquals(from.getContactTitle(), to.getContactTitle());
        assertEquals(from.getCustomerID(), to.getCustomerID());
        assertEquals(from.getFax(), to.getFax());
        assertEquals(from.getPhone(), to.getPhone());
    }

}
