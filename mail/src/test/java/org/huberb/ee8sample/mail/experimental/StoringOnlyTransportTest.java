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

import java.util.Properties;
import javax.mail.Provider;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
import org.huberb.ee8sample.mail.MimeMessageF;
import org.huberb.ee8sample.mail.SessionF;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 *
 * @author berni3
 */
public class StoringOnlyTransportTest {

    /**
     * Test of sendMessage method, of class StoringOnlyTransport.
     */
    @Test
    public void testSendMessage() throws Exception {
        Properties props = new Properties() {
            {
                setProperty("mail.transport.protocol.rfc822", StoringOnlyTransport.protocol());
                setProperty("mail.transport.protocol.smtp", StoringOnlyTransport.protocol());
                setProperty("mail." + StoringOnlyTransport.protocol() + ".class", StoringOnlyTransport.class.getName());
            }
        };
        Provider provider = StoringOnlyTransport.provider();
        Session session = Session.getInstance(props, null);
        SessionF.Consumers.debug(true).accept(session);
        session.addProvider(provider);
        Transport transport = session.getTransport(provider);
        assertEquals(StoringOnlyTransport.class.getName(), transport.getClass().getName());
        assertEquals(StoringOnlyTransport.protocol(), transport.getURLName().getProtocol());

        MimeMessage mm = SessionF.MimeMessages.mimeMessage().apply(session);
        MimeMessageF.Consumers.from("me")
                .andThen(MimeMessageF.Consumers.to("you"))
                .andThen(MimeMessageF.Consumers.subject("the-subject"))
                .andThen(MimeMessageF.Consumers.text("the-text"))
                .accept(mm);
        Transport.send(mm);

        StoringOnlyTransport storingOnlyTransport = (StoringOnlyTransport) session.getTransport(StoringOnlyTransport.protocol());
        assertEquals(1, storingOnlyTransport.sentMessages().size());
        assertEquals("addresses: [you], msg: subject: 'the-subject', text: 'the-text'", storingOnlyTransport.sentMessages().get(0));

        storingOnlyTransport.clearSentMessages();
        assertEquals(0, storingOnlyTransport.sentMessages().size());
    }

}
