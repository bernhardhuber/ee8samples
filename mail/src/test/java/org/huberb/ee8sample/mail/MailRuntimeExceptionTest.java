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

import org.huberb.ee8sample.mail.Supports.MailRuntimeException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

/**
 *
 * @author berni3
 */
public class MailRuntimeExceptionTest {

    @Test
    public void testMailRuntimeException_construct_message() {
        MailRuntimeException mrex1 = new MailRuntimeException("some-message");
        assertEquals("some-message", mrex1.getMessage());
        assertEquals("some-message", mrex1.getLocalizedMessage());
        assertEquals(null, mrex1.getCause());
    }

    @Test
    public void testMailRuntimeException_construct_message_cause() {
        Exception cause = new Exception("cause-message");
        MailRuntimeException mrex2 = new MailRuntimeException("some-message", cause);
        assertEquals("some-message", mrex2.getMessage());
        assertEquals("some-message", mrex2.getLocalizedMessage());
        assertNotNull(mrex2.getCause());
        assertEquals("cause-message", mrex2.getCause().getMessage());
    }
}
