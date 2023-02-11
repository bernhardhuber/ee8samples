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

import java.util.function.Function;
import javax.mail.MessagingException;
import javax.mail.Transport;
import org.huberb.ee8sample.mail.Supports.ConsumerThrowingMessagingException;

/**
 * Functional inspired wrapper for mail {@link Transport}.
 */
public class TransportF {

    public Function<Transport, Boolean> connected() {
        return transport -> transport.isConnected();
    }

    /**
     * Encapsulate {@link Consumers} accepting an connected
     * {@link Transport} instance.
     */
    public static class Consumers {

        public static void withConnected(Transport transport, ConsumerThrowingMessagingException<Transport> c) throws MessagingException {
            try {
                transport.connect();
                c.accept(transport);
            } finally {
                transport.close();
            }
        }

        public static void withConnect(Transport transport, String u, String p, ConsumerThrowingMessagingException<Transport> c) throws MessagingException {
            try {
                transport.connect(u, p);
                c.accept(transport);
            } finally {
                transport.close();
            }
        }

        public static void withConnect(Transport transport, String host, int port, String u, String p, ConsumerThrowingMessagingException<Transport> c) throws MessagingException {
            try {
                transport.connect(host, port, u, p);
                c.accept(transport);
            } finally {
                transport.close();
            }
        }
    }
    
}
