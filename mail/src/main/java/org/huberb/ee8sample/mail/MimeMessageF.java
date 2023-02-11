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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;
import javax.mail.Address;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.huberb.ee8sample.mail.Supports.ConsumerThrowingMessagingException;
import org.huberb.ee8sample.mail.Supports.FunctionThrowingMessagingException;
import org.huberb.ee8sample.mail.Supports.MailRuntimeException;

/**
 * Functional inspired wrapper for mail {@link MimeMessage}.
 */
public class MimeMessageF {

    /**
     * Encapsulate methods returning {@link ConsumerThrowingMessagingException}
     * accepting a {@link MimeMessage}.
     */
    public static class Consumers {

        public static ConsumerThrowingMessagingException<MimeMessage> from(String address) {
            return msg -> msg.setFrom(address);
        }

        public static ConsumerThrowingMessagingException<MimeMessage> from(Address address) {
            return msg -> msg.setFrom(address);
        }

        public static ConsumerThrowingMessagingException<MimeMessage> replyTo(String address) {
            return msg -> msg.setReplyTo(new Address[]{new InternetAddress(address)});
        }

        public static ConsumerThrowingMessagingException<MimeMessage> replyTo(Address address) {
            return msg -> msg.setReplyTo(new Address[]{address});
        }

        public static ConsumerThrowingMessagingException<MimeMessage> replyTo(Address[] address) {
            return msg -> msg.setReplyTo(Arrays.copyOf(address, address.length));
        }

        public static ConsumerThrowingMessagingException<MimeMessage> replyTo(List<Address> address) {
            return msg -> msg.setReplyTo(address.toArray(Address[]::new));
        }

        public static ConsumerThrowingMessagingException<MimeMessage> to(String address) {
            return recipient(RecipientType.TO, address);
        }

        public static ConsumerThrowingMessagingException<MimeMessage> to(String[] address) {
            return recipients(RecipientType.TO, Providers.addressesFromStrings().apply(address));
        }

        public static ConsumerThrowingMessagingException<MimeMessage> to(Address address) {
            return recipients(RecipientType.TO, new Address[]{address});
        }

        public static ConsumerThrowingMessagingException<MimeMessage> to(Address[] addresses) {
            return recipients(RecipientType.TO, addresses);
        }

        public static ConsumerThrowingMessagingException<MimeMessage> to(List<Address> addresses) {
            return recipients(RecipientType.TO, addresses);
        }

        public static ConsumerThrowingMessagingException<MimeMessage> recipient(Recipient rt) {
            return recipients(rt.getRecipientType(), rt.getInternetAddressAsArray());
        }

        public static ConsumerThrowingMessagingException<MimeMessage> recipient(RecipientType rt, String address) {
            return msg -> msg.setRecipients(rt, address);
        }

        public static ConsumerThrowingMessagingException<MimeMessage> recipients(RecipientType rt, String[] addresses) {
            return recipients(rt, Providers.addressesFromStrings().apply(addresses));
        }

        public static ConsumerThrowingMessagingException<MimeMessage> recipient(RecipientType rt, Address address) {
            return recipients(rt, new Address[]{address});
        }

        public static ConsumerThrowingMessagingException<MimeMessage> recipients(RecipientType rt, Address[] addresses) {
            return msg -> msg.setRecipients(rt, addresses);
        }

        public static ConsumerThrowingMessagingException<MimeMessage> recipients(RecipientType rt, List<Address> addresses) {
            return msg -> msg.setRecipients(rt, Providers.addressesFromList().apply(addresses));
        }

        public static ConsumerThrowingMessagingException<MimeMessage> subject(String subject) {
            return subject(subject, null);
        }

        public static ConsumerThrowingMessagingException<MimeMessage> subject(String subject, String charset) {
            return msg -> msg.setSubject(subject, charset);
        }

        public static ConsumerThrowingMessagingException<MimeMessage> sentDate(Date d) {
            return msg -> msg.setSentDate(d);
        }

        public static ConsumerThrowingMessagingException<MimeMessage> text(String text) {
            return msg -> msg.setText(text);
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
            return Transport::send;
        }
    }

    public static class Providers {

        public static FunctionThrowingMessagingException<MimeMessage, Address[]> allRecipients() {
            return MimeMessage::getAllRecipients;
        }

        public static FunctionThrowingMessagingException<MimeMessage, Address[]> recipients(RecipientType rt) {
            return msg -> msg.getRecipients(rt);
        }

        public static FunctionThrowingMessagingException<MimeMessage, Address[]> from() {
            return MimeMessage::getFrom;
        }

        public static FunctionThrowingMessagingException<MimeMessage, Address[]> replyTo() {
            return MimeMessage::getReplyTo;
        }

        //---
        public static FunctionThrowingMessagingException<String, Address> addressFromString() {
            return InternetAddress::new;
        }

        public static Function<String, Address> addressFromStringThrowingMailRuntimeException() {
            return address -> {
                try {
                    return addressFromString().apply(address);
                } catch (MessagingException mex) {
                    throw new MailRuntimeException("addressFromString");
                }
            };
        }

        public static Function<String[], List<Address>> addressesFromStrings() {
            return address -> {
                List<Address> addressList = new ArrayList<>();
                Stream.of(address).map(addressFromStringThrowingMailRuntimeException()).forEach(addressList::add);
                return addressList;
            };
        }

        public static Function<List<String>, List<Address>> addressesFromStringList() {
            return address -> {
                List<Address> addressList = new ArrayList<>();
                address.stream().map(addressFromStringThrowingMailRuntimeException()).forEach(addressList::add);
                return addressList;
            };
        }

        public static Function<Address[], List<Address>> addressesAsList() {
            return Arrays::asList;
        }

        public static Function<List<Address>, Address[]> addressesFromList() {
            return addresses -> addresses.toArray(Address[]::new);
        }
    }

}
