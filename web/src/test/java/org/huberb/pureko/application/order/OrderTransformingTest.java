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
package org.huberb.pureko.application.order;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author berni3
 */
public class OrderTransformingTest {

    OrderTransforming instance;

    @BeforeEach
    public void setUp() {
        instance = new OrderTransforming();
    }

    /**
     * Test of transformOrderToExistingOrderEntity method, of class
     * OrderTransforming.
     */
    @Test
    public void testTransformOrderToExistingOrderEntity() {
        OrderData from = OrderData.builder()
                .customerID("customerID")
                .employeeID("employeeID")
                .orderDate("orderDate")
                .requiredDate("requiredDate")
                .build();
        OrderEntity to = OrderEntity.builder()
                .customerID("customerID_1")
                .employeeID("employeeID_1")
                .build();

        instance.transformOrderToExistingOrderEntity(from, to);

        assertEquals("customerID", to.getCustomerID());
        assertEquals("employeeID", to.getEmployeeID());
        assertEquals("orderDate", to.getOrderDate());
        assertEquals("requiredDate", to.getRequiredDate());

        assertEquals(from.getCustomerID(), to.getCustomerID());
        assertEquals(from.getEmployeeID(), to.getEmployeeID());
        assertEquals(from.getOrderDate(), to.getOrderDate());
        assertEquals(from.getRequiredDate(), to.getRequiredDate());

    }

    /**
     * Test of transformOrderToNewOrderEntity method, of class
     * OrderTransforming.
     */
    @Test
    public void testTransformOrderToNewOrderEntity() {
        OrderData from = OrderData.builder()
                .customerID("customerID")
                .employeeID("employeeID")
                .orderDate("orderDate")
                .requiredDate("requiredDate")
                .build();

        OrderEntity to = instance.transformOrderToNewOrderEntity(from);

        assertEquals("customerID", to.getCustomerID());
        assertEquals("employeeID", to.getEmployeeID());
        assertEquals("orderDate", to.getOrderDate());
        assertEquals("requiredDate", to.getRequiredDate());

        assertEquals(from.getCustomerID(), to.getCustomerID());
        assertEquals(from.getEmployeeID(), to.getEmployeeID());
        assertEquals(from.getOrderDate(), to.getOrderDate());
        assertEquals(from.getRequiredDate(), to.getRequiredDate());
    }

    /**
     * Test of transformOrderEntityToNewOrder method, of class
     * OrderTransforming.
     */
    @Test
    public void testTransformCustomerEntityToNewCustomer() {
        OrderEntity from = OrderEntity.builder()
                .customerID("customerID")
                .employeeID("employeeID")
                .orderDate("orderDate")
                .requiredDate("requiredDate")
                .build();

        OrderData to = instance.transformOrderEntityToNewOrder(from);

        assertEquals("customerID", to.getCustomerID());
        assertEquals("employeeID", to.getEmployeeID());
        assertEquals("orderDate", to.getOrderDate());
        assertEquals("requiredDate", to.getRequiredDate());

        assertEquals(from.getCustomerID(), to.getCustomerID());
        assertEquals(from.getEmployeeID(), to.getEmployeeID());
        assertEquals(from.getOrderDate(), to.getOrderDate());
        assertEquals(from.getRequiredDate(), to.getRequiredDate());
    }

}
