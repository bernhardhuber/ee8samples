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

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import javax.mail.Address;
import javax.mail.Provider;
import javax.mail.Session;
import javax.mail.URLName;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import org.huberb.ee8sample.mail.experimental.StoringOnlyTransport;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author berni3
 */
public class SessionFTest {

    Session session;

    @BeforeEach
    public void setUp() {
        session = StoringOnlyTransport.Factory.createDefaultSession();
    }

    @Test
    public void testSessionF_debug() {
        assertAll(
                () -> assertTrue(SessionF.debug().apply(session)),
                () -> assertNotNull(SessionF.debugOut().apply(session))
        );
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        SessionF.Consumers.debug(false)
                .andThen(SessionF.Consumers.debugOut(ps))
                .accept(session);

        assertAll(
                () -> assertFalse(SessionF.debug().apply(session)),
                () -> assertNotNull(SessionF.debugOut().apply(session)),
                () -> assertEquals(ps, SessionF.debugOut().apply(session))
        );
    }

    @Test
    public void testSessionFTransports_transport() throws AddressException {
        Address address = new InternetAddress("somename@somehost");
        Provider provider = StoringOnlyTransport.Factory.provider();
        String protocol = StoringOnlyTransport.Factory.protocol();
        URLName urlName = new URLName(protocol, null, 0, null, null, null);
        assertAll(
                () -> assertFalse(SessionF.Transports.transport().apply(session).isConnected()),
                () -> assertFalse(SessionF.Transports.transport(address).apply(session).isConnected()),
                () -> assertFalse(SessionF.Transports.transport(provider).apply(session).isConnected()),
                () -> assertFalse(SessionF.Transports.transport(protocol).apply(session).isConnected()),
                () -> assertFalse(SessionF.Transports.transport(urlName).apply(session).isConnected())
        );
    }
}
