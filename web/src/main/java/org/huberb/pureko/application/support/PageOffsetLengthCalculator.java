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

/**
 *
 * @author berni3
 */
@lombok.ToString
@lombok.EqualsAndHashCode
public class PageOffsetLengthCalculator {

    private int totalLength;
    private int page;
    private int elementsPerPage;

    public PageOffsetLengthCalculator() {
        this(0, 1, 5);
    }

    public PageOffsetLengthCalculator(int totalLength, int page_1, int elementsPerPage_1) {
        this.totalLength = totalLength;
        this.page = page_1;
        this.elementsPerPage = elementsPerPage_1;
    }

//---
    public int getPage() {
        return this.page;
    }

    public int getElementsPerPage() {
        return this.elementsPerPage;
    }

    public int getTotalLength() {
        return this.totalLength;
    }
//---

    int calcNextPage() {
        assertMinMaxValues();
        final int nextOffset = page * elementsPerPage;
        final int nextPage;
        if (nextOffset < totalLength) {
            nextPage = page + 1;
        } else {
            nextPage = page;
        }
        return nextPage;

    }

    int calcPrevPage() {
        assertMinMaxValues();
        int newPage_1 = page - 1;
        if (newPage_1 >= 1) {
            return newPage_1;
        } else {
            return page;
        }
    }

    public boolean nextPageExists() {
        int v = calcNextPage();
        return (v != page);
    }

    public boolean prevPageExists() {
        int v = calcPrevPage();
        return (v != page);
    }

    public void moveToNextPage() {
        page = calcNextPage();
    }

    public void moveToPrevPage() {
        page = calcPrevPage();
    }

    public void moveToFirstPage() {
        page = 1;
    }

    public void moveToLastPage() {
        int result = (totalLength / elementsPerPage);
        if (totalLength % elementsPerPage != 0) {
            result += 1;
        }
        page = result;
    }

    public int numberOfPages() {
        int result = (totalLength / elementsPerPage);
        if (totalLength % elementsPerPage != 0) {
            result += 1;
        }
        return result;
    }

    public int[] calcOffetLength() {
        int offset = (page - 1) * elementsPerPage;
        int length = elementsPerPage;
        return new int[]{offset, length};
    }

    void assertMinMaxValues() {
        elementsPerPage = Math.max(1, elementsPerPage);
        if (page < 1) {
            page = 1;
        }
        int maxPageValue = (totalLength / elementsPerPage);
        if (totalLength % elementsPerPage != 0) {
            maxPageValue += 1;
        }

        if (page > maxPageValue) {
            page = maxPageValue;
        }
    }
}
