/*
 * Copyright 2023 berni3.
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
import java.util.Locale;
import java.util.Properties;
import java.util.stream.Stream;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import org.huberb.ee8sample.mail.BodyTextMergers.MessageFormatBodyMerger;
import org.huberb.ee8sample.mail.MimeMessageF;
import org.huberb.ee8sample.mail.SessionF;
import org.huberb.ee8sample.mail.Supports.ConsumerThrowingMessagingException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 *
 * @author berni3
 */
public class MessageFormatBodyMergerTest {

    Session session;

    @BeforeEach
    public void setUp() {
        Properties props = new Properties();
        session = Session.getInstance(props);
        session.setDebug(true);
        assertNotNull(session);
    }

    MimeMessage createAMimeMessage() throws MessagingException, IOException {

        ConsumerThrowingMessagingException<MimeMessage> c = MimeMessageF.Consumers.from("me@localhost")
                .andThen(MimeMessageF.Consumers.recipient(RecipientType.TO, "me@localhost"))
                .andThen(MimeMessageF.Consumers.subject("subject"))
                .andThen(MimeMessageF.Consumers.text("text"));

        final MimeMessage mimeMessage = SessionF.MimeMessages.mimeMessage().apply(session);
        c.accept(mimeMessage);
        assertNotNull(mimeMessage);
        assertEquals("subject", mimeMessage.getSubject());
        assertEquals("text", mimeMessage.getContent());
        assertEquals("me@localhost", mimeMessage.getFrom()[0].toString());
        assertEquals("me@localhost", mimeMessage.getRecipients(RecipientType.TO)[0].toString());
        return mimeMessage;
    }

    @Test
    public void testStringFormatBodyMergerAssignBodyText() throws MessagingException, IOException {
        final MimeMessage mimeMessage = createAMimeMessage();
        //---
        final String template = "Hello {0},\n"
                + "body text.\n";
        final ConsumerThrowingMessagingException<MimeMessage> assignBodyTextConsumer = MessageFormatBodyMerger.assignBodyText(
                template,
                new Object[]{"world"}
        );
        assignBodyTextConsumer.accept(mimeMessage);
        assertNormalized("Hello world, body text. ", (String) mimeMessage.getContent());
    }

    @Test
    public void testStringFormatBodyMergerAssignBodyText_badTemplate() throws MessagingException, IOException {
        final MimeMessage mimeMessage = createAMimeMessage();
        //---
        final String template = "Hello {1}\n"
                + "body text.\n";
        final ConsumerThrowingMessagingException<MimeMessage> assignBodyTextConsumer = MessageFormatBodyMerger.assignBodyText(
                template,
                new Object[]{}
        );
        assignBodyTextConsumer.accept(mimeMessage);
        assertNormalized("Hello {1} body text. ", (String) mimeMessage.getContent());
    }

    @ParameterizedTest
    @MethodSource("localeProvider")
    public void testStringFormatBodyMergerAssignBodyText_vary_locale(Locale locale) throws MessagingException, IOException {
        final MimeMessage mimeMessage = createAMimeMessage();
        //---
        final String template = "Hello {0},\n"
                + "body text.\n"
                + "From {1}";
        final ConsumerThrowingMessagingException<MimeMessage> assignBodyTextConsumer = MessageFormatBodyMerger.assignBodyText(
                locale,
                template,
                new Object[]{"world", "me"}
        );
        assignBodyTextConsumer.accept((MimeMessage) mimeMessage);
        assertNormalized("Hello world, body text. From me", (String) mimeMessage.getContent());
    }

    public static Stream<Locale> localeProvider() {
        return Stream.of(Locale.getDefault(), Locale.GERMAN, Locale.ENGLISH);
    }

    void assertNormalized(String exp, String res) {
        String expNorm = normalize(exp);
        String resNorm = normalize(res);
        assertEquals(expNorm, resNorm);
    }

    String normalize(String s) {
        String result = s.replace("\r\n", " ")
                .replace('\r', ' ')
                .replace('\n', ' ');
        assertTrue(result.length() <= s.length());
        return result;
    }
}
