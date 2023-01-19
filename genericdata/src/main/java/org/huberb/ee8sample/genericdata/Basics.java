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
import javax.persistence.Column;
import javax.persistence.Embeddable;
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
        @Column(name = "POSTALCODE", length = 100, nullable = false)
        private String postalcode;
        @Column(name = "COUNTRY", length = 100, nullable = false)
        private String country;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Embeddable
    public static class BankAccount implements Serializable {

        public static final long serialVersionUID = 20230115L;

        @Column(name = "NAME", length = 100, nullable = false)
        private String name;
        @Column(name = "IBAN", length = 100, nullable = false)
        private String iban;
        @Column(name = "BIC", length = 100, nullable = true)
        private String bic;
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
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Embeddable
    public static class Person implements Serializable {

        public static final long serialVersionUID = 20230115L;

        private Name personName;
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
        private Person person;
    }

}
