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

import org.huberb.pureko.application.order.OrderData;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 *
 * @author berni3
 */
public class OrderDataTest {

    /**
     * Test of equals method, of class OrderData.
     */
    @Test
    public void testEquals() {
        OrderData order1_1 = OrderData.builder()
                .customerID("customerID_1")
                .employeeID("employeeID_1")
                .build();
        OrderData order1_2 = OrderData.builder()
                .customerID("customerID_1")
                .employeeID("employeeID_1")
                .build();
        OrderData order2_1 = OrderData.builder()
                .customerID("customerID_2")
                .employeeID("employeeID_2")
                .build();

        assertTrue(order1_1.equals(order1_1));
        assertTrue(order1_1.equals(order1_2));
        assertTrue(order2_1.equals(order2_1));
        assertFalse(order1_1.equals(order2_1));
    }

    /**
     * Test of hashCode method, of class OrderData.
     */
    @Test
    public void testHashCode() {
        OrderData order1_1 = OrderData.builder()
                .customerID("customerID_1")
                .employeeID("employeeID_1")
                .build();
        OrderData order1_2 = OrderData.builder()
                .customerID("customerID_1")
                .employeeID("employeeID_1")
                .build();
        OrderData order2_1 = OrderData.builder()
                .customerID("customerID_2")
                .employeeID("employeeID_2")
                .build();

        assertTrue(order1_1.hashCode() == order1_1.hashCode());
        assertTrue(order1_1.hashCode() == order1_2.hashCode());
        assertTrue(order1_1.hashCode() != order2_1.hashCode()
        );
    }

    /**
     * Test of toString method, of class OrderData.
     */
    @Test
    public void testToString() {
        OrderData order1_1 = OrderData.builder()
                .customerID("customerID_1")
                .employeeID("employeeID_1")
                .build();
        assertNotNull(order1_1.toString());
    }

}
