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
import java.util.function.Function;
import javax.enterprise.context.ApplicationScoped;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.bind.Jsonb;
import javax.json.stream.JsonParser;

/**
 *
 * @author berni3
 */
@ApplicationScoped
public class JsonConvertersF {

    public <T> String convertToString(T t, Function<T, String> f) {
        return f.apply(t);
    }

    public <T> T convertToInstance(String s, Function<String, T> f) {
        return f.apply(s);
    }

    public static <T> Function<T, String> fromInstanceToJsonString(Jsonb jsonb) {
        return t -> {
            final String s = jsonb.toJson(t);
            return s;
        };
    }

    public static <T> Function<String, T> fromStringToInstance(Jsonb jsonb, Class<T> clazz) {
        return s -> {
            final T t = jsonb.fromJson(s, clazz);
            return t;
        };
    }

    public static Function<String, JsonObject> createJsonObjectFromJson() {
        return s -> {
            try (final StringReader sr = new StringReader(s) //
                    ; final JsonParser jsonParser = Json.createParser(sr)) {
                jsonParser.next();
                JsonObject jsonObject = jsonParser.getObject();
                return jsonObject;
            }
        };
    }

}
