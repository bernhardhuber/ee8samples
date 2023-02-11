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
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import javax.mail.internet.MimeMessage;
import org.huberb.ee8sample.mail.Supports.ConsumerThrowingMessagingException;
import org.huberb.ee8sample.mail.Supports.MailRuntimeException;

/**
 * Some very simple text template mergers.
 *
 * @author berni3
 */
public class BodyTextMergers {

    /**
     * Create a consumer for setting mime-message text.
     *
     * @param bodyText new mail body text
     * @return consumer
     */
    public static ConsumerThrowingMessagingException<MimeMessage> assignBodyText(String bodyText) {
        return (mm) -> mm.setText(bodyText);
    }

    /**
     * Merger using {@link String#format(java.lang.String, java.lang.Object...)
     * } internally for merging.
     */
    public static class StringFormatBodyMerger {

        /**
         * Create a consumer for setting mime-message text.
         *
         * @param template text template
         * @param args for the text template
         * @return consumer
         */
        public static ConsumerThrowingMessagingException<MimeMessage> assignBodyText(String template, Object[] args) {
            return StringFormatBodyMerger.assignBodyText(Locale.getDefault(), template, args);
        }

        /**
         * Create a consumer for setting mime-message text.
         *
         * @param locale used for merging args into template
         * @param template text template
         * @param args for the text template
         * @return consumer
         */
        public static ConsumerThrowingMessagingException<MimeMessage> assignBodyText(Locale locale, String template, Object[] args) {
            final StringFormatBodyMerger merger = new StringFormatBodyMerger(locale);
            final String bodyText = merger.merge(template, args);
            return BodyTextMergers.assignBodyText(bodyText);
        }

        private final Locale locale;

        /**
         * Create instance with locale {@link Locale#getDefault()}.
         */
        public StringFormatBodyMerger() {
            this(Locale.getDefault());
        }

        /**
         * Create instance with given {@link Locale} instance.
         *
         * @param locale locale used for merging args into template
         */
        public StringFormatBodyMerger(Locale locale) {
            this.locale = locale;
        }

        /**
         * Merge template with given arguments.
         *
         * @param template the template
         * @param arguments the arguments for the template
         * @return formatted {@link String}
         * @throws MailRuntimeException if merge fails.
         *
         * @see String#format(java.util.Locale, java.lang.String,
         * java.lang.Object...)
         */
        public String merge(String template, Object[] arguments) {
            try {
                return String.format(locale, template, arguments);
            } catch (IllegalArgumentException iaex) {
                final String m = String.format("merge: template: '%s', arguments: '%s'", template, Arrays.toString(arguments));
                throw new MailRuntimeException(m, iaex);
            }
        }
    }

    /**
     * Merger using pattern "@var@", and providing values of "var" as a Map.
     */
    public static class SimpleSubstitutionBodyMerger {

        /**
         * Create a consumer for setting mime-message text.
         *
         * @param template text template containing value-markers
         * @param m map holding substitution values
         * @return consumer
         */
        public static ConsumerThrowingMessagingException<MimeMessage> assignBodyText(String template, Map<String, Object> m) {
            final SimpleSubstitutionBodyMerger merger = new SimpleSubstitutionBodyMerger();
            final String bodyText = merger.merge(template, m);
            return BodyTextMergers.assignBodyText(bodyText);
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

        /**
         * Merge template with given arguments represented as {@link Map}.
         *
         * @param template text template containing value-markers
         * @param m map holding substitution values
         * @return result merging template and values
         *
         * @throws MailRuntimeException if merge fails.
         */
        public String merge(String template, Map<String, Object> m) {
            try {
                return process(template, m);
            } catch (IOException ioex) {
                throw new MailRuntimeException("merge", ioex);
            }
        }

        String process(String template, Map<String, Object> m) throws IOException {
            final StringBuilder sb = new StringBuilder();

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
         * This finds the value from Map, or somewhere.
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

    /**
     * Merger using {@link MessageFormat#format} internally for merging.
     *
     * @see MessageFormat
     * @see MessageFormat#format
     */
    public static class MessageFormatBodyMerger {

        /**
         * Create a consumer for setting mime-message text.
         *
         * @param template {@link MessageFormat} pattern
         * @param arguments arguments for the template
         * @return consumer
         *
         */
        public static ConsumerThrowingMessagingException<MimeMessage> assignBodyText(String template, Object[] arguments) {
            return assignBodyText(Locale.getDefault(), template, arguments);
        }

        /**
         * Create a consumer for setting mime-message text.
         *
         * @param locale locale used in merging
         * @param template {@link MessageFormat} pattern
         * @param arguments arguments for the template
         * @return consumer
         *
         */
        public static ConsumerThrowingMessagingException<MimeMessage> assignBodyText(Locale locale, String template, Object[] arguments) {
            MessageFormatBodyMerger messageFormatBodyMerger = new MessageFormatBodyMerger(locale);
            final String bodyText = messageFormatBodyMerger.merge(template, arguments);
            return BodyTextMergers.assignBodyText(bodyText);
        }

        private final Locale locale;

        public MessageFormatBodyMerger() {
            this(Locale.getDefault());
        }

        public MessageFormatBodyMerger(Locale locale) {
            this.locale = locale;
        }

        /**
         * Merge template with given arguments.
         *
         * @param template {@link MessageFormat} pattern
         * @param arguments arguments for the template
         * @return merged template with arguments
         * @see MessageFormat#format(java.lang.Object)
         */
        public String merge(String template, Object[] arguments) {
            try {
                final MessageFormat messageFormat = new MessageFormat(template, locale);
                return messageFormat.format(arguments);
            } catch (IllegalArgumentException iaex) {
                final String m = String.format("merge: template: '%s', arguments: '%s'", template, Arrays.toString(arguments));
                throw new MailRuntimeException(m, iaex);
            }
        }
    }
}
