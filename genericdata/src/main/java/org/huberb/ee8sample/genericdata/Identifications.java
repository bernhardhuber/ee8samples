package org.huberb.ee8sample.genericdata;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
import java.util.function.Function;

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
/**
 *
 * @author berni3
 */
public class Identifications {

    /*
Running org.huberb.ee8sample.genericdata.IdentificationsTest
xxx1 9de937e5-4e66-45c5-8793-7f7aebc084b5
xxx2 3e25960a-79db-369b-a74c-d4ec67a72c62
xxx3 3e25960a79dbc69b674cd4ec67a72c62

xxx1 940ec7a6-c2a7-4772-b029-9393fb8a3d1d
xxx2 3e25960a-79db-369b-a74c-d4ec67a72c62
xxx3 3e25960a79dbc69b674cd4ec67a72c62
xxx4 32e3fbcf329a145bdde31fd8e990aabb

     */
    String xxx1() {
        return UUID.randomUUID().toString();
    }

    String xxx2(String x) {
        return UUID.nameUUIDFromBytes(x.getBytes(Charset.forName("UTF-8"))).toString();

    }

    String xxx3(String x) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException nsae) {
            throw new InternalError("MD5 not supported", nsae);
        }
        md.update(x.getBytes(Charset.forName("UTF-8")));
        byte[] md5Digested = md.digest();
        String md5Hex = String.format("%032x", new BigInteger(1, md5Digested));
        return md5Hex;
    }

    String xxx4(String x) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException nsae) {
            throw new InternalError("MD5 not supported", nsae);
        }
        md.update(x.getBytes(Charset.forName("UTF-8")));
        md.update(ByteBuffer.allocate(4).putInt(x.hashCode()).array());
        byte[] md5Digested = md.digest();
        String md5Hex = String.format("%032x", new BigInteger(1, md5Digested));
        return md5Hex;
    }

    public static Function<Object, String> f0(String prefix) {
        return o -> {
            final String uuidAsString = UUID.randomUUID().toString();
            return String.format("%s-%s", prefix, uuidAsString);
        };
    }

    public static Function<Object, String> f1(String prefix) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException nsae) {
            throw new InternalError("MD5 not supported", nsae);
        }
        return (p) -> {
            md.update(ByteBuffer.allocate(8).putLong(System.currentTimeMillis()).array());
            md.update(ByteBuffer.allocate(4).putInt(p.hashCode()).array());
            byte[] md5Digested = md.digest();

            return String.format("%s-%032x", prefix, new BigInteger(1, md5Digested));

        };
    }

    String objectIdentification(String prefix, Object p) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException nsae) {
            throw new InternalError("MD5 not supported", nsae);
        }
        md.update(ByteBuffer.allocate(8).putLong(System.currentTimeMillis()).array());
        md.update(ByteBuffer.allocate(4).putInt(p.hashCode()).array());
        byte[] md5Digested = md.digest();

        return String.format("%s-%032x", prefix, new BigInteger(1, md5Digested));
    }
}
