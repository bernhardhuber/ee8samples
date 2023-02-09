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
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Provider;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.URLName;

/**
 *
 * @author berni3
 */
public class StoringOnlyTransport extends Transport {

    public static String protocol() {
        return "storingOnly";
    }

    public static Provider provider() {
        String protocol = protocol();
        return new Provider(Provider.Type.TRANSPORT, protocol, StoringOnlyTransport.class.getName(), "huberb", "1.0-SNAPSHOT");
    }

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
            String msgAsString = String.format("subject: '%s', text: '%s'", msg.getSubject(), String.valueOf(msg.getContent()));
            String addressesAsString = Arrays.toString(addresses);

            String result = String.format("addresses: %s, msg: %s", addressesAsString, msgAsString);
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

}
