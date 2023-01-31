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

import org.huberb.ee8sample.jndi.impl.hierarchysearch.HierDirCtx;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import javax.naming.Context;
import javax.naming.NameClassPair;
import javax.naming.NamingException;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchResult;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 *
 * @author berni3
 */
public class HierDirCtxTest {

    static DirContext createDirContext() throws NamingException {
        final DirContext ctx = new HierDirCtx(null);

        final BasicAttributes baA = new BasicAttributes() {
            {
                put("attrID1", "attrID1Value");
                put("fact", "the letter A");
            }
        };
        final DirContext a = ctx.createSubcontext("a", baA);
        a.bind("a1", "A");

        final DirContext b = ctx.createSubcontext("b", new BasicAttributes("fact", "the letter B"));
        b.bind("b1", "B");

        final Context c = b.createSubcontext("c");
        c.bind("c1", "C1");

        return ctx;
    }

    @Test
    public void hello1() throws NamingException {
        final DirContext ctx = createDirContext();
        Context c = (Context) ctx.lookup("b.c");
        assertEquals("b.c", c.getNameInNamespace());

        assertEquals(1, ctx.getAttributes("a").get("fact").size());
        assertEquals("fact", ctx.getAttributes("a").get("fact").getID());
        assertEquals("the letter A", ctx.getAttributes("a").get("fact").get());
    }

    @Test
    public void hello2() throws NamingException {
        final DirContext ctx = createDirContext();
        final List<NameClassPair> l = new ArrayList<>();
        ctx.list("").asIterator()
                .forEachRemaining(ncp -> {
                    l.add(ncp);
                });
        final BiFunction< List<NameClassPair>, String, String> matching = (ncpl, s) -> ncpl.stream()
                .filter(ncp -> ncp.getName().equals(s))
                .findFirst().get()
                .getName();
        final BiFunction< List<NameClassPair>, String, Boolean> missing = (ncpl, s) -> ncpl.stream()
                .filter(ncp -> ncp.getName().equals(s))
                .findFirst().isEmpty();

        assertEquals("a", matching.apply(l, "a"));
        assertEquals("b", matching.apply(l, "b"));
        assertEquals(true, missing.apply(l, "c"));
    }

    @Test
    public void hello3() throws NamingException {
        final DirContext ctx = createDirContext();

        List<SearchResult> l = new ArrayList<>();
        ctx.search("", new BasicAttributes("fact", "the letter A")).asIterator()
                .forEachRemaining(sr -> l.add(sr));

        assertEquals(1, l.size());
        assertEquals("a", l.get(0).getName());
        // TODO throws UnsupportedOperationException assertEquals("a", l.get(0).getNameInNamespace());
        assertEquals(1, l.get(0).getAttributes().get("fact").size());
        assertEquals("the letter A", l.get(0).getAttributes().get("fact").get());
        assertEquals("fact", l.get(0).getAttributes().get("fact").getID());
    }

}
