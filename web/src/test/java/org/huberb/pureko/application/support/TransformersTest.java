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
package org.huberb.pureko.application.support;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author berni3
 */
public class TransformersTest {

    Transformers instance;

    @BeforeEach
    public void setUp() {
        instance = new Transformers();
    }

    /**
     * Test of transformTo method, of class Transformers.
     */
    @Test
    public void testTransformTo_GenericType_Function() {
        String result = instance.transformTo(3, (Integer i) -> "" + i);
        assertEquals("3", result);
    }

    /**
     * Test of transformTo method, of class Transformers.
     */
    @Test
    public void testTransformTo_3args() {
        String result = instance.transformTo(3, "X", (Integer i, String s) -> "" + i + " " + s);
        assertEquals("3 X", result);
    }

}
