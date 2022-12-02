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
package org.huberb.pureko.application.customer;

import java.util.List;
import java.util.Map;
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
import org.huberb.pureko.application.customer.CustomerCommands.CustomerSeedCommand;
import org.huberb.pureko.application.persistence.EntityManagerIT.CustomizableEntityManagerFactory;
import org.huberb.pureko.application.persistence.EntityManagerIT.CustomizableEntityManagerFactory.DefaultPersistenceUnitInfo;
import org.huberb.pureko.application.persistence.EntityManagerIT.ImmutableMap;
import org.huberb.pureko.application.support.PersistenceModel;
import org.huberb.pureko.application.support.PersistenceModel.TypedQueryConsumers;
import org.huberb.pureko.application.support.Transformers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 *
 * @author berni3
 */
@ExtendWith(MockitoExtension.class)
public class CustomerSeedCommandTest {

    private static EntityManagerFactory entityManagerFactory;

    @Spy
    private PersistenceModel persistenceModel = new PersistenceModel(entityManagerFactory.createEntityManager());
    @Spy
    private CustomerDataFactory customerDataFactory;
    @Spy
    private Transformers transformers;

    @InjectMocks
    CustomerSeedCommand instance;

    @BeforeAll
    public static void setUpAll() {
        String h2JdbcUrl = "jdbc:h2:mem:testCustomerSeedCommandTest";
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
    }

    @AfterEach
    public void tearDown() {
        this.persistenceModel.getEntityManager().getTransaction().rollback();
        this.persistenceModel.getEntityManager().clear();
        this.persistenceModel.getEntityManager().close();
    }

    @Test
    public void testSeedDataBase() {
        assertNotNull(instance);
        assertNotNull(this.persistenceModel);
        assertNotNull(this.persistenceModel.getEntityManager());

        this.persistenceModel.getEntityManager().getTransaction().begin();
        int maxSeeded = 10;
        assertEquals(maxSeeded, instance.seedDataBase(maxSeeded));
        //---
        String ql = "from CustomerEntity c";
        List<CustomerEntity> cel = persistenceModel.findResultList(ql, CustomerEntity.class, TypedQueryConsumers.noop());
        String m = String.format("cel: %s", cel);
        assertFalse(cel.isEmpty(), m);
        this.persistenceModel.getEntityManager().getTransaction().rollback();
    }

}
