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
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import org.huberb.ee8sample.mail.MailsF.SessionTransportF;
import org.huberb.ee8sample.mail.MailsF.TransportF;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author berni3
 */
public class MailIT {

    Session session;

    @BeforeEach
    public void setUp() {
        Properties props = new Properties();
        session = Session.getInstance(props);
        session.setDebug(true);
        assertNotNull(session);
    }

    @Test
    public void hello3() throws MessagingException {
        RuntimeException rtex = Assertions.assertThrows(RuntimeException.class,
                () -> {

                    try ( Transport transport = new SessionTransportF(session).provide(SessionTransportF.Transports.transport())) {
                        assertFalse(transport.isConnected());
                        TransportF transportF = new TransportF(transport);
                        transportF.consume(TransportF.Cosumers.connect());
                        assertFalse(transport.isConnected());
                    }
                });
        assertNotNull(rtex);
        assertAll(
                () -> assertEquals("connect", rtex.getMessage()),
                () -> assertEquals("Couldn't connect to host, port: localhost, 25; timeout -1", rtex.getCause().getMessage())
        );
    }

}
