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
import javax.mail.Address;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.huberb.ee8sample.mail.Supports.ConsumerThrowingMessagingException;
import org.huberb.ee8sample.mail.Supports.FunctionThrowingMessagingException;
import org.huberb.ee8sample.mail.Supports.MailRuntimeException;

/**
 * Wrappers for sending emails using {@link javax.mail} API.
 *
 * @author berni3
 */
public class MailsF {

    static class SessionsF {

        static class Functions {

            static FunctionThrowingMessagingException<Session, Boolean> debug() {
                return session -> {
                    return session.getDebug();
                };
            }

            static FunctionThrowingMessagingException<Session, PrintStream> debugOut() {
                return session -> {
                    return session.getDebugOut();
                };
            }
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

            static FunctionThrowingMessagingException<Session, Transport> transport() {
                return session -> {
                    return session.getTransport();
                };
            }

            static FunctionThrowingMessagingException<Session, Transport> transport(Address address) {
                return session -> {
                    return session.getTransport(address);
                };
            }

            static FunctionThrowingMessagingException<Session, Transport> transport(String protocol) {
                return session -> {
                    return session.getTransport(protocol);
                };
            }

            static FunctionThrowingMessagingException<Session, Transport> transport(javax.mail.Provider provider) {
                return session -> {
                    return session.getTransport(provider);
                };
            }

            static FunctionThrowingMessagingException<Session, Transport> transport(javax.mail.URLName protocol) {
                return session -> {
                    return session.getTransport(protocol);
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

        }
    }

    static class TransportsF {

        static class Functions {

            Function<Transport, Boolean> connected() {
                return transport -> transport.isConnected();
            }
        }

        static class Consumers {

            static void withConnected(Transport transport,
                    ConsumerThrowingMessagingException<Transport> c) throws MessagingException {
                try {
                    transport.connect();
                    c.accept(transport);
                } finally {
                    transport.close();
                }
            }

            static void withConnect(Transport transport,
                    String u, String p,
                    ConsumerThrowingMessagingException<Transport> c) throws MessagingException {
                try {
                    transport.connect(u, p);
                    c.accept(transport);
                } finally {
                    transport.close();
                }
            }

            static void withConnect(Transport transport,
                    String host, int port, String u, String p,
                    ConsumerThrowingMessagingException<Transport> c) throws MessagingException {
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

        final MimeMessage mimeMessage;

        public MimeMessageF(MimeMessage mm) {
            this.mimeMessage = mm;
        }

        public MimeMessageF(Session session) {
            this.mimeMessage = new MimeMessage(session);
        }

        public MimeMessage getMimeMessage() {
            return this.mimeMessage;
        }

        public void consume(ConsumerThrowingMessagingException<MimeMessage> c) throws MessagingException {
            c.accept(this.mimeMessage);
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
        public <T> T provide(FunctionThrowingMessagingException<MimeMessage, T> f) throws MessagingException {
            return f.apply(this.mimeMessage);
        }

        static class Providers {

            static FunctionThrowingMessagingException<MimeMessage, Address[]> allRecipients() {
                return msg -> {
                    return msg.getAllRecipients();
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
                        throw new MailRuntimeException(m, ex);
                    }
                };
            }

            static Consumer<InternetAddress> validate() {
                return ia -> {
                    try {
                        ia.validate();
                    } catch (AddressException ex) {
                        final String m = String.format("validate [%s]", ia);
                        throw new MailRuntimeException(m, ex);
                    }
                };
            }
        }
    }

    /**
     * Define a recipient by its {@link RecipientType} and list of
     * {@link InternetAddress} sharing the same {@link RecipientType}.
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

            private final Recipient recipient = new Recipient();

            public RecipientBuilder addAddress(RecipientType recipientType, List<InternetAddress> internetAddress) {
                this.recipient.rt = recipientType;
                this.recipient.iaList.addAll(internetAddress);
                return this;
            }

            public RecipientBuilder recipientType(RecipientType recipientType) {
                this.recipient.rt = recipientType;
                return this;
            }

            public RecipientBuilder addAddress(InternetAddress internetAddress) {
                this.recipient.iaList.add(internetAddress);
                return this;
            }

            public Recipient build() {
                return this.recipient;
            }
        }
    }

    static class InternetAddressBuilder {

        final InternetAddress internetAddress;

        public InternetAddressBuilder() {
            this(new InternetAddress());
        }

        public InternetAddressBuilder(InternetAddress ia) {
            this.internetAddress = ia;
        }

        InternetAddressBuilder address(String address) {
            this.internetAddress.setAddress(address);
            return this;
        }

        InternetAddressBuilder personal(String personal) {
            try {
                this.internetAddress.setPersonal(personal, "UTF-8");
                return this;
            } catch (UnsupportedEncodingException ex) {
                String m = String.format("setting personal [%s]", personal);
                throw new MailRuntimeException(m, ex);
            }
        }

        InternetAddressBuilder addressPersonal(String address, String personal) {
            try {
                this.internetAddress.setAddress(address);
                this.internetAddress.setPersonal(personal, "UTF-8");
            } catch (UnsupportedEncodingException ex) {
                String m = String.format("setting address personal [%s], address [%s]", personal, address);
                throw new MailRuntimeException(m, ex);
            }
            return this;
        }

        InternetAddressBuilder validate() {
            try {
                this.internetAddress.validate();
                return this;
            } catch (AddressException ex) {
                String m = String.format("validating address [%s]", this.internetAddress.getAddress());
                throw new MailRuntimeException(m, ex);
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
