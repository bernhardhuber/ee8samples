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
package org.huberb.ee8sample.mail.experimental;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Properties;
import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import org.huberb.ee8sample.mail.MimeMessageF;
import org.huberb.ee8sample.mail.SessionF;
import org.huberb.ee8sample.mail.experimental.StoringOnlyTransport.MimeMessageStringReps;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

/**
 *
 * @author berni3
 */
public class MimeMessageStringRepsTest {

    @Test
    public void helloA() throws MessagingException, IOException {
        Session session = Session.getDefaultInstance(new Properties());
        MimeMessage mm = SessionF.MimeMessages.mimeMessage().apply(session);
        MimeMessageF.Consumers.from("me")
                .andThen(MimeMessageF.Consumers.to("you"))
                .andThen(MimeMessageF.Consumers.subject("the-subject"))
                .andThen(MimeMessageF.Consumers.text("the-text"))
                .accept(mm);
        String expected = "addresses: [you], msg: subject: 'the-subject', text: 'the-text'";
        assertEquals(expected, MimeMessageStringReps.createStringRepA(mm, mm.getAllRecipients()));
    }

    @Test
    public void helloB() throws MessagingException, IOException {
        Session session = Session.getDefaultInstance(new Properties());
        MimeMessage mm = SessionF.MimeMessages.mimeMessage().apply(session);
        MimeMessageF.Consumers.from("me")
                .andThen(MimeMessageF.Consumers.to("you"))
                .andThen(MimeMessageF.Consumers.subject("the-subject"))
                .andThen(MimeMessageF.Consumers.text("the-text"))
                .accept(mm);
        String expected = "'from':['me'], 'reply-to':['me'], 'to':['you'], 'cc':['you'], 'bcc':['you'], 'subject':'the-subject' ";
        assertEquals(expected, MimeMessageStringReps.createStringRepB(mm, mm.getAllRecipients()));
    }

    @Test
    public void helloC() throws MessagingException, IOException, ClassNotFoundException {
        Session session = Session.getDefaultInstance(new Properties());
        MimeMessage mm = SessionF.MimeMessages.mimeMessage().apply(session);
        MimeMessageF.Consumers.from("me")
                .andThen(MimeMessageF.Consumers.to("you"))
                .andThen(MimeMessageF.Consumers.subject("the-subject"))
                .andThen(MimeMessageF.Consumers.text("the-text"))
                .accept(mm);
        Address[] addresses = mm.getAllRecipients();
        assertAll(
                () -> assertNotNull(addresses),
                () -> assertEquals(1, addresses.length),
                () -> assertEquals("you", addresses[0].toString())
        );
        byte[] bytes = Serialization.serialize(addresses);
        Address[] addressesUnserialized = Serialization.unserialize(bytes);
        assertAll(
                () -> assertNotNull(addressesUnserialized),
                () -> assertEquals(1, addressesUnserialized.length),
                () -> assertEquals("you", addressesUnserialized[0].toString())
        );

    }

    static class Serialization {

        static <T extends Serializable> byte[] serialize(T t) throws IOException {
            byte[] mmBytes = null;
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream(); ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                oos.writeObject(t);
                oos.flush();
                baos.flush();
                mmBytes = baos.toByteArray();
            }
            return mmBytes;
        }

        static <T extends Serializable> T unserialize(byte[] mmBytes) throws IOException, ClassNotFoundException {
            T t = null;
            try (ByteArrayInputStream bais = new ByteArrayInputStream(mmBytes); ObjectInputStream ois = new ObjectInputStream(bais)) {
                t = (T) ois.readObject();
            }
            return t;
        }
    }
}
