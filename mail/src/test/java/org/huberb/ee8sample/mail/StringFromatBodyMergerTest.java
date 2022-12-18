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
import java.util.Properties;
import java.util.function.Consumer;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import org.huberb.ee8sample.mail.MailsF.MimeMessageF;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author berni3
 */
public class StringFromatBodyMergerTest {

    Session session;

    @BeforeEach
    public void setUp() {
        Properties props = new Properties();
        session = Session.getInstance(props);
        session.setDebug(true);
        assertNotNull(session);
    }

    @Test
    public void hello1() throws MessagingException {
        assertNotNull(session);

        MimeMessageF messageF = new MimeMessageF(session);
        Consumer<MimeMessage> c = MimeMessageF.Consumers.from("me@localhost")
                .andThen(MimeMessageF.Consumers.recipient(RecipientType.TO, "me@localhost"))
                .andThen(MimeMessageF.Consumers.subject("subject"))
                .andThen(MimeMessageF.Consumers.text("text"));

        messageF.consume(c);
        Message m = messageF.getMimeMessage();
        assertNotNull(m);

        assertEquals("subject", m.getSubject());
        assertEquals("me@localhost", m.getFrom()[0].toString());
        assertEquals("me@localhost", m.getRecipients(RecipientType.TO)[0].toString());
    }

    @Test
    public void hello2() throws MessagingException, IOException {
        assertNotNull(session);

        MimeMessageF messageF = new MimeMessageF(session);
        Consumer<MimeMessage> c = MimeMessageF.Consumers.from("me@localhost")
                .andThen(MimeMessageF.Consumers.recipient(RecipientType.TO, "me@localhost"))
                .andThen(MimeMessageF.Consumers.subject("subject"))
                .andThen(MimeMessageF.Consumers.text("text"));

        messageF.consume(c);
        Message m = messageF.getMimeMessage();
        assertNotNull(m);

        assertEquals("subject", m.getSubject());
        assertEquals("text", m.getContent());
        assertEquals("me@localhost", m.getFrom()[0].toString());
        assertEquals("me@localhost", m.getRecipients(RecipientType.TO)[0].toString());

        Consumer<MimeMessage> c1 = BodyMergers.StringFromatBodyMerger.c1("Hello,%n"
                + "body text.%n");
        c1.accept((MimeMessage) m);
        assertNormalized("Hello, body text. ", (String)m.getContent());
    }

    void assertNormalized(String exp, String res) {
        String expNorm = normalie(exp);
        String resNorm = normalie(res);
        assertEquals(expNorm, resNorm);
    }

    String normalie(String s) {
        String result = s.replace("\r\n", " ")
                .replace('\r', ' ')
                .replace('\n', ' ');
        return result;
    }
}
