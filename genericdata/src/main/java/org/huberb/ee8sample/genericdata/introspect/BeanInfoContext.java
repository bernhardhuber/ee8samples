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
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 *
 * @author berni3
 */
public class BeanInfoContext {
    
    Map<String, BeanInfo> m = new HashMap<>();
    final Class rootClass;

    public BeanInfoContext(Class rootClass) {
        this.rootClass = rootClass;
    }

    public void introspect() throws IntrospectionException {
        _introspect(0, rootClass);
    }

    void _introspect(int level, Class theClassToIntrospect) throws IntrospectionException {
        final String n = theClassToIntrospect.getName();
        if (m.containsKey(n)) {
            return;
        }
        if (level >= 100) {
            return;
        }
        final BeanInfo bi = Introspector.getBeanInfo(theClassToIntrospect);
        m.put(n, bi);
        for (int i = 0; i < bi.getPropertyDescriptors().length; i++) {
            PropertyDescriptor pd = bi.getPropertyDescriptors()[i];
            if (pd.isHidden() || pd.getName().equals("class")) {
                continue;
            }
            String pdT = pd.getPropertyType().getName();
            if (pdT.startsWith("org.huber")) {
                _introspect(level + 1, pd.getPropertyType());
            }
        }
    }

    public Map<String, BeanInfo> getM() {
        return this.m;
    }

    public List<String> flattenM(int level, String prefix) {
        return flattenM(level, prefix, this.rootClass);
    }

    public List<String> flattenM(int level, String prefix, Class baseClass) {
        String n = baseClass.getName();
        BeanInfo bi = this.m.get(n);
        return flattenM(level, "", bi.getPropertyDescriptors());
    }

    public List<String> flattenM(int level, String prefix, PropertyDescriptor[] pds) {
        List<String> result = new ArrayList<>();
        if (level >= 100) {
            return result;
        }
        BiFunction<String, String, String> fqnFunction = (_prefix, _name) -> {
            return _prefix.isEmpty() ? _name : _prefix + "$" + _name;
        };
        List<PropertyDescriptor> pd1 = findBy(pds, PRED_RW.and(PRED_NOT_CLASS).and(BASETYPES_CLASS));
        pd1.forEach(pd -> {
            Class<?> theType = pd.getPropertyType();
            TypeVariable<Class<?>>[] tv = (TypeVariable<Class<?>>[]) theType.getTypeParameters();
            String theTypeName;
            if (tv != null && tv.length > 0) {
                theTypeName = String.format("%s<%s>", theType.getName(), Arrays.asList(tv).stream().map(e -> e.getName()).collect(Collectors.joining(",")));
            } else {
                theTypeName = theType.getName();
            }
            String s = String.format("%s %s;", theTypeName, fqnFunction.apply(prefix, pd.getName()));
            result.add(s);
        });
        List<PropertyDescriptor> pd2 = findBy(pds, PRED_RW.and(PRED_NOT_CLASS).and(BASETYPES_CLASS.negate()).and(PRED_PACKAGE_STARTS_WITH.apply("org.huber")));
        pd2.forEach(pd -> {
            String newPrefix = fqnFunction.apply(prefix, pd.getName());
            String className = pd.getPropertyType().getName();
            BeanInfo bi = this.m.get(className);
            if (bi != null) {
                List<String> recursiveResult = flattenM(level + 1, newPrefix, bi.getPropertyDescriptors());
                result.addAll(recursiveResult);
            }
        });
        return result;
    }
    static Predicate<PropertyDescriptor> PRED_ALWAYS_TRUE = (PropertyDescriptor pd) -> true;
    static Predicate<PropertyDescriptor> PRED_R = (PropertyDescriptor pd) -> pd.getReadMethod() != null;
    static Predicate<PropertyDescriptor> PRED_RW = (PropertyDescriptor pd) -> pd.getReadMethod() != null && pd.getWriteMethod() != null;
    static Predicate<PropertyDescriptor> PRED_NOT_CLASS = (PropertyDescriptor pd) -> !"class".equals(pd.getName());
    static Function<String, Predicate<PropertyDescriptor>> PRED_PACKAGE_STARTS_WITH = (String pkgName) -> {
        return (PropertyDescriptor pd) -> pd.getPropertyType().getPackageName().startsWith(pkgName);
    };
    static Predicate<PropertyDescriptor> BASETYPES_CLASS = (PropertyDescriptor pd) -> {
        boolean result = false;
        final Class<?> pdType = pd.getPropertyType();
        //---
        result = result || pdType.isEnum();
        result = result || pdType.isPrimitive();
        result = result || pdType.isArray();
        //---
        result = result || pdType.getPackageName().startsWith("java.");
        //---
        result = result || Number.class.isAssignableFrom(pdType);
        result = result || CharSequence.class.isAssignableFrom(pdType);
        result = result || pdType.isAssignableFrom(java.lang.Boolean.class);
        //result = result || pdType.isAssignableFrom(java.lang.String.class);
        //---
        result = result || java.util.Date.class.isAssignableFrom(pdType);
        result = result || pdType.isAssignableFrom(java.util.Calendar.class);
        result = result || pdType.isAssignableFrom(java.util.TimeZone.class);
        result = result || pdType.isAssignableFrom(java.util.Currency.class);
        result = result || pdType.isAssignableFrom(java.util.Locale.class);
        //---
        result = result || java.util.List.class.isAssignableFrom(pdType);
        result = result || java.util.Set.class.isAssignableFrom(pdType);
        result = result || java.util.HashSet.class.isAssignableFrom(pdType);
        //---
        result = result || pdType.isAssignableFrom(java.time.LocalDate.class);
        result = result || pdType.isAssignableFrom(java.time.LocalDateTime.class);
        result = result || pdType.isAssignableFrom(java.time.LocalTime.class);
        result = result || pdType.isAssignableFrom(java.time.Clock.class);
        result = result || pdType.isAssignableFrom(java.time.Instant.class);
        result = result || pdType.isAssignableFrom(java.time.DayOfWeek.class);
        result = result || pdType.isAssignableFrom(java.time.Duration.class);
        result = result || pdType.isAssignableFrom(java.time.Month.class);
        result = result || pdType.isAssignableFrom(java.time.MonthDay.class);
        result = result || pdType.isAssignableFrom(java.time.Period.class);
        result = result || pdType.isAssignableFrom(java.time.Year.class);
        result = result || pdType.isAssignableFrom(java.time.YearMonth.class);
        return result;
    };

    static List<PropertyDescriptor> findBy(BeanInfo bi, Predicate<PropertyDescriptor> pred) {
        if (pred == null) {
            pred = PRED_ALWAYS_TRUE;
        }
        return findBy(bi.getPropertyDescriptors(), pred);
    }

    static List<PropertyDescriptor> findBy(PropertyDescriptor[] pds, Predicate<PropertyDescriptor> pred) {
        if (pred == null) {
            pred = PRED_ALWAYS_TRUE;
        }
        List<PropertyDescriptor> result = Arrays.asList(pds).stream().filter(pred).collect(Collectors.toList());
        return result;
    }
    
}
