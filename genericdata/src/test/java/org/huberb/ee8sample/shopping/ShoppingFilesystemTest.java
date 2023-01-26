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
package org.huberb.ee8sample.shopping;

import java.util.Arrays;
import org.huberb.ee8sample.fs.Filesystem.Files.Directory;
import org.huberb.ee8sample.genericdata.Basics.Item;
import org.huberb.ee8sample.genericdata.Basics.Name;
import org.huberb.ee8sample.genericdata.Basics.Person;
import org.huberb.ee8sample.shopping.ShoppingFilesystem.SupportCommands;
import org.huberb.ee8sample.shopping.Shoppings.StockItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author berni3
 */
public class ShoppingFilesystemTest {

    ShoppingFilesystem instance;

    @BeforeEach
    public void setUp() {
        this.instance = new ShoppingFilesystem();
    }

    @Test
    public void helloStockItems() {
        ShoppingFilesystem.StockItems xxx = new ShoppingFilesystem.StockItems();
        xxx.addStockItems(instance.stockItemsDirectoriesRootDirectory,
                Arrays.asList(
                        StockItem.builder()
                                .item(Item.builder()
                                        .itemCode("ITEM-CODE-1")
                                        .itemName("ITEM-NAME-1")
                                        .build())
                                .build(),
                        StockItem.builder()
                                .item(Item.builder()
                                        .itemCode("ITEM-CODE-2")
                                        .itemName("ITEM-NAME-2")
                                        .build())
                                .build()
                ));

        SupportCommands.dumpDirectory(instance.rootDirectory);
    }

    @Test
    public void helloShoppingCard() {
        ShoppingFilesystem.ShoppingCards xxx = new ShoppingFilesystem.ShoppingCards();
        Directory shoppingCardDirectory_1 = xxx.createShoppingCardDirectory(instance.shoppingCardsRootDirectory, "Shopping_card_1");
        Directory shoppingCardDirectory_2 = xxx.createShoppingCardDirectory(instance.shoppingCardsRootDirectory, "Shopping_card_2");
        Directory shoppingCardDirectory = shoppingCardDirectory_1;
        xxx.addPerson(shoppingCardDirectory, Person.builder()
                .personName(Name.builder()
                        .firstName("firstName-1")
                        .lastName("lastName-1")
                        .build())
                .build());
        xxx.addPerson(shoppingCardDirectory, Person.builder()
                .personName(Name.builder()
                        .firstName("firstName-2")
                        .lastName("lastName-2")
                        .build())
                .build());

        SupportCommands.dumpDirectory(instance.rootDirectory);

    }

}
