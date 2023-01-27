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

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import javax.mail.Address;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.huberb.ee8sample.mail.Supports.ConsumerThrowingMessagingException;

/**
 * Wrappers for sending emails using {@link javax.mail} API.
 *
 * @author berni3
 */
public class MailsF {

    static class SessionTransportF {

        final Session session;

        public SessionTransportF(Session session) {
            this.session = session;
        }

        <T> T provide(Function<Session, T> f) {
            return f.apply(this.session);
        }

        static Function<Session, Boolean> debug() {
            return session -> {
                return session.getDebug();
            };
        }

        static Function<Session, PrintStream> debugOut() {
            return session -> {
                return session.getDebugOut();
            };
        }

        void consume(Consumer<Session> c) {
            c.accept(this.session);
        }

        static class Consumers {

            static Consumer<Session> debug(boolean v) {
                return session -> {
                    session.setDebug(v);
                };
            }

            static Consumer<Session> debugOut(PrintStream out) {
                return session -> {
                    session.setDebugOut(out);
                };
            }
        }

        static class Transports {

            static Function<Session, Transport> transport() {
                return session -> {
                    try {
                        return session.getTransport();
                    } catch (NoSuchProviderException ex) {
                        String m = "get transport";
                        throw new RuntimeException(m, ex);
                    }
                };
            }

            static Function<Session, Transport> transport(Address address) {
                return session -> {
                    try {
                        return session.getTransport(address);
                    } catch (NoSuchProviderException ex) {
                        String m = "get transport";
                        throw new RuntimeException(m, ex);
                    }
                };
            }

            static Function<Session, Transport> transport(String protocol) {
                return session -> {
                    try {
                        return session.getTransport(protocol);
                    } catch (NoSuchProviderException ex) {
                        String m = "get transport";
                        throw new RuntimeException(m, ex);
                    }
                };
            }

            static Function<Session, Transport> transport(javax.mail.Provider protocol) {
                return session -> {
                    try {
                        return session.getTransport(protocol);
                    } catch (NoSuchProviderException ex) {
                        String m = "get transport";
                        throw new RuntimeException(m, ex);
                    }
                };
            }

            static Function<Session, Transport> transport(javax.mail.URLName protocol) {
                return session -> {
                    try {
                        return session.getTransport(protocol);
                    } catch (NoSuchProviderException ex) {
                        String m = "get transport";
                        throw new RuntimeException(m, ex);
                    }
                };
            }

            static Function<Transport, TransportF> transportF() {
                return transport -> {
                    return new TransportF(transport);
                };
            }
        }

        static class MimeMessages {

            static Function<Session, MimeMessage> mimeMessage() {
                return session -> new MimeMessage(session);
            }

            static Function<Session, MimeMessageF> mimeMessageF() {
                return session -> new MimeMessageF(new MimeMessage(session));
            }

            static Supplier<MimeMessage> mimeMessage(Session session) {
                return () -> new MimeMessage(session);
            }

            static Supplier<MimeMessageF> mimeMessageF(MimeMessage mimeMessage) {
                return () -> new MimeMessageF(mimeMessage);
            }
        }
    }

    static class TransportF {

        final Transport transport;

        public TransportF(Transport transport) {
            this.transport = transport;
        }

        <T> T provide(Function<Transport, T> f) {
            return f.apply(this.transport);
        }

        Function<Transport, Boolean> connected() {
            return transport -> transport.isConnected();
        }

        void consume(ConsumerThrowingMessagingException<Transport> c) throws MessagingException {
            c.accept(this.transport);
        }

        static class Consumers {

            static void whithConnected(Transport transport, ConsumerThrowingMessagingException<Transport> c) throws MessagingException {
                try {
                    transport.connect();
                    c.accept(transport);
                } finally {
                    transport.close();
                }
            }

            static void connect(Transport transport, String u, String p, ConsumerThrowingMessagingException<Transport> c) throws MessagingException {
                try {
                    transport.connect(u, p);
                    c.accept(transport);
                } finally {
                    transport.close();
                }
            }

            static void connect(Transport transport, String host, int port, String u, String p, ConsumerThrowingMessagingException<Transport> c) throws MessagingException {
                try {
                    transport.connect(host, port, u, p);
                    c.accept(transport);
                } finally {
                    transport.close();
                }
            }
        }
    }

    static class MimeMessageF {

        final MimeMessage mimeMesssage;

        public MimeMessageF(MimeMessage mm) {
            this.mimeMesssage = mm;
        }

        public MimeMessageF(Session session) {
            this.mimeMesssage = new MimeMessage(session);
        }

        public MimeMessage getMimeMessage() {
            return this.mimeMesssage;
        }

        public void consume(ConsumerThrowingMessagingException<MimeMessage> c) throws MessagingException {
            c.accept(this.mimeMesssage);
        }

        static class Consumers {

            static ConsumerThrowingMessagingException<MimeMessage> from(String address) {
                return msg -> {
                    msg.setFrom(address);
                };
            }

            static ConsumerThrowingMessagingException<MimeMessage> recipient(RecipientType rt, String address) {
                return msg -> {
                    msg.setRecipients(rt, address);
                };
            }

            static ConsumerThrowingMessagingException<MimeMessage> recipient(RecipientType rt, Address address) {
                return recipients(rt, new Address[]{address});
            }

            static ConsumerThrowingMessagingException<MimeMessage> recipients(RecipientType rt, Address[] addresses) {
                return msg -> {
                    msg.setRecipients(rt, addresses);
                };
            }

            static ConsumerThrowingMessagingException<MimeMessage> subject(String subject) {
                return subject(subject, null);
            }

