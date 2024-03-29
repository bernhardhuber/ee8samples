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
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import org.huberb.ee8sample.mail.BodyTextMergers.SimpleSubstitutionBodyMerger;
import org.huberb.ee8sample.mail.MimeMessageF;
import org.huberb.ee8sample.mail.SessionF;
import org.huberb.ee8sample.mail.Supports.ConsumerThrowingMessagingException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author berni3
 */
public class SimpleSubstitutionBodyMergerTest {

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
    public void testSimpleSubstitutionBodyMergerAssignBodyText() throws MessagingException, IOException {
        final MimeMessage mimeMessage = createAMimeMessage();
        //---
        Map<String, Object> map = new HashMap<>() {
            {
                put("name", "world");
            }
        };
        final String template = "Hello @name@,\r\n"
                + "body text.\r\n";
        final ConsumerThrowingMessagingException<MimeMessage> assignBodyTextConsumer = SimpleSubstitutionBodyMerger.assignBodyText(
                template,
                map
        );
        assignBodyTextConsumer.accept(mimeMessage);
        assertNormalized("Hello world, body text. ", (String) mimeMessage.getContent());
    }

    @Test
    public void testSimpleSubstitutionBodyMergerAssignBodyText_badTemplate() throws MessagingException, IOException {
        final MimeMessage mimeMessage = createAMimeMessage();
        //---
        Map<String, Object> map = new HashMap<>() {
            {
                put("name", "world");
            }
        };
        final String template = "Hello @name@, @hello@, @world@\r\n"
                + "body text.\r\n";
        final ConsumerThrowingMessagingException<MimeMessage> assignBodyTextConsumer = SimpleSubstitutionBodyMerger.assignBodyText(
                template,
                map
        );
        assignBodyTextConsumer.accept(mimeMessage);
        assertNormalized("Hello world, ,  body text. ", (String) mimeMessage.getContent());
    }
    @Test
    public void testSimpleSubstitutionBodyMergerAssignBodyText_badTemplate2() throws MessagingException, IOException {
        final MimeMessage mimeMessage = createAMimeMessage();
        //---
        Map<String, Object> map = new HashMap<>() {
            {
                put("name", "world");
            }
        };
        final String template = "Hello @name\r\n"
                + "body text.\r\n";
        final ConsumerThrowingMessagingException<MimeMessage> assignBodyTextConsumer = SimpleSubstitutionBodyMerger.assignBodyText(
                template,
                map
        );
        assignBodyTextConsumer.accept(mimeMessage);
        assertNormalized("Hello ", (String) mimeMessage.getContent());
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
