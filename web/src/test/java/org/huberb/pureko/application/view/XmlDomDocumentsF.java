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
import java.io.InputStream;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Define a thin functional style layer for parsing xml documents to
 * {@link Document}.
 *
 */
class XmlDomDocumentsF {

    /**
     * Functional layer over {@link DocumentBuilderFactory}.
     */
    static class DocumentBuilderFactoryF {

        final DocumentBuilderFactory dbf;

        public DocumentBuilderFactoryF(DocumentBuilderFactory dbf) {
            this.dbf = dbf;
        }

        DocumentBuilderFactory getDocumentBuilderFactory() {
            return this.dbf;
        }

        //---
        void set(Consumer<DocumentBuilderFactory> c) {
            c.accept(dbf);
        }

        static Consumer<DocumentBuilderFactory> attribute(String name, Object value) {
            return dbf -> dbf.setAttribute(name, value);
        }

        static Consumer<DocumentBuilderFactory> feature(String name, boolean value) {
            return dbf -> {
                try {
                    dbf.setFeature(name, value);
                } catch (ParserConfigurationException ex) {
                    String m = String.format("setting feature [%s] = [%s]", name, value);
                    throw new RuntimeException(m, ex);
                }
            };
        }

        static Consumer<DocumentBuilderFactory> coalescing(boolean value) {
            return dbf -> {
                dbf.setCoalescing(value);
            };
        }

        static Consumer<DocumentBuilderFactory> expandEntityReferences(boolean value) {
            return dbf -> {
                dbf.setExpandEntityReferences(value);
            };
        }

        static Consumer<DocumentBuilderFactory> ignoringComments(boolean value) {
            return dbf -> {
                dbf.setIgnoringComments(value);
            };
        }

        static Consumer<DocumentBuilderFactory> ignoringElementContentWhitespace(boolean value) {
            return dbf -> {
                dbf.setIgnoringElementContentWhitespace(value);
            };
        }

        static Consumer<DocumentBuilderFactory> namespaceAware(boolean value) {
            return dbf -> {
                dbf.setNamespaceAware(value);
            };
        }

        static Consumer<DocumentBuilderFactory> schema(Schema value) {
            return dbf -> {
                dbf.setSchema(value);
            };
        }

        static Consumer<DocumentBuilderFactory> validating(boolean value) {
            return dbf -> {
                dbf.setValidating(value);
            };
        }

        static Consumer<DocumentBuilderFactory> xincludeAware(boolean value) {
            return dbf -> {
                dbf.setXIncludeAware(value);
            };
        }

        //---
        <T> T get(Function<DocumentBuilderFactory, T> f) {
            return f.apply(dbf);
        }

        static Function<DocumentBuilderFactory, Object> attribute(String name) {
            return dbf -> dbf.getAttribute(name);
        }

        static Function<DocumentBuilderFactory, Boolean> feature(String name) {
            return dbf -> {
                try {
                    return dbf.getFeature(name);
                } catch (ParserConfigurationException ex) {
                    String m = String.format("getting feature [%s]", name);
                    throw new RuntimeException(m, ex);
                }
            };
        }

        static Function<DocumentBuilderFactory, Boolean> coalescing() {
            return dbf -> dbf.isCoalescing();
        }

        static Function<DocumentBuilderFactory, Boolean> expandEntityReferences() {
            return dbf -> dbf.isExpandEntityReferences();
        }

        static Function<DocumentBuilderFactory, Boolean> ignoringComments() {
            return dbf -> dbf.isIgnoringComments();
        }

        static Function<DocumentBuilderFactory, Boolean> ignoringElementContentWhitespace() {
            return dbf -> dbf.isIgnoringElementContentWhitespace();
        }

        static Function<DocumentBuilderFactory, Boolean> namespaceAware() {
            return dbf -> dbf.isNamespaceAware();
        }

        static Function<DocumentBuilderFactory, Schema> schema() {
            return dbf -> dbf.getSchema();
        }

        static Function<DocumentBuilderFactory, Boolean> validating() {
            return dbf -> dbf.isValidating();
        }

        static Function<DocumentBuilderFactory, Boolean> xincludeAware() {
            return dbf -> dbf.isXIncludeAware();
        }

        //---
        DocumentBuilder newDocumentBuilder() {
            try {
                return dbf.newDocumentBuilder();
            } catch (ParserConfigurationException ex) {
                throw new RuntimeException("newDocumentBuilder", ex);
            }
        }
    }

