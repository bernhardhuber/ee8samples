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

    Consumer<String[]> cInternetAddressF_1 = d -> {
        String inaddress = d[0];
        String inpersonal = d[1];
        String expectedAddress = d[2];

        InternetAddressF iaf = new InternetAddressF();
        iaf.consume(Consumers.address(inaddress)
                .andThen(Consumers.validate())
        );

        InternetAddress ia = iaf.getInternetAddress();
        assertEquals(expectedAddress, ia.getAddress());
    };
    Consumer<String[]> cInternetAddressF_2 = d -> {
        String inaddress = d[0];
        String inpersonal = d[1];
        String expectedAddress = d[2];
        InternetAddressF iaf = new InternetAddressF();
        iaf.consume(Consumers.addressPersonalValidate(inaddress, inpersonal));

        InternetAddress ia = iaf.getInternetAddress();
        assertEquals(expectedAddress, ia.getAddress());

    };
    Consumer<String[]> cInternetAddressBuilderF_1 = d -> {
        String inaddress = d[0];
        String inpersonal = d[1];
        String expectedAddress = d[2];
        InternetAddress ia = new InternetAddressBuilderF()
                .address(inaddress)
                .build();

        assertEquals(expectedAddress, ia.getAddress());
    };
    Consumer<String[]> cInternetAddressBuilderTraditional_1 = d -> {
        String inaddress = d[0];
        String inpersonal = d[1];
        String expectedAddress = d[2];

        InternetAddress ia = new InternetAddressBuilderTraditional()
                .address(inaddress)
                .build();

        assertEquals(expectedAddress, ia.getAddress());
    };

    @ParameterizedTest
    @CsvSource(value = {
        "me@localhost, '', me@localhost",
        "you@somehost.com, '', you@somehost.com",})
    public void hello(String inAddress, String inPersonal, String expectedAddress) {
        String[] d = new String[]{inAddress, inPersonal, expectedAddress};
        assertAll(
                () -> cInternetAddressF_1.accept(d),
                () -> cInternetAddressF_2.accept(d),
                () -> cInternetAddressBuilderF_1.accept(d),
                () -> cInternetAddressBuilderTraditional_1.accept(d));

    }

}