            static ConsumerThrowingMessagingException<MimeMessage> subject(String subject, String charset) {
                return msg -> {
                    msg.setSubject(subject, charset);
                };
            }

            static ConsumerThrowingMessagingException<MimeMessage> sentDate(Date d) {
                return msg -> {
                    msg.setSentDate(d);
                };
            }

            static ConsumerThrowingMessagingException<MimeMessage> text(String text) {
                return msg -> {
                    msg.setText(text);
                };
            }
        }

        //---
        public <T> T provide(Function<MimeMessage, T> f) {
            return f.apply(this.mimeMesssage);
        }

        static class Providers {

            static Function<MimeMessage, Address[]> allRecipients() {
                return msg -> {
                    try {
                        return msg.getAllRecipients();
                    } catch (MessagingException ex) {
                        String m = String.format("get all Recipients");
                        throw new RuntimeException(m, ex);
                    }
                };
            }
        }
    }

    static class InternetAddressF {

        final InternetAddress address;

        public InternetAddressF() {
            this(new InternetAddress());
        }

        public InternetAddressF(InternetAddress address) {
            this.address = address;
        }

        InternetAddress getInternetAddress() {
            return this.address;
        }

        void consume(Consumer<InternetAddress> c) {
            c.accept(this.address);
        }

        static class Consumers {

            static Consumer<InternetAddress> addressPersonalValidate(String address, String personal) {
                return address(address).andThen(personal(personal)).andThen(validate());
            }

            static Consumer<InternetAddress> address(String address) {
                return ia -> ia.setAddress(address);
            }

            static Consumer<InternetAddress> personal(String personal) {
                return personal(personal, null);
            }

            static Consumer<InternetAddress> personal(String personal, String charset) {
                return ia -> {
                    try {
                        ia.setPersonal(personal, charset);
                    } catch (UnsupportedEncodingException ex) {
                        String m = String.format("personal [%s]", personal);
                        throw new RuntimeException(m, ex);
                    }
                };
            }

            static Consumer<InternetAddress> validate() {
                return ia -> {
                    try {
                        ia.validate();
                    } catch (AddressException ex) {
                        final String m = String.format("validate [%s]", ia);
                        throw new RuntimeException(m, ex);
                    }
                };
            }
        }
    }

    /**
     * Define a recipient by its {@link RecipientType} and list of
     * {@link InternetAddress} sharing the same {@link RecipientType}.
     *
     */
    static class Recipient {

        private RecipientType rt;
        private final List<InternetAddress> iaList = new ArrayList<>();

        public RecipientType getRt() {
            return rt;
        }

        public InternetAddress[] getInternetAddressAsArray() {
            return iaList.toArray(InternetAddress[]::new);
        }

        public List<InternetAddress> getInternetAddress() {
            return iaList;
        }

        public static class RecipientBuilder {

            private Recipient recipient = new Recipient();

            public RecipientBuilder addAddress(RecipientType rt, List<InternetAddress> ia) {
                this.recipient.rt = rt;
                this.recipient.iaList.addAll(ia);
                return this;
            }

            public RecipientBuilder recipientType(RecipientType rt) {
                this.recipient.rt = rt;
                return this;
            }

            public RecipientBuilder addAddress(InternetAddress ia) {
                this.recipient.iaList.add(ia);
                ;
                return this;
            }

            public Recipient build() {
                return this.recipient;
            }
        }
    }

    static class InternetAddressBuilderF {

        final InternetAddressF addressF = new InternetAddressF();

        InternetAddressBuilderF address(String address) {
            addressF.consume(InternetAddressF.Consumers.address(address));
            return this;
        }

        InternetAddressBuilderF personal(String address) {
            addressF.consume(InternetAddressF.Consumers.personal(address));
            return this;
        }

        InternetAddressBuilderF addressPersonal(String address, String personal) {
            addressF.consume(InternetAddressF.Consumers.address(address)
                    .andThen(InternetAddressF.Consumers.personal(personal)));
            return this;
        }

        InternetAddress build() {
            addressF.consume(InternetAddressF.Consumers.validate());
            return this.addressF.getInternetAddress();
        }
    }

    static class InternetAddressBuilderTraditional {

        final InternetAddress internetAddress;

        public InternetAddressBuilderTraditional() {
            this(new InternetAddress());
        }

        public InternetAddressBuilderTraditional(InternetAddress ia) {
            this.internetAddress = ia;
        }

        InternetAddressBuilderTraditional address(String address) {
            this.internetAddress.setAddress(address);
            return this;
        }

        InternetAddressBuilderTraditional personal(String personal) {
            try {
                this.internetAddress.setPersonal(personal, "UTF-8");
                return this;
            } catch (UnsupportedEncodingException ex) {
                String m = String.format("setting personal [%s]", personal);
                throw new RuntimeException(m, ex);
            }
        }

        InternetAddressBuilderTraditional addressPersonal(String address, String personal) {
            try {
                this.internetAddress.setAddress(address);
                this.internetAddress.setPersonal(personal, "UTF-8");
            } catch (UnsupportedEncodingException ex) {
                String m = String.format("setting address personal [%s], address [%s]", personal, address);
                throw new RuntimeException(m, ex);
            }
            return this;
        }

        InternetAddressBuilderTraditional validate() {
            try {
                this.internetAddress.validate();
                return this;
            } catch (AddressException ex) {
                String m = String.format("validting address [%s]", this.internetAddress.getAddress());
                throw new RuntimeException(m, ex);
            }
        }

        String getAddress() {
            return this.getAddress();
        }

        InternetAddress build() {
            return this.internetAddress;
        }
    }

}
