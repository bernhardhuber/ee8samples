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
import java.util.Properties;
import java.util.stream.Stream;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
    public void hello1(String theEmailAddress) throws MessagingException {
        Session session = Session.getInstance(new Properties(), null);
        MimeMessage mimeMessage = new MimeMessage(session);
        MimeMessageF.Consumers.from(theEmailAddress).accept(mimeMessage);
        assertEquals(theEmailAddress, mimeMessage.getFrom()[0].toString());
    }

    @ParameterizedTest
    @MethodSource("emailAddresses")
    public void hello2(String theEmailAddress) throws MessagingException {
        Session session = Session.getInstance(new Properties(), null);
        MimeMessage mimeMessage = new MimeMessage(session);
        MimeMessageF.Consumers.replyTo(theEmailAddress).accept(mimeMessage);
        assertEquals(theEmailAddress, mimeMessage.getReplyTo()[0].toString());
    }

    @ParameterizedTest
    @MethodSource("emailAddresses")
    public void testTo(String theEmailAddress) throws MessagingException {
        Session session = Session.getInstance(new Properties(), null);
        MimeMessage mimeMessage = new MimeMessage(session);
        MimeMessageF.Consumers.to(theEmailAddress).accept(mimeMessage);
        assertEquals(theEmailAddress, mimeMessage.getRecipients(RecipientType.TO)[0].toString());
    }

    @ParameterizedTest
    @MethodSource("emailAddresses")
    public void testRecipientTo(String theEmailAddress) throws MessagingException {
        Session session = Session.getInstance(new Properties(), null);
        MimeMessage mimeMessage = new MimeMessage(session);
        MimeMessageF.Consumers.recipient(RecipientType.TO, theEmailAddress).accept(mimeMessage);
        assertEquals(theEmailAddress, mimeMessage.getRecipients(RecipientType.TO)[0].toString());
    }

    @ParameterizedTest
    @MethodSource("emailAddresses")
    public void testRecipientCc(String theEmailAddress) throws MessagingException {
        Session session = Session.getInstance(new Properties(), null);
        MimeMessage mimeMessage = new MimeMessage(session);
        MimeMessageF.Consumers.recipient(RecipientType.CC, theEmailAddress).accept(mimeMessage);
        assertEquals(theEmailAddress, mimeMessage.getRecipients(RecipientType.CC)[0].toString());
    }

    @ParameterizedTest
    @MethodSource("emailAddresses")
    public void testRecipientBcc(String theEmailAddress) throws MessagingException {
        Session session = Session.getInstance(new Properties(), null);
        MimeMessage mimeMessage = new MimeMessage(session);
        MimeMessageF.Consumers.recipient(RecipientType.BCC, theEmailAddress).accept(mimeMessage);
        assertEquals(theEmailAddress, mimeMessage.getRecipients(RecipientType.BCC)[0].toString());
    }
}
