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
package org.huberb.pureko.application.support;

import java.util.Collections;
import java.util.Map;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author berni3
 */
@ApplicationScoped
public class PersistenceProducers {

    /**
     * Produce and dispose an {@link EntityManagerFactory}.
     */
    @ApplicationScoped
    public static class EntityManagerFactoryProducer {

        final String persistenceUnitName = "ee8samplesPU";
        final Map properties = Collections.emptyMap();

        /**
         * Creates an {@link EntityManagerFactory} instance.
         *
         * @return an {@link EntityManagerFactory} instance
         * @see EntityManager
         * @see Persistence#createEntityManagerFactory(java.lang.String,
         * java.util.Map)
         */
        @Produces
        @ApplicationScoped
        public EntityManagerFactory create() {
            return Persistence
                    .createEntityManagerFactory(persistenceUnitName, properties);
        }

        /**
         * Close the created {@link EntityManagerFactory}.
         * <p>
         * This is a callback it is invoked when scope of the created
         * {@link EntityManagerFactory} instance ends.
         *
         * @param factory
         * @see EntityManagerFactory#close()
         */
        public void destroy(@Disposes EntityManagerFactory factory) {
            factory.close();
        }
    }

    /**
     * Produce and dispose an {@link EntityManager}.
     * <p>
     * Note: A concrete {@link EntityManagerFactory} is used for creating an
     * {@link EntityManager} instance.
     */
    @ApplicationScoped
    public static class EntityManagerProducer {

        @Inject
        transient EntityManagerFactory emf;

        /**
         * Create an {@link EntityManger} instance.
         *
         * @return entity manager instance
         * @see EntityManagerFactory#createEntityManager()
         */
        @Produces
        @RequestScoped
        public EntityManager create() {
            return emf.createEntityManager();
        }

        /**
         * Close the created {@link EntityManager} instance.
         * <p>
         * This is a callback which is invoked when the scope of created
         * {@link EntityManager} ends. It closes the entity-manager
         *
         * @param em
         * @see EntityManager#close()
         */
        public void close(@Disposes EntityManager em) {
            if (em.isOpen()) {
                em.flush();
            }
            em.close();
        }
    }
}
