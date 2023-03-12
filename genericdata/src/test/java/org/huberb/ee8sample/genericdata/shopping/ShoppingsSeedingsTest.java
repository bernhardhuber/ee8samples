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

import org.huberb.ee8sample.genericdata.shopping.ShoppingsSeedings;
import java.util.List;
import java.util.Map;
import org.huberb.ee8sample.genericdata.shopping.ShoppingsSeedings.SeedingEntries;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author berni3
 */
public class ShoppingsSeedingsTest {

    ShoppingsSeedings instance;

    @BeforeEach
    public void setUp() {
        this.instance = new ShoppingsSeedings();
    }

    /**
     * Test of seedItems method, of class ShoppingsSeedings.
     */
    @Test
    public void testSeedItems() {
        final Map<SeedingEntries, Object> map = instance.seedItems(20, 5);
        //System.out.printf("seedItems: %s%n", map);

        assertNotNull(map);
        map.entrySet().forEach(e -> {
            String m = "" + e;
            Assertions.assertAll(
                    () -> assertNotNull(e.getKey(), m),
                    () -> assertNotNull(e.getValue(), m),
                    () -> assertTrue(e.getValue() instanceof List, m),
                    () -> assertTrue(((List) e.getValue()).size() > 0, m)
            );
        });
    }
/*
seedItems: {
invoiceList=[
Shoppings.Invoice(super=Shoppings.AbstractEntityIdVersion(id=null, version=null), organisation=Basics.Organisation(organisationName=null), orgAddress=Basics.Address(address=null, city=null, region=null, postalcode=null, country=null), 

shoppingItemList=[
  Shoppings.ShoppingItem(super=org.huberb.ee8sample.genericdata.Shoppings$ShoppingItem@60851c8b, item=Basics.Item(itemName=Tödliche Versprechen/Das Imperium der Wölfe, itemCode=ITEM-01645), quantity=null), 
  Shoppings.ShoppingItem(super=org.huberb.ee8sample.genericdata.Shoppings$ShoppingItem@147028f9, item=Basics.Item(itemName=Des Teufels General, itemCode=ITEM-14241), quantity=null), 
  Shoppings.ShoppingItem(super=org.huberb.ee8sample.genericdata.Shoppings$ShoppingItem@148b06c2, item=Basics.Item(itemName=Des Teufels General, itemCode=ITEM-31597), quantity=null), 
  Shoppings.ShoppingItem(super=org.huberb.ee8sample.genericdata.Shoppings$ShoppingItem@e1d53d24, item=Basics.Item(itemName=Lerne lachen ohne zu weinen, itemCode=ITEM-56219), quantity=null)], 
person=Basics.Person(personName=null), personAddress=Basics.Address(address=null, city=null, region=null, postalcode=null, country=null))], 

deliveryList=[Shoppings.Delivery(super=Shoppings.AbstractEntityIdVersion(id=null, version=null), deliverIdentif=deliverIdentif, orderIdentif=orderIdentif, status=preparing)], 

orderList=[Shoppings.Order(super=Shoppings.AbstractEntityIdVersion(id=null, version=null), orderIdentif=orderIdentif, 
  shoppingItemList=[
    Shoppings.ShoppingItem(super=org.huberb.ee8sample.genericdata.Shoppings$ShoppingItem@60851c8b, item=Basics.Item(itemName=Tödliche Versprechen/Das Imperium der Wölfe, itemCode=ITEM-01645), quantity=null), 
    Shoppings.ShoppingItem(super=org.huberb.ee8sample.genericdata.Shoppings$ShoppingItem@147028f9, item=Basics.Item(itemName=Des Teufels General, itemCode=ITEM-14241), quantity=null), 
    Shoppings.ShoppingItem(super=org.huberb.ee8sample.genericdata.Shoppings$ShoppingItem@148b06c2, item=Basics.Item(itemName=Des Teufels General, itemCode=ITEM-31597), quantity=null), 
    Shoppings.ShoppingItem(super=org.huberb.ee8sample.genericdata.Shoppings$ShoppingItem@e1d53d24, item=Basics.Item(itemName=Lerne lachen ohne zu weinen, itemCode=ITEM-56219), quantity=null)])], 

shoppingCardList=[
  Shoppings.ShoppingCard(super=Shoppings.AbstractEntityIdVersion(id=null, version=null), 
  shoppingItemList=[
    Shoppings.ShoppingItem(super=org.huberb.ee8sample.genericdata.Shoppings$ShoppingItem@60851c8b, item=Basics.Item(itemName=Tödliche Versprechen/Das Imperium der Wölfe, itemCode=ITEM-01645), quantity=null), 
    Shoppings.ShoppingItem(super=org.huberb.ee8sample.genericdata.Shoppings$ShoppingItem@147028f9, item=Basics.Item(itemName=Des Teufels General, itemCode=ITEM-14241), quantity=null), 
    Shoppings.ShoppingItem(super=org.huberb.ee8sample.genericdata.Shoppings$ShoppingItem@148b06c2, item=Basics.Item(itemName=Des Teufels General, itemCode=ITEM-31597), quantity=null), 
    Shoppings.ShoppingItem(super=org.huberb.ee8sample.genericdata.Shoppings$ShoppingItem@e1d53d24, item=Basics.Item(itemName=Lerne lachen ohne zu weinen, itemCode=ITEM-56219), quantity=null)], 
  loginUser=Basics.LoginUser(userName=jasper.herweg, person=Basics.Person(personName=Basics.Name(title=, firstName=Leila, middleName=, lastName=Kappe)))), 

  Shoppings.ShoppingCard(super=Shoppings.AbstractEntityIdVersion(id=null, version=null), 
  shoppingItemList=[
    Shoppings.ShoppingItem(super=org.huberb.ee8sample.genericdata.Shoppings$ShoppingItem@2847a064, item=Basics.Item(itemName=Über das Studium der griechischen Poesie, itemCode=ITEM-98200), quantity=null), 
    Shoppings.ShoppingItem(super=org.huberb.ee8sample.genericdata.Shoppings$ShoppingItem@b2386951, item=Basics.Item(itemName=Der Untertan, itemCode=ITEM-97996), quantity=null), 
    Shoppings.ShoppingItem(super=org.huberb.ee8sample.genericdata.Shoppings$ShoppingItem@4855e8b6, item=Basics.Item(itemName=Die Räuber, itemCode=ITEM-48951), quantity=null), 
    Shoppings.ShoppingItem(super=org.huberb.ee8sample.genericdata.Shoppings$ShoppingItem@de81c217, item=Basics.Item(itemName=Siddhartha, itemCode=ITEM-06130), quantity=null), 
    Shoppings.ShoppingItem(super=org.huberb.ee8sample.genericdata.Shoppings$ShoppingItem@a7a91955, item=Basics.Item(itemName=Frühlings Erwachen, itemCode=ITEM-57138), quantity=null), 
    Shoppings.ShoppingItem(super=org.huberb.ee8sample.genericdata.Shoppings$ShoppingItem@b2386951, item=Basics.Item(itemName=Der Untertan, itemCode=ITEM-97996), quantity=null), 
    Shoppings.ShoppingItem(super=org.huberb.ee8sample.genericdata.Shoppings$ShoppingItem@4855e8b6, item=Basics.Item(itemName=Die Räuber, itemCode=ITEM-48951), quantity=null)], 
  loginUser=Basics.LoginUser(userName=vito.nannen, person=Basics.Person(personName=Basics.Name(title=, firstName=Lewis, middleName=, lastName=Wenk)))), 

  Shoppings.ShoppingCard(super=Shoppings.AbstractEntityIdVersion(id=null, version=null), 
  shoppingItemList=[
    Shoppings.ShoppingItem(super=org.huberb.ee8sample.genericdata.Shoppings$ShoppingItem@a7a91955, item=Basics.Item(itemName=Frühlings Erwachen, itemCode=ITEM-57138), quantity=null), 
    Shoppings.ShoppingItem(super=org.huberb.ee8sample.genericdata.Shoppings$ShoppingItem@a7a91955, item=Basics.Item(itemName=Frühlings Erwachen, itemCode=ITEM-57138), quantity=null), 
    Shoppings.ShoppingItem(super=org.huberb.ee8sample.genericdata.Shoppings$ShoppingItem@3884f95d, item=Basics.Item(itemName=Winnetou, itemCode=ITEM-60977), quantity=null)], 
  loginUser=Basics.LoginUser(userName=joeline.huth, person=Basics.Person(personName=Basics.Name(title=, firstName=Björn, middleName=, lastName=Cordes)))), 
  
  Shoppings.ShoppingCard(super=Shoppings.AbstractEntityIdVersion(id=null, version=null), 
  shoppingItemList=[], 
  loginUser=Basics.LoginUser(userName=nika.lutz, person=Basics.Person(personName=Basics.Name(title=, firstName=Eveline, middleName=, lastName=Daubner)))), 

  Shoppings.ShoppingCard(super=Shoppings.AbstractEntityIdVersion(id=null, version=null), 
  shoppingItemList=[
    Shoppings.ShoppingItem(super=org.huberb.ee8sample.genericdata.Shoppings$ShoppingItem@232094a8, item=Basics.Item(itemName=Das Versprechen, itemCode=ITEM-30931), quantity=null), 
    Shoppings.ShoppingItem(super=org.huberb.ee8sample.genericdata.Shoppings$ShoppingItem@147028f9, item=Basics.Item(itemName=Des Teufels General, itemCode=ITEM-14241), quantity=null)], 
  loginUser=Basics.LoginUser(userName=annabelle.keiner, person=Basics.Person(personName=Basics.Name(title=, firstName=Hagen, middleName=, lastName=Hügel))))], 

stockItemList=[

  Shoppings.StockItem(super=Shoppings.AbstractEntityIdVersion(id=null, version=null), item=Basics.Item(itemName=Siddhartha, itemCode=ITEM-06130), availableCount=78), 
  Shoppings.StockItem(super=Shoppings.AbstractEntityIdVersion(id=null, version=null), item=Basics.Item(itemName=Des Teufels General, itemCode=ITEM-14241), availableCount=34), 
  Shoppings.StockItem(super=Shoppings.AbstractEntityIdVersion(id=null, version=null), item=Basics.Item(itemName=Die neuen Leiden des jungen Werthers, itemCode=ITEM-67301), availableCount=14), 
  Shoppings.StockItem(super=Shoppings.AbstractEntityIdVersion(id=null, version=null), item=Basics.Item(itemName=Winnetou, itemCode=ITEM-60977), availableCount=51), 
  Shoppings.StockItem(super=Shoppings.AbstractEntityIdVersion(id=null, version=null), item=Basics.Item(itemName=Des Teufels General, itemCode=ITEM-31597), availableCount=91), 
  Shoppings.StockItem(super=Shoppings.AbstractEntityIdVersion(id=null, version=null), item=Basics.Item(itemName=Die Räuber, itemCode=ITEM-48951), availableCount=85), 
  Shoppings.StockItem(super=Shoppings.AbstractEntityIdVersion(id=null, version=null), item=Basics.Item(itemName=Emilia Galotti, itemCode=ITEM-72433), availableCount=42), 
  Shoppings.StockItem(super=Shoppings.AbstractEntityIdVersion(id=null, version=null), item=Basics.Item(itemName=Die Leute von Seldwyla, itemCode=ITEM-42705), availableCount=36), 
  Shoppings.StockItem(super=Shoppings.AbstractEntityIdVersion(id=null, version=null), item=Basics.Item(itemName=Die zärtlichen Schwestern, itemCode=ITEM-72198), availableCount=22), 
  Shoppings.StockItem(super=Shoppings.AbstractEntityIdVersion(id=null, version=null), item=Basics.Item(itemName=Im Westen nichts Neues, itemCode=ITEM-06155), availableCount=89), 
  Shoppings.StockItem(super=Shoppings.AbstractEntityIdVersion(id=null, version=null), item=Basics.Item(itemName=Über das Studium der griechischen Poesie, itemCode=ITEM-98200), availableCount=38), 
  Shoppings.StockItem(super=Shoppings.AbstractEntityIdVersion(id=null, version=null), item=Basics.Item(itemName=Der Untertan, itemCode=ITEM-97996), availableCount=69), 
  Shoppings.StockItem(super=Shoppings.AbstractEntityIdVersion(id=null, version=null), item=Basics.Item(itemName=Tödliche Versprechen/Das Imperium der Wölfe, itemCode=ITEM-01645), availableCount=31), 
  Shoppings.StockItem(super=Shoppings.AbstractEntityIdVersion(id=null, version=null), item=Basics.Item(itemName=Die Verwandlung/Erstes Leid, itemCode=ITEM-17667), availableCount=94), 
  Shoppings.StockItem(super=Shoppings.AbstractEntityIdVersion(id=null, version=null), item=Basics.Item(itemName=Das Versprechen, itemCode=ITEM-30931), availableCount=80), 
  Shoppings.StockItem(super=Shoppings.AbstractEntityIdVersion(id=null, version=null), item=Basics.Item(itemName=Das Boot, itemCode=ITEM-29228), availableCount=3), 
  Shoppings.StockItem(super=Shoppings.AbstractEntityIdVersion(id=null, version=null), item=Basics.Item(itemName=Romeo und Julia auf dem Dorfe, itemCode=ITEM-50385), availableCount=64), 
  Shoppings.StockItem(super=Shoppings.AbstractEntityIdVersion(id=null, version=null), item=Basics.Item(itemName=Lerne lachen ohne zu weinen, itemCode=ITEM-56219), availableCount=71), 
  Shoppings.StockItem(super=Shoppings.AbstractEntityIdVersion(id=null, version=null), item=Basics.Item(itemName=Die verlorene Ehre der Katharina Blum, itemCode=ITEM-92064), availableCount=53), 
  Shoppings.StockItem(super=Shoppings.AbstractEntityIdVersion(id=null, version=null), item=Basics.Item(itemName=Frühlings Erwachen, itemCode=ITEM-57138), availableCount=54)]}


*/
}
