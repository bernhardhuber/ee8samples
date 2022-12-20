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
package org.huberb.ee8sample.mail;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author berni3
 */
public class BodyTextMergers {

    /**
     * Merger using {@link String#format(java.lang.String, java.lang.Object...)
     * } internally for merging.
     */
    public static class StringFormatBodyMerger {

        private final Locale locale;

        public StringFormatBodyMerger() {
            this(Locale.getDefault());
        }

        public StringFormatBodyMerger(Locale l) {
            this.locale = l;
        }

        public static Consumer<MimeMessage> assignBodyText(String template, Object[] args) {
            return (mm) -> {
                try {
                    StringFormatBodyMerger merger = new StringFormatBodyMerger();
                    String bodyText = merger.merge(template, args);
                    mm.setText(bodyText);
                } catch (MessagingException ex) {
                    throw new RuntimeException("merge", ex);
                }
            };
        }

        public String merge(String template, Object[] args) {
            // TODO how to handle IllegalArgumentException?
            // maybe thrown by String#format
            final String result = String.format(locale, template, args);
            return result;
        }
    }

    /**
     * Merger using pattern "@var@", and providing values of "var" as a Map.
     */
    public static class SimpleSubstitutionBodyMerger {

        public static Consumer<MimeMessage> assignBodyText(String template, Map<String, Object> m) {
            return (mm) -> {
                try {
                    SimpleSubstitutionBodyMerger merger = new SimpleSubstitutionBodyMerger();
                    String bodyText = merger.merge(template, m);
                    mm.setText(bodyText);
                } catch (MessagingException ex) {
                    throw new RuntimeException("merge", ex);
                }
            };
        }

        private final int EOF = -1;
        private final char varDelimStart;
        private final char varDelimEnd;

        public SimpleSubstitutionBodyMerger() {
            this('@', '@');
        }

        public SimpleSubstitutionBodyMerger(char varDelimStart, char varDelimEnd) {
            this.varDelimStart = varDelimStart;
            this.varDelimEnd = varDelimEnd;

        }

        public String merge(String template, Map<String, Object> m) {
            try {
                return process(template, m);
            } catch (IOException ex) {
                throw new RuntimeException("merge", ex);
            }
        }

        String process(String template, Map<String, Object> m) throws IOException {
            final StringBuilder sb = new StringBuilder();

            // This can be easiliy FileReader or any Reader
            try (final Reader sr = new StringReader(template)) {
                for (int c; (c = sr.read()) != EOF;) {
                    if (c == varDelimStart) {
                        final String var = readVariable(sr);
                        final String val = getValue(m, var);
                        sb.append(val);
                    } else {
                        sb.append((char) c);
                    }
                }
            }
            return sb.toString();
        }

        private String readVariable(Reader sr) throws IOException {
            final StringBuilder nameSB = new StringBuilder();
            for (int c; (c = sr.read()) != EOF;) {
                if (c == varDelimEnd) {
                    break;
                }
                nameSB.append((char) c);
            }
            return nameSB.toString();
        }

        /**
         * This finds the value from Map, or somewhere
         */
        private String getValue(Map<String, Object> m, String var) {
            String strV = "";
            final Object v = m.getOrDefault(var, "");
            if (v != null) {
                strV = String.valueOf(v);
            }
            return strV;
        }
    }
}
