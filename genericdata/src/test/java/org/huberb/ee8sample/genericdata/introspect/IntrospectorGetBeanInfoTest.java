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
package org.huberb.ee8sample.genericdata.introspect;

import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.huberb.ee8sample.genericdata.Basics;
import org.huberb.ee8sample.genericdata.shopping.Shoppings;
import org.junit.jupiter.api.Test;

/**
 *
 * @author berni3
 */
public class IntrospectorGetBeanInfoTest {

    @Test
    @org.junit.jupiter.api.Disabled
    public void hello_getBeanInfo$Basics$Item() throws IntrospectionException {
        BeanInfo biItem = java.beans.Introspector.getBeanInfo(Basics.Item.class);
        /*
bitem java.beans.GenericBeanInfo@3c130745,
BeanDescriptor: java.beans.BeanDescriptor[name=Basics$Item; beanClass=class org.huberb.ee8sample.genericdata.Basics$Item]
BeanDescriptor2: beandescriptor
name Basics$Item,
beanClass class org.huberb.ee8sample.genericdata.Basics$Item,
customizerClass: null,
displayName: Basics$Item,
shortDescription: Basics$Item,
attributes: ,

MethodDescriptor: 
[java.beans.MethodDescriptor[
  name=getClass; method=public final native java.lang.Class java.lang.Object.getClass()], 
  java.beans.MethodDescriptor[name=getItemCode; method=public java.lang.String org.huberb.ee8sample.genericdata.Basics$Item.getItemCode()], 
  java.beans.MethodDescriptor[name=setItemCode; method=public void org.huberb.ee8sample.genericdata.Basics$Item.setItemCode(java.lang.String)], 
  java.beans.MethodDescriptor[name=getItemName; method=public java.lang.String org.huberb.ee8sample.genericdata.Basics$Item.getItemName()], 
  java.beans.MethodDescriptor[name=wait; method=public final void java.lang.Object.wait() throws java.lang.InterruptedException], 
  java.beans.MethodDescriptor[name=notifyAll; method=public final native void java.lang.Object.notifyAll()], 
  java.beans.MethodDescriptor[name=notify; method=public final native void java.lang.Object.notify()], 
  java.beans.MethodDescriptor[name=wait; method=public final void java.lang.Object.wait(long,int) throws java.lang.InterruptedException], 
  java.beans.MethodDescriptor[name=hashCode; method=public int org.huberb.ee8sample.genericdata.Basics$Item.hashCode()], 
  java.beans.MethodDescriptor[name=builder; method=public static org.huberb.ee8sample.genericdata.Basics$Item$ItemBuilder org.huberb.ee8sample.genericdata.Basics$Item.builder()], 
  java.beans.MethodDescriptor[name=wait; method=public final native void java.lang.Object.wait(long) throws java.lang.InterruptedException], 
  java.beans.MethodDescriptor[name=equals; method=public boolean org.huberb.ee8sample.genericdata.Basics$Item.equals(java.lang.Object)], 
  java.beans.MethodDescriptor[name=toString; method=public java.lang.String org.huberb.ee8sample.genericdata.Basics$Item.toString()], 
  java.beans.MethodDescriptor[name=setItemName; method=public void org.huberb.ee8sample.genericdata.Basics$Item.setItemName(java.lang.String)]]
PropertyDescriptor: 
[java.beans.PropertyDescriptor[
  name=class; 
  values={expert=false; visualUpdate=false; hidden=false; enumerationValues=[Ljava.lang.Object;@e056f20; required=false}; 
  propertyType=class java.lang.Class; 
  readMethod=public final native java.lang.Class java.lang.Object.getClass()], 

java.beans.PropertyDescriptor[
  name=itemCode; 
  values={expert=false; visualUpdate=false; hidden=false; enumerationValues=[Ljava.lang.Object;@4b0b0854; required=false}; 
  propertyType=class java.lang.String; 
  readMethod=public java.lang.String org.huberb.ee8sample.genericdata.Basics$Item.getItemCode(); 
  writeMethod=public void org.huberb.ee8sample.genericdata.Basics$Item.setItemCode(java.lang.String)], 

java.beans.PropertyDescriptor[
  name=itemName; 
  values={expert=false; visualUpdate=false; hidden=false; enumerationValues=[Ljava.lang.Object;@19bb07ed; required=false}; 
  propertyType=class java.lang.String; 
  readMethod=public java.lang.String org.huberb.ee8sample.genericdata.Basics$Item.getItemName(); 
  writeMethod=public void org.huberb.ee8sample.genericdata.Basics$Item.setItemName(java.lang.String)]]
         */

        System.out.println(formatBeanInfo(biItem));

    }

