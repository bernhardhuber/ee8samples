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

import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.mail.Address;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.huberb.ee8sample.mail.MimeMessageF.Consumers;
import static org.huberb.ee8sample.mail.MimeMessageF.Consumers.recipients;
import org.huberb.ee8sample.mail.Recipient.RecipientBuilder;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 *
 * @author berni3
 */
public class MimeMessageFTest {

    public static Stream<String> emailAddresses() {
        return Stream.of(
                "somefirstname.somelastname@somehost",
                "somefirstname.somelastname@somehost.somedomain",
                "somename@somehost"
        );
    }
    Session session;

    @BeforeEach
    public void setUp() {
        session = Session.getInstance(new Properties(), null);
    }

    @ParameterizedTest
    @MethodSource("emailAddresses")
    public void testAddressFromString(String theEmailAddress) throws MessagingException {

        assertEquals(theEmailAddress,
                MimeMessageF.Providers.addressFromString().apply(theEmailAddress).toString());
    }

    @ParameterizedTest
    @MethodSource("emailAddresses")
    public void testAddressFromStringThrowingMailRuntimeException(String theEmailAddress) {
        assertEquals(theEmailAddress,
                MimeMessageF.Providers.addressFromStringThrowingMailRuntimeException().apply(theEmailAddress).toString());
    }

    @ParameterizedTest
    @MethodSource("emailAddresses")
    public void testAddressesFromStrings(String theEmailAddress) {
        assertEquals(theEmailAddress, MimeMessageF.Providers.addressesFromStrings()
                .apply(new String[]{theEmailAddress})
                .get(0).toString()
        );
    }

    @ParameterizedTest
    @MethodSource("emailAddresses")
    public void testAddressesFromStringList(String theEmailAddress) {
        assertEquals(theEmailAddress, MimeMessageF.Providers.addressesFromStringList()
                .apply(Arrays.asList(theEmailAddress))
                .get(0).toString()
        );
    }

    @ParameterizedTest
    @MethodSource("emailAddresses")
    public void testAddressesAsList(String theEmailAddress) throws AddressException {
        assertEquals(theEmailAddress, MimeMessageF.Providers.addressesAsList()
                .apply(new InternetAddress[]{new InternetAddress(theEmailAddress)})
                .get(0).toString()
        );
    }

    @ParameterizedTest
    @MethodSource("emailAddresses")
    public void testAddressesFromList(String theEmailAddress) throws AddressException {
        assertEquals(theEmailAddress, MimeMessageF.Providers.addressesFromList()
                .apply(Arrays.asList(new InternetAddress(theEmailAddress)))[0].toString()
        );
    }

    //---
    @ParameterizedTest
    @MethodSource("emailAddresses")
    public void testFromString(String theEmailAddress) throws MessagingException {
        MimeMessage mimeMessage = new MimeMessage(session);
        MimeMessageF.Consumers.from(theEmailAddress).accept(mimeMessage);
        assertEquals(theEmailAddress, mimeMessage.getFrom()[0].toString());
    }

    @ParameterizedTest
    @MethodSource("emailAddresses")
    public void testFromAddress(String theEmailAddress) throws MessagingException {

        MimeMessage mimeMessage = new MimeMessage(session);
        MimeMessageF.Consumers.from(new InternetAddress(theEmailAddress)).accept(mimeMessage);
        assertEquals(theEmailAddress, mimeMessage.getFrom()[0].toString());
    }

    @ParameterizedTest
    @MethodSource("emailAddresses")
    public void testReplyToString(String theEmailAddress) throws MessagingException {
        MimeMessage mimeMessage = new MimeMessage(session);
        MimeMessageF.Consumers.replyTo(theEmailAddress).accept(mimeMessage);
        assertEquals(theEmailAddress, mimeMessage.getReplyTo()[0].toString());
    }

    @ParameterizedTest
    @MethodSource("emailAddresses")
    public void testReplyToAddress(String theEmailAddress) throws MessagingException {
        MimeMessage mimeMessage = new MimeMessage(session);
        MimeMessageF.Consumers.replyTo(new InternetAddress(theEmailAddress)).accept(mimeMessage);
        assertEquals(theEmailAddress, mimeMessage.getReplyTo()[0].toString());
    }

