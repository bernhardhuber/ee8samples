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

import java.io.StringReader;
import java.util.List;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.bind.JsonbBuilder;
import javax.json.stream.JsonParser;

/**
 *
 * @author berni3
 */
public abstract class JsonConverter<T> {

    public String createJsonArrayFrom(List<T> customerList) {
        final String s = JsonbBuilder.create().toJson(customerList);
        return s;
    }

    public String createJsonObjectFrom(T customer) {
        final String s = JsonbBuilder.create().toJson(customer);
        return s;
    }

    public T createInstanceFromJson(String s, Class<T> clazz) {
        final T t = JsonbBuilder.create().fromJson(s, clazz);
        return t;
    }

    public JsonObject createJsonObjectFromJson(String s) {

        try (final StringReader sr = new StringReader(s); final JsonParser jsonParser = Json.createParser(sr)) {
            jsonParser.next();
            JsonObject jsonObject = jsonParser.getObject();
            return jsonObject;
        }
    }

}
