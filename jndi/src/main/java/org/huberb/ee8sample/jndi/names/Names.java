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

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import javax.naming.CompoundName;
import javax.naming.Name;
import javax.naming.NameParser;
import javax.naming.NamingException;

/**
 *
 * @author berni3
 */
public class Names {

    static class HierParser implements NameParser {

        private static final Properties syntax = new Properties();

        /**
         * @see CompoundName
         */
        static {
            //syntax.put("jndi.syntax.direction", "right_to_left");
            syntax.put("jndi.syntax.direction", "left_to_right");
            syntax.put("jndi.syntax.separator", ".");
            syntax.put("jndi.syntax.ignorecase", "false");
            syntax.put("jndi.syntax.escape", "\\");
            syntax.put("jndi.syntax.beginquote", "'");
        }

        @Override
        public Name parse(String name) throws NamingException {
            return new CompoundName(name, syntax);
        }
    }

    static class NameSplitted {

        private final List<String> components;

        NameSplitted(List<String> components) {
            this.components = components;
        }

        NameSplitted(String n) {
            components = new NameSplitter().split(n);
        }

        List<String> getAll() {
            return Collections.unmodifiableList(components);
        }

        void consume(Consumer<List<String>> c) {
            c.accept(getAll());
        }

        Supplier<NameSplitted> prefix(int n) {
            return () -> new NameSplitted(components.subList(0, n));
        }

        Supplier<NameSplitted> suffix(int n) {
            return () -> new NameSplitted(components.subList(n, components.size()));
        }

        String flatten() {
            String flat = components.stream().collect(Collectors.joining("."));
            return flat;
        }

        boolean matchesExact(NameSplitted matching) {
            final String flat = flatten();
            final String flatMatching = matching.flatten();
            return flat.equals(flatMatching);
        }

        boolean matchesPrefix(NameSplitted matching) {
            final String flat = flatten();
            final String flatMatching = matching.flatten();
            return flat.startsWith(flatMatching);
        }

        boolean matchesSuffix(NameSplitted matching) {
            final String flat = flatten();
            final String flatMatching = matching.flatten();
            return flat.endsWith(flatMatching);
        }

        boolean matchesExact2(NameSplitted matching) {
            boolean result = true;
            result = result && this.components.size() == matching.components.size();
            for (int i = 0; result && i < this.components.size(); i++) {
                result = result && this.components.get(i).equals(matching.components.get(i));
            }
            return result;
        }

        @Override
        public String toString() {
            return String.valueOf(this.components);
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 83 * hash + Objects.hashCode(this.components);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final NameSplitted other = (NameSplitted) obj;
            return Objects.equals(this.components, other.components);
        }

    }

    static class NameSplittedContext {

        final Map<NameSplitted, Value<Object>> m;

        public NameSplittedContext() {
            this.m = Collections.synchronizedMap(new HashMap<>());
        }

        void consume(Consumer<Map<NameSplitted, Value<Object>>> c) {
            c.accept(this.m);
        }

        Optional<Entry<NameSplitted, Value<Object>>> find(NameSplitted n) {
            return m.entrySet().stream()
                    .filter(e -> e.getKey().equals(n))
                    .findFirst();
        }

        List<Entry<NameSplitted, Value<Object>>> findPrefix(NameSplitted n) {
            return m.entrySet().stream()
                    .filter(e -> e.getKey().matchesPrefix(n))
                    .map(e -> e)
                    .collect(Collectors.toUnmodifiableList());
        }

        List<Entry<NameSplitted, Value<Object>>> findSuffix(NameSplitted n) {
            return m.entrySet().stream()
                    .filter(e -> e.getKey().matchesSuffix(n))
                    .map(e -> e)
                    .collect(Collectors.toUnmodifiableList());
        }

        NameSplittedContext put(NameSplitted ns, Value v) {
            m.put(ns, v);
            return this;
        }

        NameSplittedContext remove(NameSplitted ns) {
            m.remove(ns);
            return this;
        }
    }

    static class Value<T> {

        private static final Value EMPTY = new Value();

        static final Value empty() {
            return EMPTY;
        }

        @Override
        public String toString() {
            return "value=''";
        }
    }

}
