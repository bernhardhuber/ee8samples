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
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;
import org.huberb.pureko.application.customer.CustomerTransforming.TransformCustomerEntityToNewCustomer;
import org.huberb.pureko.application.customer.CustomerTransforming.TransformCustomerToNewCustomerEntity;
import org.huberb.pureko.application.support.PersistenceModel;
import org.huberb.pureko.application.support.PersistenceModel.TypedQueryConsumers;
import org.huberb.pureko.application.support.Transformers;

/**
 * Customer CRUD Commands.
 *
 * @author berni3
 */
@ApplicationScoped
public class CustomerCommands {

    @ApplicationScoped
    public static class CreateDefaultCustomerCommand {

        @Inject
        private CustomerDataFactory customerDataFactory;

        @Transactional
        public CustomerData createDefaultCustomerData() {
            final CustomerData cd = createDefaultCustomer();
            return cd;
        }

        private CustomerData createDefaultCustomer() {
            final CustomerData customer = customerDataFactory
                    .createDataFakerCustomerList(1)
                    .get(0);
            return customer;
        }
    }

    @ApplicationScoped
    public static class SeedCustomersCommand {

        @Inject
        private PersistenceModel persistenceModel;
        @Inject
        private CustomerDataFactory customerDataFactory;
        @Inject
        private Transformers transformers;

        @Transactional
        public int seedDataBase(int maxSeeded) {
            final Number n = persistenceModel.findNamedSingleResult(
                    "countOfCustomerEntity", Number.class,
                    TypedQueryConsumers.noop());

            int countOfSeeded = 0;
            if (n.intValue() == 0 && maxSeeded > 0) {
                final List<CustomerData> customerDataList = customerDataFactory.createDataFakerCustomerList(maxSeeded);
                for (final CustomerData cd : customerDataList) {
                    final CustomerEntity ce = transformers.transformTo(cd, new TransformCustomerToNewCustomerEntity());
                    persistenceModel.create(ce);
                    countOfSeeded += 1;
                }
            }
            return countOfSeeded;
        }
    }

    @ApplicationScoped
    public static class ReadCustomerCommand {

        @Inject
        private PersistenceModel persistenceModel;
        @Inject
        private Transformers transformers;

        @Transactional
        public List<CustomerData> readCustomers() {
            return readCustomersUsingForLoop();
        }

        //---
        List<CustomerData> readCustomersUsingForLoop() {
            final String ql = "from Customer";
            final List<CustomerEntity> resultList = persistenceModel.findResultList(ql, CustomerEntity.class, TypedQueryConsumers.noop());
            final List<CustomerData> l = new ArrayList<>();
            resultList.forEach((CustomerEntity ce) -> {
                final CustomerData cd = transformers.transformTo(ce, CustomerTransforming.transformCustomerEntityToNewCustomer());
                l.add(cd);
            });
            return l;
        }

        List<CustomerData> readCustomersStreamCollectorsToList() {
            final String ql = "from Customer";
            final TransformCustomerEntityToNewCustomer f = CustomerTransforming.transformCustomerEntityToNewCustomer();
            final List<CustomerEntity> resultList = persistenceModel.findResultList(ql, CustomerEntity.class, TypedQueryConsumers.noop());

            final List<CustomerData> l = resultList.stream()
                    .map(e -> f.apply(e))
                    .collect(Collectors.toList());
            return l;
        }
    }

    @ApplicationScoped
    public static class UpdateCustomerCommand {

        @Inject
        private PersistenceModel persistenceModel;
        @Inject
        private Transformers transformers;

        @Transactional
        public void updateCustomer(CustomerData customerData) {
            validateCustomerData(customerData);

            final Function<CustomerData, CustomerEntity> func = CustomerTransforming.transformCustomerToNewCustomerEntity();
            final CustomerEntity ce = transformers.transformTo(customerData, func);
            validateCustomerEntity(ce);
            String cID = customerData.getCustomerID();

            //persistenceModel.update(ce);
        }

        void validateCustomerData(CustomerData cd) {
            Objects.nonNull(cd.getCustomerID());
        }

        void validateCustomerEntity(CustomerEntity ce) {
            Objects.nonNull(ce.getId());
            Objects.nonNull(ce.getVersion());
            Objects.nonNull(ce.getCustomerID());
        }



    }

    @ApplicationScoped
    public static class DeleteCustomerCommand {

        @Inject
        private PersistenceModel persistenceModel;
        @Inject
        private Transformers transformers;

        @Transactional
        public void deleteCustomer(CustomerData customerData) {
            String cID = customerData.getCustomerID();
            final Function<CustomerData, CustomerEntity> func = CustomerTransforming.transformCustomerToNewCustomerEntity();
            final CustomerEntity ce = transformers.transformTo(customerData, func);
        }

        void validateConstraints(CustomerEntity ce) {

            List<String> ml = new ArrayList<>();
            if (ce.getId() == null) {
                ml.add("CustomerEntity id may not be null");
            }
            if (ce.getVersion() == null) {
                ml.add("CustomerEntity version may not be null");
            }
            if (ce.getCustomerID() == null) {
                ml.add("CustomerEntity customerID may not be null");
            }
            if (!ml.isEmpty()) {
                ConstraintViolationException cvex = new javax.validation.ConstraintViolationException(
                        "not valid",
                        Collections.emptySet());
                throw cvex;
            }
        }
    }
}
