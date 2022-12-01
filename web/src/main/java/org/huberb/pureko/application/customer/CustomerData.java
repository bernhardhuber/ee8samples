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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.huberb.pureko.application.customer.AgnosticDatas.MetaData;

/**
 *
 * @author berni3
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerData implements Serializable {
public static final long serialVersionUID = 20221201L;

    private MetaData metaData;
    private String customerID;
    private String companyName;
    private String contactName;
    private String contactTitle;
    private String phone;
    private String fax;
    private FullAddress fullAddress;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FullAddress implements Serializable {
public static final long serialVersionUID = 20221201L;

        private String address;
        private String city;
        private String region;
        private String postalcode;
        private String country;

    }

}
