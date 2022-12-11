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
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 *
 * @author berni3
 */
public class HtmlParsingTest {

    @Test
    public void given_html_then_no_src_attribute_references_http_url() throws Exception {
        final File start = new File("src/main/webapp");
        final List<Path> l = FindFiles.findTheFilesReturningList(start, ".html");
        assertTrue(l.size() > 0L);
        l.forEach((Path p) -> {
            final File fFromP = p.toFile();
            final Document document = parseToJsoupHtmlDocument(fFromP);
            assertAttrValueDoesNot("src", "http").accept(document);
        });
    }

    @Test
    public void given_html_then_attributes_are_unique() throws Exception {
        final File start = new File("src/main/webapp");
        final List<Path> l = FindFiles.findTheFilesReturningList(start, ".html");
        assertTrue(l.size() > 0L);
        l.forEach((Path p) -> {
            final File fFromP = p.toFile();
            final Document document = parseToJsoupHtmlDocument(fFromP);
            assertAttrValueUnique("id").accept(document);
            assertAttrValueUnique("src").accept(document);
            assertAttrValueUnique("name").accept(document);
        });
    }

    static Document parseToJsoupHtmlDocument(File f) {
        try {
            Document document = Jsoup.parse(f, "UTF-8");
            assertNotNull(document);
            assertFalse(document.hasParent());
            assertTrue(document.hasText());
            return document;
        } catch (IOException ex) {
            throw new RuntimeException(String.format("parseToJsoupHtmlDocument file [%s]", f), ex);
        }
    }

    static Consumer<Document> assertAttrValueDoesNot(String attributeName, String doesNotContain) {
        return (Document document) -> {
            final Elements elements = document.getElementsByAttribute(attributeName);
            elements.forEach(e -> {
                final String n = e.normalName();
                final String attrV = e.attr(attributeName);
                final Supplier<String> mSupp = () -> String.format("node [%s], attribute [%s: '%s']", n, attributeName, attrV);
                assertFalse(attrV.contains(doesNotContain), mSupp);
            });
        };
    }

    static Consumer<Document> assertAttrValueUnique(String attributeName) {
        return (Document document) -> {
            final Set<String> synthKeyList = new HashSet<>();
            final Set<String> dupSynthKeyList = new HashSet<>();
            final Elements elements = document.getElementsByAttribute(attributeName);
            elements.forEach(e -> {
                final String n = e.normalName();
                final String attrV = e.attr(attributeName);

                final String synthKey = n + "_" + attributeName + "_" + attrV;
                if (synthKeyList.contains(synthKey)) {
                    dupSynthKeyList.add(synthKey);
                } else {
                    synthKeyList.add(synthKey);
                }

                final Supplier<String> mSupp = () -> String.format("node [%s], attribute [%s], dups [%s]", n, attributeName, dupSynthKeyList);
                assertTrue(dupSynthKeyList.isEmpty(), mSupp);
            });
        };
    }
}
