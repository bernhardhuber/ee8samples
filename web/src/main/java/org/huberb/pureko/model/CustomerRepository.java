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
package org.huberb.pureko.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import net.datafaker.Faker;
import org.huberb.pureko.model.Customer.FullAddress;

/**
 *
 * @author berni3
 */
@ApplicationScoped
public class CustomerRepository {

    @Inject
    private DataFakerFactory dataFakerFactory;

    public List<Customer> loadCustomers(int n) {
        final List<Customer> customerList = dataFakerFactory.createDataFakerCustomerList(n);
        return customerList;
    }

    @ApplicationScoped
    public static class DataFakerFactory {

        /**
         * Create list of customer using {link Faker} data values.
         *
         * @param n
         * @return
         */
        public List<Customer> createDataFakerCustomerList(int n) {
            final List<Customer> customerList = new ArrayList<>();
            for (int i = 0; i < n; i += 1) {
                final Customer customer = createCustomerUsingFaker(i);
                customerList.add(customer);
            }
            return customerList;
        }

        public Customer createCustomerUsingFaker(int i) {
            Faker faker = Faker.instance(Locale.forLanguageTag("de-AT"));
            final FullAddress fullAddress = FullAddress.builder()
                    .address(faker.address().streetAddress())
                    .city(faker.address().city())
                    .country(faker.address().country())
                    .postalcode(faker.address().postcode())
                    .region(faker.address().state())
                    .build();
            final Customer customer = Customer.builder()
                    .customerID("customerID-" + i)
                    .companyName(faker.company().name())
                    .contactName(faker.name().fullName())
                    .contactTitle(faker.name().title())
                    .fax(faker.phoneNumber().phoneNumber())
                    .phone(faker.phoneNumber().cellPhone())
                    .fullAddress(fullAddress)
                    .build();
            return customer;
        }
    }

    @ApplicationScoped
    public static class NaiveFakerFactory {

        public List<Customer> createNaiveFakeCustomerList(int n) {
            final List<Customer> customerList = new ArrayList<>();
            for (int i = 0; i < n; i += 1) {
                final Customer customer = createCustomerNaive(i);
                customerList.add(customer);
            }
            return customerList;
        }

        public Customer createCustomerNaive(int i) {
            final FullAddress fullAddress = FullAddress.builder()
                    .address("address-" + i)
                    .city("city-" + i)
                    .country("country-" + i)
                    .postalcode("postalcode-")
                    .region("region-" + i)
                    .build();
            final Customer customer = Customer.builder()
                    .customerID("customerID-" + i)
                    .companyName("companyName-" + i)
                    .contactName("contactName-" + i)
                    .contactTitle("contactTitle-" + i)
                    .fax("fax-" + i)
                    .phone("phone-" + i)
                    .fullAddress(fullAddress)
                    .build();
            return customer;
        }
    }
}
