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
package org.huberb.pureko.application.support;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Base64;
import java.util.Scanner;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

/**
 *
 * @author berni3
 */
public class IdVersionTransferEncodings {

    static interface IdVersionTransferEncoding {

        String encode(Long id, Integer version);

        String decode(String s, Long id, Integer version);

    }

    /**
     *
     * @author berni3
     */
    public static class NaiveIdVersionTransferEncoding implements IdVersionTransferEncoding {

        private static final String DELIM = "_";

        @Override
        public String encode(Long id, Integer version) {
            return String.format("%s" + DELIM + "%s", id, version);
        }

        @Override
        public String decode(String s, Long id, Integer version) {
            final Scanner scn = new Scanner(s).useDelimiter(DELIM);
            final long idParsed = scn.nextLong();
            final int versionParsed = scn.nextInt();
            return String.format("%s" + DELIM + "%s", id, version);
        }

    }

    /**
     *
     * @author berni3
     */
    public static class DigestIdVersionTransferEncoding implements IdVersionTransferEncoding {

        private static final String B64_DELIMITER = "::";
        private static final String MESSAGE_DIGEST_ALGORITHM_NAME_SHA_1 = "SHA-1";

        @Override
        public String encode(Long id, Integer version) {
            try {
                final String password = String.format("%d_%d", id, version);
                final String createdDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(Instant.now().toString()).toString();
                final byte[] passwordDigestBytes = constructPasswordDigest(createdDate, password);
                final Base64.Encoder base64Encoder = Base64.getEncoder();
                final String passwordDigestBase64Encoded = base64Encoder.encodeToString(passwordDigestBytes);
                final String createdDateB64Encoded = base64Encoder.encodeToString(createdDate.toString().getBytes(StandardCharsets.UTF_8));
                final String result = String.format("%s" + B64_DELIMITER + "%s", passwordDigestBase64Encoded, createdDateB64Encoded);
                return result;
            } catch (DatatypeConfigurationException ex) {
                throw new RuntimeException(ex);
            }
        }

        @Override
        public String decode(String b64, Long id, Integer version) {
            final String password = String.format("%d_%d", id, version);
            final int index_r_n = b64.indexOf(B64_DELIMITER);
            final Base64.Decoder base64Decoder = Base64.getDecoder();
            final String a_passwordB64 = b64.substring(0, index_r_n);
            final String b_createdDateB64 = b64.substring(index_r_n + 2);
            final byte[] b_createdDate = base64Decoder.decode(b_createdDateB64);
            final byte[] passwordDigestBytes = constructPasswordDigest(new String(b_createdDate), password);
            final Base64.Encoder base64Encoder = Base64.getEncoder();
            final String passwordDigestBase64Encoded = base64Encoder.encodeToString(passwordDigestBytes);
            final String createdDateB64Encoded = base64Encoder.encodeToString(b_createdDate);
            final String result = String.format("%s" + B64_DELIMITER + "%s", passwordDigestBase64Encoded, createdDateB64Encoded);
            return result;
        }

        private byte[] constructPasswordDigest(String createdDate, String password) {
            try {
                final MessageDigest sha1MessageDigest = MessageDigest.getInstance(MESSAGE_DIGEST_ALGORITHM_NAME_SHA_1);
                final String createdDateAsString = createdDate;
                sha1MessageDigest.update(createdDateAsString.getBytes(StandardCharsets.UTF_8));
                sha1MessageDigest.update(password.getBytes(StandardCharsets.UTF_8));
                return sha1MessageDigest.digest();
            } catch (NoSuchAlgorithmException ex) {
                throw new RuntimeException(ex);
            }
        }

    }

}
