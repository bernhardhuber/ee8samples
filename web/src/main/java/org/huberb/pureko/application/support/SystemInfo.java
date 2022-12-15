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

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import javax.persistence.EntityManager;
import org.huberb.pureko.application.support.JsonValues.JsonifyableToJsonValue;
import org.huberb.pureko.application.support.JsonValues.JsonifyableToObject;

/**
 *
 * @author berni3
 */
@RequestScoped
public class SystemInfo {

    private static final Logger LOG = Logger.getLogger(SystemInfo.class.getName());
    @Inject
    private BeanManager beanManager;

    @Inject
    private EntityManager em;

    public JsonValue pu() {

        Function<EntityManager, JsonValue> embeddablesF = (_em) -> {
            JsonArrayBuilder jab = Json.createArrayBuilder();
            _em.getMetamodel().getEmbeddables().forEach(et -> {
                jab.add(et.getJavaType().getName());
            });
            return jab.build();
        };
        Function<EntityManager, JsonValue> entitiesF = (_em) -> {
            JsonArrayBuilder jab = Json.createArrayBuilder();
            _em.getMetamodel().getEntities().forEach(et -> {
                JsonObjectBuilder job = Json.createObjectBuilder();
                job.add("java-type-name", et.getJavaType().getName());
                job.add("name", et.getName());
                job.add("persistent-type-name", et.getPersistenceType().name());
                jab.add(job.build());
            });
            return jab.build();
        };
        Function<EntityManager, JsonValue> managedTypesF = (_em) -> {
            JsonArrayBuilder jab = Json.createArrayBuilder();
            _em.getMetamodel().getManagedTypes().forEach(mt -> {
                jab.add(mt.getJavaType().getName());
            });
            return jab.build();
        };

        JsonObjectBuilder job = Json.createObjectBuilder();
        job.add("properties", JsonifyableToJsonValue.jvJsonValue().apply(em.getProperties()));
        job.add("embeddables", embeddablesF.apply(em));
        job.add("entities", entitiesF.apply(em));
        job.add("managedType", managedTypesF.apply(em));
        final JsonObject jo = job.build();
        LOG.info(() -> jo.toString());
        return jo;

    }

    public JsonValue cdi() {
        final JsonArrayBuilder jab = Json.createArrayBuilder();
        final Set<Bean<?>> beans = beanManager.getBeans(Object.class,
                new AnnotationLiteral<Any>() {
        });
        for (Bean<?> bean : beans) {
            jab.add(Json.createObjectBuilder(mapCdiBeanToMap().apply(bean)).build());
        }
        final JsonArray ja = jab.build();
        LOG.info(() -> ja.toString());
        return ja;
    }

    static Function<Bean<?>, Map<String, Object>> mapCdiBeanToMap() {
        final JsonifyableToObject jv = new JsonifyableToObject();
        return (b) -> {
            Map<String, Object> m = Map.ofEntries(
                    Map.entry("beanClass", JsonifyableToObject.jvObject().apply(b.getBeanClass().getName())),
                    Map.entry("injectionPoints", JsonifyableToObject.jvObject().apply(b.getInjectionPoints())),
                    Map.entry("name", JsonifyableToObject.jvObject().apply(b.getName())),
                    Map.entry("qualifiers", JsonifyableToObject.jvObject().apply(b.getQualifiers())),
                    Map.entry("scope", JsonifyableToObject.jvObject().apply(b.getScope().getName())),
                    Map.entry("stereotypes", JsonifyableToObject.jvObject().apply(b.getStereotypes())),
                    Map.entry("types", JsonifyableToObject.jvObject().apply(b.getTypes())),
                    Map.entry("alternative", JsonifyableToObject.jvObject().apply(b.isAlternative())),
                    Map.entry("nullable", JsonifyableToObject.jvObject().apply(b.isNullable()))
            );
            return m;
        };
    }

}
