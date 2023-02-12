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
import javax.mail.Service;
import javax.mail.Transport;
import org.huberb.ee8sample.mail.Supports.ConsumerThrowingMessagingException;

/**
 * Functional inspired wrapper for mail {@link Transport}.
 */
public class TransportF {

    public static Function<Transport, Boolean> connected() {
        return Service::isConnected;
    }

    private TransportF() {
    }

    /**
     * Encapsulate {@link Consumers} accepting an connected {@link Transport}
     * instance.
     */
    public static class Consumers {

        private Consumers() {
        }

        /**
         * Connect transport and invoke Consumer.
         *
         * @param transport connect this transport
         * @param c consumer consuming the connected transport
         * @throws MessagingException if connecting or consumer fails
         *
         * @see Transport#connect()
         */
        public static void withConnected(Transport transport,
                ConsumerThrowingMessagingException<Transport> c) throws MessagingException {
            try (transport) {
                transport.connect();
                c.accept(transport);
            }
        }

        /**
         * Connect transport and invoke Consumer.
         *
         * @param transport connect this transport
         * @param u specified username for connecting
         * @param p specified password for connecting
         * @param c consumer consuming the connected transport
         * @throws MessagingException if connecting or consumer fails
         *
         * @see Transport#connect(java.lang.String, java.lang.String) *
         */
        public static void withConnected(Transport transport, String u, String p,
                ConsumerThrowingMessagingException<Transport> c) throws MessagingException {
            try (transport) {
                transport.connect(u, p);
                c.accept(transport);
            }
        }

        /**
         * Connect transport and invoke Consumer.
         *
         * @param transport connect this transport
         * @param host connect to this host:port
         * @param port connect to this host:port
         * @param u specified username for connecting
         * @param p specified password for connecting
         * @param c consumer consuming the connected transport
         * @throws MessagingException if connecting or consumer fails
         *
         * @see Transport#connect(java.lang.String, int, java.lang.String,
         * java.lang.String)
         */
        public static void withConnected(Transport transport, String host, int port, String u, String p,
                ConsumerThrowingMessagingException<Transport> c) throws MessagingException {
            try (transport) {
                transport.connect(host, port, u, p);
                c.accept(transport);
            }
        }
    }

}
