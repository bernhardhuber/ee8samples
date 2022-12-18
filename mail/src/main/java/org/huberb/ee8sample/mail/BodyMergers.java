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

import java.util.Optional;
import java.util.function.Consumer;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author berni3
 */
public class BodyMergers {

    static class StringFromatBodyMerger {

        private StringFromatBodyMerger() {
        }

        static Consumer<MimeMessage> c1(String template) {
            return (mm) -> {
                try {
                    String bodyText = merge(mm, template);
                    mm.setText(bodyText);
                } catch (MessagingException ex) {
                    throw new RuntimeException("merge", ex);
                }
            };
        }

        static String merge(MimeMessage mm, String template) throws MessagingException {
            final String subject = Optional.ofNullable(mm).map(e -> {
                try {
                    return e.getSubject();
                } catch (MessagingException ex) {
                    throw new RuntimeException("subject", ex);
                }
            }).orElse("");
            final String from = Optional.ofNullable(mm).map(e -> {
                try {
                    return e.getFrom();
                } catch (MessagingException ex) {
                    throw new RuntimeException("from", ex);
                }
            })
                    .map(e -> e[0])
                    .map(e -> e.toString())
                    .orElse("");
            final String to = Optional.ofNullable(mm).map(e -> {
                try {
                    return e.getRecipients(RecipientType.TO);
                } catch (MessagingException ex) {
                    throw new RuntimeException("from", ex);
                }
            })
                    .map(e -> e[0])
                    .map(e -> e.toString())
                    .orElse("");

            final String result = String.format(template,
                    subject,
                    from,
                    to
            );
            return result;
        }
    }
}
