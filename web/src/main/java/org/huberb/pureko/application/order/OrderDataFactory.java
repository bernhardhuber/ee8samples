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

import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import javax.enterprise.context.ApplicationScoped;
import net.datafaker.Faker;
import org.huberb.pureko.application.order.OrderData.ShipInfo;

/**
 *
 * @author berni3
 */
@ApplicationScoped
public class OrderDataFactory {

    /**
     * Create list of order using {link Faker} data values.
     *
     * @param nMax
     * @return
     */
    public List<OrderData> createDataFakerOrderList(int nMax) {
        final Faker faker = Faker.instance(Locale.forLanguageTag("de-AT"));
        return createDataFakerOrderList(nMax, createNthDataFakerOrderData(faker));
    }

    public List<OrderData> createNaiveFakeOrderList(int nMax) {
        return createDataFakerOrderList(nMax, createNthNaiveOrderData());
    }

    /**
     * Create list of order using {link Faker} data values.
     *
     * @param nMax
     * @param f
     * @return
     */
    public List<OrderData> createDataFakerOrderList(int nMax, Function<Integer, OrderData> f) {
        final List<OrderData> orderDataList = new ArrayList<>();
        for (int i = 0; i < nMax; i += 1) {
            final OrderData orderData = f.apply(i);
            orderDataList.add(orderData);
        }
        return orderDataList;
    }

    public static Function<Integer, OrderData> createNthDataFakerOrderData(Faker faker) {
        final Function<Integer, String> f = (days) -> {
            java.sql.Timestamp ts = faker.date().past(days, TimeUnit.DAYS);
            String s = java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME
                    .withZone(ZoneId.systemDefault())
                    .format(Instant.ofEpochMilli(ts.getTime()));
            return s;
        };
        return (Integer i) -> {

//            final String pattern = "yyyy-MM-dd";
//            final String shippedDate = faker.time().past(2 * 360, ChronoUnit.HALF_DAYS, pattern);
//            final String orderDate = faker.time().past(2 * 360, ChronoUnit.HALF_DAYS, pattern);
//            final String requiredDate = faker.time().past(2 * 360, ChronoUnit.HALF_DAYS, pattern);
            final String shippedDate = f.apply(360);
            final String orderDate = f.apply(360);
            final String requiredDate = f.apply(360);
            final ShipInfo shipInfo = ShipInfo.builder()
                    .freight(faker.numerify("###"))
                    .shipName(faker.funnyName().name())
                    .shipAddress(faker.address().streetAddress())
                    .shipCity(faker.address().city())
                    .shipCountry(faker.address().country())
                    .shipPostalcode(faker.address().postcode())
                    .shipRegion(faker.address().state())
                    .shipVia(faker.numerify("##"))
                    .shippedDate(shippedDate)
                    .build();
            final OrderData orderData = OrderData.builder()
                    .customerID("customerID-" + i)
                    .employeeID("employeeID-" + i)
                    .orderDate(orderDate)
                    .requiredDate(requiredDate)
                    .shipInfo(shipInfo)
                    .build();
            return orderData;
        };
    }

    public static Function<Integer, OrderData> createNthNaiveOrderData() {
        return (Integer i) -> {
            final ShipInfo shipInfo = ShipInfo.builder()
                    .freight("10" + i)
                    .shipName("shipName-" + i)
                    .shipAddress("shipAddress-" + i)
                    .shipCity("shipCity-" + i)
                    .shipCountry("shipCountry-" + i)
                    .shipPostalcode("123" + i)
                    .shipRegion("shipRegion-" + i)
                    .shipVia("shipVia-" + i)
                    .shippedDate("2022-02-01")
                    .build();
            final OrderData orderData = OrderData.builder()
                    .customerID("customerID-" + i)
                    .employeeID("employeeID-" + i)
                    .orderDate("2022-01-01")
                    .requiredDate("2022-03-01")
                    .shipInfo(shipInfo)
                    .build();
            return orderData;
        };
    }
}
