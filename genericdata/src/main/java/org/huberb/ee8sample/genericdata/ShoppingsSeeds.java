/*
 * Copyright 2023 berni3.
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
package org.huberb.ee8sample.genericdata;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.datafaker.Faker;
import org.huberb.ee8sample.genericdata.Basics.Address;
import org.huberb.ee8sample.genericdata.Basics.LoginUser;
import org.huberb.ee8sample.genericdata.Basics.Name;
import org.huberb.ee8sample.genericdata.Basics.Organisation;
import org.huberb.ee8sample.genericdata.Basics.Person;
import org.huberb.ee8sample.genericdata.Shoppings.Delivery.Status;
import org.huberb.ee8sample.genericdata.Shoppings.Item;

/**
 *
 * @author berni3
 */
public class ShoppingsSeeds {

    Map<String, Object> seedItems(int itemCount) {
        final Faker faker = Faker.instance(Locale.forLanguageTag("de-AT"));

        List<Item> itemList = Stream.iterate(0, i -> i < itemCount, i -> i + 1)
                .map(i -> {
                    Shoppings.Item item = Shoppings.Item.builder()
                            .availableCount(faker.number().numberBetween(0L, 100L))
                            .itemIdentif(faker.numerify("ITEM-#####"))
                            .itemName(faker.book().title())
                            .build();
                    return item;
                }).collect(Collectors.toList());

        List<Shoppings.ShoppingItem> shoppingItemList = Arrays.asList(
                Shoppings.ShoppingItem.builder()
                        .itemIdentif("itemIdentif")
                        .loginUser(LoginUser.builder()
                                .person(Person.builder()
                                        .name(Name.builder()
                                                .firstName(faker.name().firstName())
                                                .lastName(faker.name().lastName())
                                                .middleName("")
                                                .title("")
                                                .build())
                                        .build())
                                .userName(faker.name().username())
                                .build())
                        .build()
        );

        List<Shoppings.Delivery> deliveryList = Arrays.asList(
                Shoppings.Delivery.builder()
                        .deliverIdentif("deliverIdentif")
                        .orderIdentif("orderIdentif")
                        .status(Status.preparing)
                        .build());
        List<Shoppings.Invoice> invoiceList = Arrays.asList(
                Shoppings.Invoice.builder()
                        .itemIdentif("itemIdentif")
                        .orgAddress(Address.builder().build())
                        .organisation(Organisation.builder().build())
                        .person(Person.builder().build())
                        .personAddress(Address.builder().build())
                        .quantity(faker.number().numberBetween(1L, 20L))
                        .build());
        List<Shoppings.Order> orderList = Arrays.asList(
                Shoppings.Order.builder()
                        .customerIdentif("customerIdentif")
                        .orderIdentif("orderIdentif")
                        .build());

        Map<String, Object> m = new HashMap<>() {
            {
                put("itemList", itemList);
                put("shoppingItemList", shoppingItemList);
                put("orderList", orderList);
                put("invoiceList", invoiceList);
                put("deliveryList", deliveryList);
            }
        };
        return m;
    }
}
