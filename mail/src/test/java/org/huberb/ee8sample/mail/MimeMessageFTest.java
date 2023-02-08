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
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.mail.Address;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.huberb.ee8sample.mail.Recipient.RecipientBuilder;
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
    public void testAddressesFromStringList(String theEmailAddress) {
        assertEquals(theEmailAddress, MimeMessageF.Providers.addressesFromStringList()
                .apply(Arrays.asList(theEmailAddress))
                .get(0).toString()
        );
    }

    @ParameterizedTest
    @MethodSource("emailAddresses")
    public void testAddressesFromStrings(String theEmailAddress) {
        assertEquals(theEmailAddress, MimeMessageF.Providers.addressesFromStrings()
                .apply(new String[]{theEmailAddress})
                .get(0).toString()
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
    public void testReplyToAddresses() throws MessagingException {
        List<String> addressesAsStringList = emailAddresses().collect(Collectors.toList());

        List<Address> addressesList = addressesAsStringList.
                stream()
                .collect(
                        () -> new ArrayList<Address>(),
                        (l, s) -> l.add(MimeMessageF.Providers.addressFromStringThrowingMailRuntimeException().apply(s)),
                        (l1, l2) -> l1.addAll(l2));
        MimeMessage mimeMessage = new MimeMessage(session);
        MimeMessageF.Consumers.replyTo(addressesList).accept(mimeMessage);
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
}