    @Test
    public void testReplyToAddressesList() throws MessagingException {
        List<String> addressesAsStringList = emailAddresses().collect(Collectors.toList());
        List<Address> addressesList = addressesAsStringList.stream()
                .map(e -> MimeMessageF.Providers.addressFromStringThrowingMailRuntimeException().apply(e))
                .collect(Collectors.toList());
        MimeMessage mimeMessage = new MimeMessage(session);
        MimeMessageF.Consumers.replyTo(addressesList).accept(mimeMessage);
        assertEquals(3, mimeMessage.getReplyTo().length);
        assertEquals(addressesAsStringList.get(0), mimeMessage.getReplyTo()[0].toString());
        assertEquals(addressesAsStringList.get(1), mimeMessage.getReplyTo()[1].toString());
        assertEquals(addressesAsStringList.get(2), mimeMessage.getReplyTo()[2].toString());
    }

    @Test
    public void testReplyToAddresses() throws MessagingException {
        List<String> addressesAsStringList = emailAddresses().collect(Collectors.toList());
        InternetAddress[] internetAddresses = addressesAsStringList.stream()
                .map(e -> MimeMessageF.Providers.addressFromStringThrowingMailRuntimeException().apply(e))
                .collect(Collectors.toList())
                .toArray(InternetAddress[]::new);
        MimeMessage mimeMessage = new MimeMessage(session);
        MimeMessageF.Consumers.replyTo(internetAddresses).accept(mimeMessage);
        assertEquals(3, mimeMessage.getReplyTo().length);
        assertEquals(addressesAsStringList.get(0), mimeMessage.getReplyTo()[0].toString());
        assertEquals(addressesAsStringList.get(1), mimeMessage.getReplyTo()[1].toString());
        assertEquals(addressesAsStringList.get(2), mimeMessage.getReplyTo()[2].toString());
    }

    @ParameterizedTest
    @MethodSource("emailAddresses")
    public void testTo(String theEmailAddress) throws MessagingException {
        MimeMessage mimeMessage = new MimeMessage(session);
        MimeMessageF.Consumers.to(theEmailAddress).accept(mimeMessage);
        assertEquals(theEmailAddress, mimeMessage.getRecipients(RecipientType.TO)[0].toString());
    }

    @ParameterizedTest
    @MethodSource("emailAddresses")
    public void testTo_Strings(String theEmailAddress) throws MessagingException {
        MimeMessage mimeMessage = new MimeMessage(session);
        MimeMessageF.Consumers.to(new String[]{theEmailAddress}).accept(mimeMessage);
        assertEquals(theEmailAddress, mimeMessage.getRecipients(RecipientType.TO)[0].toString());
    }

    @ParameterizedTest
    @MethodSource("emailAddresses")
    public void testTo_Address(String theEmailAddress) throws MessagingException {
        MimeMessage mimeMessage = new MimeMessage(session);
        MimeMessageF.Consumers.to(new InternetAddress(theEmailAddress)).accept(mimeMessage);
        assertEquals(theEmailAddress, mimeMessage.getRecipients(RecipientType.TO)[0].toString());
    }

    @ParameterizedTest
    @MethodSource("emailAddresses")
    public void testTo_Addresses(String theEmailAddress) throws MessagingException {
        MimeMessage mimeMessage = new MimeMessage(session);
        MimeMessageF.Consumers.to(new InternetAddress[]{new InternetAddress(theEmailAddress)}).accept(mimeMessage);
        assertEquals(theEmailAddress, mimeMessage.getRecipients(RecipientType.TO)[0].toString());
    }

    @ParameterizedTest
    @MethodSource("emailAddresses")
    public void testTo_AddressList(String theEmailAddress) throws MessagingException {
        MimeMessage mimeMessage = new MimeMessage(session);
        MimeMessageF.Consumers.to(Arrays.asList(new InternetAddress(theEmailAddress))).accept(mimeMessage);
        assertEquals(theEmailAddress, mimeMessage.getRecipients(RecipientType.TO)[0].toString());
    }

    @ParameterizedTest
    @MethodSource("emailAddresses")
    public void testReciptientToUsingRecipientBuilder(String theEmailAddress) throws MessagingException {
        MimeMessage mimeMessage = new MimeMessage(session);
        MimeMessageF.Consumers.recipient(new RecipientBuilder()
                .recipientType(RecipientType.TO)
                .addAddress(new InternetAddress(theEmailAddress))
                .build())
                .accept(mimeMessage);
        assertEquals(theEmailAddress, mimeMessage.getRecipients(RecipientType.TO)[0].toString());
    }

    @ParameterizedTest
    @MethodSource("emailAddresses")
    public void testRecipientTo(String theEmailAddress) throws MessagingException {
        MimeMessage mimeMessage = new MimeMessage(session);
        MimeMessageF.Consumers.recipient(RecipientType.TO, theEmailAddress).accept(mimeMessage);
        assertEquals(theEmailAddress, mimeMessage.getRecipients(RecipientType.TO)[0].toString());
    }

