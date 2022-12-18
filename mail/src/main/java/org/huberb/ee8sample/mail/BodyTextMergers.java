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
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author berni3
 */
public class BodyTextMergers {

    static class StringFormatBodyMerger {

        private StringFormatBodyMerger() {
        }

        static Consumer<MimeMessage> assignBodyText(String template, Object[] args) {
            return (mm) -> {
                try {
                    String bodyText = merge(template, args).get();
                    mm.setText(bodyText);
                } catch (MessagingException ex) {
                    throw new RuntimeException("merge", ex);
                }
            };
        }

        static Supplier<String> merge(String template, Object[] args) {
            return () -> {
                // TODO how to handle IllegalArgumentException?
                // maybe thrown by String#format
                final String result = String.format(template, args);
                return result;
            };
        }
    }

    static class SimpleSubstitutionBodyMerger {

        protected SimpleSubstitutionBodyMerger() {
        }

        static Consumer<MimeMessage> assignBodyText(String template, Map<String, Object> m) {
            return (mm) -> {
                try {
                    String bodyText = merge(template, m).get();
                    mm.setText(bodyText);
                } catch (MessagingException ex) {
                    throw new RuntimeException("merge", ex);
                }
            };
        }

        static Supplier<String> merge(String template, Map<String, Object> m) {
            return () -> {
                try {
                    return new SimpleSubstitutionBodyMerger().process(template, m);
                } catch (IOException ex) {
                    throw new RuntimeException("merge", ex);
                }
            };
        }

        final char varDelimStart = '@';
        final char varDelimEnd = '@';

        String process(String template, Map<String, Object> m) throws IOException {
            final StringBuilder sb = new StringBuilder();

            // This can be easiliy FileReader or any Reader
            try (final Reader sr = new StringReader(template)) {
                for (int c; (c = sr.read()) != -1;) {
                    if (c == varDelimStart) {
                        final String var = readVariable(sr);
                        final String val = getValue(m, var);
                        sb.append(val);
                    } else {
                        sb.append((char)c);
                    }
                }
            }
            return sb.toString();
        }

        private String readVariable(Reader sr) throws IOException {
            final StringBuilder nameSB = new StringBuilder();
            for (int c; (c = sr.read()) != -1;) {
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
