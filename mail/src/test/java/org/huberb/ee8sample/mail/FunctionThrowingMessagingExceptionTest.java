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

import javax.mail.MessagingException;
import org.huberb.ee8sample.mail.Supports.FunctionThrowingMessagingException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 *
 * @author berni3
 */
public class FunctionThrowingMessagingExceptionTest {

    @Test
    public void hello1() throws MessagingException {
        final StringBuilder sb = new StringBuilder();
        FunctionThrowingMessagingException<StringBuilder, Integer> ftmexA = _sb -> {
            _sb.append("A");
            return _sb.length();
        };
        final int result = ftmexA.apply(sb);
        assertAll(
                () -> assertEquals(1, result),
                () -> assertEquals("A", sb.toString()),
                () -> assertEquals(1, sb.length())
        );
    }

    @Test
    public void hello2() throws MessagingException {
        final StringBuilder sb = new StringBuilder();
        FunctionThrowingMessagingException<String, StringBuilder> ftmexInit = _s -> {
            sb.delete(0, sb.length());
            sb.append(_s);
            return sb;
        };
        FunctionThrowingMessagingException<StringBuilder, Integer> ftmexA = _sb -> {
            _sb.append("A");
            return _sb.length();
        };

        final int result = ftmexA
                .compose(ftmexInit)
                .andThen(i -> i + 1)
                .apply("X");
        assertAll(
                () -> assertEquals(3, result),
                () -> assertEquals("XA", sb.toString()),
                () -> assertEquals(2, sb.length())
        );
    }
}
