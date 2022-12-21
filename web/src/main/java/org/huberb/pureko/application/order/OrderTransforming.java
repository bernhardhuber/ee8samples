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

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import javax.enterprise.context.ApplicationScoped;
import org.huberb.pureko.application.order.OrderData.ShipInfo;
import org.huberb.pureko.application.order.OrderEntity.ShipInfoEmbeddable;

/**
 *
 * @author berni3
 */
@ApplicationScoped
public class OrderTransforming {

    public BiFunction<OrderData, OrderEntity, OrderEntity> transformOrderToExistingOrderEntity() {
        return (cd, ce) -> new OrderTransforming().transformOrderToExistingOrderEntity(cd, ce);
    }

    public OrderEntity transformOrderToExistingOrderEntity(OrderData from, OrderEntity to) {
        to.setCustomerID(from.getCustomerID());
        to.setEmployeeID(from.getEmployeeID());
        to.setOrderDate(from.getOrderDate());
        to.setRequiredDate(from.getRequiredDate());

        ShipInfo fromShipInfo = Optional.ofNullable(from.getShipInfo()).orElse(new ShipInfo());
        ShipInfoEmbeddable toFullAddressEmbeddable = Optional.ofNullable(to.getShipInfo()).orElse(new ShipInfoEmbeddable());
        toFullAddressEmbeddable.setFreight(fromShipInfo.getFreight());
        toFullAddressEmbeddable.setShipAddress(fromShipInfo.getShipAddress());
        toFullAddressEmbeddable.setShipCity(fromShipInfo.getShipCity());
        toFullAddressEmbeddable.setShipCountry(fromShipInfo.getShipCountry());
        toFullAddressEmbeddable.setShipName(fromShipInfo.getShipName());
        toFullAddressEmbeddable.setShipPostalcode(fromShipInfo.getShipPostalcode());
        toFullAddressEmbeddable.setShipRegion(fromShipInfo.getShipRegion());
        toFullAddressEmbeddable.setShipVia(fromShipInfo.getShipVia());
        toFullAddressEmbeddable.setShippedDate(fromShipInfo.getShippedDate());

        return to;
    }

    public Function<OrderData, OrderEntity> transformOrderToNewOrderEntity() {
        return (cd) -> transformOrderToNewOrderEntity(cd);
    }

    public OrderEntity transformOrderToNewOrderEntity(OrderData from) {
        final ShipInfo shipInfo = Optional.ofNullable(from.getShipInfo())
                .orElse(new ShipInfo());
        final OrderEntity to = OrderEntity.builder()
                .customerID(from.getCustomerID())
                .employeeID(from.getEmployeeID())
                .orderDate(from.getOrderDate())
                .requiredDate(from.getRequiredDate())
                .shipInfo(ShipInfoEmbeddable.builder()
                        .freight(shipInfo.getFreight())
                        .shipAddress(shipInfo.getShipAddress())
                        .shipCity(shipInfo.getShipCity())
                        .shipCountry(shipInfo.getShipCountry())
                        .shipName(shipInfo.getShipName())
                        .shipPostalcode(shipInfo.getShipPostalcode())
                        .shipRegion(shipInfo.getShipRegion())
                        .shipVia(shipInfo.getShipVia())
                        .shippedDate(shipInfo.getShippedDate())
                        .build())
                .build();
        return to;
    }

    public Function<OrderEntity, OrderData> transformOrderEntityToNewOrder() {
        return (ce) -> transformOrderEntityToNewOrder(ce);
    }

    public OrderData transformOrderEntityToNewOrder(OrderEntity from) {

        final ShipInfoEmbeddable shipInfo = Optional.ofNullable(from.getShipInfo())
                .orElse(new ShipInfoEmbeddable());

        final OrderData to = OrderData.builder()
                .customerID(from.getCustomerID())
                .employeeID(from.getEmployeeID())
                .orderDate(from.getOrderDate())
                .requiredDate(from.getRequiredDate())
                .shipInfo(ShipInfo.builder()
                        .freight(shipInfo.getFreight())
                        .shipAddress(shipInfo.getShipAddress())
                        .shipCity(shipInfo.getShipCity())
                        .shipCountry(shipInfo.getShipCountry())
                        .shipName(shipInfo.getShipName())
                        .shipPostalcode(shipInfo.getShipPostalcode())
                        .shipRegion(shipInfo.getShipRegion())
                        .shipVia(shipInfo.getShipVia())
                        .shippedDate(shipInfo.getShippedDate())
                        .build())
                .build();
        return to;
    }

}
