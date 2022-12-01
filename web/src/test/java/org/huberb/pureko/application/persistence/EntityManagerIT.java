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
package org.huberb.pureko.application.persistence;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.SharedCacheMode;
import javax.persistence.ValidationMode;
import javax.persistence.spi.ClassTransformer;
import javax.persistence.spi.PersistenceUnitInfo;
import javax.persistence.spi.PersistenceUnitTransactionType;
import javax.sql.DataSource;
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
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.huberb.pureko.application.customer.CustomerEntity;
import org.huberb.pureko.application.order.OrderEntity;
import org.huberb.pureko.application.persistence.EntityManagerIT.CustomizableEntityManagerFactory.DefaultPersistenceUnitInfo;
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
public class EntityManagerIT {

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
        assertEquals(2, this.entityManager.getMetamodel().getEntities().size());

        this.entityManager.getTransaction().begin();

        final CustomerEntity c1 = CustomerEntity.builder()
                .companyName("companyNameFromC1")
                .build();
        this.entityManager.persist(c1);
        this.entityManager.flush();
        assertTrue(this.entityManager.contains(c1));
        final CustomerEntity c2 = this.entityManager.createQuery("from CustomerEntity", CustomerEntity.class).getSingleResult();
        assertEquals("companyNameFromC1", c2.getCompanyName());
        this.entityManager.getTransaction().commit();
    }

    @Test
    public void testPersistOrderEntity() {
        assertNotNull(this.entityManager);
        assertTrue(this.entityManager.isOpen());
        assertEquals(2, this.entityManager.getMetamodel().getEntities().size());

        this.entityManager.getTransaction().begin();
        final OrderEntity o1 = OrderEntity.builder()
                .customerID("customerIDFromO1")
                .build();
        this.entityManager.persist(o1);
        this.entityManager.flush();
        assertTrue(this.entityManager.contains(o1));
        final OrderEntity c2 = this.entityManager.createQuery("from OrderEntity", OrderEntity.class).getSingleResult();
        assertEquals("customerIDFromO1", c2.getCustomerID());

        this.entityManager.getTransaction().rollback();
    }

    public static class CustomizableEntityManagerFactory {

        private PersistenceUnitInfo pui;
        private Map<String, Object> props;

        public static CustomizableEntityManagerFactory builder() {
            return new CustomizableEntityManagerFactory();
        }

        public CustomizableEntityManagerFactory() {
            pui = new DefaultPersistenceUnitInfo();
            props = new HashMap<>();
        }

        CustomizableEntityManagerFactory putAllProps(Map<String, Object> props) {
            this.props.putAll(props);
            return this;
        }

        CustomizableEntityManagerFactory putProps(String k, Object v) {
            this.props.put(k, v);
            return this;
        }

        CustomizableEntityManagerFactory assignPersistenceUnit(PersistenceUnitInfo pui) {
            this.pui = pui;
            return this;
        }

        EntityManagerFactory build() {
            EntityManagerFactory entityManagerFactory = new HibernatePersistenceProvider()
                    .createContainerEntityManagerFactory(pui, props);
            return entityManagerFactory;
        }

        static class DefaultPersistenceUnitInfo implements PersistenceUnitInfo {

            String persistenceUnitName = "ApplicationPersistenceUnit";
            String persistenceProviderClassName = "org.hibernate.jpa.HibernatePersistenceProvider";
            PersistenceUnitTransactionType persistenceUnitTransactionType = PersistenceUnitTransactionType.RESOURCE_LOCAL;
            DataSource jtaDataSource = null;
            DataSource nonJtaDataSource = null;
            List<String> mappingFileNames = Collections.emptyList();
            List<URL> jarFileUrls;
            URL persistenceUnitRootUrl = null;
            List<String> managedClassNames = Collections.emptyList();
            boolean excludeUnlistedClasses = false;
            SharedCacheMode sharedCaceMode = SharedCacheMode.UNSPECIFIED;
            ValidationMode validationMode = ValidationMode.AUTO;
            Properties properties = new Properties();
            String persistenceXMLSchemaVersion = null;
            ClassLoader classLoader = null;
            ClassLoader newTempClassLoader = null;

            static DefaultPersistenceUnitInfo builder() {
                return new DefaultPersistenceUnitInfo();
            }

            public DefaultPersistenceUnitInfo() {
                try {
                    this.jarFileUrls = Collections.list(this.getClass()
                            .getClassLoader()
                            .getResources(""));
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            }

            //---
            DefaultPersistenceUnitInfo excludeUnlistedClasses(boolean v) {
                this.excludeUnlistedClasses = v;
                return this;
            }

            DefaultPersistenceUnitInfo build() {
                return this;
            }

            //---
            @Override
            public String getPersistenceUnitName() {
                return persistenceUnitName;
            }

            @Override
            public String getPersistenceProviderClassName() {
                return persistenceProviderClassName;
            }

            @Override
            public PersistenceUnitTransactionType getTransactionType() {
                return persistenceUnitTransactionType;
            }

            @Override
            public DataSource getJtaDataSource() {
                return jtaDataSource;
            }

            @Override
            public DataSource getNonJtaDataSource() {
                return nonJtaDataSource;
            }

            @Override
            public List<String> getMappingFileNames() {
                return mappingFileNames;
            }

            @Override
            public List<URL> getJarFileUrls() {
                return jarFileUrls;
            }

            @Override
            public URL getPersistenceUnitRootUrl() {
                return persistenceUnitRootUrl;
            }

            @Override
            public List<String> getManagedClassNames() {
                return managedClassNames;
            }

            @Override
            public boolean excludeUnlistedClasses() {
                return excludeUnlistedClasses;
            }

            @Override
            public SharedCacheMode getSharedCacheMode() {
                return sharedCaceMode;
            }

            @Override
            public ValidationMode getValidationMode() {
                return validationMode;
            }

            @Override
            public Properties getProperties() {
                return properties;
            }

            @Override
            public String getPersistenceXMLSchemaVersion() {
                return persistenceXMLSchemaVersion;
            }

            @Override
            public ClassLoader getClassLoader() {
                return classLoader;
            }

            @Override
            public void addTransformer(ClassTransformer transformer) {

            }

            @Override
            public ClassLoader getNewTempClassLoader() {
                return newTempClassLoader;
            }
        }

    }

    public static class ImmutableMap<K, V> {

        private Map<K, V> m = new HashMap<>();

        static <K, V> ImmutableMap<K, V> builder() {
            return new ImmutableMap<>();
        }

        ImmutableMap<K, V> put(K k, V v) {
            m.put(k, v);
            return this;
        }

        Map<K, V> build() {
            return this.m;
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