    String formatBeanInfo(BeanInfo beanInfo) {
        Function<BeanDescriptor, String> fBeanDescriptor2String = bd -> {
            StringBuilder sb = new StringBuilder();
            bd.attributeNames().asIterator().forEachRemaining(attr -> sb.append(String.format("%s,", attr)));
            return String.format("beandescriptor%n"
                    + "name %s,%n"
                    + "beanClass %s,%n"
                    + "customizerClass: %s,%n"
                    + "displayName: %s,%n"
                    + "shortDescription: %s,%n"
                    + "attributes: %s,%n",
                    bd.getName(),
                    bd.getBeanClass(),
                    bd.getCustomizerClass(),
                    bd.getDisplayName(),
                    bd.getShortDescription(),
                    sb.toString()
            );
        };
        return String.format("beanInfo %s,%n"
                + "BeanDescriptor: %s%n"
                + "BeanDescriptor2: %s%n"
                + "MethodDescriptor: %s%n"
                + "PropertyDescriptor: %s%n",
                beanInfo,
                beanInfo.getBeanDescriptor(),
                fBeanDescriptor2String.apply(beanInfo.getBeanDescriptor()),
                Arrays.toString(beanInfo.getMethodDescriptors()),
                Arrays.toString(beanInfo.getPropertyDescriptors())
        );
    }

