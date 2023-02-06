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

    /**
     * Functional inspired wrapper for mail {@link Session}.
     */
    public static class SessionF {

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

    /**
     * Functional inspired wrapper for mail {@link Transport}.
     */
    public static class TransportF {

        public Function<Transport, Boolean> connected() {
            return transport -> transport.isConnected();
        }

        /**
         * Encapsulate {@link Consumers} accepting an connected
         * {@link Transport} instance.
         */
        public static class Consumers {

            public static void withConnected(Transport transport,
                    ConsumerThrowingMessagingException<Transport> c) throws MessagingException {
                try {
                    transport.connect();
                    c.accept(transport);
                } finally {
                    transport.close();
                }
            }

            public static void withConnect(Transport transport,
                    String u, String p,
                    ConsumerThrowingMessagingException<Transport> c) throws MessagingException {
                try {
                    transport.connect(u, p);
                    c.accept(transport);
                } finally {
                    transport.close();
                }
            }

            public static void withConnect(Transport transport,
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

    /**
     * Functional inspired wrapper for mail {@link MimeMessage}.
     */
    public static class MimeMessageF {

        /**
         * Encapsulate methods returning
         * {@link ConsumerThrowingMessagingException} accepting a
         * {@link MimeMessage}.
         */
        public static class Consumers {

            public static ConsumerThrowingMessagingException<MimeMessage> from(String address) {
                return msg -> {
                    msg.setFrom(address);
                };
            }

            public static ConsumerThrowingMessagingException<MimeMessage> recipient(RecipientType rt, String address) {
                return msg -> {
                    msg.setRecipients(rt, address);
                };
            }

            public static ConsumerThrowingMessagingException<MimeMessage> recipient(RecipientType rt, Address address) {
                return recipients(rt, new Address[]{address});
            }

            public static ConsumerThrowingMessagingException<MimeMessage> recipients(RecipientType rt, Address[] addresses) {
                return msg -> {
                    msg.setRecipients(rt, addresses);
                };
            }

            public static ConsumerThrowingMessagingException<MimeMessage> subject(String subject) {
                return subject(subject, null);
            }

            public static ConsumerThrowingMessagingException<MimeMessage> subject(String subject, String charset) {
                return msg -> {
                    msg.setSubject(subject, charset);
                };
            }

            public static ConsumerThrowingMessagingException<MimeMessage> sentDate(Date d) {
                return msg -> {
                    msg.setSentDate(d);
                };
            }

            public static ConsumerThrowingMessagingException<MimeMessage> text(String text) {
                return msg -> {
                    msg.setText(text);
                };
            }

            /**
             * Create an instance of {@link ConsumerThrowingMessagingException}
             * accepting a {@link MimeMessage}, the consumer sends the
             * {@link MimeMessage} via {@link Transport#send(javax.mail.Message)
             * }.
             *
             * @return consumer
             */
            public static ConsumerThrowingMessagingException<MimeMessage> send() {
                return msg -> {
                    Transport.send(msg);
                };
            }

        }

        public static class Providers {

            public static FunctionThrowingMessagingException<MimeMessage, Address[]> allRecipients() {
                return msg -> {
                    return msg.getAllRecipients();
                };
            }

            public static FunctionThrowingMessagingException<MimeMessage, Address[]> recipients(RecipientType rt) {
                return msg -> {
                    return msg.getRecipients(rt);
                };
            }

            public static FunctionThrowingMessagingException<MimeMessage, Address[]> from() {
                return msg -> {
                    return msg.getFrom();
                };
            }

            public static FunctionThrowingMessagingException<MimeMessage, Address[]> replyTo() {
                return msg -> {
                    return msg.getReplyTo();
                };
            }

            public static Function<Address[], List<Address>> addressesAsList() {
                return addresses -> Arrays.asList(addresses);
            }
        }
    }

    /**
     * Functional inspired wrapper for mail {@link InternetAddress}.
     */
    public static class InternetAddressF {

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
    public static class Recipient {

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

    public static class InternetAddressBuilder {

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

        InternetAddress build() {
            return this.internetAddress;
        }
    }

}
