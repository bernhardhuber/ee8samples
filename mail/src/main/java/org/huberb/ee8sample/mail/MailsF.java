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
import java.util.Arrays;
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

/**
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

        void consume(Consumer<Transport> c) {
            c.accept(this.transport);
        }

        static class Cosumers {

            static Consumer<Transport> connect() {
                return transport -> {
                    try {
                        transport.connect();
                    } catch (MessagingException ex) {
                        String m = "connect";
                        throw new RuntimeException(m, ex);
                    }
                };
            }

            static Consumer<Transport> connect(String u, String p) {
                return transport -> {
                    try {
                        transport.connect(u, p);
                    } catch (MessagingException ex) {
                        String m = String.format("connect user [%s], urlname [%s]", u, transport.getURLName());
                        throw new RuntimeException(m, ex);
                    }
                };
            }

            static Consumer<Transport> connect(String host, String u, String p) {
                return transport -> {
                    try {
                        transport.connect(host, u, p);
                    } catch (MessagingException ex) {
                        String m = String.format("connect host [%s], user [%s], urlname [%s]", host, u, transport.getURLName());
                        throw new RuntimeException(m, ex);
                    }
                };
            }

            static Consumer<Transport> connect(String host, int port, String u, String p) {
                return transport -> {
                    try {
                        transport.connect(host, port, u, p);
                    } catch (MessagingException ex) {
                        String m = String.format("connect host [%s], port [%d], user [%s], urlname [%s]", host, port, u, transport.getURLName());
                        throw new RuntimeException(m, ex);
                    }
                };
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

        public void consume(Consumer<MimeMessage> c) {
            c.accept(this.mimeMesssage);
        }

        static class Consumers {

            static Consumer<MimeMessage> from(String address) {
                return msg -> {
                    try {
                        msg.setFrom(address);
                    } catch (MessagingException ex) {
                        String m = String.format("set from address [%s]", address);
                        throw new RuntimeException(m, ex);
                    }
                };
            }

            static Consumer<MimeMessage> recipient(RecipientType rt, String address) {
                return msg -> {
                    try {
                        msg.setRecipients(rt, address);
                    } catch (MessagingException ex) {
                        String m = String.format("set recipient type [%s], addresses %s", rt, address);
                        throw new RuntimeException(m, ex);
                    }
                };
            }

            static Consumer<MimeMessage> recipient(RecipientType rt, Address address) {
                return recipients(rt, new Address[]{address});
            }

            static Consumer<MimeMessage> recipients(RecipientType rt, Address[] addresses) {
                return msg -> {
                    try {
                        msg.setRecipients(rt, addresses);
                    } catch (MessagingException ex) {
                        String m = String.format("set recipient type [%s], addresseses %s", rt, Arrays.toString(addresses));
                        throw new RuntimeException(m, ex);
                    }
                };
            }

            static Consumer<MimeMessage> subject(String subject) {
                return subject(subject, null);
            }

            static Consumer<MimeMessage> subject(String subject, String charset) {
                return msg -> {
                    try {
                        msg.setSubject(subject, charset);
                    } catch (MessagingException ex) {
                        String m = String.format("set subject [%s]", subject);
                        throw new RuntimeException(m, ex);
                    }
                };
            }

            static Consumer<MimeMessage> text(String text) {
                return msg -> {
                    try {
                        msg.setText(text);
                    } catch (MessagingException ex) {
                        String m = String.format("set text [%s]", text);
                        throw new RuntimeException(m, ex);
                    }
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

    static class Recipient {

        RecipientType rt;
        final List<InternetAddress> iaList = new ArrayList<>();

        public RecipientType getRt() {
            return rt;
        }

        public InternetAddress[] getIaAsArray() {
            return iaList.toArray(InternetAddress[]::new);
        }

        public List<InternetAddress> getIa() {
            return iaList;
        }

        static class RecipientBuilder {

            Recipient recipient = new Recipient();

            RecipientBuilder addAddress(RecipientType rt, List<InternetAddress> ia) {
                this.recipient.rt = rt;
                this.recipient.iaList.addAll(ia);
                return this;
            }

            RecipientBuilder recipientType(RecipientType rt) {
                this.recipient.rt = rt;
                return this;
            }

            RecipientBuilder addAddress(InternetAddress ia) {
                this.recipient.iaList.add(ia);
                ;
                return this;
            }

            Recipient build() {
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
