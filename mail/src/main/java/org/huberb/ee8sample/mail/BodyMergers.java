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

import java.util.function.Consumer;
import java.util.function.Supplier;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author berni3
 */
public class BodyMergers {

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
}
