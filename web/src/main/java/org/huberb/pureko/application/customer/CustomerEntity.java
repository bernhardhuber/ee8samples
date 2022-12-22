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

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.huberb.pureko.application.support.ValidationContentCheckGroup;

/**
 *
 * @author berni3
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedQueries({
    @NamedQuery(name = "countOfCustomerEntity", query = "select count(ce) from CustomerEntity as ce "),
    @NamedQuery(name = "findByCustomerID", query = "from CustomerEntity as ce where ce.customerID = :customerID")
})
public class CustomerEntity implements Serializable {

    private static final long serialVersionUID = 20221127L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;
    @Version
    @Column(name = "version", updatable = false, nullable = false)
    private Integer version;

    //---
    @Column(nullable = false, length = 100)
    @javax.validation.constraints.NotBlank
    @javax.validation.constraints.Size(min = 1, max = 100)
    @lombok.NonNull
    private String customerID;
    //---
    @Column(nullable = false, length = 100)
    @javax.validation.constraints.NotBlank
    @javax.validation.constraints.Size(min = 1, max = 100)
    @javax.validation.constraints.Pattern(regexp = "^\\S+.*\\S+$", groups = {ValidationContentCheckGroup.class})
    @lombok.NonNull
    private String companyName;
    //---
    @Column(nullable = true, length = 100)
    @javax.validation.constraints.Size(min = 1, max = 100)
    @javax.validation.constraints.Pattern(regexp = "^\\S+.*\\S+$", groups = {ValidationContentCheckGroup.class})
    private String contactName;
    //---
    private String contactTitle;
    private String phone;
    private String fax;
    @Embedded
    private FullAddressEmbeddable fullAddress;

    @Embeddable
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FullAddressEmbeddable {

        private String address;
        private String city;
        private String region;
        private String postalcode;
        private String country;

    }
}
