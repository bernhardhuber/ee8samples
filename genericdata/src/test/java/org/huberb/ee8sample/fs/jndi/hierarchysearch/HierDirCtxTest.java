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
package org.huberb.ee8sample.fs.jndi.hierarchysearch;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import org.junit.jupiter.api.Test;

/**
 *
 * @author berni3
 */
public class HierDirCtxTest {

    public HierDirCtxTest() {
    }

    @Test
    public void hello1() throws NamingException {

        DirContext ctx = new HierDirCtx(null);

        DirContext a = ctx.createSubcontext("a", new BasicAttributes("fact", "the letter A"));
        a.bind("a1", "A");
        DirContext b = ctx.createSubcontext("b", new BasicAttributes("fact", "the letter B"));
        b.bind("b1", "B");
        Context c = b.createSubcontext("c");
        c.bind("c1", "C1");

        System.out.println("c's full name: " + c.getNameInNamespace());
        System.out.println("attributes of a: " + ctx.getAttributes("a"));

        System.out.println("list: ");
        NamingEnumeration enumE = ctx.list("");
        while (enumE.hasMore()) {
            System.out.println(enumE.next());
        }

        System.out.println("search: ");
        enumE = ctx.search("", new BasicAttributes("fact", "the letter A"));

        while (enumE.hasMore()) {
            System.out.println(enumE.next());
        }

    }

}
