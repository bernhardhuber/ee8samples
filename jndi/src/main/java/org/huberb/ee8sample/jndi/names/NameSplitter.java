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
package org.huberb.ee8sample.jndi.names;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 * @author berni3
 */
public class NameSplitter {

    static final String SEPARATOR = ".";
    static final String QUOTE1 = "'";
    static final String QUOTE2 = "\"";

    private String separator = SEPARATOR;
    private String quote1 = QUOTE1;
    private String quote2 = QUOTE2;

    final Map<Token, TokenInfo> m;

    Set<TokenInfo> createTokenInfosForAll() {
        return m.entrySet().stream()
                .map(entry -> entry.getValue())
                .collect(Collectors.toSet());
    }

    Set<TokenInfo> createTokenFor(Token token) {
        return Set.of(m.get(token));
    }

    public NameSplitter() {
        this.m = new HashMap<>() {
            {
                put(Token.separator, new TokenInfo(separator, Token.separator));
                put(Token.quote1, new TokenInfo(quote1, Token.quote1));
                put(Token.quote2, new TokenInfo(quote2, Token.quote2));
            }
        };
    }

    public List<String> split(String aName) {
        List<String> result = new ArrayList<>(5);

        Word word = new Word(aName, 0);
        StringBuilder sb = new StringBuilder();

        while (word.beginIndexLtLength()) {
            TokenInfo tokenInfo = findAnyTokenIn(word, createTokenInfosForAll());
            if (tokenInfo.token == Token.separator) {
                String comp = sb.toString();
                result.add(comp);
                sb.delete(0, sb.length());

                word.inc(tokenInfo.value.length());
            } else if (tokenInfo.token == Token.quote1 || tokenInfo.token == Token.quote2) {
                Token endToken = tokenInfo.token;

                sb.delete(0, sb.length());
                word.inc(tokenInfo.value.length());
                while (word.beginIndexLtLength()) {
                    TokenInfo tokenInfo2 = findAnyTokenIn(word, createTokenFor(endToken));
                    if (tokenInfo2.token == endToken) {
                        String comp = sb.toString();
                        result.add(comp);
                        sb.delete(0, sb.length());

                        word.inc(tokenInfo2.value.length());
                    } else {
                        sb.append(word.charAtBeginIndex());
                        word.inc();
                    }
                }
                //} else if (token == Token.quote2) {
            } else {
                sb.append(tokenInfo.accumulator.toString());
                word.inc();
            }
        }
        if (!sb.isEmpty()) {
            String comp = sb.toString();
            result.add(comp);
        }
        return result;
    }

    static class TokenInfo {

        final String value;
        final Token token;
        final StringBuilder accumulator;

        TokenInfo(String value, Token token) {
            this(value, token, "");
        }

        TokenInfo(String value, Token token, String sbInitial) {
            this.value = value;
            this.token = token;
            this.accumulator = new StringBuilder(sbInitial);
        }
    }

    enum Token {
        none,
        separator, quote1, quote2;
    }

    TokenInfo findAnyTokenIn(Word word, Set<TokenInfo> tokenInfoSet) {
        final String underTest = word.substringFromBeginIndex();

        TokenInfo foundTokenInfo = tokenInfoSet.stream().filter(ti -> underTest.startsWith(ti.value))
                .findFirst()
                .orElse(null);

        if (foundTokenInfo != null) {
            return foundTokenInfo;
        } else {
            return new TokenInfo(String.valueOf(word.charAtBeginIndex()), Token.none);
        }
    }

    void gobbleUntil(Word word, Token stopToken, Set<TokenInfo> tokenInfoSet) {
        // TODO return Token + matched token-String + accum
        StringBuilder accum = new StringBuilder();

        TokenInfo foundToken = findAnyTokenIn(word, tokenInfoSet);

        if (foundToken.token == stopToken) {
            String result = accum.toString();
            word.inc(foundToken.value.length());
        } else {
            accum.append(word.charAtBeginIndex());
            word.inc();
        }
    }

    static class Word {

        final String fullValue;
        int beginIndex;
        final int length;
        final StringBuilder accumulator;

        Word(String n, int beginIndex) {
            this.fullValue = n;
            this.beginIndex = beginIndex;
            this.length = n.length();

            this.accumulator = new StringBuilder();
        }

        void inc() {
            inc(1);
        }

        void inc(int inc) {
            beginIndex += inc;
        }

        boolean beginIndexLtLength() {
            return beginIndex < length;
        }

        String substringFromBeginIndex() {
            return fullValue.substring(beginIndex);
        }

        char charAtBeginIndex() {
            return fullValue.charAt(beginIndex);
        }

    }
}
