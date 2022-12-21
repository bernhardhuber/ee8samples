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

import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import net.datafaker.Faker;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author berni3
 */
public class OrderDataFactoryTest {

    OrderDataFactory instance;

    @BeforeEach
    public void setUp() {
        instance = new OrderDataFactory();
    }

    /**
     * Test of createDataFakerOrderList method, of class OrderDataFactory.
     */
    @Test
    public void testCreateDataFakerOrderList_int() {
        List<OrderData> result = instance.createDataFakerOrderList(5);
        assertNotNull(result);
        assertEquals(5, result.size());
    }

    /**
     * Test of createNaiveFakeOrderList method, of class OrderDataFactory.
     */
    @Test
    public void testCreateNaiveFakeOrderList() {
        List<OrderData> result = instance.createNaiveFakeOrderList(5);
        assertNotNull(result);
        assertEquals(5, result.size());
    }

    /**
     * Test of createDataFakerOrderList method, of class OrderDataFactory.
     */
    @Test
    public void testCreateDataFakerOrderList_int_Function() {
        Function<Integer, OrderData> f = (i) -> OrderData.builder().build();
        List<OrderData> result = instance.createDataFakerOrderList(5, f);
        assertNotNull(result);
        assertEquals(5, result.size());
    }

    /**
     * Test of createNthDataFakerOrderData method, of class OrderDataFactory.
     */
    @Test
    public void testCreateNthDataFakerOrderData() {
        final Faker faker = Faker.instance(Locale.forLanguageTag("de-AT"));
        Function<Integer, OrderData> f = OrderDataFactory.createNthDataFakerOrderData(faker);
        OrderData od = f.apply(11);
        assertNotNull(od);
    }

    /**
     * Test of createNthNaiveOrderData method, of class OrderDataFactory.
     */
    @Test
    public void testCreateNthNaiveOrderData() {
        final Faker faker = Faker.instance(Locale.forLanguageTag("de-AT"));
        Function<Integer, OrderData> f = OrderDataFactory.createNthNaiveOrderData();
        OrderData od = f.apply(11);
        assertNotNull(od);
    }

}
