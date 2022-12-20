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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.Query;
import javax.transaction.Transactional;
import org.huberb.pureko.application.support.PersistenceModel;
import org.huberb.pureko.application.support.PersistenceModel.QueryConsumers;
import org.huberb.pureko.application.support.Transformers;

/**
 * Order CRUD Commands.
 *
 * @author berni3
 */
@ApplicationScoped
public class OrderCommands {

    @RequestScoped
    public static class ReadDefaultCustomerCommand {

        @Inject
        private OrderDataFactory orderDataFactory;

        @Transactional
        public OrderData createDefaultOrderData() {
            final OrderData cd = createDefaultOrder(1).get(0);
            return cd;
        }

        @Transactional
        public List<OrderData> createDefaultOrderData(int count) {
            final List<OrderData> odList = createDefaultOrder(count);
            return odList;
        }

        private List<OrderData> createDefaultOrder(int count) {
            final List<OrderData> orderList = orderDataFactory
                    .createDataFakerCustomerList(count);
            return orderList;
        }
    }

    @RequestScoped
    public static class SeedOrdersCommand {

        @Inject
        private PersistenceModel persistenceModel;
        @Inject
        private OrderDataFactory orderDataFactory;
        @Inject
        private Transformers transformers;
        @Inject
        private OrderTransforming orderTransforming;

        @Transactional
        public int seedDataBase(int maxSeeded) {
            final Number n = persistenceModel.findNamedSingleResult(
                    "countOfOrderEntity", Number.class,
                    QueryConsumers.noop());

            int countOfSeeded = 0;
            if (n.intValue() == 0 && maxSeeded > 0) {
                final List<OrderData> orderDataList = orderDataFactory.createDataFakerCustomerList(maxSeeded);
                for (final OrderData cd : orderDataList) {
                    final OrderEntity ce = transformers.transformTo(cd, orderTransforming.transformOrderToNewOrderEntity());
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
        private OrderTransforming orderTransforming;

        @Transactional
        public OrderData readCustomerById(Long id) {
            return findCustomerById(id);
        }

        //---
        OrderData findCustomerById(Long id) {
            OrderEntity orderEntity = persistenceModel.findById(id, OrderEntity.class);
            final OrderData cd = transformers.transformTo(orderEntity,
                    orderTransforming.transformOrderEntityToNewOrder());
            return cd;
        }

        @Transactional
        public OrderData readCustomerByCustomerId(String orderId) {
            return findCustomerByCustomerId(orderId);
        }

        OrderData findCustomerByCustomerId(String customerID) {
            final OrderData cd;
            int chooseImpl = new Random().nextInt(99) % 2;
            if (chooseImpl == 1) {
                cd = findCustomerByCustomerId_1(customerID);
            } else {
                cd = findCustomerByCustomerId_2(customerID);
            }
            return cd;
        }

        OrderData findCustomerByCustomerId_1(String customerID) {
            String ce = OrderEntity.class.getSimpleName();
            final String ql = "from " + ce + " as ce where ce.customerID = :customerID";
            Consumer<Query> c = (q) -> q.setParameter("customerID", customerID);
            OrderEntity customerEntity = persistenceModel.findSingleResult(ql, OrderEntity.class, c);
            final OrderData cd = transformers.transformTo(customerEntity,
                    orderTransforming.transformOrderEntityToNewOrder());
            return cd;
        }

        OrderData findCustomerByCustomerId_2(String customerID) {
            Consumer<Query> c = (q) -> q.setParameter("customerID", customerID);
            OrderEntity customerEntity = persistenceModel.findNamedSingleResult("findByCustomerID", OrderEntity.class, c);
            final OrderData cd = transformers.transformTo(customerEntity,
                    orderTransforming.transformOrderEntityToNewOrder());
            return cd;
        }
    }

    @RequestScoped
    public static class ReadAllCustomersCommand {

        @Inject
        private PersistenceModel persistenceModel;
        @Inject
        private Transformers transformers;
        @Inject
        private OrderTransforming orderTransforming;

        @Transactional
        public List<OrderData> readCustomers() {
            return readCustomersUsingForLoop();
        }

        //---
        List<OrderData> readCustomersUsingForLoop() {
            String oe = OrderEntity.class.getSimpleName();
            final String ql = "from " + oe;
            final List<OrderEntity> resultList = persistenceModel.findResultList(
                    ql,
                    OrderEntity.class,
                    QueryConsumers.noop());
            final List<OrderData> l = new ArrayList<>();
            resultList.forEach((OrderEntity ce) -> {
                final OrderData cd = transformers.transformTo(ce,
                        orderTransforming.transformOrderEntityToNewOrder());
                l.add(cd);
            });
            return l;
        }

        List<OrderData> readCustomersStreamCollectorsToList() {
            String oe = OrderEntity.class.getSimpleName();
            final String ql = "from " + oe;
            final Function<OrderEntity, OrderData> f = orderTransforming.transformOrderEntityToNewOrder();
            final List<OrderEntity> resultList = persistenceModel.findResultList(
                    ql,
                    OrderEntity.class,
                    QueryConsumers.noop());

            final List<OrderData> l = resultList.stream()
                    .map(e -> f.apply(e))
                    .collect(Collectors.toList());
            return l;
        }
    }
}
