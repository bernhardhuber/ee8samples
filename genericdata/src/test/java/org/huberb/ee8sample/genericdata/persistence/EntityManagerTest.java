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
import org.huberb.ee8sample.genericdata.Shoppings.ShoppingCard;
import org.huberb.ee8sample.genericdata.Shoppings.StockItem;
import org.huberb.ee8sample.genericdata.ShoppingsSeedings;
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
        String h2JdbcUrl = "jdbc:h2:mem:testCreateEntityManagerUsingCustomizableEntityManagerFactory";
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
    public void testShoppingSeedings() {
        assertNotNull(this.entityManager);
        assertTrue(this.entityManager.isOpen());
        assertEquals(5, this.entityManager.getMetamodel().getEntities().size());

        this.entityManager.getTransaction().begin();
        //---
        final ShoppingsSeedings shoppingsSeedings = new ShoppingsSeedings();
        final Map<String, Object> map = shoppingsSeedings.seedItems(20, 5);
        {
            final List<StockItem> stockItemList = (List<StockItem>) map.get("stockItemList");
            assertEquals(20, stockItemList.size());
            for (int i = 0; i < stockItemList.size(); i += 1) {
                StockItem stockItem = stockItemList.get(i);
                this.entityManager.persist(stockItem);
            }
            final Number count = this.entityManager.createQuery("select count(e) from Shoppings$StockItem as e", Number.class)
                    .getSingleResult();
            assertEquals(stockItemList.size(), count.intValue());
        }
        {
            final List<ShoppingCard> shoppingCardList = (List<ShoppingCard>) map.get("shoppingCardList");
            assertEquals(5, shoppingCardList.size());
            for (int i = 0; i < shoppingCardList.size(); i += 1) {
                ShoppingCard shoppingCard = shoppingCardList.get(i);
                this.entityManager.persist(shoppingCard);
            }
            final Number count = this.entityManager.createQuery("select count(e) from Shoppings$ShoppingCard as e", Number.class)
                    .getSingleResult();
            assertEquals(shoppingCardList.size(), count.intValue());
        }
        //---
        this.entityManager.getTransaction().rollback();
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
