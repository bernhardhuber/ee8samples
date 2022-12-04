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
package org.huberb.pureko.application.order;

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
    @NamedQuery(name = "countOfOrderEntity", query = "SELECT COUNT(o) FROM OrderEntity o")
})

public class OrderEntity implements Serializable {

    private static final long serialVersionUID = 20221127L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;
    @Version
    @Column(name = "version", updatable = false, nullable = false)
    private Integer version;

    @Column(nullable = false, length = 100)
    @javax.validation.constraints.Size(min = 1, max = 100)
    private String customerID;
    @Column(nullable = false, length = 100)
    @javax.validation.constraints.Size(min = 1, max = 100)
    private String employeeID;
    private String orderDate;
    private String requiredDate;
    @Embedded
    private ShipInfoEmbeddable shipInfo;

    @Embeddable
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShipInfoEmbeddable {

        private String shippedDate;
        private String shipVia;
        private String freight;
        @Column(nullable = false, length = 100)
        private String shipName;
        private String shipAddress;
        private String shipCity;
        private String shipRegion;
        private String shipPostalcode;
        private String shipCountry;

    }
}
