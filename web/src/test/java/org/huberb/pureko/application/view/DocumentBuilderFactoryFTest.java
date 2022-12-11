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
package org.huberb.pureko.application.view;

import javax.xml.parsers.DocumentBuilderFactory;
import org.huberb.pureko.application.view.XmlDomDocumentsF.DocumentBuilderFactoryF;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;

/**
 *
 * @author berni3
 */
public class DocumentBuilderFactoryFTest {

    @Test
    public void testGetFunctions() {
        DocumentBuilderFactoryF dbff = new DocumentBuilderFactoryF(DocumentBuilderFactory.newInstance());
        Object[] getValues = new Object[]{
            dbff.get(DocumentBuilderFactoryF.coalescing()),
            dbff.get(DocumentBuilderFactoryF.expandEntityReferences()),
            dbff.get(DocumentBuilderFactoryF.ignoringComments()),
            dbff.get(DocumentBuilderFactoryF.ignoringElementContentWhitespace()),
            dbff.get(DocumentBuilderFactoryF.namespaceAware()),
            dbff.get(DocumentBuilderFactoryF.schema()),
            dbff.get(DocumentBuilderFactoryF.validating()),
            dbff.get(DocumentBuilderFactoryF.xincludeAware())
        };
        assertAll(
                () -> assertEquals(false, getValues[0]),
                () -> assertEquals(true, getValues[1]),
                () -> assertEquals(false, getValues[2]),
                () -> assertEquals(false, getValues[3]),
                () -> assertEquals(false, getValues[4]),
                () -> assertNull(getValues[5]),
                () -> assertEquals(false, getValues[6]),
                () -> assertEquals(false, getValues[7])
        );
        assertAll(
                () -> assertEquals(false, dbff.getDocumentBuilderFactory().isNamespaceAware()),
                () -> assertEquals(false, dbff.get(DocumentBuilderFactoryF.namespaceAware())),
                () -> assertEquals(
                        dbff.getDocumentBuilderFactory().isNamespaceAware(),
                        dbff.get(DocumentBuilderFactoryF.namespaceAware()))
        );
    }

    @Test
    public void testSomeGetAndSetFunctions() {
        DocumentBuilderFactoryF dbff = new DocumentBuilderFactoryF(DocumentBuilderFactory.newInstance());

        assertEquals(false, dbff.get(DocumentBuilderFactoryF.namespaceAware()));
        assertEquals(false, dbff.get(DocumentBuilderFactoryF.validating()));
        assertEquals(false, dbff.get(DocumentBuilderFactoryF.xincludeAware()));

        dbff.set(
                DocumentBuilderFactoryF.namespaceAware(true)
                        .andThen(DocumentBuilderFactoryF.validating(true))
                        .andThen(DocumentBuilderFactoryF.xincludeAware(true))
        );

        assertEquals(true, dbff.get(DocumentBuilderFactoryF.namespaceAware()));
        assertEquals(true, dbff.get(DocumentBuilderFactoryF.validating()));
        assertEquals(true, dbff.get(DocumentBuilderFactoryF.xincludeAware()));
    }
}
