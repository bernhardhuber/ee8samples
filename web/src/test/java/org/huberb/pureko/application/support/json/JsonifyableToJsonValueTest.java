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
package org.huberb.pureko.application.support.json;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;
import org.huberb.pureko.application.support.json.JsonValues.JsonifyableToJsonValue;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 *
 * @author berni3
 */
public class JsonifyableToJsonValueTest {

    @ParameterizedTest
    @MethodSource(value = "streamOfL")
    public void testListWithBasicValuesAndNestedMap(List<?> l) {
        assertNotNull(l);
        JsonValue o = JsonifyableToJsonValue.jvJsonValue().apply(l);
        assertNotNull(o);

        JsonObject jo = Json.createObjectBuilder().add("o", o).build();
        assertNotNull(jo);
        /*
org.opentest4j.AssertionFailedError: 
expected: <{"o":[  "","string1",1,2,3.299999952316284,4.4,5,6.6,{"k0String":"","k1String":"","k2List":[1,2,3]}]}> 
but was : <{"o":[null,"string1",1,2,3.299999952316284,4.4,5,6.6,{"k0String":null,"k1String":"","k2List":[1,2,3]}]}>
         */
        assertEquals("{\"o\":"
                + "["
                + "null,"
                + "\"string1\","
                + "1,"
                + "2,"
                + "3.299999952316284,"
                + "4.4,"
                + "5,"
                + "6.6,"
                + "{"
                + "\"k0String\":null,"
                + "\"k1String\":\"\","
                + "\"k2List\":[1,2,3]"
                + "}"
                + "]"
                + "}", jo.toString());
        JsonArray ja = jo.asJsonObject().getJsonArray("o");
        assertAll(
                () -> assertEquals(JsonValue.NULL, ja.get(0)),
                () -> assertEquals("string1", ja.getString(1))
        );
        assertAll(
                () -> assertEquals(1, ja.getInt(2)),
                () -> assertEquals(2, ja.getInt(3)),
                () -> assertEquals(3.299999952316284d, ja.getJsonNumber(4).doubleValue()),
                () -> assertEquals(4.4d, ja.getJsonNumber(5).doubleValue()),
                () -> assertEquals(5, ja.getJsonNumber(6).bigIntegerValue().intValue()),
                () -> assertEquals(6.6d, ja.getJsonNumber(7).bigDecimalValue().doubleValue())
        );
    }

    static Stream<?> streamOfL() {
        final List l0 = Arrays.asList(
                null,
                "string1",
                Integer.valueOf(1),
                Long.valueOf(2L),
                Float.valueOf(3.3f),
                Double.valueOf(4.4d),
                BigInteger.valueOf(5L),
                BigDecimal.valueOf(6.6d),
                new HashMap() {
            {
                this.put("k0String", null);
                this.put("k1String", "");
                this.put("k2List", new ArrayList<>() {
                    {
                        add(1);
                        add(2);
                        add(3);
                    }
                });
            }
        });
        final List l1 = new ArrayList<>() {
            {
                add(null);
                add("string1");
                add(Integer.valueOf(1));
                add(Long.valueOf(2L));
                add(Float.valueOf(3.3f));
                add(Double.valueOf(4.4d));
                add(BigInteger.valueOf(5L));
                add(BigDecimal.valueOf(6.6d));

                add(new HashMap() {
                    {
                        this.put("k0String", null);
                        this.put("k1String", "");
                        this.put("k2List", new ArrayList<>() {
                            {
                                add(1);
                                add(2);
                                add(3);
                            }
                        });
                    }
                });
            }
        };
        return Stream.of(l0, l1);
    }

    @Test
    public void testBasics() {
        JsonifyableToJsonValue instance = new JsonifyableToJsonValue();
        assertAll(
                () -> assertEquals(JsonValue.TRUE, instance.jvBoolean(Boolean.TRUE)),
                () -> assertEquals(JsonValue.FALSE, instance.jvBoolean(Boolean.FALSE))
        );

        assertAll(
                () -> assertEquals(Json.createValue("String"), instance.jvCharSequence("String")),
                () -> assertEquals(Json.createValue("StringBuilder"), instance.jvCharSequence(new StringBuilder("StringBuilder"))),
                () -> assertEquals(Json.createValue("StringBuffer"), instance.jvCharSequence(new StringBuffer("StringBuffer")))
        );
        assertAll(
                () -> assertEquals(Json.createValue("DECEMBER"), instance.jvEnum(java.time.Month.DECEMBER))
        );
        assertAll(
                () -> assertEquals(Json.createValue(1), instance.jvNumber(1)),
                () -> assertEquals(Json.createValue(1L), instance.jvNumber(1L)),
                () -> assertEquals(Json.createValue(1.1d), instance.jvNumber(1.1d)),
                () -> assertEquals(Json.createValue(1.100000023841858d), instance.jvNumber(1.1f)),
                () -> assertEquals(Json.createValue(BigInteger.ONE), instance.jvNumber(BigInteger.ONE)),
                () -> assertEquals(Json.createValue(BigDecimal.ONE), instance.jvNumber(BigDecimal.ONE))
        );
    }

    @Test
    public void testDate() {
        JsonifyableToJsonValue instance = new JsonifyableToJsonValue();

        Calendar cal = new Calendar.Builder().setDate(2022, Calendar.DECEMBER, 8).build();
        JsonValue s = instance.jvDate(cal.getTime());
        assertEquals(Json.createValue("2022-12-07T23:00:00Z"), s);
    }

    @Test
    public void testCalendar() {
        JsonifyableToJsonValue instance = new JsonifyableToJsonValue();

        Calendar cal = new Calendar.Builder().setDate(2022, Calendar.DECEMBER, 8).build();
        JsonValue s = instance.jvCalendar(cal);
        assertEquals(Json.createValue("2022-12-07T23:00:00Z"), s);
    }

    @Test
    public void testInstance() {
        JsonifyableToJsonValue instance = new JsonifyableToJsonValue();

        LocalDateTime instant = java.time.LocalDateTime.of(2022, Month.DECEMBER, 8, 0, 0);
        java.time.ZonedDateTime zdt = java.time.ZonedDateTime.of(instant, ZoneId.of("GMT+1"));
        JsonValue s = instance.jvTemporalAccessor(zdt);
        assertEquals(Json.createValue("Wed, 7 Dec 2022 23:00:00 GMT"), s);
    }

}
