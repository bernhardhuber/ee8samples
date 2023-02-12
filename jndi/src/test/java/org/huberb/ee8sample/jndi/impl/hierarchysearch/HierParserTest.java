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
package org.huberb.ee8sample.jndi.impl.hierarchysearch;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import javax.naming.Name;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 *
 * @author berni3
 */
public class HierParserTest {

    /**
     * Test of parse method, of class FlatNameParser.
     */
    @ParameterizedTest
    @ValueSource(strings = {"name", "anothername123", "_A_B_-_C_D_"})
    public void testParse(String theName) throws Exception {
        HierParser fnp = new HierParser();

        Name name = fnp.parse(theName);
        assertNotNull(name);
        assertAll(
                () -> assertEquals(theName, name.get(0)),
                () -> assertEquals(true, name.getAll().hasMoreElements()),
                () -> assertEquals(theName, name.getAll().nextElement()),
                () -> assertEquals(false, name.isEmpty()),
                () -> assertEquals(1, name.size())
        );
    }

    /**
     * Test of parse method, of class FlatNameParser.
     */
    @ParameterizedTest
    @MethodSource("namesSubnames")
    public void testParse_1_subname(String theName, String name0, String name1) throws Exception {
        HierParser fnp = new HierParser();

        Name name = fnp.parse(theName);
        assertNotNull(name);
        assertAll(
                () -> assertEquals(name0, name.get(0)),
                () -> assertEquals(name1, name.get(1)),
                () -> assertEquals(true, name.getAll().hasMoreElements()),
                () -> assertEquals(name0, name.getAll().nextElement()),
                () -> assertEquals(false, name.isEmpty()),
                () -> assertEquals(2, name.size())
        );
    }

    public static Stream<String[]> namesSubnames() {
        List<String[]> l = new ArrayList<>() {
            {
                add(new String[]{"name.subname", "name", "subname"});
                add(new String[]{"anothername.123", "anothername", "123"});
                add(new String[]{"_A_B_._C_D_", "_A_B_", "_C_D_"});
            }
        };
        return l.stream();
    }
}
