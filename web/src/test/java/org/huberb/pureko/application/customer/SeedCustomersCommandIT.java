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
import javax.persistence.EntityManagerFactory;
import org.huberb.pureko.application.customer.CustomerCommands.SeedCustomersCommand;
import org.huberb.pureko.application.persistence.CustomizableEntityManagerFactory;
import org.huberb.pureko.application.support.persistence.AbstractPersistenceModel.QueryConsumers;
import org.huberb.pureko.application.support.persistence.PersistenceModel;
import org.huberb.pureko.application.support.Transformers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
public class SeedCustomersCommandIT {

    private static EntityManagerFactory entityManagerFactory;

    @Spy
    private PersistenceModel persistenceModel = new PersistenceModel(entityManagerFactory.createEntityManager());

    @Spy
    private CustomerDataFactory customerDataFactory;
    @Spy
    private Transformers transformers;
    @Spy
    private CustomerTransforming customerTransforming;

    @InjectMocks
    SeedCustomersCommand instance;

    @BeforeAll
    public static void setUpAll() {
        String h2JdbcUrl = "jdbc:h2:mem:testCustomerSeedCommandTest";
        entityManagerFactory = CustomizableEntityManagerFactory.createH2EntityManagerFactory(h2JdbcUrl);
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
        assertTrue(this.persistenceModel.getEntityManager().isOpen());

        this.persistenceModel.getEntityManager().getTransaction().begin();
        assertTrue(this.persistenceModel.getEntityManager().getTransaction().isActive());

        int maxSeeded = 10;
        assertEquals(maxSeeded, instance.seedDataBase(maxSeeded));
        //---
        String ql = "from CustomerEntity c";
        List<CustomerEntity> cel = persistenceModel.findResultList(ql, CustomerEntity.class, QueryConsumers.noop());
        String m = String.format("cel: %s", cel);
        assertFalse(cel.isEmpty(), m);
        this.persistenceModel.getEntityManager().getTransaction().rollback();
    }

}
