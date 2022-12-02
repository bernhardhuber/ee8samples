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
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
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
    public static class CustomerSeedCommand {

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

            Integer countOfSeeded = 0;
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
    public static class CustomerReadCommand {

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
                final CustomerData cd = transformers.transformTo(ce, new TransformCustomerEntityToNewCustomer());
                l.add(cd);
            });
            return l;
        }

        List<CustomerData> readCustomersStreamCollectorsToList() {
            final String ql = "from Customer";
            final TransformCustomerEntityToNewCustomer f = new TransformCustomerEntityToNewCustomer();
            final List<CustomerEntity> resultList = persistenceModel.findResultList(ql, CustomerEntity.class, TypedQueryConsumers.noop());

            final List<CustomerData> l = resultList.stream()
                    .map(e -> f.apply(e))
                    .collect(Collectors.toList());
            return l;
        }
    }
}
