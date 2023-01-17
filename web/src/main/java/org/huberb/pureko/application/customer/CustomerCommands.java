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
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.Query;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;
import org.huberb.pureko.application.support.AbstractPersistenceModel.QueryConsumers;
import org.huberb.pureko.application.support.PersistenceModel;
import org.huberb.pureko.application.support.RandomChoosing;
import org.huberb.pureko.application.support.Transformers;

/**
 * Customer CRUD Commands.
 *
 * @author berni3
 */
@ApplicationScoped
public class CustomerCommands {

    @RequestScoped
    public static class ReadDefaultCustomerCommand {

        @Inject
        private CustomerDataFactory customerDataFactory;

        @Transactional
        public CustomerData createDefaultCustomerData() {
            final CustomerData cd = createDefaultCustomer(1).get(0);
            return cd;
        }

        @Transactional
        public List<CustomerData> createDefaultCustomerData(int count) {
            final List<CustomerData> cdList = createDefaultCustomer(count);
            return cdList;
        }

        private List<CustomerData> createDefaultCustomer(int count) {
            final List<CustomerData> customerList = customerDataFactory
                    .createDataFakerCustomerList(count);
            return customerList;
        }
    }

    @RequestScoped
    public static class SeedCustomersCommand {

        @Inject
        private PersistenceModel persistenceModel;
        @Inject
        private CustomerDataFactory customerDataFactory;
        @Inject
        private Transformers transformers;
        @Inject
        private CustomerTransforming customerTransforming;

        @Transactional
        public int seedDataBase(int maxSeeded) {
            final Number n = persistenceModel.findNamedSingleResult(
                    "countOfCustomerEntity", Number.class,
                    QueryConsumers.noop());

            int countOfSeeded = 0;
            if (n.intValue() == 0 && maxSeeded > 0) {
                final List<CustomerData> customerDataList = customerDataFactory.createDataFakerCustomerList(maxSeeded);
                for (final CustomerData cd : customerDataList) {
                    final CustomerEntity ce = transformers.transformTo(cd, customerTransforming.transformCustomerToNewCustomerEntity());
                    persistenceModel.create(ce);
                    countOfSeeded += 1;
                }
            }
            return countOfSeeded;
        }
    }

    @RequestScoped
    public static class ReadSingleCustomersCommand {

        @Inject
        private PersistenceModel persistenceModel;
        @Inject
        private Transformers transformers;
        @Inject
        private CustomerTransforming customerTransforming;

        @Transactional
        public CustomerData readCustomerById(Long id) {
            return findCustomerById(id);
        }

        //---
        CustomerData findCustomerById(Long id) {
            CustomerEntity customerEntity = persistenceModel.findById(id, CustomerEntity.class);
            final CustomerData cd = transformers.transformTo(customerEntity,
                    customerTransforming.transformCustomerEntityToNewCustomer());
            return cd;
        }

        @Transactional
        public CustomerData readCustomerByCustomerId(String customerId) {
            return findCustomerByCustomerId(customerId);
        }

        CustomerData findCustomerByCustomerId(String customerID) {
            final CustomerData cd;
            int choosen = RandomChoosing.randomlyChoose(2).get();
            if (choosen == 1) {
                cd = findCustomerByCustomerId_1(customerID);
            } else {
                cd = findCustomerByCustomerId_2(customerID);
            }
            return cd;
        }

        CustomerData findCustomerByCustomerId_1(String customerID) {
            String ce = CustomerEntity.class.getSimpleName();
            final String ql = "from " + ce + " as ce where ce.customerID = :customerID";
            Consumer<Query> c = (q) -> q.setParameter("customerID", customerID);
            CustomerEntity customerEntity = persistenceModel.findSingleResult(ql, CustomerEntity.class, c);
            final CustomerData cd = transformers.transformTo(customerEntity,
                    customerTransforming.transformCustomerEntityToNewCustomer());
            return cd;
        }

        CustomerData findCustomerByCustomerId_2(String customerID) {
            Consumer<Query> c = (q) -> q.setParameter("customerID", customerID);
            CustomerEntity customerEntity = persistenceModel.findNamedSingleResult("findByCustomerID", CustomerEntity.class, c);
            final CustomerData cd = transformers.transformTo(customerEntity,
                    customerTransforming.transformCustomerEntityToNewCustomer());
            return cd;
        }
// 

    }

