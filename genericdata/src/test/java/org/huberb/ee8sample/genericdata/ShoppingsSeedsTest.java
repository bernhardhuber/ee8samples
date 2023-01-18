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
package org.huberb.ee8sample.genericdata;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author berni3
 */
public class ShoppingsSeedsTest {

    ShoppingsSeeds instance;

    @BeforeEach
    public void setUp() {
        this.instance = new ShoppingsSeeds();
    }

    /**
     * Test of seedItems method, of class ShoppingsSeeds.
     */
    @Test
    public void testSeedItems() {
        Map<String, Object> map = instance.seedItems(10);
        assertNotNull(map);
        map.entrySet().forEach(e -> {
            String m = "" + e;
            Assertions.assertAll(
                    () -> assertNotNull(e.getKey(), m),
                    () -> assertNotNull(e.getValue(), m),
                    () -> assertTrue(e.getValue() instanceof List, m),
                    () -> assertTrue(((List) e.getValue()).size() > 0, m)
            );
        });
    }

}
