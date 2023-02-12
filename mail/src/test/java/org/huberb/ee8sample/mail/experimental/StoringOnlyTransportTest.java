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
package org.huberb.ee8sample.mail.experimental;

import java.io.IOException;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
import org.huberb.ee8sample.mail.MimeMessageF;
import org.huberb.ee8sample.mail.SessionF;
import org.huberb.ee8sample.mail.experimental.StoringOnlyTransport.MessageAddresses;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 *
 * @author berni3
 */
public class StoringOnlyTransportTest {

    /**
     * Test of StoringOnlyTransport.Factory.createDefaultSession method, of
     * class StoringOnlyTransport.
     */
    @Test
    public void testStoringOnlyTransport_is_registered() throws MessagingException {
        final Session session = StoringOnlyTransport.Factory.createDefaultSession();
        final Transport transport = session.getTransport(StoringOnlyTransport.Factory.provider());
        assertAll(
                () -> assertEquals(StoringOnlyTransport.class.getName(), transport.getClass().getName()),
                () -> assertEquals(StoringOnlyTransport.Factory.protocol(), transport.getURLName().getProtocol())
        );
    }

    /**
     * Test of sendMessage method, of class StoringOnlyTransport.
     */
    @Test
    public void testSendMessage_via_StoringOnlyTransport() throws MessagingException, IOException {
        final Session session = StoringOnlyTransport.Factory.createDefaultSession();

        final MimeMessage mm = SessionF.MimeMessages.mimeMessage().apply(session);
        MimeMessageF.Consumers.from("me")
                .andThen(MimeMessageF.Consumers.to("you"))
                .andThen(MimeMessageF.Consumers.subject("the-subject"))
                .andThen(MimeMessageF.Consumers.text("the-text"))
                .accept(mm);
        Transport.send(mm);

        final StoringOnlyTransport storingOnlyTransport = (StoringOnlyTransport) session.getTransport(StoringOnlyTransport.Factory.protocol());
        assertEquals(1, storingOnlyTransport.sentMessages().size());

        final MessageAddresses ma = storingOnlyTransport.sentMessages().get(0);
        final String expected = "'from':['me'], 'reply-to':['me'], 'to':['you'], 'cc':['you'], 'bcc':['you'], 'subject':'the-subject' ";
        assertEquals(expected, StoringOnlyTransport.MimeMessageStringReps.createStringRepB(ma.getMessage(), ma.getAddresses()));

        storingOnlyTransport.clearSentMessages();
        assertEquals(0, storingOnlyTransport.sentMessages().size());
    }

}
