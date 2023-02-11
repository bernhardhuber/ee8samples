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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.mail.Address;
import javax.mail.Header;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Provider;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.URLName;
import org.huberb.ee8sample.mail.SessionF;

/**
 *
 * @author berni3
 */
public class StoringOnlyTransport extends Transport {

    static class Factory {

        public static String protocol() {
            return "storingOnly";
        }

        public static Provider provider() {
            String protocol = protocol();
            return new Provider(Provider.Type.TRANSPORT, protocol, StoringOnlyTransport.class.getName(), "huberb", "1.0-SNAPSHOT");
        }

        public static Session createDefaultSession() {
            String protocol = StoringOnlyTransport.Factory.protocol();
            Properties props = new Properties() {
                {
                    setProperty("mail.transport.protocol.rfc822", protocol);
                    setProperty("mail.transport.protocol.smtp", protocol);
                    setProperty("mail." + protocol + ".class", StoringOnlyTransport.class.getName());
                }
            };
            Provider provider = StoringOnlyTransport.Factory.provider();
            Session session = Session.getInstance(props, null);
            SessionF.Consumers.debug(true).accept(session);
            session.addProvider(provider);
            return session;
        }
    }

    //-------------------------------------------------------------------------
    private final static List<String> SENT_MESSAGES_LIST = Collections.synchronizedList(new ArrayList<String>());

    private final static List<String> dataSingletonInstance() {
        return SENT_MESSAGES_LIST;
    }

    public StoringOnlyTransport(Session session, URLName urlname) {
        super(session, urlname);
    }

    @Override
    public void sendMessage(Message msg, Address[] addresses) throws MessagingException {
        try {
            String result = MimeMessageStringReps.createStringRepB(msg, addresses);
            dataSingletonInstance().add(result);
        } catch (IOException ioex) {
            throw new MessagingException("sendMessage", ioex);
        }
    }

    @Override
    protected boolean protocolConnect(String host, int port, String user,
            String password) throws MessagingException {
        return true;
    }

    public List<String> sentMessages() {
        return dataSingletonInstance();
    }

    public void clearSentMessages() {
        dataSingletonInstance().clear();
    }

    static class MimeMessageStringReps {

        static String createStringRepA(Message msg, Address[] addresses) throws MessagingException, IOException {
            String msgAsString = String.format("subject: '%s', text: '%s'",
                    msg.getSubject(),
                    String.valueOf(msg.getContent())
            );
            String addressesAsString = Arrays.toString(addresses);

            String result = String.format("addresses: %s, msg: %s", addressesAsString, msgAsString);
            return result;
        }

        public static Function<Address[], List<Address>> addressesF = (addresses) -> {
            return Optional.ofNullable(addresses).map(as -> Arrays.asList(as)).orElse(Collections.emptyList());
        };

        static String createStringRepB(Message msg, Address[] addresses) throws MessagingException, IOException {
            StringBuilder sb = new StringBuilder();

            List<Address> froms = addressesF.apply(msg.getFrom());
            List<Address> replyTos = addressesF.apply(msg.getReplyTo());
            List<Address> tos = addressesF.apply(msg.getRecipients(RecipientType.TO));
            List<Address> ccs = addressesF.apply(msg.getRecipients(RecipientType.TO));
            List<Address> bccs = addressesF.apply(msg.getRecipients(RecipientType.TO));
            List<Header> headers = Collections.emptyList();
            String subject = msg.getSubject();

            Function<List<? extends Address>, String> listToStringF = (l) -> Optional.ofNullable(l)
                    .map(lst -> lst.stream().map(a -> String.format("'%s'", a.toString())).collect(Collectors.joining(",", "[", "]")))
                    .orElse("[]");

            sb.append(String.format("'from':%s, ", Optional.ofNullable(froms).map(l -> listToStringF.apply(l)).orElse("")));
            sb.append(String.format("'reply-to':%s, ", Optional.ofNullable(replyTos).map(l -> listToStringF.apply(l)).orElse("")));

            sb.append(String.format("'to':%s, ", Optional.ofNullable(tos).map(l -> listToStringF.apply(l)).orElse("")));
            sb.append(String.format("'cc':%s, ", Optional.ofNullable(ccs).map(l -> listToStringF.apply(l)).orElse("")));
            sb.append(String.format("'bcc':%s, ", Optional.ofNullable(bccs).map(l -> listToStringF.apply(l)).orElse("")));

            sb.append(String.format("'subject':'%s' ", Optional.ofNullable(subject).map(f -> f.toString()).orElse("")));

            return sb.toString();
        }
    }

}
