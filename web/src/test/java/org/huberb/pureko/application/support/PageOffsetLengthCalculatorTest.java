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

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 *
 * @author berni3
 */
public class PageOffsetLengthCalculatorTest {

    /**
     * Test of calcNextPage method, of class PageOffsetLengthCalculator.
     *
     * @param totalLength
     * @param page
     * @param elementsPerPage
     * @param expectedPage
     */
    @ParameterizedTest()
    @CsvSource({
        "10,1,5, 2",
        "10,2,5, 2",
        "11,1,5, 2",
        "11,2,5, 3",
        "11,3,5, 3",})
    public void testCalcNextPage(int totalLength, int page, int elementsPerPage,
            int expectedPage) {
        final PageOffsetLengthCalculator instance = new PageOffsetLengthCalculator(totalLength, page, elementsPerPage);
        assertEquals(expectedPage, instance.calcNextPage());
        assertAll(
                () -> assertEquals(elementsPerPage, instance.getElementsPerPage()),
                () -> assertEquals(page, instance.getPage()),
                () -> assertEquals(totalLength, instance.getTotalLength())
        );
    }

    /**
     * Test of calcPrevPage method, of class PageOffsetLengthCalculator.
     *
     * @param expectedPage
     */
    @ParameterizedTest()
    @CsvSource({
        "10,2,5, 1",
        "10,1,5, 1",
        "11,3,5, 2",
        "11,2,5, 1",
        "11,1,5, 1",})
    public void testCalcPrevPage(int totalLength, int page, int elementsPerPage,
            int expectedPage) {
        final PageOffsetLengthCalculator instance = new PageOffsetLengthCalculator(totalLength, page, elementsPerPage);
        assertEquals(expectedPage, instance.calcPrevPage());
        assertAll(
                () -> assertEquals(elementsPerPage, instance.getElementsPerPage()),
                () -> assertEquals(page, instance.getPage()),
                () -> assertEquals(totalLength, instance.getTotalLength())
        );

    }

    /**
     * Test of nextPageExists method, of class PageOffsetLengthCalculator.
     *
     * @param totalLength
     * @param page
     * @param elementsPerPage
     * @param expectedNextPageExists
     */
    @ParameterizedTest()
    @CsvSource({
        "10,1,5, true",
        "10,2,5, false",
        "11,1,5, true",
        "11,2,5, true",
        "11,3,5, false",})
    public void testNextPageExists(int totalLength, int page, int elementsPerPage,
            boolean expectedNextPageExists) {
        final PageOffsetLengthCalculator instance = new PageOffsetLengthCalculator(totalLength, page, elementsPerPage);
        assertEquals(expectedNextPageExists, instance.nextPageExists());
        assertAll(
                () -> assertEquals(elementsPerPage, instance.getElementsPerPage()),
                () -> assertEquals(page, instance.getPage()),
                () -> assertEquals(totalLength, instance.getTotalLength())
        );
    }

    /**
     * Test of prevPageExists method, of class PageOffsetLengthCalculator.
     *
     * @param totalLength
     * @param page
     * @param elementsPerPage
     * @param expectedPrevPageExists
     */
    @ParameterizedTest()
    @CsvSource({
        "10,2,5, true",
        "10,1,5, false",
        "11,3,5, true",
        "11,2,5, true",
        "11,1,5, false",})
    public void testPrevPageExists(int totalLength, int page, int elementsPerPage,
            boolean expectedPrevPageExists) {
        final PageOffsetLengthCalculator instance = new PageOffsetLengthCalculator(totalLength, page, elementsPerPage);
        assertEquals(expectedPrevPageExists, instance.prevPageExists());
        assertAll(
                () -> assertEquals(elementsPerPage, instance.getElementsPerPage()),
                () -> assertEquals(page, instance.getPage()),
                () -> assertEquals(totalLength, instance.getTotalLength())
        );
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
     * Test of assertMinMaxValues method, of class PageOffsetLengthCalculator.
     */
    @Test
    public void testAssertMinMaxValues_singleSample() {
        PageOffsetLengthCalculator instance = new PageOffsetLengthCalculator(10, 1, 5);
        instance.assertMinMaxValues();
        assertAll(
                () -> assertEquals(5, instance.getElementsPerPage()),
                () -> assertEquals(1, instance.getPage()),
                () -> assertEquals(10, instance.getTotalLength())
        );
    }

    @ParameterizedTest()
    @CsvSource({
        "10,1,5, 10,1,5",
        // lower bound value 1
        "10,0,5, 10,1,5",
        "10,-1,5, 10,1,5",
        // upper bound page value 2
        "10,3,5, 10,2,5",
        "10,4,5, 10,2,5",
        // lower bound value 1
        "11,1,5, 11,1,5",
        "11,0,5, 11,1,5",
        "11,-1,5, 11,1,5",
        // upper bound page value 3
        "11,4,5, 11,3,5",
        "11,5,5, 11,3,5",})
    public void testAssertMinMaxValues(
            int totalLength, int page, int elementsPerPage,
            int expectedTotalLength, int expectedPage, int expectedElementsPerPage) {

        PageOffsetLengthCalculator instance = new PageOffsetLengthCalculator(totalLength, page, elementsPerPage);
        instance.assertMinMaxValues();
        assertAll(
                () -> assertEquals(expectedElementsPerPage, instance.getElementsPerPage()),
                () -> assertEquals(expectedPage, instance.getPage()),
                () -> assertEquals(expectedTotalLength, instance.getTotalLength())
        );
    }
}
