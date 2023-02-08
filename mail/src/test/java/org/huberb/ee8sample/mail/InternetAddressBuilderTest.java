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

import java.util.stream.Stream;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 *
 * @author berni3
 */
public class InternetAddressBuilderTest {

    @ParameterizedTest
    @MethodSource("createSddressPersonalStream")
    public void test_address_validate(String theEmailAddress, String thePersonalname) throws AddressException {
        //final String theEmailAddress = "somename@somehost";
        InternetAddress internetAddress = new InternetAddressBuilder()
                .address(theEmailAddress)
                .validate()
                .build();
        assertAll(
                () -> assertEquals(theEmailAddress, internetAddress.getAddress()),
                () -> assertEquals(null, internetAddress.getGroup(true)),
                () -> assertEquals(null, internetAddress.getGroup(false)),
                () -> assertEquals(null, internetAddress.getPersonal()),
                () -> assertEquals("rfc822", internetAddress.getType())
        );
    }

    @ParameterizedTest
    @MethodSource("createSddressPersonalStream")
    public void  test_addresspersonal_validate(String theEmailAddress, String thePersonalname) throws AddressException {
        InternetAddress internetAddress = new InternetAddressBuilder()
                .addressPersonal(theEmailAddress, thePersonalname)
                .validate()
                .build();
        assertAll(
                () -> assertEquals(theEmailAddress, internetAddress.getAddress()),
                () -> assertEquals(null, internetAddress.getGroup(true)),
                () -> assertEquals(null, internetAddress.getGroup(false)),
                () -> assertEquals(thePersonalname, internetAddress.getPersonal()),
                () -> assertEquals("rfc822", internetAddress.getType())
        );
    }

    @ParameterizedTest
    @MethodSource("createSddressPersonalStream")
    public void test_address_personal_validate(String theEmailAddress, String thePersonalname) throws AddressException {
        InternetAddress internetAddress = new InternetAddressBuilder()
                .address(theEmailAddress)
                .personal(thePersonalname)
                .validate()
                .build();
        assertAll(
                () -> assertEquals(theEmailAddress, internetAddress.getAddress()),
                () -> assertEquals(null, internetAddress.getGroup(true)),
                () -> assertEquals(null, internetAddress.getGroup(false)),
                () -> assertEquals(thePersonalname, internetAddress.getPersonal()),
                () -> assertEquals("rfc822", internetAddress.getType())
        );
    }

    public static Stream<String[]> createSddressPersonalStream() {
        Stream.Builder<String[]> b = Stream.builder();
        return b
                .add(new String[]{"somename@somehost", "Somename"})
                .add(new String[]{"somefirstname.somelastname@somehost", "Somefirstanem Somelastname"})
                .add(new String[]{"somename@somehost.com", "Somename"})
                .build();
    }
}
