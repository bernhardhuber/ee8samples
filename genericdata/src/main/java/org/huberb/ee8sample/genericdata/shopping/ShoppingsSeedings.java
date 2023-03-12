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
package org.huberb.ee8sample.genericdata.shopping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.datafaker.Faker;
import org.huberb.ee8sample.genericdata.Basics;
import org.huberb.ee8sample.genericdata.Basics.Address;
import org.huberb.ee8sample.genericdata.Basics.LoginUser;
import org.huberb.ee8sample.genericdata.Basics.Name;
import org.huberb.ee8sample.genericdata.Basics.Organisation;
import org.huberb.ee8sample.genericdata.Basics.Person;
import org.huberb.ee8sample.genericdata.shopping.Shoppings.Delivery.Status;
import org.huberb.ee8sample.genericdata.shopping.Shoppings.StockItem;

/**
 *
 * @author berni3
 */
public class ShoppingsSeedings {

    public enum SeedingEntries {
        stockItemList,
        shoppingCardList,
        orderList,
        invoiceList,
        deliveryList;
    }

    public EnumMap<SeedingEntries, Object> seedItems(int stockItemCount, int shoppingCardCount) {
        final Faker faker = Faker.instance(Locale.forLanguageTag("de-AT"));

        final List<StockItem> stockItemList = Stream.iterate(0, i -> i < stockItemCount, i -> i + 1)
                .map(i -> {
                    Shoppings.StockItem item = Shoppings.StockItem.builder()
                            .availableCount(faker.number().numberBetween(0L, 100L))
                            .item(Basics.Item.builder()
                                    .itemCode(faker.numerify("ITEM-#####"))
                                    .itemName(faker.book().title())
                                    .build())
                            .build();
                    return item;
                }).collect(Collectors.toList());

        final List<Shoppings.ShoppingCard> shoppingCardList = new ArrayList<>();
        {
            for (int i = 0; i < shoppingCardCount; i++) {
                final int stockItemsCount = faker.number().numberBetween(0, 10);
                final List<Shoppings.ShoppingItem> shoppingItemList = new ArrayList<>();
                for (int j = 0; j < stockItemsCount; j += 1) {
                    final int stockItemIndex = faker.number().numberBetween(0, stockItemList.size());
                    final Shoppings.ShoppingItem shoppingItem = Shoppings.ShoppingItem.builder()
                            .item(stockItemList.get(stockItemIndex).getItem())
                            .quantity(faker.number().numberBetween(1L, 3L))
                            .build();
                    shoppingItemList.add(shoppingItem);
                }

                final Shoppings.ShoppingCard shoppingCard = Shoppings.ShoppingCard.builder()
                        .shoppingItemList(shoppingItemList)
                        .loginUser(LoginUser.builder()
                                .person(Person.builder()
                                        .personName(Name.builder()
                                                .firstName(faker.name().firstName())
                                                .lastName(faker.name().lastName())
                                                .middleName("")
                                                .title("")
                                                .build()).build())
                                .userName(faker.name().username())
                                .build())
                        .build();
                shoppingCardList.add(shoppingCard);
            }
        }
        final List<Shoppings.Delivery> deliveryList = Arrays.asList(
                Shoppings.Delivery.builder()
                        .deliverIdentif("deliverIdentif")
                        .orderIdentif("orderIdentif")
                        .status(Status.preparing)
                        .build());
        final List<Shoppings.Invoice> invoiceList = Arrays.asList(
                Shoppings.Invoice.builder()
                        .shoppingItemList(shoppingCardList.get(0).getShoppingItemList())
                        .orgAddress(Address.builder().build())
                        .organisation(Organisation.builder()
                                .organisationName("Organisation-A")
                                .build())
                        .person(Person.builder()
                                .personName(Name.builder()
                                        .firstName(faker.name().firstName())
                                        .lastName(faker.name().lastName())
                                        .middleName("")
                                        .title("")
                                        .build()).build())
                        .personAddress(Address.builder().build())
                        .build());
        final List<Shoppings.Order> orderList = Arrays.asList(
                Shoppings.Order.builder()
                        .shoppingItemList(shoppingCardList.get(0).getShoppingItemList())
                        .orderIdentif("orderIdentif")
                        .build());

        EnumMap<SeedingEntries, Object> result = new EnumMap<>(SeedingEntries.class) {
            {
                put(SeedingEntries.stockItemList, stockItemList);
                put(SeedingEntries.shoppingCardList, shoppingCardList);
                put(SeedingEntries.orderList, orderList);
                put(SeedingEntries.invoiceList, invoiceList);
                put(SeedingEntries.deliveryList, deliveryList);
            }
        };
        return result;

    }

}
