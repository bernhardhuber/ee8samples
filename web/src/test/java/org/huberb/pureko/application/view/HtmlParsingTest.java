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
import java.util.List;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.jsoup.Jsoup;
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

// 
//
//    static class DocumentBuilderFactoryF {
//
//        final DocumentBuilderFactory dbf;
//
//        public DocumentBuilderFactoryF(DocumentBuilderFactory dbf) {
//            this.dbf = dbf;
//        }
//
//        void consume(Consumer<DocumentBuilderFactory> c) {
//            c.accept(dbf);
//        }
//
//        <T> T get(Function<DocumentBuilderFactory, T> f) {
//            return f.apply(dbf);
//        }
//
//        DocumentBuilder newDocumentBuilder() {
//            try {
//                return dbf.newDocumentBuilder();
//            } catch (ParserConfigurationException ex) {
//                throw new RuntimeException("newDocumentBuilder", ex);
//            }
//        }
//    }
//
//    static class DocumentBuilderF {
//
//        private final DocumentBuilder db;
//
//        public DocumentBuilderF(DocumentBuilder db) {
//            this.db = db;
//        }
//
//        void set(Consumer<DocumentBuilder> c) {
//            c.accept(db);
//        }
//
//        static Consumer<DocumentBuilder> reset() {
//            return db -> {
//                db.reset();
//            };
//        }
//
//        static Consumer<DocumentBuilder> entityResolver(EntityResolver er) {
//            return db -> {
//                db.setEntityResolver(er);
//            };
//        }
//
//        static EntityResolver creatEntityResolver(BiFunction<String, String, InputSource> resolveEntity) {
//            EntityResolver er = new EntityResolver() {
//                @Override
//                public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
//                    InputSource is = resolveEntity.apply(systemId, publicId);
//                    return is;
//                }
//            };
//            return er;
//        }
//
//        static Consumer<DocumentBuilder> errorHandler(ErrorHandler eh) {
//            return db -> {
//                db.setErrorHandler(eh);
//            };
//        }
//
//        static ErrorHandler createErrorHandler(
//                Consumer<SAXParseException> warning,
//                Consumer<SAXParseException> error,
//                Consumer<SAXParseException> fatalError) {
//            ErrorHandler eh = new ErrorHandler() {
//                @Override
//                public void warning(SAXParseException exception) throws SAXException {
//                    warning.accept(exception);
//
//                }
//
//                @Override
//                public void error(SAXParseException exception) throws SAXException {
//                    error.accept(exception);
//                }
//
//                @Override
//                public void fatalError(SAXParseException exception) throws SAXException {
//                    fatalError.accept(exception);
//                }
//            };
//            return eh;
//        }
//
//        Document transformTo(Function<DocumentBuilder, Document> f) {
//            return f.apply(db);
//        }
//
//        static Function<DocumentBuilder, Document> parse(File f) {
//            return (db) -> {
//                try {
//                    Document document = db.parse(f);
//                    return document;
//                } catch (IOException | SAXException ex) {
//                    String m = String.format("parse file: [%s]", f);
//                    throw new RuntimeException(m, ex);
//                }
//            };
//        }
//
//        static Function<DocumentBuilder, Document> parse(InputSource is) {
//            return (db) -> {
//                try {
//                    Document document = db.parse(is);
//                    return document;
//                } catch (IOException | SAXException ex) {
//                    String m = String.format("parse inputsource: [%s]", is);
//                    throw new RuntimeException(m, ex);
//                }
//            };
//        }
//
//        static Function<DocumentBuilder, Document> parse(InputStream is) {
//            return (db) -> {
//                try {
//                    Document document = db.parse(is);
//                    return document;
//                } catch (IOException | SAXException ex) {
//                    String m = String.format("parse inputstream: [%s]", is);
//                    throw new RuntimeException(m, ex);
//                }
//            };
//        }
//
//        static Function<DocumentBuilder, Document> parse(String uri) {
//            return (db) -> {
//                try {
//                    Document document = db.parse(uri);
//                    return document;
//                } catch (IOException | SAXException ex) {
//                    String m = String.format("parse uri: [%s]", uri);
//                    throw new RuntimeException(m, ex);
//                }
//            };
//        }
//
//        static Function<DocumentBuilder, Document> parse(InputStream is, String systemId) {
//            return (db) -> {
//                try {
//                    Document document = db.parse(is, systemId);
//                    return document;
//                } catch (IOException | SAXException ex) {
//                    String m = String.format("parse inputstream: [%s], systemId: [%s]", is, systemId);
//                    throw new RuntimeException(m, ex);
//                }
//            };
//        }
//
//        <T> T get(Function<DocumentBuilder, T> f) {
//            return f.apply(db);
//        }
//
//        static Function<DocumentBuilder, DocumentBuilderInfo> documentBuilderInfo() {
//            return (db) -> {
//                final DocumentBuilderInfo dbi = new DocumentBuilderInfo(
//                        db.getDOMImplementation(),
//                        db.getSchema(),
//                        db.isNamespaceAware(),
//                        db.isValidating(),
//                        db.isXIncludeAware());
//                return dbi;
//            };
//        }
//
//        static class DocumentBuilderInfo {
//
//            public final DOMImplementation domImplementation;
//            public final Schema schema;
//            public final boolean isNamespaceAware,
//                    isValidating,
//                    isXIncludeAware;
//
//            public DocumentBuilderInfo(DOMImplementation domImplementation, Schema schema,
//                    boolean isNamespaceAware, boolean isValidating, boolean isXIncludeAware) {
//                this.domImplementation = domImplementation;
//                this.schema = schema;
//                this.isNamespaceAware = isNamespaceAware;
//                this.isValidating = isValidating;
//                this.isXIncludeAware = isXIncludeAware;
//            }
//        }
//
//    }
    static class FindFiles {

        static List<Path> findTheFilesReturningList(File start, String matchingExt) throws IOException {
            Stream<Path> result = findTheFilesReturningStream(start, matchingExt);
            List<Path> l = result.collect(Collectors.toList());
            return l;
        }

        static Stream<Path> findTheFilesReturningStream(File start, String matchingExt) throws IOException {
            BiPredicate<Path, BasicFileAttributes> matcher = (p, bfa) -> {
                return bfa.isRegularFile()
                        && p.toFile().getName().endsWith(matchingExt);
            };
            FileVisitOption[] options = new FileVisitOption[0];
            Stream<Path> result = Files.find(start.toPath(), 10, matcher, options);
            return result;
        }
    }

    @Test
    public void helloHtml() throws Exception {
        final File start = new File("src/main/webapp");
        final List<Path> l = FindFiles.findTheFilesReturningList(start, ".html");
        assertTrue(l.size() > 0L);
        l.forEach((Path p) -> {
            final File fFromP = p.toFile();
            org.jsoup.nodes.Document document = parseToJsoupHtmlDocument(fFromP);
            assertAttrValueDoesNot(document, "src", "http");
        });
    }

    static org.jsoup.nodes.Document parseToJsoupHtmlDocument(File f) {
        //File f = new File("src/main/webapp/index.html");
        try {
            org.jsoup.nodes.Document document = Jsoup.parse(f, "UTF-8");
            assertNotNull(document);
            assertFalse(document.hasParent());
            assertTrue(document.hasText());
            return document;
        } catch (IOException ex) {
            throw new RuntimeException(String.format("parseToJsoupHtmlDocument file [%s]", f), ex);
        }
    }

    static void assertAttrValueDoesNot(org.jsoup.nodes.Document document, String attributeName, String doesNotContain) {
        Elements elements = document.getElementsByAttribute(attributeName);
        elements.forEach(e -> {
            final String n = e.normalName();
            final String attrV = e.attr(attributeName);
            String m = String.format("node [%s], attribute [%s: '%s']", n, attributeName, attrV);
            assertFalse(attrV.contains(doesNotContain), m);
        });
    }
}
