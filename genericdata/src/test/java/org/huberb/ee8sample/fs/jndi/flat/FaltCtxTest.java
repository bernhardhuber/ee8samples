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
package org.huberb.ee8sample.fs.jndi.flat;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import javax.naming.Binding;
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
public class FaltCtxTest {

    FlatCtx flatCtx;

    @BeforeEach
    public void setUp() throws NamingException {
        Hashtable env = new Hashtable();
        this.flatCtx = new FlatCtx(env);
        this.flatCtx.bind("a", "A1");
        this.flatCtx.bind("b", 10);

    }

    @Test
    public void hello1() throws NamingException {
        assertEquals("A1", flatCtx.lookup("a"));
        assertEquals(10, flatCtx.lookup("b"));
    }

    @Test
    public void hello2() throws NamingException {
        assertAll(
                () -> assertTrue(flatCtx.list("").hasMore()),
                () -> assertTrue(flatCtx.list("").hasMoreElements())
        );
        List<NameClassPair> ncpList = new ArrayList<>();
        flatCtx.list("").asIterator().forEachRemaining((ncp) -> {
            ncpList.add(ncp);

        });
        System.out.format("name class pair: %s%n", ncpList);
    }

    @Test
    public void hello3() throws NamingException {
        assertAll(
                () -> assertTrue(flatCtx.listBindings("").hasMore()),
                () -> assertTrue(flatCtx.listBindings("").hasMoreElements())
        );
        List<Binding> bindingList = new ArrayList<>();
        flatCtx.listBindings("").asIterator().forEachRemaining((bnd) -> {
            bindingList.add(bnd);

        });
        System.out.format("binding list: %s%n", bindingList);

    }

    @Test
    public void hello4() throws NamingException {
        assertTrue(flatCtx.getEnvironment().isEmpty());
        assertEquals("", flatCtx.getNameInNamespace());
    }
}