    /**
     * Functional layer over {@link DocumentBuilder}.
     */
    static class DocumentBuilderF {

        private final DocumentBuilder db;

        public DocumentBuilderF(DocumentBuilder db) {
            this.db = db;
        }

        //---
        void set(Consumer<DocumentBuilder> c) {
            c.accept(db);
        }

        static Consumer<DocumentBuilder> reset() {
            return db -> {
                db.reset();
            };
        }

        static Consumer<DocumentBuilder> entityResolver(EntityResolver er) {
            return db -> {
                db.setEntityResolver(er);
            };
        }

        static EntityResolver createEntityResolver(BiFunction<String, String, InputSource> resolveEntity) {
            final EntityResolver er = new EntityResolver() {
                @Override
                public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
                    InputSource is = resolveEntity.apply(systemId, publicId);
                    return is;
                }
            };
            return er;
        }

        //---
        static Consumer<DocumentBuilder> errorHandler(ErrorHandler eh) {
            return db -> {
                db.setErrorHandler(eh);
            };
        }

        static ErrorHandler createErrorHandler(Consumer<SAXParseException> warning, Consumer<SAXParseException> error, Consumer<SAXParseException> fatalError) {
            final ErrorHandler eh = new ErrorHandler() {
                @Override
                public void warning(SAXParseException exception) throws SAXException {
                    warning.accept(exception);
                }

                @Override
                public void error(SAXParseException exception) throws SAXException {
                    error.accept(exception);
                }

                @Override
                public void fatalError(SAXParseException exception) throws SAXException {
                    fatalError.accept(exception);
                }
            };
            return eh;
        }

        //---
        Document transformTo(Function<DocumentBuilder, Document> f) {
            return f.apply(db);
        }

        static Function<DocumentBuilder, Document> parse(File f) {
            return db -> {
                try {
                    Document document = db.parse(f);
                    return document;
                } catch (IOException | SAXException ex) {
                    String m = String.format("parse file: [%s]", f);
                    throw new RuntimeException(m, ex);
                }
            };
        }

        static Function<DocumentBuilder, Document> parse(InputSource is) {
            return db -> {
                try {
                    Document document = db.parse(is);
                    return document;
                } catch (IOException | SAXException ex) {
                    String m = String.format("parse inputsource: [%s]", is);
                    throw new RuntimeException(m, ex);
                }
            };
        }

        static Function<DocumentBuilder, Document> parse(InputStream is) {
            return db -> {
                try {
                    Document document = db.parse(is);
                    return document;
                } catch (IOException | SAXException ex) {
                    String m = String.format("parse inputstream: [%s]", is);
                    throw new RuntimeException(m, ex);
                }
            };
        }

        static Function<DocumentBuilder, Document> parse(String uri) {
            return db -> {
                try {
                    Document document = db.parse(uri);
                    return document;
                } catch (IOException | SAXException ex) {
                    String m = String.format("parse uri: [%s]", uri);
                    throw new RuntimeException(m, ex);
                }
            };
        }

        static Function<DocumentBuilder, Document> parse(InputStream is, String systemId) {
            return db -> {
                try {
                    Document document = db.parse(is, systemId);
                    return document;
                } catch (IOException | SAXException ex) {
                    String m = String.format("parse inputstream: [%s], systemId: [%s]", is, systemId);
                    throw new RuntimeException(m, ex);
                }
            };
        }

        //---
        <T> T get(Function<DocumentBuilder, T> f) {
            return f.apply(db);
        }

        static Function<DocumentBuilder, DocumentBuilderInfo> documentBuilderInfo() {
            return db -> {
                final DocumentBuilderInfo dbi = new DocumentBuilderInfo(db.getDOMImplementation(), db.getSchema(), db.isNamespaceAware(), db.isValidating(), db.isXIncludeAware());
                return dbi;
            };
        }

        static class DocumentBuilderInfo {

            public final DOMImplementation domImplementation;
            public final Schema schema;
            public final boolean isNamespaceAware;
            public final boolean isValidating;
            public final boolean isXIncludeAware;

            public DocumentBuilderInfo(DOMImplementation domImplementation, Schema schema, boolean isNamespaceAware, boolean isValidating, boolean isXIncludeAware) {
                this.domImplementation = domImplementation;
                this.schema = schema;
                this.isNamespaceAware = isNamespaceAware;
                this.isValidating = isValidating;
                this.isXIncludeAware = isXIncludeAware;
            }
        }
    }

}
