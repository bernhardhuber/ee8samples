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

import java.util.function.Function;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 *
 * @author berni3
 */
public class DelegatingHolderTest {

    @Test
    public void hello() {
        Delegating ncd = new Delegating();
        assertAll(
                () -> assertEquals("s1", ncd.getS1()),
                () -> assertEquals("s2", ncd.getS2()),
                () -> assertTrue(ncd.isB1()),
                () -> assertFalse(ncd.isB2())
        );
    }

    static class Holder<T> {

        final Function<Long, T> f;
        Long id;
        T t;

        public Holder(Long id, Function<Long, T> f) {
            this.f = f;
            recreateTWithNewId(id);
        }

        final public void recreateTWithOldId() {
            this.t = null;
        }

        final public void recreateTWithNewId(Long id) {
            this.id = id;
            this.t = null;
        }

        public T getT() {
            if (t == null) {
                t = f.apply(id);
            }
            return t;
        }

    }

    static class Delegating implements Values {

        final Holder<Values> vh;
        final Function<Long, Values> f = (id) -> create(id);

        Delegating() {
            this.vh = new Holder<Values>(0L, f);
        }

        private Values create(Long id) {
            Values v = new Values() {
                @Override
                public String getS1() {
                    return "s1";
                }

                @Override
                public String getS2() {
                    return "s2";
                }

                @Override
                public boolean isB1() {
                    return true;
                }

                @Override
                public boolean isB2() {
                    return false;
                }
            };
            return v;
        }

        @Override
        public String getS1() {
            vh.recreateTWithNewId(1L);
            return vh.getT().getS1();
        }

        @Override
        public String getS2() {
            vh.recreateTWithOldId();
            return vh.getT().getS2();
        }

        @Override
        public boolean isB1() {
            return vh.getT().isB1();
        }

        @Override
        public boolean isB2() {
            return vh.getT().isB2();
        }

    }

    interface Values {

        public String getS1();

        public String getS2();

        public boolean isB1();

        public boolean isB2();

    }

}
