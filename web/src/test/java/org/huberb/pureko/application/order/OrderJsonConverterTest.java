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

import org.huberb.pureko.application.order.OrderJsonConverter;
import org.huberb.pureko.application.order.Order;
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
public class OrderJsonConverterTest {

    OrderJsonConverter instance;

    @BeforeEach
    public void setUp() {
        instance = new OrderJsonConverter();
    }

    /**
     * Test of createJsonArrayFrom method, of class OrderJsonConverter.
     */
    @Test
    public void testCreateJsonArrayFrom() {
        final List<Order> orderList = Arrays.asList(
                Order.builder()
                        .customerID("customerID_1")
                        .employeeID("employeeID_1")
                        .build(),
                Order.builder()
                        .customerID("customerID_2")
                        .employeeID("employeeID_2")
                        .build()
        );
        final String s = instance.createJsonArrayFrom(orderList);
        assertEquals("["
                + "{\"customerID\":\"customerID_1\",\"employeeID\":\"employeeID_1\"},"
                + "{\"customerID\":\"customerID_2\",\"employeeID\":\"employeeID_2\"}"
                + "]", s);
    }

    /**
     * Test of createJsonObjectFrom method, of class OrderJsonConverter.
     */
    @Test
    public void testCreateJsonObjectFrom() {
        final Order order = Order.builder()
                .customerID("customerID_1")
                .employeeID("employeeID_1")
                .build();
        final String s = instance.createJsonObjectFrom(order);
        assertEquals("{\"customerID\":\"customerID_1\",\"employeeID\":\"employeeID_1\"}", s);
    }

    /**
     * Test of createOrderFromJson method, of class OrderJsonConverter.
     */
    @Test
    public void testCreateOrderFromJson() {
        final Order order = Order.builder()
                .customerID("customerID_1")
                .employeeID("employeeID_1")
                .build();
        final String s = instance.createJsonObjectFrom(order);
        assertEquals("{\"customerID\":\"customerID_1\",\"employeeID\":\"employeeID_1\"}", s);
        final Order orderFromJson = instance.createOrderFromJson(s);

        assertAll(
                () -> order.equals(orderFromJson),
                () -> "customerID_1".equals(orderFromJson.getCustomerID()),
                () -> "employeeID_1".equals(orderFromJson.getEmployeeID())
        );
    }

    /**
     * Test of createJsonObjectFromJson method, of class OrderJsonConverter.
     */
    @Test
    public void testCreateJsonObjectFromJson() {
        String s = "{\"customerID\":\"customerID_1\",\"employeeID\":\"employeeID_1\"}";
        JsonObject jsonObject = instance.createJsonObjectFromJson(s);
        assertTrue(jsonObject.containsKey("customerID"), s);
        assertTrue(jsonObject.containsKey("employeeID"), s);

        assertEquals("customerID_1", jsonObject.getString("customerID"));
        assertEquals("employeeID_1", jsonObject.getString("employeeID"));
    }

}
