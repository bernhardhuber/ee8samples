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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 *
 * @author berni3
 */
public class XmlParsingTest {

    static final DocumentBuilderFactory DOCUMENT_BUILDER_FACTORY = DocumentBuilderFactory.newInstance();

    @Test
    public void helloXml() throws Exception {
        parseXmlFApi();
    }

    void parseXmlStandardApi() throws ParserConfigurationException, SAXException, IOException {
        final File f = new File("src/main/resources/META-INF/persistence.xml");
        final DocumentBuilder db = DOCUMENT_BUILDER_FACTORY.newDocumentBuilder();
        final Document document = db.parse(f);
        assertNotNull(document);
    }

    void parseXmlFApi() {
        final File f = new File("src/main/resources/META-INF/persistence.xml");

        final Document document = new DocumentBuilderF(new DocumentBuilderFactoryF(DOCUMENT_BUILDER_FACTORY).newDocumentBuilder())
                .transformTo(DocumentBuilderF.parse(f));
        assertNotNull(document);
    }

    static class DocumentBuilderFactoryF {

        final DocumentBuilderFactory dbf;

        public DocumentBuilderFactoryF(DocumentBuilderFactory dbf) {
            this.dbf = dbf;
        }

        void consume(Consumer<DocumentBuilderFactory> c) {
            c.accept(dbf);
        }

        <T> T get(Function<DocumentBuilderFactory, T> f) {
            return f.apply(dbf);
        }

        DocumentBuilder newDocumentBuilder() {
            try {
                return dbf.newDocumentBuilder();
            } catch (ParserConfigurationException ex) {
                throw new RuntimeException("newDocumentBuilder", ex);
            }
        }
    }

    static class DocumentBuilderF {

        private final DocumentBuilder db;

        public DocumentBuilderF(DocumentBuilder db) {
            this.db = db;
        }

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

        static EntityResolver creatEntityResolver(BiFunction<String, String, InputSource> resolveEntity) {
            EntityResolver er = new EntityResolver() {
                @Override
                public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
                    InputSource is = resolveEntity.apply(systemId, publicId);
                    return is;
                }
            };
            return er;
        }

        static Consumer<DocumentBuilder> errorHandler(ErrorHandler eh) {
            return db -> {
                db.setErrorHandler(eh);
            };
        }

        static ErrorHandler createErrorHandler(
                Consumer<SAXParseException> warning,
                Consumer<SAXParseException> error,
                Consumer<SAXParseException> fatalError) {
            ErrorHandler eh = new ErrorHandler() {
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

        Document transformTo(Function<DocumentBuilder, Document> f) {
            return f.apply(db);
        }

        static Function<DocumentBuilder, Document> parse(File f) {
            return (db) -> {
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
            return (db) -> {
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
            return (db) -> {
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
            return (db) -> {
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
            return (db) -> {
                try {
                    Document document = db.parse(is, systemId);
                    return document;
                } catch (IOException | SAXException ex) {
                    String m = String.format("parse inputstream: [%s], systemId: [%s]", is, systemId);
                    throw new RuntimeException(m, ex);
                }
            };
        }

        <T> T get(Function<DocumentBuilder, T> f) {
            return f.apply(db);
        }

        static Function<DocumentBuilder, DocumentBuilderInfo> documentBuilderInfo() {
            return (db) -> {
                final DocumentBuilderInfo dbi = new DocumentBuilderInfo(
                        db.getDOMImplementation(),
                        db.getSchema(),
                        db.isNamespaceAware(),
                        db.isValidating(),
                        db.isXIncludeAware());
                return dbi;
            };
        }

        static class DocumentBuilderInfo {

            public final DOMImplementation domImplementation;
            public final Schema schema;
            public final boolean isNamespaceAware,
                    isValidating,
                    isXIncludeAware;

            public DocumentBuilderInfo(DOMImplementation domImplementation, Schema schema,
                    boolean isNamespaceAware, boolean isValidating, boolean isXIncludeAware) {
                this.domImplementation = domImplementation;
                this.schema = schema;
                this.isNamespaceAware = isNamespaceAware;
                this.isValidating = isValidating;
                this.isXIncludeAware = isXIncludeAware;
            }
        }

    }

}
