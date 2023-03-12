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
import java.util.Locale;
import java.util.TimeZone;
import java.util.stream.Stream;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import org.huberb.pureko.application.support.json.JsonValues.JsonifyableToObject;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 *
 * @author berni3
 */
public class JsonifyableToObjectTest {

    private Locale wasLocaleSet;
    private TimeZone wasTimeZoneSet;

    @BeforeEach
    public void setUp() {
        wasLocaleSet = Locale.getDefault();
        wasTimeZoneSet = TimeZone.getDefault();

        Locale.setDefault(Locale.UK);
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/London"));
    }

    @AfterEach
    public void tearDown() {
        Locale.setDefault(wasLocaleSet);
        TimeZone.setDefault(wasTimeZoneSet);
    }

    @ParameterizedTest
    @MethodSource(value = "streamOfL")
    public void testListWithBasicValuesAndNestedMap(List<?> l) {
        assertNotNull(l);
        Object o = JsonifyableToObject.jvObject().apply(l);
        assertNotNull(o);

        JsonObject jo = Json.createObjectBuilder(new HashMap() {
            {
                put("o", o);
            }
        }).build();
        assertNotNull(jo);
        assertEquals("{\"o\":"
                + "["
                + "\"\","
                + "\"string1\","
                + "1,"
                + "2,"
                + "3.299999952316284,"
                + "4.4,"
                + "5,"
                + "6.6,"
                + "{"
                + "\"k0String\":\"\","
                + "\"k1String\":\"\","
                + "\"k2List\":[1,2,3]"
                + "}"
                + "]"
                + "}", jo.toString());
        JsonArray ja = jo.asJsonObject().getJsonArray("o");
        assertEquals("", ja.getString(0));
        assertEquals("string1", ja.getString(1));
        assertEquals(1, ja.getInt(2));
        assertEquals(2, ja.getInt(3));
        assertEquals(3.299999952316284d, ja.getJsonNumber(4).doubleValue());
        assertEquals(4.4d, ja.getJsonNumber(5).doubleValue());
        assertEquals(5, ja.getJsonNumber(6).bigIntegerValue().intValue());
        assertEquals(6.6d, ja.getJsonNumber(7).bigDecimalValue().doubleValue());
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
        JsonifyableToObject instance = new JsonifyableToObject();
        assertAll(
                () -> assertEquals(Boolean.TRUE, instance.jvBoolean(Boolean.TRUE)),
                () -> assertEquals(Boolean.FALSE, instance.jvBoolean(Boolean.FALSE))
        );
        assertAll(
                () -> assertEquals("String", instance.jvCharSequence("String")),
                () -> assertEquals("StringBuilder", instance.jvCharSequence(new StringBuilder("StringBuilder"))),
                () -> assertEquals("StringBuffer", instance.jvCharSequence(new StringBuffer("StringBuffer")))
        );
        assertAll(
                () -> assertEquals("DECEMBER", instance.jvEnum(java.time.Month.DECEMBER))
        );
        assertAll(
                () -> assertEquals(1, instance.jvNumber(1)),
                () -> assertEquals(1L, instance.jvNumber(1L)),
                () -> assertEquals(1.1d, instance.jvNumber(1.1d)),
                () -> assertEquals(1.100000023841858d, instance.jvNumber(1.1f)),
                () -> assertEquals(BigInteger.ONE, instance.jvNumber(BigInteger.ONE)),
                () -> assertEquals(BigDecimal.ONE, instance.jvNumber(BigDecimal.ONE))
        );
    }

    @Test
    public void testDate() {
        JsonifyableToObject instance = new JsonifyableToObject();

        Calendar cal = new Calendar.Builder()
                .setTimeZone(TimeZone.getTimeZone("Europe/Vienna")).setDate(2022, Calendar.DECEMBER, 8)
                .build();
        String s = instance.jvDate(cal.getTime());
        assertEquals("2022-12-07T23:00:00Z", s);
    }

    @Test
    public void testCalendar() {
        JsonifyableToObject instance = new JsonifyableToObject();

        Calendar cal = new Calendar.Builder()
                .setTimeZone(TimeZone.getTimeZone("Europe/Vienna"))
                .setDate(2022, Calendar.DECEMBER, 8)
                .build();
        String s = instance.jvCalendar(cal);
        assertEquals("2022-12-07T23:00:00Z", s);
    }

    @Test
    public void testInstance() {
        JsonifyableToObject instance = new JsonifyableToObject();

        LocalDateTime instant = java.time.LocalDateTime.of(2022, Month.DECEMBER, 8, 0, 0);
        java.time.ZonedDateTime zdt = java.time.ZonedDateTime.of(instant, ZoneId.of("GMT+1"));
        String s = instance.jvTemporalAccessor(zdt);
        assertEquals("Wed, 7 Dec 2022 23:00:00 GMT", s);
    }

}
