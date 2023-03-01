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
package org.huberb.ee8sample.genericdata;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author berni3
 */
public class Basics {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Embeddable
    public static class Item implements Serializable {

        public static final long serialVersionUID = 20230115L;

        @Column(name = "ITEM_NAME", length = 100, nullable = false)
        private String itemName;

        @Column(name = "ITEM_CODE", length = 100, nullable = false)
        private String itemCode;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Embeddable
    public static class Address implements Serializable {

        public static final long serialVersionUID = 20230115L;

        @Column(name = "ADDRESS", length = 100, nullable = false)
        private String address;
        @Column(name = "CITY", length = 100, nullable = false)
        private String city;
        @Column(name = "REGION", length = 100, nullable = true)
        private String region;
        @Column(name = "POSTALCODE", length = 20, nullable = false)
        private String postalcode;
        @Column(name = "COUNTRY", length = 100, nullable = false)
        private String country;
        public static final String EMPTY_VALUE = "";

        public static Supplier<Address> createEmptyAddress() {
            return () -> {
                return Address.builder()
                        .address(BankAccount.EMPTY_VALUE)
                        .city(BankAccount.EMPTY_VALUE)
                        .region(BankAccount.EMPTY_VALUE)
                        .postalcode(BankAccount.EMPTY_VALUE)
                        .country(BankAccount.EMPTY_VALUE)
                        .build();
            };
        }

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Embeddable
    public static class BankAccount implements Serializable {

        public static final long serialVersionUID = 20230115L;

        @Column(name = "ACCOUNT_OWNER", length = 100, nullable = false)
        private String accountOwner;
        @Column(name = "IBAN", length = 100, nullable = false)
        private String iban;
        @Column(name = "BIC", length = 100, nullable = true)
        private String bic;

        public static final String EMPTY_VALUE = "";

        public static Comparator<BankAccount> compareByNameBicIban() {
            final Function<String, String> mapToBlank = s -> Optional.of(s).orElse(EMPTY_VALUE);
            return (BankAccount ba1, BankAccount ba2) -> {
                return Comparator.comparing((BankAccount n) -> mapToBlank.apply(n.accountOwner))
                        .thenComparing(Comparator.comparing((BankAccount ba) -> mapToBlank.apply(ba.bic)))
                        .thenComparing(Comparator.comparing((BankAccount ba) -> mapToBlank.apply(ba.iban)))
                        .compare(ba1, ba2);
            };
        }

        public static Supplier<BankAccount> createEmptyBankAccount() {
            return () -> {
                return BankAccount.builder()
                        .accountOwner(BankAccount.EMPTY_VALUE)
                        .bic(BankAccount.EMPTY_VALUE)
                        .iban(BankAccount.EMPTY_VALUE)
                        .build();
            };
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Embeddable
    public static class Name implements Serializable {

        public static final long serialVersionUID = 20230115L;

        @Column(name = "TITLE", length = 100, nullable = true)
        private String title;
        @Column(name = "FIRST_NAME", length = 100, nullable = false)
        private String firstName;
        @Column(name = "MIDDLE_NAME", length = 100, nullable = true)
        private String middleName;
        @Column(name = "LAST_NAME", length = 100, nullable = false)
        private String lastName;

        public static final String EMPTY_VALUE = "";

        public static Comparator<Name> compareByLastNameFirstNameMiddleNameTitle() {
            final Function<String, String> mapToBlank = s -> Optional.of(s).orElse(EMPTY_VALUE);
            return (Name n1, Name n2) -> {
                return Comparator.comparing((Name n) -> mapToBlank.apply(n.lastName))
                        .thenComparing(Comparator.comparing((Name n) -> mapToBlank.apply(n.firstName)))
                        .thenComparing(Comparator.comparing((Name n) -> mapToBlank.apply(n.middleName)))
                        .thenComparing(Comparator.comparing((Name n) -> mapToBlank.apply(n.title)))
                        .compare(n1, n2);
            };
        }

        public static Supplier<Name> createEmptyName() {
            return () -> {
                return Name.builder()
                        .firstName(EMPTY_VALUE)
                        .lastName(EMPTY_VALUE)
                        .middleName(EMPTY_VALUE)
                        .title(EMPTY_VALUE)
                        .build();
            };
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Embeddable
    public static class Person implements Serializable {

        public static final long serialVersionUID = 20230115L;

        private Name personName;

        public static Comparator<Person> compareByName() {
            return (Person pers1, Person pers2) -> {
                Comparator<Name> c = Name.compareByLastNameFirstNameMiddleNameTitle();
                Function<Name, Name> mapToBlank = n -> {
                    return Optional.ofNullable(n)
                            .orElse(Name.createEmptyName().get());
                };
                return c.compare(mapToBlank.apply(pers1.personName), mapToBlank.apply(pers2.personName));
            };
        }

        public static Supplier<Person> createEmptyPerson() {
            return () -> {
                return Person.builder()
                        .personName(Name.createEmptyName().get())
                        .build();
            };
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Embeddable
    public static class Organisation implements Serializable {

        public static final long serialVersionUID = 20230115L;

        @Column(name = "ORGANISATION_NAME", length = 100, nullable = false)
        private String organisationName;

        public final static String EMPTY_VALUE = "";

        public static Comparator<Organisation> compareByOrganisationName() {
            final Function<String, String> mapToBlank = s -> Optional.of(s).orElse(EMPTY_VALUE);
            return (Organisation n1, Organisation n2) -> {
                return Comparator.comparing((Organisation n) -> mapToBlank.apply(n.organisationName))
                        .compare(n1, n2);
            };
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Embeddable
    public static class LoginUser implements Serializable {

        public static final long serialVersionUID = 20230115L;
        @Column(name = "USER_NANE", length = 100, nullable = false)
        private String userName;
        @Embedded
        private Person person;

        public static Supplier<LoginUser> createEmptyPerson() {
            return () -> {
                return LoginUser.builder()
                        .userName("")
                        .person(Person.createEmptyPerson().get())
                        .build();
            };
        }

    }

}
