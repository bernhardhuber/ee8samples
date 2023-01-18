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
package org.huberb.pureko.application.persistence;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
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

/**
 *
 * @author berni3
 */
public class CustomizableEntityManagerFactory {

    private PersistenceUnitInfo pui;
    private Map<String, Object> props;

    public static CustomizableEntityManagerFactory builder() {
        return new CustomizableEntityManagerFactory();
    }

    public CustomizableEntityManagerFactory() {
        pui = new DefaultPersistenceUnitInfo();
        props = new HashMap<>();
    }

    public CustomizableEntityManagerFactory putAllProps(Map<String, Object> props) {
        this.props.putAll(props);
        return this;
    }

    public CustomizableEntityManagerFactory putProps(String k, Object v) {
        this.props.put(k, v);
        return this;
    }

    public CustomizableEntityManagerFactory assignPersistenceUnit(PersistenceUnitInfo pui) {
        this.pui = pui;
        return this;
    }

    public EntityManagerFactory build() {
        EntityManagerFactory entityManagerFactory = new HibernatePersistenceProvider()
                .createContainerEntityManagerFactory(pui, props);
        return entityManagerFactory;
    }

    public static EntityManagerFactory createH2EntityManagerFactory(String jdbcUrl) {
        final Map<String, Object> props = ImmutableMap.<String, Object>builder()
                //.put(JPA_JDBC_DRIVER, JDBC_DRIVER)
                .put(JPA_JDBC_URL, jdbcUrl)
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
        EntityManagerFactory _entityManagerFactory = CustomizableEntityManagerFactory.builder()
                .putProps(JPA_JDBC_URL, jdbcUrl)
                .putProps(DIALECT, H2Dialect.class)
                .putProps(HBM2DDL_AUTO, org.hibernate.tool.schema.Action.CREATE_ONLY)
                .putAllProps(props)
                .assignPersistenceUnit(pui)
                .assignPersistenceUnit(DefaultPersistenceUnitInfo.builder()
                        .excludeUnlistedClasses(true)
                        .build())
                .build();
        return _entityManagerFactory;
    }

    public static class DefaultPersistenceUnitInfo implements PersistenceUnitInfo {

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

        public static DefaultPersistenceUnitInfo builder() {
            return new DefaultPersistenceUnitInfo();
        }

        public DefaultPersistenceUnitInfo() {
            try {
                this.jarFileUrls = Collections.list(this.getClass().getClassLoader().getResources(""));
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }

        //---
        public DefaultPersistenceUnitInfo excludeUnlistedClasses(boolean v) {
            this.excludeUnlistedClasses = v;
            return this;
        }

        public DefaultPersistenceUnitInfo build() {
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

    public static class ImmutableMap<K, V> {

        private Map<K, V> m = new HashMap<>();

        public static <K, V> ImmutableMap<K, V> builder() {
            return new ImmutableMap<>();
        }

        public ImmutableMap<K, V> put(K k, V v) {
            m.put(k, v);
            return this;
        }

        public Map<K, V> build() {
            return this.m;
        }
    }

}
