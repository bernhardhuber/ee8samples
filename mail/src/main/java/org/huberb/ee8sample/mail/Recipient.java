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

import java.util.ArrayList;
import java.util.List;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;

/**
 * Define a recipient by its {@link RecipientType} and list of
 * {@link InternetAddress} sharing the same {@link RecipientType}.
 */
public class Recipient {

    private RecipientType recipientType;
    private final List<InternetAddress> internetAddressList = new ArrayList<>();

    public RecipientType getRecipientType() {
        return recipientType;
    }

    public InternetAddress[] getInternetAddressAsArray() {
        return internetAddressList.toArray(InternetAddress[]::new);
    }

    public List<InternetAddress> getInternetAddress() {
        return internetAddressList;
    }

    public static class RecipientBuilder {

        private final Recipient recipient = new Recipient();

        public RecipientBuilder addAddress(RecipientType recipientType, List<InternetAddress> internetAddress) {
            this.recipient.recipientType = recipientType;
            this.recipient.internetAddressList.addAll(internetAddress);
            return this;
        }

        public RecipientBuilder recipientType(RecipientType recipientType) {
            this.recipient.recipientType = recipientType;
            return this;
        }

        public RecipientBuilder addAddress(InternetAddress internetAddress) {
            this.recipient.internetAddressList.add(internetAddress);
            return this;
        }

        public Recipient build() {
            return this.recipient;
        }
    }
    
}