    @RequestScoped
    public static class ReadAllCustomersCommand {

        @Inject
        private PersistenceModel persistenceModel;
        @Inject
        private Transformers transformers;
        @Inject
        private CustomerTransforming customerTransforming;

        @Transactional
        public List<CustomerData> readCustomers() {
            final List<CustomerData> cdList;
            int choosen = RandomChoosing.randomlyChoose(2).get();
            if (choosen == 1) {
                cdList = readCustomersUsingForLoop();
            } else {
                cdList = readCustomersStreamCollectorsToList();
            }
            return cdList;
        }

        //---
        List<CustomerData> readCustomersUsingForLoop() {
            String en = CustomerEntity.class.getSimpleName();
            final String ql = "from " + en;
            final List<CustomerEntity> resultList = persistenceModel.findResultList(
                    ql,
                    CustomerEntity.class,
                    QueryConsumers.noop());
            final List<CustomerData> l = new ArrayList<>();
            resultList.forEach((CustomerEntity ce) -> {
                final CustomerData cd = transformers.transformTo(ce,
                        customerTransforming.transformCustomerEntityToNewCustomer());
                l.add(cd);
            });
            return l;
        }

        List<CustomerData> readCustomersStreamCollectorsToList() {
            String en = CustomerEntity.class.getSimpleName();
            final String ql = "from " + en;
            final Function<CustomerEntity, CustomerData> f = customerTransforming.transformCustomerEntityToNewCustomer();
            final List<CustomerEntity> resultList = persistenceModel.findResultList(
                    ql,
                    CustomerEntity.class,
                    QueryConsumers.noop());

            final List<CustomerData> l = resultList.stream()
                    .map(e -> f.apply(e))
                    .collect(Collectors.toList());
            return l;
        }
    }

    @RequestScoped
    public static class CreateNewCustomerCommand {

        @Inject
        private PersistenceModel persistenceModel;
        @Inject
        private Transformers transformers;
        @Inject
        private CustomerTransforming customerTransforming;

        @Transactional
        public CustomerData createCustomer(CustomerData customerData) {
            validateCustomerData(customerData);

            final Function<CustomerData, CustomerEntity> func = customerTransforming.transformCustomerToNewCustomerEntity();
            final CustomerEntity ce = transformers.transformTo(customerData, func);
            validateCustomerEntity(ce);
            String cID = customerData.getCustomerID();

            persistenceModel.create(ce);

            CustomerData createdCustomerData = transformers.transformTo(ce,
                    customerTransforming.transformCustomerEntityToNewCustomer());
            return createdCustomerData;
        }

        void validateCustomerData(CustomerData cd) {
            Objects.nonNull(cd.getCustomerID());
        }

        void validateCustomerEntity(CustomerEntity ce) {
            Objects.nonNull(ce.getCustomerID());
            if (ce.getId() != null || ce.getVersion() != null) {
                throw new RuntimeException("Invalid customerEntity for create");
            }
        }

    }

    @RequestScoped
    public static class UpdateCustomerCommand {

        @Inject
        private PersistenceModel persistenceModel;
        @Inject
        private Transformers transformers;
        @Inject
        private CustomerTransforming customerTransforming;

        @Transactional
        public CustomerData updateCustomer(CustomerData customerData) {
            validateCustomerData(customerData);

            final Function<CustomerData, CustomerEntity> func = customerTransforming.transformCustomerToNewCustomerEntity();
            final CustomerEntity ce = transformers.transformTo(customerData, func);
            validateCustomerEntity(ce);

            persistenceModel.update(ce);

            CustomerData createdCustomerData = transformers.transformTo(ce,
                    customerTransforming.transformCustomerEntityToNewCustomer());
            return createdCustomerData;
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

    @RequestScoped
    public static class DeleteCustomerCommand {

        @Inject
        private PersistenceModel persistenceModel;
        @Inject
        private Transformers transformers;
        @Inject
        private CustomerTransforming customerTransforming;

        @Transactional
        public void deleteCustomer(CustomerData customerData) {
            String cID = customerData.getCustomerID();
            final Function<CustomerData, CustomerEntity> func = customerTransforming.transformCustomerToNewCustomerEntity();
            final CustomerEntity ce = transformers.transformTo(customerData, func);

            persistenceModel.remove(ce);
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
