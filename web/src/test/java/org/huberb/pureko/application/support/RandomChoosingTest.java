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

import java.util.function.Supplier;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 *
 * @author berni3
 */
public class RandomChoosingTest {

    public RandomChoosingTest() {
    }

    /**
     * Test of randomlyChoose method, of class RandomChoosing.
     */
    @Test
    public void testRandomlyChoose() {
        int v = 2;
        Supplier<Integer> intSupp = RandomChoosing.randomlyChoose(v);
        for (int i = 0; i < 100; i++) {
            Integer rv = intSupp.get();
            String m = String.format("i %d: v %d, rv %d", i, v, rv);
            assertTrue(rv == 0 || rv <= v - 1, m);
        }
    }

    /**
     * Test of randomlyChoose method, of class RandomChoosing.
     */
    @Test
    public void testRandomlyChoose_2_200() {
        for (int v = 2; v < 200; v++) {
            Supplier<Integer> intSupp = RandomChoosing.randomlyChoose(v);
            for (int i = 0; i < 100; i += 1) {
                Integer rv = intSupp.get();
                String m = String.format("i %d: v %d rv %d", i, v, rv);
                assertTrue(rv >= 0 || rv <= v - 1, m);
            }
        }
    }

}
