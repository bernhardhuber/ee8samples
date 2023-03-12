/*
 * Copyright 2023 berni3.
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
package org.huberb.ee8sample.genericdata.shopping;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.huberb.ee8sample.genericdata.Basics.Address;
import org.huberb.ee8sample.genericdata.Basics.Item;
import org.huberb.ee8sample.genericdata.Basics.LoginUser;
import org.huberb.ee8sample.genericdata.Basics.Organisation;
import org.huberb.ee8sample.genericdata.Basics.Person;
import static org.huberb.ee8sample.genericdata.shopping.Shoppings.Delivery.COUNT_OF_DELIVERIES_QUERY;
import static org.huberb.ee8sample.genericdata.shopping.Shoppings.Invoice.COUNT_OF_INVOICES_QUERY;
import static org.huberb.ee8sample.genericdata.shopping.Shoppings.Order.COUNT_OF_ORDERS_QUERY;
import static org.huberb.ee8sample.genericdata.shopping.Shoppings.ShoppingCard.COUNT_OF_SHOPPING_CARDS_QUERY;
import static org.huberb.ee8sample.genericdata.shopping.Shoppings.ShoppingCard.FIND_SHOPPING_CARD_BY_USER;
import static org.huberb.ee8sample.genericdata.shopping.Shoppings.StockItem.COUNT_OF_STOCK_ITEMS_QUERY;
import static org.huberb.ee8sample.genericdata.shopping.Shoppings.StockItem.FIND_STOCK_ITEMS_BY_CODE_QUERY;
import static org.huberb.ee8sample.genericdata.shopping.Shoppings.StockItem.FIND_STOCK_ITEMS_BY_NAME_QUERY;

/**
 *
 * @author berni3
 */
public class Shoppings {

    private Shoppings() {
    }

    @Data
    @NoArgsConstructor
    @MappedSuperclass
    public static class AbstractEntityIdVersion implements Serializable {

        public static final long serialVersionUID = 20230221L;

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        @Column(name = "ID", updatable = false, nullable = false)
        private Long id;
        @Version
        @Column(name = "VERSION", updatable = false, nullable = false)
        private Integer version;

        @Column(name = "CREATED_WHEN", columnDefinition = "TIMESTAMP")
        private java.time.LocalDateTime createdWhen;
        @Column(name = "UPDATED_WHEN", columnDefinition = "TIMESTAMP")
        private java.time.LocalDateTime updatedWhen;

        @PrePersist
        void onPrePersist() {
            if (this.createdWhen == null) {
                this.createdWhen = LocalDateTime.now();
            }
        }

        @PreUpdate()
        void onPreUpdate() {
            this.updatedWhen = LocalDateTime.now();
        }
    }

    @Data
    @Builder
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    @NoArgsConstructor
    @AllArgsConstructor
    @Entity
    @Table(name = "STOCK_ITEM")
    @NamedQueries({
        @NamedQuery(name = COUNT_OF_STOCK_ITEMS_QUERY, query = "select count(e) from Shoppings$StockItem as e"),
        @NamedQuery(name = FIND_STOCK_ITEMS_BY_NAME_QUERY, query = "select e from Shoppings$StockItem as e where e.item.itemName = :itemName"),
        @NamedQuery(name = FIND_STOCK_ITEMS_BY_CODE_QUERY, query = "select e from Shoppings$StockItem as e where e.item.itemCode = :itemCode"),})
    public static class StockItem extends AbstractEntityIdVersion implements Serializable {

        public static final String COUNT_OF_STOCK_ITEMS_QUERY = "COUNT_OF_STOCK_ITEMS_QUERY";
        public static final String FIND_STOCK_ITEMS_BY_NAME_QUERY = "FIND_STOCK_ITEMS_BY_NAME_QUERY";
        public static final String FIND_STOCK_ITEMS_BY_CODE_QUERY = "FIND_STOCK_ITEMS_BY_CODE_QUERY";

        public static final long serialVersionUID = 20230115L;

        @Embedded
        private Item item;

        @Column(name = "AVAILABLE_COUNT", nullable = false)
        private Long availableCount;
    }

    @Data
    @Builder
    @EqualsAndHashCode
    @ToString(callSuper = true)
    @NoArgsConstructor
    @AllArgsConstructor
    @Embeddable
    public static class ShoppingItem implements Serializable {

        public static final long serialVersionUID = 20230115L;

        @Embedded
        private Item item;

        @Column(name = "QUANTITY", nullable = false)
        private Long quantity;

    }

