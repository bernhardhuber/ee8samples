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

import java.io.UnsupportedEncodingException;
import java.util.function.Consumer;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import org.huberb.ee8sample.mail.Supports.MailRuntimeException;

/**
 * Functional inspired wrapper for mail {@link InternetAddress}.
 */
public class InternetAddressF {

    static class Consumers {

        static Consumer<InternetAddress> addressPersonalValidate(String address, String personal) {
            return address(address).andThen(personal(personal)).andThen(validate());
        }

        static Consumer<InternetAddress> address(String address) {
            return ia -> ia.setAddress(address);
        }

        static Consumer<InternetAddress> personal(String personal) {
            return personal(personal, null);
        }

        static Consumer<InternetAddress> personal(String personal, String charset) {
            return ia -> {
                try {
                    ia.setPersonal(personal, charset);
                } catch (UnsupportedEncodingException ex) {
                    String m = String.format("personal [%s]", personal);
                    throw new MailRuntimeException(m, ex);
                }
            };
        }

        static Consumer<InternetAddress> validate() {
            return ia -> {
                try {
                    ia.validate();
                } catch (AddressException ex) {
                    final String m = String.format("validate [%s]", ia);
                    throw new MailRuntimeException(m, ex);
                }
            };
        }
    }
    
}
