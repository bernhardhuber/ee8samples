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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import javax.enterprise.context.ApplicationScoped;
import net.datafaker.Faker;
import org.huberb.pureko.application.customer.CustomerData.FullAddress;

/**
 *
 * @author berni3
 */
@ApplicationScoped
public class CustomerDataFactory {

    /**
     * Create list of customer using {link Faker} data values.
     *
     * @param nMax
     * @return
     */
    public List<CustomerData> createDataFakerCustomerList(int nMax) {
        final Faker faker = Faker.instance(Locale.forLanguageTag("de-AT"));
        return createDataFakerCustomerList(nMax, createNthDataFakerCustomerData(faker));
    }

    public List<CustomerData> createNaiveFakeCustomerList(int nMax) {
        return createDataFakerCustomerList(nMax, createNthNaiveCustomerData());
    }

    /**
     * Create list of customer using {link Faker} data values.
     *
     * @param nMax
     * @param f
     * @return
     */
    public List<CustomerData> createDataFakerCustomerList(int nMax, Function<Integer, CustomerData> f) {
        final List<CustomerData> customerDataList = new ArrayList<>();
        for (int i = 0; i < nMax; i += 1) {
            final CustomerData customerData = f.apply(i);
            customerDataList.add(customerData);
        }
        return customerDataList;
    }

    public static Function<Integer, CustomerData> createNthDataFakerCustomerData(Faker faker) {
        return (Integer i) -> {
            final FullAddress fullAddress = FullAddress.builder()
                    .address(faker.address().streetAddress())
                    .city(faker.address().city())
                    .country(faker.address().country())
                    .postalcode(faker.address().postcode())
                    .region(faker.address().state())
                    .build();
            final CustomerData customerData = CustomerData.builder()
                    .customerID("customerID-" + i)
                    .companyName(faker.company().name())
                    .contactName(faker.name().fullName())
                    .contactTitle(faker.name().title())
                    .fax(faker.phoneNumber().phoneNumber())
                    .phone(faker.phoneNumber().cellPhone())
                    .fullAddress(fullAddress)
                    .build();
            return customerData;
        };
    }

    public static Function<Integer, CustomerData> createNthNaiveCustomerData() {
        return (Integer i) -> {
            final FullAddress fullAddress = FullAddress.builder()
                    .address("address-" + i)
                    .city("city-" + i)
                    .country("country-" + i)
                    .postalcode("postalcode-")
                    .region("region-" + i)
                    .build();
            final CustomerData customerData = CustomerData.builder()
                    .customerID("customerID-" + i)
                    .companyName("companyName-" + i)
                    .contactName("contactName-" + i)
                    .contactTitle("contactTitle-" + i)
                    .fax("fax-" + i)
                    .phone("phone-" + i)
                    .fullAddress(fullAddress)
                    .build();
            return customerData;
        };
    }
}
