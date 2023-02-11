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
import java.util.stream.Stream;
import javax.mail.Message.RecipientType;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 *
 * @author berni3
 */
public class RecipientTest {

    @ParameterizedTest
    @MethodSource("emailAddresses")
    public void hello1(String theEmailAddress) throws AddressException {
        final Recipient recipient = new Recipient.RecipientBuilder()
                .recipientType(RecipientType.TO)
                .addAddress(new InternetAddress(theEmailAddress))
                .build();
        assertEquals(RecipientType.TO, recipient.getRecipientType());
        assertEquals(theEmailAddress, recipient.getInternetAddress().get(0).toString());
        assertEquals(theEmailAddress, recipient.getInternetAddressAsArray()[0].toString());
    }

    @ParameterizedTest
    @MethodSource("emailAddresses")
    public void hello2(String theEmailAddress) throws AddressException {
        final Recipient recipient = new Recipient.RecipientBuilder()
                .addAddress(RecipientType.TO, Arrays.asList(new InternetAddress(theEmailAddress)))
                .build();
        assertEquals(RecipientType.TO, recipient.getRecipientType());
        assertEquals(theEmailAddress, recipient.getInternetAddress().get(0).toString());
        assertEquals(theEmailAddress, recipient.getInternetAddressAsArray()[0].toString());
    }

    public static Stream<String> emailAddresses() {
        return Stream.of(
                "somefirstname.somelastname@somehost",
                "somefirstname.somelastname@somehost.somedomain",
                "somename@somehost"
        );
    }
}
