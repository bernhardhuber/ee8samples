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
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.Query;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;
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
    public static class ReadDefaultOrderCommand {

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
                    .createDataFakerOrderList(count);
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
                final List<OrderData> orderDataList = orderDataFactory.createDataFakerOrderList(maxSeeded);
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
    public static class ReadSingleOrdersCommand {

        @Inject
        private PersistenceModel persistenceModel;
        @Inject
        private Transformers transformers;
        @Inject
        private OrderTransforming orderTransforming;

        @Transactional
        public OrderData readOrderById(Long id) {
            return findOrderById(id);
        }

        //---
        OrderData findOrderById(Long id) {
            OrderEntity orderEntity = persistenceModel.findById(id, OrderEntity.class);
            final OrderData cd = transformers.transformTo(orderEntity,
                    orderTransforming.transformOrderEntityToNewOrder());
            return cd;
        }

        @Transactional
        public OrderData readOrderByOrderId(String orderId) {
            return findOrderByOrderId(orderId);
        }

        OrderData findOrderByOrderId(String customerID) {
            final OrderData cd;
            final int chooseImpl = randomlyChoose(2).get();
            if (chooseImpl == 1) {
                cd = findOrderByCustomerId_1(customerID);
            } else {
                cd = findOrderByCustomerId_2(customerID);
            }
            return cd;
        }

        OrderData findOrderByCustomerId_1(String customerID) {
            String oe = OrderEntity.class.getSimpleName();
            final String ql = "from " + oe + " as oe where oe.customerID = :customerID";
            Consumer<Query> c = (q) -> q.setParameter("customerID", customerID);
            OrderEntity customerEntity = persistenceModel.findSingleResult(ql, OrderEntity.class, c);
            final OrderData cd = transformers.transformTo(customerEntity,
                    orderTransforming.transformOrderEntityToNewOrder());
            return cd;
        }

        OrderData findOrderByCustomerId_2(String customerID) {
            Consumer<Query> c = (q) -> q.setParameter("customerID", customerID);
            OrderEntity orderEntity = persistenceModel.findNamedSingleResult("findByCustomerID", OrderEntity.class, c);
            final OrderData cd = transformers.transformTo(orderEntity,
                    orderTransforming.transformOrderEntityToNewOrder());
            return cd;
        }
    }

    @RequestScoped
    public static class ReadAllOrdersCommand {

        @Inject
        private PersistenceModel persistenceModel;
        @Inject
        private Transformers transformers;
        @Inject
        private OrderTransforming orderTransforming;

        @Transactional
        public List<OrderData> readOrders() {
            final List<OrderData> odList;
            int choosen = randomlyChoose(2).get();
            if (choosen == 1) {
                odList = readOrdersUsingForLoop();
            } else {
                odList = readOrdersStreamCollectorsToList();
            }
            return odList;
        }

        //---
        List<OrderData> readOrdersUsingForLoop() {
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

        List<OrderData> readOrdersStreamCollectorsToList() {
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

    public static Supplier<Integer> randomlyChoose(int numberOfOptions) {
        return () -> {
            final int randRange;
            if (numberOfOptions < 100) {
                randRange = 100;
            } else {
                randRange = numberOfOptions;
            }
            int choosen = new Random().nextInt(randRange) % numberOfOptions;
            return choosen;
        };
    }

//-----------------------------------------------------------------------------
//-----------------------------------------------------------------------------
    @RequestScoped
    public static class CreateNewOrderCommand {

        @Inject
        private PersistenceModel persistenceModel;
        @Inject
        private Transformers transformers;
        @Inject
        private OrderTransforming orderTransforming;

        @Transactional
        public OrderData createOrder(OrderData orderData) {
            validateCustomerData(orderData);

            final Function<OrderData, OrderEntity> func = orderTransforming.transformOrderToNewOrderEntity();
            final OrderEntity oe = transformers.transformTo(orderData, func);
            validateOrderEntity(oe);
            String cID = orderData.getCustomerID();

            persistenceModel.create(oe);

            OrderData createdCustomerData = transformers.transformTo(oe,
                    orderTransforming.transformOrderEntityToNewOrder());
            return createdCustomerData;
        }

        void validateCustomerData(OrderData od) {
            Objects.nonNull(od.getCustomerID());
        }

        void validateOrderEntity(OrderEntity oe) {
            Objects.nonNull(oe.getCustomerID());
            if (oe.getId() != null || oe.getVersion() != null) {
                throw new RuntimeException("Invalid customerEntity for create");
            }
        }

    }

    @RequestScoped
    public static class UpdateOrderCommand {

        @Inject
        private PersistenceModel persistenceModel;
        @Inject
        private Transformers transformers;
        @Inject
        private OrderTransforming orderTransforming;

        @Transactional
        public OrderData updateOrder(OrderData orderData) {
            validateOrderData(orderData);

            final Function<OrderData, OrderEntity> func = orderTransforming.transformOrderToNewOrderEntity();
            final OrderEntity ce = transformers.transformTo(orderData, func);
            validateOrderEntity(ce);

            persistenceModel.update(ce);

            OrderData createdOrderData = transformers.transformTo(ce,
                    orderTransforming.transformOrderEntityToNewOrder());
            return createdOrderData;
        }

        void validateOrderData(OrderData od) {
            Objects.nonNull(od.getCustomerID());
        }

        void validateOrderEntity(OrderEntity oe) {
            Objects.nonNull(oe.getId());
            Objects.nonNull(oe.getVersion());
            Objects.nonNull(oe.getCustomerID());
        }

    }

    @RequestScoped
    public static class DeleteOrderCommand {

        @Inject
        private PersistenceModel persistenceModel;
        @Inject
        private Transformers transformers;
        @Inject
        private OrderTransforming customerTransforming;

        @Transactional
        public void deleteOrder(OrderData orderData) {
            String cID = orderData.getCustomerID();
            final Function<OrderData, OrderEntity> func = customerTransforming.transformOrderToNewOrderEntity();
            final OrderEntity ce = transformers.transformTo(orderData, func);

            persistenceModel.remove(ce);
        }

        void validateConstraints(OrderEntity oe) {

            List<String> ml = new ArrayList<>();
            if (oe.getId() == null) {
                ml.add("CustomerEntity id may not be null");
            }
            if (oe.getVersion() == null) {
                ml.add("CustomerEntity version may not be null");
            }
            if (oe.getCustomerID() == null) {
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
