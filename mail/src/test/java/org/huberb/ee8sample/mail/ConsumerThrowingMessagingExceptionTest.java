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
import org.huberb.ee8sample.mail.Supports.ConsumerThrowingMessagingException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 *
 * @author berni3
 */
public class ConsumerThrowingMessagingExceptionTest {

    @Test
    public void hello1() throws MessagingException {
        StringBuilder result = new StringBuilder();
        ConsumerThrowingMessagingException<StringBuilder> ctmexA = (StringBuilder s) -> {
            s.append("A");
        };
        ctmexA.accept(result);
        assertEquals("A", result.toString());
    }

    @Test
    public void hello2() throws MessagingException {
        StringBuilder result = new StringBuilder();
        ConsumerThrowingMessagingException<StringBuilder> ctmexA = (StringBuilder s) -> {
            s.append("A");
        };
        ConsumerThrowingMessagingException<StringBuilder> ctmexB = (StringBuilder s) -> {
            s.append("B");
        };
        ctmexA.andThen(ctmexB).accept(result);
        assertEquals("AB", result.toString());
    }
}
