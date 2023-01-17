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

import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import static org.hibernate.cfg.AvailableSettings.DIALECT;
import static org.hibernate.cfg.AvailableSettings.FORMAT_SQL;
import static org.hibernate.cfg.AvailableSettings.GENERATE_STATISTICS;
import static org.hibernate.cfg.AvailableSettings.HBM2DDL_AUTO;
import static org.hibernate.cfg.AvailableSettings.JPA_JDBC_URL;
import static org.hibernate.cfg.AvailableSettings.QUERY_STARTUP_CHECKING;
import static org.hibernate.cfg.AvailableSettings.SHOW_SQL;
import static org.hibernate.cfg.AvailableSettings.STATEMENT_BATCH_SIZE;
import static org.hibernate.cfg.AvailableSettings.USE_QUERY_CACHE;
import static org.hibernate.cfg.AvailableSettings.USE_REFLECTION_OPTIMIZER;
import static org.hibernate.cfg.AvailableSettings.USE_SECOND_LEVEL_CACHE;
import static org.hibernate.cfg.AvailableSettings.USE_STRUCTURED_CACHE;
import org.hibernate.dialect.H2Dialect;
import org.huberb.ee8sample.genericdata.persistence.CustomizableEntityManagerFactory.DefaultPersistenceUnitInfo;
import org.huberb.ee8sample.genericdata.persistence.CustomizableEntityManagerFactory.ImmutableMap;
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
        final Map<String, Object> props = ImmutableMap.<String, Object>builder()
                //.put(JPA_JDBC_DRIVER, JDBC_DRIVER)
                .put(JPA_JDBC_URL, h2JdbcUrl)
                .put(DIALECT, H2Dialect.class)
                .put(HBM2DDL_AUTO, org.hibernate.tool.schema.Action.CREATE_ONLY)
                .put(SHOW_SQL, true)
                .put(FORMAT_SQL, true)
                .put(QUERY_STARTUP_CHECKING, false)
                .put(GENERATE_STATISTICS, false)
                .put(USE_REFLECTION_OPTIMIZER, false)
                .put(USE_SECOND_LEVEL_CACHE, false)
                .put(USE_QUERY_CACHE, false)
                .put(USE_STRUCTURED_CACHE, false)
                .put(STATEMENT_BATCH_SIZE, 20)
                .build();
        final DefaultPersistenceUnitInfo pui = new DefaultPersistenceUnitInfo();
        entityManagerFactory = CustomizableEntityManagerFactory.builder()
                .putProps(JPA_JDBC_URL, h2JdbcUrl)
                .putProps(DIALECT, H2Dialect.class)
                .putProps(HBM2DDL_AUTO, org.hibernate.tool.schema.Action.CREATE_ONLY)
                .putAllProps(props)
                .assignPersistenceUnit(pui)
                .assignPersistenceUnit(DefaultPersistenceUnitInfo.builder()
                        .excludeUnlistedClasses(true)
                        .build())
                .build();
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
    public void testPersistCustomerEntity() {
        assertNotNull(this.entityManager);
        assertTrue(this.entityManager.isOpen());
        assertEquals(5, this.entityManager.getMetamodel().getEntities().size());

        this.entityManager.getTransaction().begin();

//        final CustomerEntity c1 = CustomerEntity.builder()
//                .customerID("customerID1FromC11")
//                .companyName("companyName1FromC1")
//                .build();
//        this.entityManager.persist(c1);
//        this.entityManager.flush();
//        assertTrue(this.entityManager.contains(c1));
//        final CustomerEntity c2 = this.entityManager.createQuery("from CustomerEntity", CustomerEntity.class).getSingleResult();
//        assertNotNull(c2.getId());
//        assertEquals(0, c2.getVersion());
//        assertEquals("customerID1FromC11", c2.getCustomerID());
//        assertEquals("companyName1FromC1", c2.getCompanyName());
        this.entityManager.getTransaction().commit();
    }

    @Test
    public void testPersistOrderEntity() {
        assertNotNull(this.entityManager);
        assertTrue(this.entityManager.isOpen());
        assertEquals(5, this.entityManager.getMetamodel().getEntities().size());

        this.entityManager.getTransaction().begin();
//        final OrderEntity o1 = OrderEntity.builder()
//                .customerID("customerID1FromC1")
//                .employeeID("employeeID1FromC1")
//                .shipInfo(ShipInfoEmbeddable.builder()
//                        .shipName("shipName1FromC1")
//                        .build())
//                .build();
//        this.entityManager.persist(o1);
//        this.entityManager.flush();
//        assertTrue(this.entityManager.contains(o1));
//        final OrderEntity c2 = this.entityManager.createQuery("from OrderEntity", OrderEntity.class).getSingleResult();
//        assertNotNull(c2.getId());
//        assertEquals(0, c2.getVersion());
//        assertEquals("customerID1FromC1", c2.getCustomerID());
//        assertEquals("employeeID1FromC1", c2.getEmployeeID());
//        assertEquals("shipName1FromC1", c2.getShipInfo().getShipName());

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
            <property name="javax.persistence.schema-generation.database.action" value="create"/>
            <property name="javax.persistence.jdbc.driver" value="org.h2.Driver"/>
            <property name="javax.persistence.jdbc.url" value="jdbc:h2:mem:testJpa;DB_CLOSE_DELAY=-1"/>
            <property name="javax.persistence.jdbc.user" value="sa1"/>
            <property name="javax.persistence.jdbc.password" value="sa1"/>
            <!--
            create
            create-drop
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
