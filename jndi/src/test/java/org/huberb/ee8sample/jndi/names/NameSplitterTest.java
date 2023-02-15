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
package org.huberb.ee8sample.jndi.names;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 *
 * @author berni3
 */
public class NameSplitterTest {

    /**
     * Test of split method, of class NameSplitter.
     */
    @Test
    public void testSplit() {
        NameSplitter instance = new NameSplitter();
        {
            String name = "";
            assertAll(
                    () -> assertEquals(0, instance.split(name).size())
            );
        }
        {
            String name = "abc";
            assertAll(
                    () -> assertEquals(1, instance.split(name).size()),
                    () -> assertEquals("abc", instance.split(name).get(0))
            );
        }
        {
            String name = "a.bc";
            assertAll(
                    () -> assertEquals(2, instance.split(name).size()),
                    () -> assertEquals("a", instance.split(name).get(0)),
                    () -> assertEquals("bc", instance.split(name).get(1))
            );
        }
        {
            String name = "a.b.c";
            assertAll(
                    () -> assertEquals(3, instance.split(name).size()),
                    () -> assertEquals("a", instance.split(name).get(0)),
                    () -> assertEquals("b", instance.split(name).get(1)),
                    () -> assertEquals("c", instance.split(name).get(2))
            );
        }
        {
            String name = "a=x.b=y.c=z";
            assertAll(
                    () -> assertEquals(3, instance.split(name).size()),
                    () -> assertEquals("a=x", instance.split(name).get(0)),
                    () -> assertEquals("b=y", instance.split(name).get(1)),
                    () -> assertEquals("c=z", instance.split(name).get(2))
            );
        }
        {
            String name = "a.'b.c'";
            assertAll(
                    () -> assertEquals(2, instance.split(name).size()),
                    () -> assertEquals("a", instance.split(name).get(0)),
                    () -> assertEquals("b.c", instance.split(name).get(1))
            );
        }
        {
            String name = "a.\"b.c\"";
            assertAll(
                    () -> assertEquals(2, instance.split(name).size()),
                    () -> assertEquals("a", instance.split(name).get(0)),
                    () -> assertEquals("b.c", instance.split(name).get(1))
            );
        }
    }

}
