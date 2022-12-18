/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.huberb.ee8sample.mail;

import java.util.function.Consumer;
import javax.mail.internet.InternetAddress;
import org.huberb.ee8sample.mail.MailsF.InternetAddressBuilderF;
import org.huberb.ee8sample.mail.MailsF.InternetAddressBuilderTraditional;
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

            InternetAddress ia = new InternetAddressBuilderF()
                    .address(inaddress)
                    .build();

            assertEquals(expectedAddress, ia.toString());
        };
        static Consumer<String[]> cInternetAddressBuilderTraditional_address = d -> {
            String expectedAddress = d[0];
            String inaddress = d[1];

            InternetAddress ia = new InternetAddressBuilderTraditional()
                    .address(inaddress)
                    .build();

            assertEquals(expectedAddress, ia.toString());
        };
    }

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

            InternetAddress ia = new InternetAddressBuilderF()
                    .address(inaddress)
                    .personal(inpersonal)
                    .build();

            assertEquals(expectedAddress, ia.toString());
        };
        static Consumer<String[]> cInternetAddressBuilderTraditional_address_personal = d -> {
            String expectedAddress = d[0];
            String inaddress = d[1];
            String inpersonal = d[2];

            InternetAddress ia = new InternetAddressBuilderTraditional()
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
