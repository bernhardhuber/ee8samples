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
package org.huberb.ee8sample.genericdata.persistence;

import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.huberb.ee8sample.shopping.Shoppings.Delivery;
import static org.huberb.ee8sample.shopping.Shoppings.Delivery.COUNT_OF_DELIVERIES_QUERY;
import org.huberb.ee8sample.shopping.Shoppings.Invoice;
import org.huberb.ee8sample.shopping.Shoppings.ShoppingCard;
import static org.huberb.ee8sample.shopping.Shoppings.ShoppingCard.COUNT_OF_SHOPPING_CARDS_QUERY;
import org.huberb.ee8sample.shopping.Shoppings.StockItem;
import static org.huberb.ee8sample.shopping.Shoppings.StockItem.COUNT_OF_STOCK_ITEMS_QUERY;
import org.huberb.ee8sample.shopping.ShoppingsSeedings;
import org.huberb.ee8sample.shopping.ShoppingsSeedings.SeedingEntries;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author berni3
 */
public class EntityManagerTest {

    private static EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    @BeforeAll
    public static void setUpAll() {
        String h2JdbcUrl = "jdbc:h2:mem:testEntityManagerTest";
        entityManagerFactory = CustomizableEntityManagerFactory.createH2EntityManagerFactory(h2JdbcUrl);
    }

    @AfterAll
    public static void tearDownAll() {
        entityManagerFactory.close();
    }

    @BeforeEach
    public void setUp() {
        entityManager = entityManagerFactory.createEntityManager();
    }

    @AfterEach
    public void tearDown() {
        if (entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().rollback();
        }
        entityManager.clear();
        entityManager.close();
    }

    @Test
    public void testJpaBasics() {
        assertNotNull(this.entityManager);
        assertTrue(this.entityManager.isOpen());
        assertEquals(5, this.entityManager.getMetamodel().getEntities().size());

        this.entityManager.getTransaction().begin();
        //---
        this.entityManager.getTransaction().rollback();
    }

    @Test
    public void testShoppingSeedings_StockItems() {
        assertNotNull(this.entityManager);
        assertTrue(this.entityManager.isOpen());
        assertEquals(5, this.entityManager.getMetamodel().getEntities().size());

        this.entityManager.getTransaction().begin();
        //---
        final ShoppingsSeedings shoppingsSeedings = new ShoppingsSeedings();
        final Map<SeedingEntries, Object> map = shoppingsSeedings.seedItems(20, 5);
        {
            final List<StockItem> stockItemList = (List<StockItem>) map.get(SeedingEntries.stockItemList);
            assertEquals(20, stockItemList.size());
            stockItemList.forEach(si -> {
                this.entityManager.persist(si);
            });
            final Number count = this.entityManager
                    .createNamedQuery(COUNT_OF_STOCK_ITEMS_QUERY, Number.class)
                    .getSingleResult();
            assertEquals(stockItemList.size(), count.intValue());
        }
    }

    @Test
    public void testShoppingSeedings_ShoppingCards() {
        assertNotNull(this.entityManager);
        assertTrue(this.entityManager.isOpen());
        assertEquals(5, this.entityManager.getMetamodel().getEntities().size());

        this.entityManager.getTransaction().begin();
        //---
        final ShoppingsSeedings shoppingsSeedings = new ShoppingsSeedings();
        final Map<SeedingEntries, Object> map = shoppingsSeedings.seedItems(20, 5);
        {
            final List<ShoppingCard> shoppingCardList = (List<ShoppingCard>) map.get(SeedingEntries.shoppingCardList);
            assertEquals(5, shoppingCardList.size());
            shoppingCardList.forEach(sc -> {
                this.entityManager.persist(sc);
            });
            final Number count = this.entityManager
                    .createNamedQuery(COUNT_OF_SHOPPING_CARDS_QUERY, Number.class)
                    .getSingleResult();
            assertEquals(shoppingCardList.size(), count.intValue());
        }
    }

