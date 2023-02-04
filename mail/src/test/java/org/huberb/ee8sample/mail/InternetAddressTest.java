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

import java.util.function.Consumer;
import javax.mail.internet.InternetAddress;
import org.huberb.ee8sample.mail.MailsF.InternetAddressBuilder;
import org.huberb.ee8sample.mail.MailsF.InternetAddressF;
import org.huberb.ee8sample.mail.MailsF.InternetAddressF.Consumers;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 *
 * @author berni3
 */
public class InternetAddressTest {

    /**
     * Build internet address from address without personal.
     */
    static class G1 {

        static Consumer<String[]> cInternetAddressF_address = d -> {
            String expectedAddress = d[0];
            String inaddress = d[1];

            InternetAddressF iaf = new InternetAddressF();
            iaf.consume(Consumers.address(inaddress)
                    .andThen(Consumers.validate())
            );

            InternetAddress ia = iaf.getInternetAddress();
            assertEquals(expectedAddress, ia.toString());
        };
        static Consumer<String[]> cInternetAddressFv2_address_null = d -> {
            String expectedAddress = d[0];
            String inaddress = d[1];

            InternetAddressF iaf = new InternetAddressF();
            iaf.consume(Consumers.addressPersonalValidate(inaddress, null));

            InternetAddress ia = iaf.getInternetAddress();
            assertEquals(expectedAddress, ia.toString());

        };
        static Consumer<String[]> cInternetAddressBuilderF_address = d -> {
            String expectedAddress = d[0];
            String inaddress = d[1];

            InternetAddress ia = new InternetAddressBuilder()
                    .address(inaddress)
                    .build();

            assertEquals(expectedAddress, ia.toString());
        };
        static Consumer<String[]> cInternetAddressBuilderTraditional_address = d -> {
            String expectedAddress = d[0];
            String inaddress = d[1];

            InternetAddress ia = new InternetAddressBuilder()
                    .address(inaddress)
                    .build();

            assertEquals(expectedAddress, ia.toString());
        };
    }

    /**
     * Build internet address from address plus personal.
     */
    static class G2 {

        static Consumer<String[]> cInternetAddressF_address_personal = d -> {
            String expectedAddress = d[0];
            String inaddress = d[1];
            String inpersonal = d[2];

            InternetAddressF iaf = new InternetAddressF();
            iaf.consume(Consumers.address(inaddress)
                    .andThen(Consumers.personal(inpersonal))
                    .andThen(Consumers.validate())
            );

            InternetAddress ia = iaf.getInternetAddress();
            assertEquals(expectedAddress, ia.toString());
        };
        static Consumer<String[]> cInternetAddressFv2_address_personal = d -> {
            String expectedAddress = d[0];
            String inaddress = d[1];
            String inpersonal = d[2];

            InternetAddressF iaf = new InternetAddressF();
            iaf.consume(Consumers.addressPersonalValidate(inaddress, inpersonal));

            InternetAddress ia = iaf.getInternetAddress();
            assertEquals(expectedAddress, ia.toString());

        };
        static Consumer<String[]> cInternetAddressBuilderF_address_personal = d -> {
            String expectedAddress = d[0];
            String inaddress = d[1];
            String inpersonal = d[2];

            InternetAddress ia = new InternetAddressBuilder()
                    .address(inaddress)
                    .personal(inpersonal)
                    .build();

            assertEquals(expectedAddress, ia.toString());
        };
        static Consumer<String[]> cInternetAddressBuilderTraditional_address_personal = d -> {
            String expectedAddress = d[0];
            String inaddress = d[1];
            String inpersonal = d[2];

            InternetAddress ia = new InternetAddressBuilder()
                    .address(inaddress)
                    .personal(inpersonal)
                    .build();

            assertEquals(expectedAddress, ia.toString());
        };
    }

    @ParameterizedTest
    @CsvSource(value = {
        "me@localhost,me@localhost",
        "you@somehost.com,you@somehost.com",
        "some@somehost.com,some@somehost.com",})
    public void given_address_then_create_an_InternetAddress(String expectedAddress, String inAddress) {
        String[] d = new String[]{expectedAddress, inAddress};
        assertAll(() -> G1.cInternetAddressF_address.accept(d),
                () -> G1.cInternetAddressFv2_address_null.accept(d),
                () -> G1.cInternetAddressBuilderF_address.accept(d),
                () -> G1.cInternetAddressBuilderTraditional_address.accept(d));
    }

    @ParameterizedTest
    @CsvSource(value = {
        "Me <me@localhost>, me@localhost, Me",
        "You Too <you@somehost.com>,you@somehost.com,You Too",})
    public void given_address_and_personal_then_create_an_InternetAddress(String expectedAddress, String inAddress, String inPersonal) {
        String[] d = new String[]{expectedAddress, inAddress, inPersonal};
        assertAll(() -> G2.cInternetAddressF_address_personal.accept(d),
                () -> G2.cInternetAddressFv2_address_personal.accept(d),
                () -> G2.cInternetAddressBuilderF_address_personal.accept(d),
                () -> G2.cInternetAddressBuilderTraditional_address_personal.accept(d)
        );
    }

}
