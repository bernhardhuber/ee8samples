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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import javax.persistence.EntityManager;

/**
 *
 * @author berni3
 */
@ApplicationScoped
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
            _em.getMetamodel().getEmbeddables().forEach(et -> {
                JsonObjectBuilder job = Json.createObjectBuilder();
                job.add("java-type-name", _em.getMetamodel().getEntities().iterator().next().getJavaType().getName());
                job.add("name", _em.getMetamodel().getEntities().iterator().next().getName());
                job.add("persistent-type-name", _em.getMetamodel().getEntities().iterator().next().getPersistenceType().name());
                jab.add(job.build());
            });
            return jab.build();
        };
        Function<EntityManager, JsonValue> managedTypesF = (_em) -> {
            JsonArrayBuilder jab = Json.createArrayBuilder();
            _em.getMetamodel().getManagedTypes().forEach(et -> {
                jab.add(et.getJavaType().getName());
            });
            return jab.build();
        };

        JsonifyableValue jv = new JsonifyableValue();
        JsonObjectBuilder job = Json.createObjectBuilder();
        job.add("properties", Json.createObjectBuilder(jv.jvMap(em.getProperties())).build());
        job.add("embeddables", embeddablesF.apply(em));
        job.add("entities", entitiesF.apply(em));
        job.add("managedType", managedTypesF.apply(em));
        return job.build();
    }

    public JsonValue cdi() {
        JsonArrayBuilder jab = Json.createArrayBuilder();
        final Set<Bean<?>> beans = beanManager.getBeans(Object.class,
                new AnnotationLiteral<Any>() {
        });
        for (Bean<?> bean : beans) {
            final String beanInfo = String.format("bean: "
                    + "bean class %s, "
                    + "bean injections points %s, "
                    + "bean name %s, "
                    + "bean qualifiers %s, "
                    + "bean scope %s, "
                    + "bean sterotypes %s, "
                    + "bean types %s, "
                    + "bean alternative %s, "
                    + "bean nullable %s, ",
                    bean.getBeanClass().getName(),
                    bean.getInjectionPoints(),
                    bean.getName(),
                    bean.getQualifiers(),
                    bean.getScope(),
                    bean.getStereotypes(),
                    bean.getTypes(),
                    bean.isAlternative(),
                    bean.isNullable()
            );
            LOG.info(() -> beanInfo);

            jab.add(Json.createObjectBuilder(mapCdiBeanToMap().apply(bean)).build());
        }
        return jab.build();
    }

    static Function<Bean<?>, Map<String, Object>> mapCdiBeanToMap() {
        final JsonifyableValue jv = new JsonifyableValue();
        return (b) -> {
            Map<String, Object> m = Map.ofEntries(
                    Map.entry("beanClass", jv.jvObject(b.getBeanClass().getName())),
                    Map.entry("injectionPoints", jv.jvObject(b.getInjectionPoints())),
                    Map.entry("name", jv.jvObject(b.getName())),
                    Map.entry("qualifiers", jv.jvObject(b.getQualifiers())),
                    Map.entry("scope", jv.jvObject(b.getScope().getName())),
                    Map.entry("stereotypes", jv.jvObject(b.getStereotypes())),
                    Map.entry("types", jv.jvObject(b.getTypes())),
                    Map.entry("alternative", b.isAlternative()),
                    Map.entry("nullable", b.isNullable())
            );
            return m;
        };
    }

    static class JsonifyableValue {

        Object jvObject(Object o) {
            if (o == null) {
                return "";
            } else if (o instanceof String) {
                return jvString((String) o);
            } else if (o instanceof Number) {
                return jvNumber((Number) o);
            } else if (o instanceof Boolean) {
                return jvBoolean((Boolean) o);
            } else if (o instanceof Collection) {
                return jvCollection((Collection) o);
            } else if (o instanceof Map) {
                return jvMap((Map) o);
            } else {
                return String.valueOf(o);
            }
        }

        Number jvNumber(Number n) {
            return n;
        }

        Boolean jvBoolean(Boolean b) {
            return b;
        }

        String jvString(String s) {
            return s;
        }

        Map jvMap(Map m) {
            Map result = new HashMap<>();
            for (Iterator<Entry> it = m.entrySet().iterator(); it.hasNext();) {
                Entry e = it.next();
                Object k = e.getKey();
                if (k == null) {
                    continue;
                }
                Object v = e.getValue();
                Object kV = jvObject(k);
                Object vV = jvObject(v);
                result.put(kV, vV);

            }
            return result;
        }

        List jvCollection(Collection l) {
            List result = new ArrayList<>();
            for (Object e : l) {
                Object ejfy = jvObject(e);
                result.add(ejfy);
            }
            return result;
        }
    }
}