    @ParameterizedTest
    @MethodSource("emailAddresses")
    public void testRecipientCc(String theEmailAddress) throws MessagingException {
        MimeMessage mimeMessage = new MimeMessage(session);
        MimeMessageF.Consumers.recipient(RecipientType.CC, theEmailAddress).accept(mimeMessage);
        assertEquals(theEmailAddress, mimeMessage.getRecipients(RecipientType.CC)[0].toString());
    }

    @ParameterizedTest
    @MethodSource("emailAddresses")
    public void testRecipientBcc(String theEmailAddress) throws MessagingException {
        MimeMessage mimeMessage = new MimeMessage(session);
        MimeMessageF.Consumers.recipient(RecipientType.BCC, theEmailAddress).accept(mimeMessage);
        assertEquals(theEmailAddress, mimeMessage.getRecipients(RecipientType.BCC)[0].toString());
    }

    @ParameterizedTest
    @MethodSource("emailAddresses")
    public void testRecipientsTo(String theEmailAddress) throws MessagingException {
        MimeMessage mimeMessage = new MimeMessage(session);
        MimeMessageF.Consumers.recipients(RecipientType.TO, new String[]{theEmailAddress}).accept(mimeMessage);
        assertEquals(theEmailAddress, mimeMessage.getRecipients(RecipientType.TO)[0].toString());
    }

    @ParameterizedTest
    @MethodSource("emailAddresses")
    public void testRecipientsCc(String theEmailAddress) throws MessagingException {
        MimeMessage mimeMessage = new MimeMessage(session);
        MimeMessageF.Consumers.recipients(RecipientType.CC, new String[]{theEmailAddress}).accept(mimeMessage);
        assertEquals(theEmailAddress, mimeMessage.getRecipients(RecipientType.CC)[0].toString());
    }

    @ParameterizedTest
    @MethodSource("emailAddresses")
    public void testRecipientsBcc(String theEmailAddress) throws MessagingException {
        MimeMessage mimeMessage = new MimeMessage(session);
        MimeMessageF.Consumers.recipients(RecipientType.BCC, new String[]{theEmailAddress}).accept(mimeMessage);
        assertEquals(theEmailAddress, mimeMessage.getRecipients(RecipientType.BCC)[0].toString());
    }

    @ParameterizedTest
    @MethodSource("emailAddresses")
    public void testAllRecipients(String theEmailAddress) throws MessagingException {
        String fromtheEmailAddress = "from" + theEmailAddress;
        String replytheEmailAddress = "replyTo" + theEmailAddress;
        String totheEmailAddress = "to" + theEmailAddress;
        String cctheEmailAddress = "cc" + theEmailAddress;
        String bcctheEmailAddress = "bcc" + theEmailAddress;

        MimeMessage mimeMessage = new MimeMessage(session);
        MimeMessageF.Consumers
                .from(fromtheEmailAddress)
                .andThen(Consumers.replyTo(replytheEmailAddress))
                .andThen(recipients(RecipientType.TO, new String[]{totheEmailAddress}))
                .andThen(recipients(RecipientType.CC, new String[]{cctheEmailAddress}))
                .andThen(recipients(RecipientType.BCC, new String[]{bcctheEmailAddress}))
                .accept(mimeMessage);
        assertAll(
                () -> assertEquals(fromtheEmailAddress, mimeMessage.getFrom()[0].toString()),
                () -> assertEquals(replytheEmailAddress, mimeMessage.getReplyTo()[0].toString()),
                () -> assertEquals(totheEmailAddress, mimeMessage.getRecipients(RecipientType.TO)[0].toString()),
                () -> assertEquals(cctheEmailAddress, mimeMessage.getRecipients(RecipientType.CC)[0].toString()),
                () -> assertEquals(bcctheEmailAddress, mimeMessage.getRecipients(RecipientType.BCC)[0].toString())
        );
        assertAll(
                () -> assertEquals(fromtheEmailAddress, MimeMessageF.Providers.from().apply(mimeMessage)[0].toString()),
                () -> assertEquals(replytheEmailAddress, MimeMessageF.Providers.replyTo().apply(mimeMessage)[0].toString()),
                () -> assertEquals(3, MimeMessageF.Providers.allRecipients().apply(mimeMessage).length),
                () -> assertEquals(totheEmailAddress, MimeMessageF.Providers.recipients(RecipientType.TO).apply(mimeMessage)[0].toString()),
                () -> assertEquals(cctheEmailAddress, MimeMessageF.Providers.recipients(RecipientType.CC).apply(mimeMessage)[0].toString()),
                () -> assertEquals(bcctheEmailAddress, MimeMessageF.Providers.recipients(RecipientType.BCC).apply(mimeMessage)[0].toString())
        );

    }
}
