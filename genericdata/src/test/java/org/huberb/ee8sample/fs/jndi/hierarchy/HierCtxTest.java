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
package org.huberb.ee8sample.fs.jndi.hierarchy;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.NameClassPair;
import javax.naming.NamingException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author berni3
 */
public class HierCtxTest {

    HierCtx hierCtx;

    @BeforeEach
    public void setUp() throws NamingException {
        Hashtable env = new Hashtable();
        this.hierCtx = new HierCtx(env);
        this.hierCtx.bind("a", "A1");
        this.hierCtx.bind("b", 10);

        Context c = this.hierCtx.createSubcontext("c");
        c.bind("d", "D1");

    }

    @Test
    public void hello1() throws NamingException {
        assertEquals("A1", hierCtx.lookup("a"));
        assertEquals(10, hierCtx.lookup("b"));
        assertEquals("D1", hierCtx.lookup("c.d"));
    }

    @Test
    public void hello2() throws NamingException {
        assertAll(() -> assertTrue(hierCtx.list("").hasMore()),
                () -> assertTrue(hierCtx.list("").hasMoreElements())
        );
        List<NameClassPair> ncpList = new ArrayList<>();
        hierCtx.list("").asIterator().forEachRemaining((ncp) -> {
            ncpList.add(ncp);

        });
        System.out.format("name class pair: %s%n", ncpList);
    }

    @Test
    public void hello3() throws NamingException {
        assertAll(() -> assertTrue(hierCtx.listBindings("").hasMore()),
                () -> assertTrue(hierCtx.listBindings("").hasMoreElements())
        );
        List<Binding> bindingList = new ArrayList<>();
        hierCtx.listBindings("").asIterator().forEachRemaining((bnd) -> {
            bindingList.add(bnd);
        });

        System.out.format("binding list: %s%n", bindingList);

    }

    @Test
    public void hello4() throws NamingException {
        assertTrue(hierCtx.getEnvironment().isEmpty());
        assertEquals("", hierCtx.getNameInNamespace());
    }

 
    @Test
    public void testCreateSubcontext() throws NamingException {
        Context ctx = new HierCtx(null);

        Context a = ctx.createSubcontext("a");
        Context b = a.createSubcontext("b");
        Context c = b.createSubcontext("c");

        assertEquals("a.b.c", c.getNameInNamespace());

        assertAll(
                () -> assertTrue(ctx.lookup("") instanceof HierCtx),
                () -> assertEquals("ROOT CONTEXT", ctx.lookup("").toString())
        );
        assertAll(
                () -> assertTrue(ctx.lookup("a") instanceof HierCtx),
                () -> assertEquals("a", ctx.lookup("a").toString())
        );
        assertAll(
                () -> assertTrue(ctx.lookup("a.b") instanceof HierCtx),
                () -> assertEquals("b", ctx.lookup("a.b").toString())
        );
        assertAll(
                () -> assertTrue(a.lookup("b.c") instanceof HierCtx),
                () -> assertEquals("c", a.lookup("b.c").toString())
        );
    }
}
