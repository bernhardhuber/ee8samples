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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.ZoneId;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;

/**
 *
 * @author berni3
 */
public class JsonValues {

    /**
     * Convert to {@link JsonValue}.
     */
    static class JsonifyableToJsonValue {
        //---

        private static final JsonifyableToJsonValue INSTANCE = new JsonifyableToJsonValue();

        private static JsonifyableToJsonValue instance() {
            return INSTANCE;
        }

        public static Function<Object, JsonValue> jvJsonValue() {
            return (o) -> instance().jvObject(o);
        }

        public JsonValue jvObject(Object o) {
            final JsonValue result;
            if (o == null) {
                result = JsonValue.NULL;
            } else if (o instanceof Number) {
                result = jvNumber((Number) o);
            } else if (o instanceof Boolean) {
                result = jvBoolean((Boolean) o);
            } else if (o instanceof CharSequence) {
                result = jvCharSequence((CharSequence) o);
            } else if (o instanceof Enum<?>) {
                result = jvEnum((Enum) o);
            } else if (o instanceof Date) {
                result = jvDate((Date) o);
            } else if (o instanceof Calendar) {
                result = jvCalendar((Calendar) o);
            } else if (o instanceof TemporalAccessor) {
                result = jvTemporalAccessor((TemporalAccessor) o);
            } else if (o instanceof Map) {
                result = jvMap((Map) o);
            } else if (o instanceof Collection) {
                result = jvCollection((Collection) o);
            } else {
                // TODO handle Date/Time/Values
                result = JsonValue.NULL;
            }
            return result;
        }

        // basic values
        public JsonValue jvNumber(Number n) {
            final JsonValue result;
            if (n == null) {
                result = JsonValue.NULL;
            } else if (n instanceof Integer) {
                result = Json.createValue(n.intValue());
            } else if (n instanceof Long) {
                result = Json.createValue(n.longValue());
            } else if (n instanceof Double) {
                result = Json.createValue(n.doubleValue());
            } else if (n instanceof Float) {
                result = Json.createValue(n.doubleValue());
            } else if (n instanceof BigInteger) {
                result = Json.createValue((BigInteger) n);
            } else if (n instanceof BigDecimal) {
                result = Json.createValue((BigDecimal) n);
            } else {
                result = JsonValue.NULL;
            }
            return result;
        }

        public JsonValue jvBoolean(Boolean b) {
            return b ? JsonValue.TRUE : JsonValue.FALSE;
        }

        public JsonValue jvCharSequence(CharSequence s) {
            return Json.createValue(((CharSequence) s).toString());
        }

        public JsonValue jvEnum(Enum<?> e) {
            return Json.createValue(e.name());
        }

        // date/time
        public JsonValue jvDate(Date d) {
            final TemporalAccessor temporal = d.toInstant();
            final String result = java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME
                    .withZone(ZoneId.of("UTC"))
                    .format(temporal);
            return Json.createValue(result);
        }

        public JsonValue jvCalendar(Calendar cal) {
            final TemporalAccessor temporal = cal.toInstant();
            final String result = java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME
                    .withZone(ZoneId.of("UTC"))
                    .format(temporal);
            return Json.createValue(result);
        }

        public JsonValue jvTemporalAccessor(TemporalAccessor temporal) {
            final String result = java.time.format.DateTimeFormatter.RFC_1123_DATE_TIME
                    .withZone(ZoneId.of("UTC"))
                    .format(temporal);
            return Json.createValue(result);
        }

        // map, collection
        public JsonValue jvMap(Map<?, ?> m) {
            final JsonObjectBuilder job = Json.createObjectBuilder();
            m.forEach((k, v) -> {
                if (k != null) {
                    String name = k.toString();
                    JsonValue value = jvObject(v);
                    job.add(name, value);
                }
            });
            return job.build();
        }

        public JsonValue jvCollection(Collection<?> c) {
            JsonArrayBuilder jab = Json.createArrayBuilder();
            c.forEach(e -> {
                jab.add(jvObject(e));
            });
            return jab.build();
        }
    }

    /**
     * Convert to {@link Object} which shall be compatible to {@link JsonValue}.
     */
    static class JsonifyableToObject {
        //---

        private static final JsonifyableToObject INSTANCE = new JsonifyableToObject();

        private static JsonifyableToObject instance() {
            return INSTANCE;
        }

        public static Function<Object, Object> jvObject() {
            return (o) -> instance().jvObject(o);
        }

        public Object jvObject(Object o) {
            final Object result;
            if (o == null) {
                result = "";
            } else if (o instanceof CharSequence) {
                result = jvCharSequence((CharSequence) o);
            } else if (o instanceof Number) {
                result = jvNumber((Number) o);
            } else if (o instanceof Boolean) {
                result = jvBoolean((Boolean) o);
            } else if (o instanceof Enum<?>) {
                result = jvEnum((Enum) o);
            } else if (o instanceof Date) {
                result = jvDate((Date) o);
            } else if (o instanceof Calendar) {
                result = jvCalendar((Calendar) o);
            } else if (o instanceof TemporalAccessor) {
                result = jvTemporalAccessor((TemporalAccessor) o);
            } else if (o instanceof Collection) {
                result = jvCollection((Collection) o);
            } else if (o instanceof Map) {
                result = jvMap((Map) o);
            } else {
                // TODO handle Date/Time/Values
                return String.valueOf(o);
            }
            return result;
        }

        // basic values
        public Number jvNumber(Number n) {
            if (n instanceof Float) {
                return ((Float) n).doubleValue();
            }
            return n;
        }

        public Boolean jvBoolean(Boolean b) {
            return b;
        }

        public String jvCharSequence(CharSequence s) {
            return s.toString();
        }

        public String jvEnum(Enum<?> e) {
            return e.name();
        }

        // date/time
        public String jvDate(Date d) {
            final TemporalAccessor temporal = d.toInstant();
            final String result = java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME
                    .withZone(ZoneId.of("UTC"))
                    .format(temporal);
            return result;
        }

        public String jvCalendar(Calendar cal) {
            final TemporalAccessor temporal = cal.toInstant();
            final String result = java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME
                    .withZone(ZoneId.of("UTC"))
                    .format(temporal);
            return result;
        }

        public String jvTemporalAccessor(TemporalAccessor temporal) {
            final String result = java.time.format.DateTimeFormatter.RFC_1123_DATE_TIME
                    .withZone(ZoneId.of("UTC"))
                    .format(temporal);
            return result;
        }

        // map, collection
        public Map jvMap(Map<?, ?> m) {
            final Map result = new HashMap<>();
            m.forEach((k, v) -> {
                if (k != null) {
                    Object kV = jvObject(k);
                    Object vV = jvObject(v);
                    result.put(kV, vV);
                }
            });
            return result;
        }

        public List jvCollection(Collection<?> l) {
            final List result = new ArrayList<>();
            l.forEach(e -> {
                Object ejfy = jvObject(e);
                result.add(ejfy);
            });
            return result;
        }
    }
}
