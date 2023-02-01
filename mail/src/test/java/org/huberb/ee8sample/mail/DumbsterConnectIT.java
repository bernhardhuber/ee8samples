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

import com.dumbster.smtp.SimpleSmtpServer;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import org.huberb.ee8sample.mail.DumbsterSendMessageIT.DumbsterSessionBuilder;
import org.huberb.ee8sample.mail.MailsF.SessionsF;
import org.huberb.ee8sample.mail.MailsF.TransportsF;
import org.huberb.ee8sample.mail.Supports.ConsumerThrowingMessagingException;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 *
 * @author berni3
 */
public class DumbsterConnectIT {

    @Test
    public void testCanConnect() throws IOException, MessagingException {
        try (SimpleSmtpServer dumbster = SimpleSmtpServer.start(SimpleSmtpServer.AUTO_SMTP_PORT)) {
            final Session session = new DumbsterSessionBuilder()
                    .port(dumbster.getPort())
                    .build();

            try (Transport transport = SessionsF.Transports.transport().apply(session)) {
                assertFalse(transport.isConnected());
                TransportsF.Consumers.withConnected(transport,
                        (t) -> {
                            assertTrue(transport.isConnected());
                        });
            }
        }

    }

    @Test
    public void testCannotConnect() throws IOException {
        try (SimpleSmtpServer dumbster = SimpleSmtpServer.start(SimpleSmtpServer.AUTO_SMTP_PORT)) {
            final Session session = new DumbsterSessionBuilder()
                    .port(dumbster.getPort())
                    .build();
            dumbster.stop();

            MessagingException rtex = Assertions.assertThrows(MessagingException.class,
                    () -> {
                        try (Transport transport = SessionsF.Transports.transport().apply(session)) {
                            assertFalse(transport.isConnected());

                            TransportsF.Consumers.withConnected(transport, ConsumerThrowingMessagingException.NOOP());
                            assertFalse(transport.isConnected());
                        }
                    });
            assertNotNull(rtex);
            assertAll(
                    () -> {
                        Pattern exMessagePattern = Pattern.compile("Couldn't connect to host, port: localhost, [0-9]+; timeout -1");
                        String exMessage = rtex.getMessage();
                        Matcher matcher = exMessagePattern.matcher(exMessage);
                        String m = String.format("pattern: '%s'%n"
                                + "exception message: '%s'%n", exMessagePattern, exMessage);
                        assertTrue(matcher.matches(), m);
                    }
            );

        }
    }

}
