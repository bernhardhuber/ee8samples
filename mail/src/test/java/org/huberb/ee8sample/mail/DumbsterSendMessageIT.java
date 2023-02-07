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
import com.dumbster.smtp.SmtpMessage;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.huberb.ee8sample.mail.MimeMessageF;
import org.huberb.ee8sample.mail.SessionF;
import org.huberb.ee8sample.mail.Supports.ConsumerThrowingMessagingException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 *
 * @author berni3
 */
public class DumbsterSendMessageIT {

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    public void testSimpleSendMessage(int i) throws MessagingException, IOException {
        try (SimpleSmtpServer dumbster = SimpleSmtpServer.start(SimpleSmtpServer.AUTO_SMTP_PORT)) {

            final Session session = new DumbsterSessionBuilder().port(dumbster.getPort()).build();

            final MimeMessage mimeMessage;
            switch (i) {
                case 1:
                    mimeMessage = createMessage1(session, "sender@here.com", "receiver@there.com", "Test", "Test Body");
                    break;
                case 2:
                    mimeMessage = createMessage2(session, "sender@here.com", "receiver@there.com", "Test", "Test Body");
                    break;
                case 3:
                    mimeMessage = createMessage3(session, "sender@here.com", "receiver@there.com", "Test", "Test Body");
                    break;
                default:
                    mimeMessage = null;
            }
            // send the message
            Transport.send(mimeMessage);

            final List<SmtpMessage> smptMessageList = dumbster.getReceivedEmails();
            assertEquals(1, smptMessageList.size());
            final SmtpMessage smtpMessage = smptMessageList.get(0);
            assertEquals("Test", smtpMessage.getHeaderValue("Subject"));
            assertEquals("Test Body", smtpMessage.getBody());
            assertEquals("receiver@there.com", smtpMessage.getHeaderValue("To"));
        }
    }

    static class DumbsterSessionBuilder {

        Session session;

        public DumbsterSessionBuilder port(int port) {
            Properties mailProps = createMailProperties(port);
            this.session = Session.getInstance(mailProps, null);
            //session.setDebug(true);
            return this;
        }

        public Session build() {
            return session;
        }

        private Properties createMailProperties(int port) {
            Properties mailProps = new Properties();
            mailProps.setProperty("mail.smtp.host", "localhost");
            mailProps.setProperty("mail.smtp.port", "" + port);
            mailProps.setProperty("mail.smtp.sendpartial", "true");
            return mailProps;
        }
    }

    private static MimeMessage createMessage1(Session session,
            String from, String to,
            String subject,
            String body) throws MessagingException {
        MimeMessage msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(from));
        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
        msg.setSubject(subject);
        msg.setSentDate(new Date());
        msg.setText(body);
        return msg;
    }

    private static MimeMessage createMessage2(Session session,
            String from, String to,
            String subject,
            String body) throws MessagingException {

        ConsumerThrowingMessagingException<MimeMessage> c = MimeMessageF.Consumers.from(from)
                .andThen(MimeMessageF.Consumers.recipient(RecipientType.TO, to))
                .andThen(MimeMessageF.Consumers.subject(subject))
                .andThen(MimeMessageF.Consumers.sentDate(new Date()))
                .andThen(MimeMessageF.Consumers.text(body));

        final MimeMessage mimeMessage = SessionF.MimeMessages.mimeMessage().apply(session);
        c.accept(mimeMessage);
        return mimeMessage;
    }

    private static MimeMessage createMessage3(Session session,
            String from, String to,
            String subject,
            String body) throws MessagingException {

        ConsumerThrowingMessagingException<MimeMessage> c = (MimeMessage msg) -> {
            msg.setFrom(new InternetAddress(from));
            msg.setSubject(subject);
            msg.setSentDate(new Date());
            msg.setText(body);
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
        };
        final MimeMessage mimeMessage = SessionF.MimeMessages.mimeMessage().apply(session);
        c.accept(mimeMessage);
        return mimeMessage;
    }
}
