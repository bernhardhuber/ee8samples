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

import org.junit.jupiter.api.Test;

/**
 *
 * @author berni3
 */
public class IdentificationsTest {

    /**
     * Test of xxx1 method, of class Identifications.
     */
    @Test
    public void testXxx1() {
        Identifications instance = new Identifications();
        System.out.format("xxx1 %s%n", instance.xxx1());
    }

    /**
     * Test of xxx2 method, of class Identifications.
     */
    @Test
    public void testXxx2() {
        Identifications instance = new Identifications();
        System.out.format("xxx2 %s%n", instance.xxx2("Hello world"));
    }

    /**
     * Test of xxx3 method, of class Identifications.
     */
    @Test
    public void testXxx3() {
        Identifications instance = new Identifications();
        System.out.format("xxx3 %s%n", instance.xxx3("Hello world"));
    }

    /**
     * Test of xxx4 method, of class Identifications.
     */
    @Test
    public void testXxx4() {
        Identifications instance = new Identifications();
        System.out.format("xxx4 %s%n", instance.xxx4("Hello world"));
    }

    /**
     * Test of xxx4 method, of class Identifications.
     */
    @Test
    public void testXxx5() {
        Identifications instance = new Identifications();
        System.out.format("xxx5-String %s%n", instance.objectIdentification("String", "Hello world"));
        System.out.format("xxx5-PERSON %s%n", instance.objectIdentification("PERSON", Basics.Person.createEmptyPerson()));
        System.out.format("xxx5-String %s%n", instance.objectIdentification("String", "Hello world"));
        System.out.format("xxx5-PERSON %s%n", instance.objectIdentification("PERSON", Basics.Person.createEmptyPerson()));
    }

    @Test
    public void testF0() {
        System.out.format("f0-String %s%n", Identifications.f0("String").apply("Hello world"));
        System.out.format("f0-PERSON %s%n", Identifications.f0("PERSON").apply(Basics.Person.createEmptyPerson()));
        System.out.format("f0-String %s%n", Identifications.f0("String").apply("Hello world"));
        System.out.format("f0-PERSON %s%n", Identifications.f0("PERSON").apply(Basics.Person.createEmptyPerson()));

    }

    @Test
    public void testF1() {
        System.out.format("f1-String %s%n", Identifications.f1("String").apply("Hello world"));
        System.out.format("f1-PERSON %s%n", Identifications.f1("PERSON").apply(Basics.Person.createEmptyPerson()));
        System.out.format("f1-String %s%n", Identifications.f1("String").apply("Hello world"));
        System.out.format("f1-PERSON %s%n", Identifications.f1("PERSON").apply(Basics.Person.createEmptyPerson()));

    }

}
