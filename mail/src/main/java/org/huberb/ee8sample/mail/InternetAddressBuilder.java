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
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import org.huberb.ee8sample.mail.Supports.MailRuntimeException;

/**
 *
 * @author berni3
 */
public class InternetAddressBuilder {
    
    final InternetAddress internetAddress;

    public InternetAddressBuilder() {
        this(new InternetAddress());
    }

    public InternetAddressBuilder(InternetAddress ia) {
        this.internetAddress = ia;
    }

    InternetAddressBuilder address(String address) {
        this.internetAddress.setAddress(address);
        return this;
    }

    InternetAddressBuilder personal(String personal) {
        try {
            this.internetAddress.setPersonal(personal, "UTF-8");
            return this;
        } catch (UnsupportedEncodingException ex) {
            String m = String.format("setting personal [%s]", personal);
            throw new MailRuntimeException(m, ex);
        }
    }

    InternetAddressBuilder addressPersonal(String address, String personal) {
        try {
            this.internetAddress.setAddress(address);
            this.internetAddress.setPersonal(personal, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            String m = String.format("setting address personal [%s], address [%s]", personal, address);
            throw new MailRuntimeException(m, ex);
        }
        return this;
    }

    InternetAddressBuilder validate() {
        try {
            this.internetAddress.validate();
            return this;
        } catch (AddressException ex) {
            String m = String.format("validating address [%s]", this.internetAddress.getAddress());
            throw new MailRuntimeException(m, ex);
        }
    }

    InternetAddress build() {
        return this.internetAddress;
    }
    
}
