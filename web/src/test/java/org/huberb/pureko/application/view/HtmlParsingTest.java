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
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
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

    /**
     * Find files.
     */
    static class FindFiles {

        /**
         * Find regular files matching a given extension.
         *
         * @param start base directory
         * @param matchingExt required matched extension
         * @return
         * @throws IOException
         *
         * @see Files#find(java.nio.file.Path, int,
         * java.util.function.BiPredicate, java.nio.file.FileVisitOption...)
         */
        static List<Path> findTheFilesReturningList(File start, String matchingExt) throws IOException {
            Stream<Path> result = findTheFilesReturningStream(start, matchingExt);
            List<Path> l = result.collect(Collectors.toList());
            return l;
        }

        /**
         * Find regular files matching a given extension.
         *
         * @param start base directory
         * @param matchingExt required matched extension
         * @return lazy stream of {@link Path}
         * @throws IOException
         *
         * @see Files#find(java.nio.file.Path, int,
         * java.util.function.BiPredicate, java.nio.file.FileVisitOption...)
         */
        static Stream<Path> findTheFilesReturningStream(File start, String matchingExt) throws IOException {
            BiPredicate<Path, BasicFileAttributes> matcher = (p, bfa) -> {
                return bfa.isRegularFile()
                        && p.toFile().getName().endsWith(matchingExt);
            };
            final FileVisitOption[] options = new FileVisitOption[0];
            final int maxDepth = 10;
            Stream<Path> result = Files.find(start.toPath(), maxDepth, matcher, options);
            return result;
        }
    }

    @Test
    public void given_html_then_no_src_attribute_references_http_url() throws Exception {
        final File start = new File("src/main/webapp");
        final List<Path> l = FindFiles.findTheFilesReturningList(start, ".html");
        assertTrue(l.size() > 0L);
        l.forEach((Path p) -> {
            final File fFromP = p.toFile();
            Document document = parseToJsoupHtmlDocument(fFromP);
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
            Document document = parseToJsoupHtmlDocument(fFromP);
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
            Elements elements = document.getElementsByAttribute(attributeName);
            elements.forEach(e -> {
                final String n = e.normalName();
                final String attrV = e.attr(attributeName);
                String m = String.format("node [%s], attribute [%s: '%s']", n, attributeName, attrV);
                assertFalse(attrV.contains(doesNotContain), m);
            });
        };
    }

    static Consumer<Document> assertAttrValueUnique(String attributeName) {
        return (Document document) -> {
            Set<String> synthKeyList = new HashSet<>();
            Set<String> dupSynthKeyList = new HashSet<>();
            Elements elements = document.getElementsByAttribute(attributeName);
            elements.forEach(e -> {
                final String n = e.normalName();
                final String attrV = e.attr(attributeName);

                String synthKey = n + "_" + attrV;
                if (synthKeyList.contains(synthKey)) {
                    dupSynthKeyList.add(synthKey);
                } else {
                    synthKeyList.add(synthKey);
                }

                String m = String.format("node [%s], attribute [%s], dups [%s]", n, attributeName, dupSynthKeyList);
                assertTrue(dupSynthKeyList.isEmpty(), m);
            });
        };
    }
}