    @Test
    public void hello_BeanInfoContext1() throws IntrospectionException {
        /*
bic Shoppings.Invoice {org.huberb.ee8sample.genericdata.Basics$Address=java.beans.GenericBeanInfo@6e171cd7, org.huberb.ee8sample.shopping.Shoppings$Invoice=java.beans.GenericBeanInfo@402bba4f, org.huberb.ee8sample.genericdata.Basics$Person=java.beans.GenericBeanInfo@795cd85e, org.huberb.ee8sample.genericdata.Basics$Name=java.beans.GenericBeanInfo@59fd97a8, org.huberb.ee8sample.genericdata.Basics$Organisation=java.beans.GenericBeanInfo@f5ac9e4}
org.huberb.ee8sample.genericdata.Basics$Address: bitem java.beans.GenericBeanInfo@6e171cd7,
BeanDescriptor: java.beans.BeanDescriptor[name=Basics$Address; beanClass=class org.huberb.ee8sample.genericdata.Basics$Address]
BeanDescriptor2: beandescriptor
name Basics$Address,
beanClass class org.huberb.ee8sample.genericdata.Basics$Address,
customizerClass: null,
displayName: Basics$Address,
shortDescription: Basics$Address,
attributes: ,


PropertyDescriptor: [java.beans.PropertyDescriptor[name=address; values={expert=false; visualUpdate=false; hidden=false; enumerationValues=[Ljava.lang.Object;@4d9e68d0; required=false}; propertyType=class java.lang.String; readMethod=public java.lang.String org.huberb.ee8sample.genericdata.Basics$Address.getAddress(); writeMethod=public void org.huberb.ee8sample.genericdata.Basics$Address.setAddress(java.lang.String)], java.beans.PropertyDescriptor[name=city; values={expert=false; visualUpdate=false; hidden=false; enumerationValues=[Ljava.lang.Object;@42e99e4a; required=false}; propertyType=class java.lang.String; readMethod=public java.lang.String org.huberb.ee8sample.genericdata.Basics$Address.getCity(); writeMethod=public void org.huberb.ee8sample.genericdata.Basics$Address.setCity(java.lang.String)], java.beans.PropertyDescriptor[name=class; values={expert=false; visualUpdate=false; hidden=false; enumerationValues=[Ljava.lang.Object;@14dd9eb7; required=false}; propertyType=class java.lang.Class; readMethod=public final native java.lang.Class java.lang.Object.getClass()], java.beans.PropertyDescriptor[name=country; values={expert=false; visualUpdate=false; hidden=false; enumerationValues=[Ljava.lang.Object;@52e6fdee; required=false}; propertyType=class java.lang.String; readMethod=public java.lang.String org.huberb.ee8sample.genericdata.Basics$Address.getCountry(); writeMethod=public void org.huberb.ee8sample.genericdata.Basics$Address.setCountry(java.lang.String)], java.beans.PropertyDescriptor[name=postalcode; values={expert=false; visualUpdate=false; hidden=false; enumerationValues=[Ljava.lang.Object;@6c80d78a; required=false}; propertyType=class java.lang.String; readMethod=public java.lang.String org.huberb.ee8sample.genericdata.Basics$Address.getPostalcode(); writeMethod=public void org.huberb.ee8sample.genericdata.Basics$Address.setPostalcode(java.lang.String)], java.beans.PropertyDescriptor[name=region; values={expert=false; visualUpdate=false; hidden=false; enumerationValues=[Ljava.lang.Object;@62150f9e; required=false}; propertyType=class java.lang.String; readMethod=public java.lang.String org.huberb.ee8sample.genericdata.Basics$Address.getRegion(); writeMethod=public void org.huberb.ee8sample.genericdata.Basics$Address.setRegion(java.lang.String)]]

org.huberb.ee8sample.shopping.Shoppings$Invoice: bitem java.beans.GenericBeanInfo@402bba4f,
BeanDescriptor: java.beans.BeanDescriptor[name=Shoppings$Invoice; beanClass=class org.huberb.ee8sample.shopping.Shoppings$Invoice]
BeanDescriptor2: beandescriptor
name Shoppings$Invoice,
beanClass class org.huberb.ee8sample.shopping.Shoppings$Invoice,
customizerClass: null,
displayName: Shoppings$Invoice,
shortDescription: Shoppings$Invoice,
attributes: ,

PropertyDescriptor: [

java.beans.PropertyDescriptor[name=class; values={expert=false; visualUpdate=false; hidden=false; enumerationValues=[Ljava.lang.Object;@14dd9eb7; required=false}; propertyType=class java.lang.Class; readMethod=public final native java.lang.Class java.lang.Object.getClass()], 

java.beans.PropertyDescriptor[name=createdWhen; values={expert=false; visualUpdate=false; hidden=false; enumerationValues=[Ljava.lang.Object;@1a451d4d; required=false}; propertyType=class java.time.LocalDateTime; readMethod=public java.time.LocalDateTime org.huberb.ee8sample.shopping.Shoppings$AbstractEntityIdVersion.getCreatedWhen(); writeMethod=public void org.huberb.ee8sample.shopping.Shoppings$AbstractEntityIdVersion.setCreatedWhen(java.time.LocalDateTime)], 

java.beans.PropertyDescriptor[name=id; values={expert=false; visualUpdate=false; hidden=false; enumerationValues=[Ljava.lang.Object;@7fa98a66; required=false}; propertyType=class java.lang.Long; readMethod=public java.lang.Long org.huberb.ee8sample.shopping.Shoppings$AbstractEntityIdVersion.getId(); writeMethod=public void org.huberb.ee8sample.shopping.Shoppings$AbstractEntityIdVersion.setId(java.lang.Long)], 

java.beans.PropertyDescriptor[name=orgAddress; values={expert=false; visualUpdate=false; hidden=false; enumerationValues=[Ljava.lang.Object;@15ff3e9e; required=false}; propertyType=class org.huberb.ee8sample.genericdata.Basics$Address; readMethod=public org.huberb.ee8sample.genericdata.Basics$Address org.huberb.ee8sample.shopping.Shoppings$Invoice.getOrgAddress(); writeMethod=public void org.huberb.ee8sample.shopping.Shoppings$Invoice.setOrgAddress(org.huberb.ee8sample.genericdata.Basics$Address)], 

java.beans.PropertyDescriptor[name=organisation; values={expert=false; visualUpdate=false; hidden=false; enumerationValues=[Ljava.lang.Object;@5fdcaa40; required=false}; propertyType=class org.huberb.ee8sample.genericdata.Basics$Organisation; readMethod=public org.huberb.ee8sample.genericdata.Basics$Organisation org.huberb.ee8sample.shopping.Shoppings$Invoice.getOrganisation(); writeMethod=public void org.huberb.ee8sample.shopping.Shoppings$Invoice.setOrganisation(org.huberb.ee8sample.genericdata.Basics$Organisation)], 

java.beans.PropertyDescriptor[name=person; values={expert=false; visualUpdate=false; hidden=false; enumerationValues=[Ljava.lang.Object;@6dc17b83; required=false}; propertyType=class org.huberb.ee8sample.genericdata.Basics$Person; readMethod=public org.huberb.ee8sample.genericdata.Basics$Person org.huberb.ee8sample.shopping.Shoppings$Invoice.getPerson(); writeMethod=public void org.huberb.ee8sample.shopping.Shoppings$Invoice.setPerson(org.huberb.ee8sample.genericdata.Basics$Person)], 

java.beans.PropertyDescriptor[name=personAddress; values={expert=false; visualUpdate=false; hidden=false; enumerationValues=[Ljava.lang.Object;@5e0826e7; required=false}; propertyType=class org.huberb.ee8sample.genericdata.Basics$Address; readMethod=public org.huberb.ee8sample.genericdata.Basics$Address org.huberb.ee8sample.shopping.Shoppings$Invoice.getPersonAddress(); writeMethod=public void org.huberb.ee8sample.shopping.Shoppings$Invoice.setPersonAddress(org.huberb.ee8sample.genericdata.Basics$Address)], 

java.beans.PropertyDescriptor[name=shoppingItemList; values={expert=false; visualUpdate=false; hidden=false; enumerationValues=[Ljava.lang.Object;@32eff876; required=false}; propertyType=interface java.util.List; readMethod=public java.util.List org.huberb.ee8sample.shopping.Shoppings$Invoice.getShoppingItemList(); writeMethod=public void org.huberb.ee8sample.shopping.Shoppings$Invoice.setShoppingItemList(java.util.List)], 

java.beans.PropertyDescriptor[name=updatedWhen; values={expert=false; visualUpdate=false; hidden=false; enumerationValues=[Ljava.lang.Object;@8dbdac1; required=false}; propertyType=class java.time.LocalDateTime; readMethod=public java.time.LocalDateTime org.huberb.ee8sample.shopping.Shoppings$AbstractEntityIdVersion.getUpdatedWhen(); writeMethod=public void org.huberb.ee8sample.shopping.Shoppings$AbstractEntityIdVersion.setUpdatedWhen(java.time.LocalDateTime)], 

java.beans.PropertyDescriptor[name=version; values={expert=false; visualUpdate=false; hidden=false; enumerationValues=[Ljava.lang.Object;@6e20b53a; required=false}; propertyType=class java.lang.Integer; readMethod=public java.lang.Integer org.huberb.ee8sample.shopping.Shoppings$AbstractEntityIdVersion.getVersion(); writeMethod=public void org.huberb.ee8sample.shopping.Shoppings$AbstractEntityIdVersion.setVersion(java.lang.Integer)]]

org.huberb.ee8sample.genericdata.Basics$Person: bitem java.beans.GenericBeanInfo@795cd85e,
BeanDescriptor: java.beans.BeanDescriptor[name=Basics$Person; beanClass=class org.huberb.ee8sample.genericdata.Basics$Person]
BeanDescriptor2: beandescriptor
name Basics$Person,
beanClass class org.huberb.ee8sample.genericdata.Basics$Person,
customizerClass: null,
displayName: Basics$Person,
shortDescription: Basics$Person,
attributes: ,

PropertyDescriptor: [java.beans.PropertyDescriptor[name=class; values={expert=false; visualUpdate=false; hidden=false; enumerationValues=[Ljava.lang.Object;@14dd9eb7; required=false}; propertyType=class java.lang.Class; readMethod=public final native java.lang.Class java.lang.Object.getClass()], java.beans.PropertyDescriptor[name=personName; values={expert=false; visualUpdate=false; hidden=false; enumerationValues=[Ljava.lang.Object;@71809907; required=false}; propertyType=class org.huberb.ee8sample.genericdata.Basics$Name; readMethod=public org.huberb.ee8sample.genericdata.Basics$Name org.huberb.ee8sample.genericdata.Basics$Person.getPersonName(); writeMethod=public void org.huberb.ee8sample.genericdata.Basics$Person.setPersonName(org.huberb.ee8sample.genericdata.Basics$Name)]]

org.huberb.ee8sample.genericdata.Basics$Name: bitem java.beans.GenericBeanInfo@59fd97a8,
BeanDescriptor: java.beans.BeanDescriptor[name=Basics$Name; beanClass=class org.huberb.ee8sample.genericdata.Basics$Name]
BeanDescriptor2: beandescriptor
name Basics$Name,
beanClass class org.huberb.ee8sample.genericdata.Basics$Name,
customizerClass: null,
displayName: Basics$Name,
shortDescription: Basics$Name,
attributes: ,

PropertyDescriptor: [java.beans.PropertyDescriptor[name=class; values={expert=false; visualUpdate=false; hidden=false; enumerationValues=[Ljava.lang.Object;@14dd9eb7; required=false}; propertyType=class java.lang.Class; readMethod=public final native java.lang.Class java.lang.Object.getClass()], java.beans.PropertyDescriptor[name=firstName; values={expert=false; visualUpdate=false; hidden=false; enumerationValues=[Ljava.lang.Object;@3ce1e309; required=false}; propertyType=class java.lang.String; readMethod=public java.lang.String org.huberb.ee8sample.genericdata.Basics$Name.getFirstName(); writeMethod=public void org.huberb.ee8sample.genericdata.Basics$Name.setFirstName(java.lang.String)], java.beans.PropertyDescriptor[name=lastName; values={expert=false; visualUpdate=false; hidden=false; enumerationValues=[Ljava.lang.Object;@6aba2b86; required=false}; propertyType=class java.lang.String; readMethod=public java.lang.String org.huberb.ee8sample.genericdata.Basics$Name.getLastName(); writeMethod=public void org.huberb.ee8sample.genericdata.Basics$Name.setLastName(java.lang.String)], java.beans.PropertyDescriptor[name=middleName; values={expert=false; visualUpdate=false; hidden=false; enumerationValues=[Ljava.lang.Object;@158da8e; required=false}; propertyType=class java.lang.String; readMethod=public java.lang.String org.huberb.ee8sample.genericdata.Basics$Name.getMiddleName(); writeMethod=public void org.huberb.ee8sample.genericdata.Basics$Name.setMiddleName(java.lang.String)], java.beans.PropertyDescriptor[name=title; values={expert=false; visualUpdate=false; hidden=false; enumerationValues=[Ljava.lang.Object;@74e52303; required=false}; propertyType=class java.lang.String; readMethod=public java.lang.String org.huberb.ee8sample.genericdata.Basics$Name.getTitle(); writeMethod=public void org.huberb.ee8sample.genericdata.Basics$Name.setTitle(java.lang.String)]]

org.huberb.ee8sample.genericdata.Basics$Organisation: bitem java.beans.GenericBeanInfo@f5ac9e4,
BeanDescriptor: java.beans.BeanDescriptor[name=Basics$Organisation; beanClass=class org.huberb.ee8sample.genericdata.Basics$Organisation]
BeanDescriptor2: beandescriptor
name Basics$Organisation,
beanClass class org.huberb.ee8sample.genericdata.Basics$Organisation,
customizerClass: null,
displayName: Basics$Organisation,
shortDescription: Basics$Organisation,
attributes: ,

PropertyDescriptor: [java.beans.PropertyDescriptor[name=class; values={expert=false; visualUpdate=false; hidden=false; enumerationValues=[Ljava.lang.Object;@14dd9eb7; required=false}; propertyType=class java.lang.Class; readMethod=public final native java.lang.Class java.lang.Object.getClass()], java.beans.PropertyDescriptor[name=organisationName; values={expert=false; visualUpdate=false; hidden=false; enumerationValues=[Ljava.lang.Object;@47af7f3d; required=false}; propertyType=class java.lang.String; readMethod=public java.lang.String org.huberb.ee8sample.genericdata.Basics$Organisation.getOrganisationName(); writeMethod=public void org.huberb.ee8sample.genericdata.Basics$Organisation.setOrganisationName(java.lang.String)]]



         */
        Function<Class, String> f = (clazz) -> {
            try {
                BeanInfoContext bic = new BeanInfoContext(clazz);
                bic.introspect();
                return bic.flattenM(0, "", clazz).stream()
                        .collect(Collectors.joining("\n"));
            } catch (IntrospectionException ex) {
                throw new RuntimeException("introspect", ex);
            }
        };
        Consumer<Class> c = (clazz) -> {
            System.out.format("flattenM %s {%n%s%n}%n",
                    clazz.getName(),
                    f.apply(clazz));

        };
        {
            c.accept(Shoppings.Invoice.class);
            c.accept(Shoppings.Order.class);
            c.accept(Shoppings.Delivery.class);
            c.accept(Shoppings.ShoppingCard.class);
            c.accept(Shoppings.ShoppingItem.class);
        }

    }


}
