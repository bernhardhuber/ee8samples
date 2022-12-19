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

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import javax.enterprise.context.ApplicationScoped;
import org.huberb.pureko.application.customer.CustomerData.FullAddress;
import org.huberb.pureko.application.customer.CustomerEntity.FullAddressEmbeddable;

/**
 *
 * @author berni3
 */
@ApplicationScoped
public class CustomerTransforming {

    public BiFunction<CustomerData, CustomerEntity, CustomerEntity> transformCustomerToExistingCustomerEntity() {
        return (cd, ce) -> new CustomerTransforming().transformCustomerToExistingCustomerEntity(cd, ce);
    }

    public CustomerEntity transformCustomerToExistingCustomerEntity(CustomerData from, CustomerEntity to) {
        to.setCompanyName(from.getCompanyName());
        to.setContactName(from.getContactName());
        to.setContactTitle(from.getContactTitle());
        to.setCustomerID(from.getCustomerID());
        to.setFax(from.getFax());
        to.setPhone(from.getPhone());
        FullAddress fromFullAddress = Optional.ofNullable(from.getFullAddress()).orElse(new FullAddress());
        FullAddressEmbeddable toFullAddressEmbeddable = Optional.ofNullable(to.getFullAddress()).orElse(new FullAddressEmbeddable());
        toFullAddressEmbeddable.setAddress(fromFullAddress.getAddress());
        toFullAddressEmbeddable.setPostalcode(fromFullAddress.getPostalcode());
        toFullAddressEmbeddable.setCity(fromFullAddress.getCity());
        toFullAddressEmbeddable.setRegion(fromFullAddress.getRegion());
        toFullAddressEmbeddable.setCountry(fromFullAddress.getCountry());
        return to;
    }

    public Function<CustomerData, CustomerEntity> transformCustomerToNewCustomerEntity() {
        return (cd) -> transformCustomerToNewCustomerEntity(cd);
    }

    public CustomerEntity transformCustomerToNewCustomerEntity(CustomerData from) {
        final FullAddress fullAddress = Optional.ofNullable(from.getFullAddress())
                .orElse(new FullAddress());
        final CustomerEntity to = CustomerEntity.builder()
                .companyName(from.getCompanyName())
                .contactName(from.getContactName())
                .contactTitle(from.getContactTitle())
                .customerID(from.getCustomerID())
                .fax(from.getFax())
                .phone(from.getPhone())
                .fullAddress(FullAddressEmbeddable.builder()
                        .address(fullAddress.getAddress())
                        .postalcode(fullAddress.getPostalcode())
                        .city(fullAddress.getCity())
                        .region(fullAddress.getRegion())
                        .country(fullAddress.getCountry())
                        .build())
                .build();
        return to;
    }

    public Function<CustomerEntity, CustomerData> transformCustomerEntityToNewCustomer() {
        return (ce) -> transformCustomerEntityToNewCustomer(ce);
    }

    public CustomerData transformCustomerEntityToNewCustomer(CustomerEntity from) {

        final FullAddressEmbeddable fullAddress = Optional.ofNullable(from.getFullAddress())
                .orElse(new FullAddressEmbeddable());

        final CustomerData to = CustomerData.builder()
                .companyName(from.getCompanyName())
                .contactName(from.getContactName())
                .contactTitle(from.getContactTitle())
                .customerID(from.getCustomerID())
                .fax(from.getFax())
                .phone(from.getPhone())
                .fullAddress(FullAddress.builder()
                        .address(fullAddress.getAddress())
                        .postalcode(fullAddress.getPostalcode())
                        .city(fullAddress.getCity())
                        .region(fullAddress.getRegion())
                        .country(fullAddress.getCountry())
                        .build())
                .build();
        return to;
    }
}
