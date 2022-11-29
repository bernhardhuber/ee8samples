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

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationInfoService;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.flywaydb.core.api.output.MigrateResult;
import org.flywaydb.core.api.output.ValidateResult;
import org.h2.jdbcx.JdbcConnectionPool;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author berni3
 */
public class FlywayTest {

    private JdbcConnectionPool h2JdbcConnectionPool;
    Flyway flyway;

    @BeforeEach
    public void setUp() {
        String jdbcUrl = "jdbc:h2:mem:testFlyway";
        h2JdbcConnectionPool = JdbcConnectionPool.create(jdbcUrl, "sa", "sa1");
        final FluentConfiguration flywayConfiguration = Flyway.configure()
                .dataSource(h2JdbcConnectionPool)
                .baselineOnMigrate(true)
                .baselineVersion("0")
                .locations("classpath:db/migration");
        flyway = new Flyway(flywayConfiguration);
    }

    @AfterEach
    public void tearDown() {
        assertEquals(0, h2JdbcConnectionPool.getActiveConnections());
        h2JdbcConnectionPool.dispose();
    }

    @Test
    public void testFlywayMigrate() {
        //--- 
        // flyway
        MigrateResult migrateResult = flyway.migrate();
        System.out.format("!!! MigrateResult: %s%n", migrateResult);
        assertAll(
                () -> assertTrue(migrateResult.success, "success"),
                () -> assertTrue(migrateResult.migrationsExecuted > 0, "migrations executed")
        );

        MigrationInfoService mis = flyway.info();
        System.out.format("!!! MigrationInfoService: %s%n", mis);
        assertNotNull(mis);
        assertEquals(false, mis.getInfoResult().allSchemasEmpty);

        ValidateResult validateResult = flyway.validateWithResult();
        System.out.format("!!! ValidateResult: %s%n", validateResult);
        String m = String.format("ValidateResult: %s", validateResult);
        assertNotNull(validateResult, m);
        assertEquals(true, validateResult.validationSuccessful, m);
    }

}
