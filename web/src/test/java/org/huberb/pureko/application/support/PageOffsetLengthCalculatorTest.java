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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 *
 * @author berni3
 */
public class PageOffsetLengthCalculatorTest {

    public PageOffsetLengthCalculatorTest() {
    }

    /**
     * Test of calcNextPage method, of class PageOffsetLengthCalculator.
     */
    @Test
    public void testCalcNextPage() {
        PageOffsetLengthCalculator instance = new PageOffsetLengthCalculator(10, 1, 5);
        assertEquals(2, instance.calcNextPage());
    }

    /**
     * Test of calcPrevPage method, of class PageOffsetLengthCalculator.
     */
    @Test
    public void testCalcPrevPage() {
        PageOffsetLengthCalculator instance = new PageOffsetLengthCalculator(10, 1, 5);
        assertEquals(1, instance.calcPrevPage());
    }

    /**
     * Test of nextPageExists method, of class PageOffsetLengthCalculator.
     */
    @Test
    public void testNextPageExists() {
        PageOffsetLengthCalculator instance = new PageOffsetLengthCalculator(10, 1, 5);
        assertTrue(instance.nextPageExists());
    }

    /**
     * Test of prevPageExists method, of class PageOffsetLengthCalculator.
     */
    @Test
    public void testPrevPageExists() {
        PageOffsetLengthCalculator instance = new PageOffsetLengthCalculator(10, 1, 5);
        assertFalse(instance.prevPageExists());
    }

    /**
     * Test of moveToNextPage method, of class PageOffsetLengthCalculator.
     */
    @Test
    public void testMoveToNextPage() {
        PageOffsetLengthCalculator instance = new PageOffsetLengthCalculator(10, 1, 5);
        instance.moveToNextPage();
        assertEquals(2, instance.getPage());
    }

    /**
     * Test of moveToPrevPage method, of class PageOffsetLengthCalculator.
     */
    @Test
    public void testMoveToPrevPage() {
        PageOffsetLengthCalculator instance = new PageOffsetLengthCalculator(10, 1, 5);
        instance.moveToPrevPage();
        assertEquals(1, instance.getPage());
    }

    /**
     * Test of moveToFirstPage method, of class PageOffsetLengthCalculator.
     */
    @Test
    public void testMoveToFirstPage() {
        PageOffsetLengthCalculator instance = new PageOffsetLengthCalculator(10, 1, 5);
        instance.moveToFirstPage();
        assertEquals(1, instance.getPage());
    }

    /**
     * Test of moveToLastPage method, of class PageOffsetLengthCalculator.
     */
    @Test
    public void testMoveToLastPage() {
        PageOffsetLengthCalculator instance = new PageOffsetLengthCalculator(10, 1, 5);
        instance.moveToLastPage();
        assertEquals(2, instance.getPage());
    }

    /**
     * Test of numberOfPages method, of class PageOffsetLengthCalculator.
     */
    @Test
    public void testNumberOfPages() {
        PageOffsetLengthCalculator instance = new PageOffsetLengthCalculator(10, 1, 5);
        assertEquals(2, instance.numberOfPages());
    }

    /**
     * Test of calcOffetLength method, of class PageOffsetLengthCalculator.
     */
    @Test
    public void testCalcOffetLength() {
        PageOffsetLengthCalculator instance = new PageOffsetLengthCalculator(10, 1, 5);
        int[] result = instance.calcOffetLength();
        assertEquals(0, result[0]);
        assertEquals(5, result[1]);
    }

    /**
     * Test of assertMinMaxValues method, of class PageOffsetLengthCalculator.
     */
    @Test
    @Disabled
    public void testAssertMinMaxValues() {
    }

}