    @Test
    public void testShoppingSeedings_Deliveries() {
        assertNotNull(this.entityManager);
        assertTrue(this.entityManager.isOpen());
        assertEquals(5, this.entityManager.getMetamodel().getEntities().size());

        this.entityManager.getTransaction().begin();
        //---
        final ShoppingsSeedings shoppingsSeedings = new ShoppingsSeedings();
        final Map<SeedingEntries, Object> map = shoppingsSeedings.seedItems(20, 5);
        {
            final List<Delivery> deliveryList = (List<Delivery>) map.get(SeedingEntries.deliveryList);
            assertEquals(1, deliveryList.size());
            deliveryList.forEach(sc -> {
                this.entityManager.persist(sc);
            });
            final Number count = this.entityManager
                    .createNamedQuery(COUNT_OF_DELIVERIES_QUERY, Number.class)
                    .getSingleResult();
            assertEquals(deliveryList.size(), count.intValue());
        }
    }

    @Test
    public void testShoppingSeedings_Invoices() {
        assertNotNull(this.entityManager);
        assertTrue(this.entityManager.isOpen());
        assertEquals(5, this.entityManager.getMetamodel().getEntities().size());

        this.entityManager.getTransaction().begin();
        //---
        final ShoppingsSeedings shoppingsSeedings = new ShoppingsSeedings();
        final Map<SeedingEntries, Object> map = shoppingsSeedings.seedItems(20, 5);
        {
            final List<Invoice> invoiceList = (List<Invoice>) map.get(SeedingEntries.invoiceList);
            assertEquals(1, invoiceList.size());
            invoiceList.forEach(sc -> {
                this.entityManager.persist(sc);
            });
            final Number count = this.entityManager
                    .createNamedQuery(Invoice.COUNT_OF_INVOICES_QUERY, Number.class)
                    .getSingleResult();
            assertEquals(invoiceList.size(), count.intValue());
        }
    }

}
/*
    <persistence-unit name="ee8samplesPU_testing" transaction-type="RESOURCE_LOCAL">
        <provider>org.hibernate.jpa.HibernatePersistenceProvider</provider>
        <class>org.huberb.pureko.application.order.OrderEntity</class>
        <class>org.huberb.pureko.application.customer.CustomerEntity</class>
        <exclude-unlisted-classes>true</exclude-unlisted-classes>
        
        <!--jta-data-source>java:jboss/datasources/ExampleDS</jta-data-source-->
        <!--jta-data-source>java:jboss/datasources/Ee8SamplesDS</jta-data-source-->
        <!-- using java:module/env/.. and mapping via web.xml resource-ref
          makes problems, as pu is created before the web.xml resource-ref mapping
          when checking ddl or processing import.sql
        -->
        <!--jta-data-source>java:/datasources/Ee8SamplesDS</jta-data-source-->
        <properties>
            <property name="javax.persistence.schema-generation.database.action" value="createH2EntityManagerFactory"/>
            <property name="javax.persistence.jdbc.driver" value="org.h2.Driver"/>
            <property name="javax.persistence.jdbc.url" value="jdbc:h2:mem:testJpa;DB_CLOSE_DELAY=-1"/>
            <property name="javax.persistence.jdbc.user" value="sa1"/>
            <property name="javax.persistence.jdbc.password" value="sa1"/>
            <!--
            createH2EntityManagerFactory
            createH2EntityManagerFactory-drop
            update
            validate
            none
            -->
            <property name="hibernate.hbm2ddl.auto" value="validate"/>
            <property name="hibernate.show_sql" value="true"/>
            <property name="hibernate.format_sql" value="true"/>
            <property name="hibernate.transaction.flush_before_completion" value="true"/>
            <property name="hibernate.dialect" value="org.hibernate.dialect.H2Dialect"/>
            <!--property name="hibernate.dialect" value="org.hibernate.dialect.MySQL5Dialect"/-->
        </properties>
       
    </persistence-unit>
 */
