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

import java.util.concurrent.atomic.AtomicInteger;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import org.huberb.ee8sample.mail.Supports.ConsumerThrowingMessagingException;
import org.huberb.ee8sample.mail.experimental.StoringOnlyTransport;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 *
 * @author berni3
 */
@ExtendWith(MockitoExtension.class)
class TransportFTest {

    //@Mock
    Transport transport;

    @BeforeEach
    public void setUp() throws NoSuchProviderException {
        Session session = StoringOnlyTransport.Factory.createDefaultSession();
        this.transport = session.getTransport(StoringOnlyTransport.Factory.provider());
        assertAll(
                () -> assertFalse(this.transport.isConnected()),
                () -> assertFalse(TransportF.connected().apply(this.transport))
        );
    }

    /**
     * Test of withConnected method, of class TransportF.
     */
    @Test
    public void testWithConnected() throws MessagingException {
        final AtomicInteger cInvoked = new AtomicInteger(0);
        ConsumerThrowingMessagingException<Transport> c = (t) -> {
            assertAll(
                    () -> assertTrue(this.transport.isConnected()),
                    () -> assertTrue(TransportF.connected().apply(this.transport))
            );
            cInvoked.incrementAndGet();
        };
        TransportF.Consumers.withConnected(this.transport, c);
        assertAll(
                () -> assertEquals(1, cInvoked.intValue()),
                () -> assertFalse(this.transport.isConnected()),
                () -> assertFalse(TransportF.connected().apply(this.transport))
        );
    }

    /**
     * Test of withConnected method, of class TransportF.
     */
    @Test
    public void testWithConnected_u_p() throws MessagingException {
        final AtomicInteger cInvoked = new AtomicInteger(0);
        ConsumerThrowingMessagingException<Transport> c = (t) -> {
            assertAll(
                    () -> assertTrue(this.transport.isConnected()),
                    () -> assertTrue(TransportF.connected().apply(this.transport))
            );
            cInvoked.incrementAndGet();
        };
        TransportF.Consumers.withConnected(this.transport, "u", "p", c);
        assertAll(
                () -> assertEquals(1, cInvoked.intValue()),
                () -> assertFalse(this.transport.isConnected()),
                () -> assertFalse(TransportF.connected().apply(this.transport))
        );
    }

    /**
     * Test of withConnected method, of class TransportF.
     */
    @Test
    public void testWithConnected_h_p_u_p() throws MessagingException {
        final AtomicInteger cInvoked = new AtomicInteger(0);
        ConsumerThrowingMessagingException<Transport> c = (t) -> {
            assertAll(
                    () -> assertTrue(this.transport.isConnected()),
                    () -> assertTrue(TransportF.connected().apply(this.transport))
            );
            cInvoked.incrementAndGet();
        };
        TransportF.Consumers.withConnected(this.transport, "h", -1, "u", "p", c);
        assertAll(
                () -> assertEquals(1, cInvoked.intValue()),
                () -> assertFalse(this.transport.isConnected()),
                () -> assertFalse(TransportF.connected().apply(this.transport))
        );
    }

}