    @Data
    @Builder
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    @NoArgsConstructor
    @AllArgsConstructor
    @Entity
    @Table(name = "SHOPPING_CARD")
    @NamedQueries({
        @NamedQuery(name = COUNT_OF_SHOPPING_CARDS_QUERY, query = "select count(e) from Shoppings$ShoppingCard as e"),
        @NamedQuery(name = FIND_SHOPPING_CARD_BY_USER, query = "select e from Shoppings$ShoppingCard as e where e.loginUser.userName = :userName")
    })
    public static class ShoppingCard extends AbstractEntityIdVersion implements Serializable {

        public final static String COUNT_OF_SHOPPING_CARDS_QUERY = "COUNT_OF_SHOPPING_CARDS_QUERY";
        public final static String FIND_SHOPPING_CARD_BY_USER = "FIND_SHOPPING_CARD_BY_USER";
        @ElementCollection
        @CollectionTable(
                name = "SHOPPING_CARD_ITEM",
                joinColumns = @JoinColumn(name = "SHOPPING_ITEM_ID")
        )
        private List<ShoppingItem> shoppingItemList;

        @Embedded
        private LoginUser loginUser;

    }

    @Data
    @Builder
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    @NoArgsConstructor
    @AllArgsConstructor
    @Entity
    @Table(name = "INVOICE")
    @NamedQueries({
        @NamedQuery(name = COUNT_OF_INVOICES_QUERY, query = "select count(e) from Shoppings$Invoice as e")
    })
    public static class Invoice extends AbstractEntityIdVersion implements Serializable {

        public static final String COUNT_OF_INVOICES_QUERY = "COUNT_OF_INVOICES_QUERY";
        public static final long serialVersionUID = 20230115L;

        //---
        private Organisation organisation;
        @Embedded()
        @AttributeOverrides({
            @AttributeOverride(name = "address", column = @Column(name = "ORG_ADDRESS")),
            @AttributeOverride(name = "city", column = @Column(name = "ORG_CITY")),
            @AttributeOverride(name = "region", column = @Column(name = "ORG_REGION")),
            @AttributeOverride(name = "postalcode", column = @Column(name = "ORG_POSTALCODE")),
            @AttributeOverride(name = "country", column = @Column(name = "ORG_COUNTRY"))
        })
        private Address orgAddress;
        //---
        @ElementCollection
        @CollectionTable(
                name = "INVOICE_SHOPPING_ITEM",
                joinColumns = @JoinColumn(name = "SHOPPING_ITEM_ID")
        )
        private List<ShoppingItem> shoppingItemList;
        //---
        @Embedded
        private Person person;
        @Embedded()
        @AttributeOverrides({
            @AttributeOverride(name = "address", column = @Column(name = "PERS_ADDRESS")),
            @AttributeOverride(name = "city", column = @Column(name = "PERS_CITY")),
            @AttributeOverride(name = "region", column = @Column(name = "PERS_REGION")),
            @AttributeOverride(name = "postalcode", column = @Column(name = "PERS_POSTALCODE")),
            @AttributeOverride(name = "country", column = @Column(name = "PERS_COUNTRY"))
        })
        private Address personAddress;

    }

    @Data
    @Builder
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    @NoArgsConstructor
    @AllArgsConstructor
    @Entity
    @Table(name = "SHOPPING_ORDER")
    @NamedQueries({
        @NamedQuery(name = COUNT_OF_ORDERS_QUERY, query = "select count(e) from Shoppings$Order as e")
    })
    public static class Order extends AbstractEntityIdVersion implements Serializable {

        public static final String COUNT_OF_ORDERS_QUERY = "COUNT_OF_ORDERS_QUERY";
        public static final long serialVersionUID = 20230115L;

        @Column(name = "ORDER_IDENTIF", unique = true)
        private String orderIdentif;

        //---
        @ElementCollection
        @CollectionTable(
                name = "ORDER_SHOPPING_ITEM",
                joinColumns = @JoinColumn(name = "SHOPPING_ITEM_ID")
        )
        private List<ShoppingItem> shoppingItemList;

    }

    @Data
    @Builder
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    @NoArgsConstructor
    @AllArgsConstructor
    @Entity
    @Table(name = "SHOPPING_DELIVERY")
    @NamedQueries({
        @NamedQuery(name = COUNT_OF_DELIVERIES_QUERY, query = "select count(e) from Shoppings$Delivery as e")
    })
    public static class Delivery extends AbstractEntityIdVersion implements Serializable {

        public static final String COUNT_OF_DELIVERIES_QUERY = "COUNT_OF_DELIVERIES_QUERY";
        public static final long serialVersionUID = 20230115L;

        @Column(name = "DELIVERY_IDENTIF", unique = true)
        private String deliverIdentif;
        @Column(name = "ORDER_IDENTIF")
        private String orderIdentif;

        enum Status {
            preparing, packaging, shipping, delivered, lost
        }
        @Column(name = "STATUS", length = 50, nullable = false)
        @Enumerated(EnumType.STRING)
        private Status status;

    }
}
