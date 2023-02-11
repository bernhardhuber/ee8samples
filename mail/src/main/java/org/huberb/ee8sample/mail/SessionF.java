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

import java.io.PrintStream;
import java.util.function.Consumer;
import java.util.function.Function;
import javax.mail.Address;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
import org.huberb.ee8sample.mail.Supports.FunctionThrowingMessagingException;

/**
 * Functional inspired wrapper for mail {@link Session}.
 */
public class SessionF {

    public static FunctionThrowingMessagingException<Session, Boolean> debug() {
        return session -> {
            return session.getDebug();
        };
    }

    public static FunctionThrowingMessagingException<Session, PrintStream> debugOut() {
        return session -> {
            return session.getDebugOut();
        };
    }

    public static class Consumers {

        public static Consumer<Session> debug(boolean v) {
            return session -> {
                session.setDebug(v);
            };
        }

        public static Consumer<Session> debugOut(PrintStream out) {
            return session -> {
                session.setDebugOut(out);
            };
        }
    }

    /**
     * Encapsulate functions for creating a {@link Transport} instance from
     * a {@link Session} instance.
     */
    public static class Transports {

        public static FunctionThrowingMessagingException<Session, Transport> transport() {
            return session -> {
                return session.getTransport();
            };
        }

        public static FunctionThrowingMessagingException<Session, Transport> transport(Address address) {
            return session -> {
                return session.getTransport(address);
            };
        }

        public static FunctionThrowingMessagingException<Session, Transport> transport(String protocol) {
            return session -> {
                return session.getTransport(protocol);
            };
        }

        public static FunctionThrowingMessagingException<Session, Transport> transport(javax.mail.Provider provider) {
            return session -> {
                return session.getTransport(provider);
            };
        }

        public static FunctionThrowingMessagingException<Session, Transport> transport(javax.mail.URLName protocol) {
            return session -> {
                return session.getTransport(protocol);
            };
        }
    }

    /**
     * Encapsulate functions for creating a {@link  MimeMessage} instance
     * from a {@link Session} instance.
     */
    public static class MimeMessages {

        public static Function<Session, MimeMessage> mimeMessage() {
            return session -> new MimeMessage(session);
        }
    }
    
}
