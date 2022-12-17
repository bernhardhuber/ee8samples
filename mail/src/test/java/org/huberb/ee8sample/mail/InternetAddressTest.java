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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 *
 * @author berni3
 */
public class InternetAddressTest {

    Consumer<String[]> cInternetAddressF_1 = d -> {
        String expectedAddress = d[0];
        String inaddress = d[1];

        InternetAddressF iaf = new InternetAddressF();
        iaf.consume(Consumers.address(inaddress)
                .andThen(Consumers.validate())
        );

        InternetAddress ia = iaf.getInternetAddress();
        assertEquals(expectedAddress, ia.toString());
    };
    Consumer<String[]> cInternetAddressF_2 = d -> {
        String expectedAddress = d[0];
        String inaddress = d[1];

        InternetAddressF iaf = new InternetAddressF();
        iaf.consume(Consumers.addressPersonalValidate(inaddress, null));

        InternetAddress ia = iaf.getInternetAddress();
        assertEquals(expectedAddress, ia.toString());

    };
    Consumer<String[]> cInternetAddressBuilderF_1 = d -> {
        String expectedAddress = d[0];
        String inaddress = d[1];

        InternetAddress ia = new InternetAddressBuilderF()
                .address(inaddress)
                .build();

        assertEquals(expectedAddress, ia.toString());
    };
    Consumer<String[]> cInternetAddressBuilderTraditional_1 = d -> {
        String expectedAddress = d[0];
        String inaddress = d[1];

        InternetAddress ia = new InternetAddressBuilderTraditional()
                .address(inaddress)
                .build();

        assertEquals(expectedAddress, ia.toString());
    };
    Consumer<String[]> cInternetAddressBuilderTraditional_2 = d -> {
        String expectedAddress = d[0];
        String inaddress = d[1];
        String inpersonal = d[2];

        InternetAddress ia = new InternetAddressBuilderTraditional()
                .address(inaddress)
                .personal(inpersonal)
                .build();

        assertEquals(expectedAddress, ia.toString());
    };

    @ParameterizedTest
    @CsvSource(value = {
        "me@localhost,me@localhost",
        "you@somehost.com,you@somehost.com",
        "some@somehost.com,some@somehost.com",})
    public void given_address_then_create_an_InternetAddress(String expectedAddress, String inAddress) {
        String[] d = new String[]{expectedAddress, inAddress};
        assertAll(
                () -> cInternetAddressF_1.accept(d),
                () -> cInternetAddressF_2.accept(d),
                () -> cInternetAddressBuilderF_1.accept(d),
                () -> cInternetAddressBuilderTraditional_1.accept(d));
    }

    @Test
    public void given_address_and_personal_then_create_an_InternetAddress() {
        String[] d = new String[]{"Me <me@localhost>", "me@localhost", "Me"};
        cInternetAddressBuilderTraditional_2.accept(d);
    }

}
