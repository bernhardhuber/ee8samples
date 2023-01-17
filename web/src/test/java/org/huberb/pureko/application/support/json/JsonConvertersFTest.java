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
package org.huberb.pureko.application.support.json;

import java.util.Arrays;
import java.util.List;
import javax.json.bind.JsonbBuilder;
import org.huberb.pureko.application.customer.CustomerData;
import org.huberb.pureko.application.order.OrderData;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author berni3
 */
public class JsonConvertersFTest {

    JsonConvertersF instance;

    @BeforeEach
    public void setUp() {
        instance = new JsonConvertersF();
    }

    /**
     * Test of convertToString method, of class JsonConvertersF.
     */
    @Test
    public void testConvertToString_OrderData() {
        CustomerData cd = CustomerData.builder()
                .customerID("customerID")
                .companyName("companyName")
                .contactName("contactName")
                .build();
        String s = instance.convertToString(cd, JsonConvertersF.fromInstanceToJsonString(JsonbBuilder.create()));
        assertAll(() -> assertNotNull(s),
                () -> assertTrue(s.length() > 0, s),
                () -> assertEquals(""
                        + "{"
                        + "\"companyName\":\"companyName\","
                        + "\"contactName\":\"contactName\","
                        + "\"customerID\":\"customerID\""
                        + "}", s)
        );
    }

    /**
     * Test of convertToString method, of class JsonConvertersF.
     */
    @Test
    public void testConvertToString_ListOfOrderData() {
        List<OrderData> odList = Arrays.asList(
                OrderData.builder()
                        .customerID("customerID_1")
                        .employeeID("employeeID_1")
                        .build(),
                OrderData.builder()
                        .customerID("customerID_2")
                        .employeeID("employeeID_2")
                        .build()
        );
        String s = instance.convertToString(odList, JsonConvertersF.fromInstanceToJsonString(JsonbBuilder.create()));
        assertAll(() -> assertNotNull(s),
                () -> assertTrue(s.length() > 0, s),
                () -> assertEquals("["
                        + "{"
                        + "\"customerID\":\"customerID_1\","
                        + "\"employeeID\":\"employeeID_1\"},"
                        + "{"
                        + "\"customerID\":\"customerID_2\","
                        + "\"employeeID\":\"employeeID_2\"}"
                        + "]", s));

    }

    /**
     * Test of convertToInstance method, of class JsonConvertersF.
     */
    @Test
    public void testConvertToInstance() {
    }

    /**
     * Test of fromListToJsonString method, of class JsonConvertersF.
     */
    @Test
    public void testFromListToJsonString() {
    }

    /**
     * Test of fromInstanceToJsonString method, of class JsonConvertersF.
     */
    @Test
    public void testFromInstanceToJsonString() {
    }

    /**
     * Test of fromStringToInstance method, of class JsonConvertersF.
     */
    @Test
    public void testFromStringToInstance() {
    }

    /**
     * Test of createJsonObjectFromJson method, of class JsonConvertersF.
     */
    @Test
    public void testCreateJsonObjectFromJson() {
    }

}
