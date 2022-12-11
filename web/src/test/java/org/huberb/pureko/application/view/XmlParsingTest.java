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

import java.io.File;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.huberb.pureko.application.view.XmlDomDocumentsF.DocumentBuilderF;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.params.ParameterizedTest;
import org.w3c.dom.Document;

/**
 *
 * @author berni3
 */
public class XmlParsingTest {

    static final DocumentBuilderFactory DOCUMENT_BUILDER_FACTORY = DocumentBuilderFactory.newInstance();

    @ParameterizedTest
    @org.junit.jupiter.params.provider.ValueSource(strings = {
        "src/main/resources/META-INF/persistence.xml",
        "src/main/webapp/WEB-INF/beans.xml",
        "src/main/webapp/WEB-INF/web.xml"
    })
    public void given_an_xml_file_then_it_is_parsable_and_well_formed(String fn) throws Exception {
        parseXmlFApi(fn);
    }

    void parseXmlFApi(String fn) throws ParserConfigurationException {
        final File f = new File(fn);
        assertAll(
                () -> f.exists(),
                () -> f.isFile(),
                () -> f.canRead()
        );

        final Document document = new DocumentBuilderF(DOCUMENT_BUILDER_FACTORY.newDocumentBuilder())
                .transformTo(DocumentBuilderF.parse(f));
        assertAll(
                () -> assertNotNull(document),
                () -> assertTrue(document.getChildNodes().getLength() > 0));
    }

}
