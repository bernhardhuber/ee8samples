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
package org.huberb.pureko.application.customer;

import org.huberb.pureko.application.support.IdVersionTransferEncodings.DigestIdVersionTransferEncoding;
import org.huberb.pureko.application.support.IdVersionTransferEncodings.NaiveIdVersionTransferEncoding;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 *
 * @author berni3
 */
public class IdVersionEncodingsTest {

    @ParameterizedTest
    @CsvSource(value = {
        "0,0, 0_0",
        "1,0, 1_0",
        "0,1, 0_1",
        "1,1, 1_1",
        "100,200, 100_200",
        "991,992, 991_992",})
    public void testNaiveIdVersionEncodingRoundTrip(long id, int version, String expectedEncoded) {
        /*
testNaiveIdVersionEncodingRoundTrip id 0, version 0
encoding s1: [0_0],
decoded s2: [0_0]
testNaiveIdVersionEncodingRoundTrip id 1, version 0
encoding s1: [1_0],
decoded s2: [1_0]
testNaiveIdVersionEncodingRoundTrip id 0, version 1
encoding s1: [0_1],
decoded s2: [0_1]
testNaiveIdVersionEncodingRoundTrip id 1, version 1
encoding s1: [1_1],
decoded s2: [1_1]
testNaiveIdVersionEncodingRoundTrip id 100, version 200
encoding s1: [100_200],
decoded s2: [100_200]
testNaiveIdVersionEncodingRoundTrip id 991, version 992
encoding s1: [991_992],
decoded s2: [991_992]
         */
        final NaiveIdVersionTransferEncoding instance = new NaiveIdVersionTransferEncoding();
        final String s1 = instance.encode(id, version);
        final String s2 = instance.decode(s1, id, version);
        final String m = String.format("testNaiveIdVersionEncodingRoundTrip id %d, version %d%n"
                + "encoding s1: [%s],%n"
                + "decoded  s2: [%s]",
                id, version,
                s1, s2);
        //System.out.println(m);
        assertAll(
                () -> assertEquals(s1, s2, m),
                () -> assertTrue(s1.length() >= 1 + 1 + 1)
        );
    }

    @ParameterizedTest
    @CsvSource(value = {
        "0,0",
        "1,0",
        "0,1",
        "1,1",
        "100,200",
        "991,992",})
    public void testIdVersionEncodingRoundTrip(Long id, Integer version) {
        /* sample results
testIdVersionEncodingRoundTrip id 0, version 0
encoding s1: [pbXFsqn234PRDQirVGfTtVOWWxA=::MjAyMi0xMi0wNFQxOTozNjo0NS4zNjE5NDM4MDBa],
decoded  s2: [pbXFsqn234PRDQirVGfTtVOWWxA=::MjAyMi0xMi0wNFQxOTozNjo0NS4zNjE5NDM4MDBa]
testIdVersionEncodingRoundTrip id 1, version 0
encoding s1: [EUy7PbXXU9Kqj+IrVi6NCDzku9g=::MjAyMi0xMi0wNFQxOTozNjo0NS40Mjk5MzVa],
decoded  s2: [EUy7PbXXU9Kqj+IrVi6NCDzku9g=::MjAyMi0xMi0wNFQxOTozNjo0NS40Mjk5MzVa]
testIdVersionEncodingRoundTrip id 0, version 1
encoding s1: [i0On+NYZOhFSRehIVUPtIthtpB8=::MjAyMi0xMi0wNFQxOTozNjo0NS40NDM5OTMzMDBa],
decoded  s2: [i0On+NYZOhFSRehIVUPtIthtpB8=::MjAyMi0xMi0wNFQxOTozNjo0NS40NDM5OTMzMDBa]
testIdVersionEncodingRoundTrip id 1, version 1
encoding s1: [I/HofSvqRFC1CV35vHMeLusw3V8=::MjAyMi0xMi0wNFQxOTozNjo0NS40NTM5OTM4MDBa],
decoded  s2: [I/HofSvqRFC1CV35vHMeLusw3V8=::MjAyMi0xMi0wNFQxOTozNjo0NS40NTM5OTM4MDBa]
testIdVersionEncodingRoundTrip id 100, version 200
encoding s1: [8RdE0ewvY9tYnrZRwQkk2or0pwQ=::MjAyMi0xMi0wNFQxOTozNjo0NS40NjE3NTA4MDBa],
decoded  s2: [8RdE0ewvY9tYnrZRwQkk2or0pwQ=::MjAyMi0xMi0wNFQxOTozNjo0NS40NjE3NTA4MDBa]
testIdVersionEncodingRoundTrip id 991, version 992
encoding s1: [E1DRyst9FvFCoZBSVyrnIPE4Plw=::MjAyMi0xMi0wNFQxOTozNjo0NS40NzI0NDExMDBa],
decoded  s2: [E1DRyst9FvFCoZBSVyrnIPE4Plw=::MjAyMi0xMi0wNFQxOTozNjo0NS40NzI0NDExMDBa]
         */
        final DigestIdVersionTransferEncoding instance = new DigestIdVersionTransferEncoding();
        final String s1 = instance.encode(id, version);
        final String s2 = instance.decode(s1, id, version);
        final String m = String.format("testIdVersionEncodingRoundTrip id %d, version %d%n"
                + "encoding s1: [%s],%n"
                + "decoded  s2: [%s]",
                id, version,
                s1, s2);
        //System.out.println(m);
        assertAll(
                () -> assertEquals(s1, s2, m),
                () -> assertEquals(70, s1.length(), m)
        );
    }

}
