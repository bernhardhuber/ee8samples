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

import java.util.Properties;
import javax.mail.Address;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.huberb.ee8sample.mail.Supports.ConsumerThrowingMessagingException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author berni3
 */
public class MimeMessageCreatingTest {

    Session session;

    @BeforeEach
    public void setUp() {
        Properties props = new Properties();
        session = Session.getInstance(props);
        session.setDebug(true);
        assertNotNull(session);
    }

    @Test
    public void testCreateInternetAddress() throws MessagingException {
        final InternetAddress addressF = new InternetAddress();
        InternetAddressF.Consumers.address("me@localhost")
                .andThen(InternetAddressF.Consumers.personal("Ich"))
                .andThen(InternetAddressF.Consumers.validate())
                .accept(addressF);
        InternetAddressF.Consumers.addressPersonalValidate("me@localhost", "Ich")
                .accept(addressF);
        assertAll(
                () -> assertEquals("me@localhost", addressF.getAddress()),
                () -> assertEquals(null, addressF.getGroup(true)),
                () -> assertEquals(null, addressF.getGroup(false)),
                () -> assertEquals("Ich", addressF.getPersonal()),
                () -> assertEquals("rfc822", addressF.getType())
        );
    }

    @Test
    public void testCreateMimeMessage_TO() throws MessagingException {
        assertNotNull(session);

        ConsumerThrowingMessagingException<MimeMessage> c = MimeMessageF.Consumers.from("me@localhost")
                .andThen(MimeMessageF.Consumers.recipient(RecipientType.TO, "me@localhost"))
                .andThen(MimeMessageF.Consumers.subject("subject"))
                .andThen(MimeMessageF.Consumers.text("text"));

        final MimeMessage mimeMessage = SessionF.MimeMessages.mimeMessage().apply(session);
        c.accept(mimeMessage);
        assertAll(() -> assertNotNull(mimeMessage),
                () -> assertEquals("subject", mimeMessage.getSubject()),
                () -> assertEquals("text", mimeMessage.getContent()),
                () -> assertEquals("me@localhost", mimeMessage.getFrom()[0].toString()),
                () -> assertEquals("me@localhost", mimeMessage.getRecipients(RecipientType.TO)[0].toString())
        );
    }

    @Test
    public void testCreateMimeMessage_TO_CC_BCC() throws MessagingException {
        Address addressesCC = new InternetAddressBuilder().addressPersonal("meCC@localhost", "I").build();
        Address[] addressesBCC = new Address[]{
            new InternetAddressBuilder().addressPersonal("youBCC@localhost", "You").build()
        };
        ConsumerThrowingMessagingException<MimeMessage> c = MimeMessageF.Consumers.from("me@localhost")
                .andThen(MimeMessageF.Consumers.recipient(RecipientType.TO, "me@localhost"))
                .andThen(MimeMessageF.Consumers.recipient(RecipientType.CC, addressesCC))
                .andThen(MimeMessageF.Consumers.recipients(RecipientType.BCC, addressesBCC))
                .andThen(MimeMessageF.Consumers.subject("subject"))
                .andThen(MimeMessageF.Consumers.text("text"));

        final MimeMessage mimeMessage = SessionF.MimeMessages.mimeMessage().apply(session);
        c.accept(mimeMessage);
        assertAll(
                () -> assertNotNull(mimeMessage),
                () -> assertEquals("subject", mimeMessage.getSubject()),
                () -> assertEquals("text", mimeMessage.getContent()),
                () -> assertEquals("me@localhost", mimeMessage.getFrom()[0].toString()),
                () -> assertEquals("me@localhost", mimeMessage.getRecipients(RecipientType.TO)[0].toString()),
                () -> assertEquals("I <meCC@localhost>", mimeMessage.getRecipients(RecipientType.CC)[0].toString()),
                () -> assertEquals("You <youBCC@localhost>", mimeMessage.getRecipients(RecipientType.BCC)[0].toString())
        );
    }

}
