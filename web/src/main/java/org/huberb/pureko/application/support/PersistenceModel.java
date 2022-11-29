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
package org.huberb.pureko.application.support;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

/**
 *
 * @author berni3
 */
@RequestScoped
public class PersistenceModel {

    @Inject
    private EntityManager em;

    public static class TypedQueryCreatorFunctions {

        public static <T> Function<EntityManager, TypedQuery<T>> createByQlString(String qlString, Class<T> resultClass) {
            return (EntityManager _em) -> {
                return _em.createQuery(qlString, resultClass);
            };
        }

        public static <T> Function<EntityManager, TypedQuery<T>> createByNamedQuery(String name, Class<T> resultClass) {
            return (EntityManager _em) -> {
                return _em.createNamedQuery(name, resultClass);
            };
        }
    }

    public static class TypedQueryConsumers {

        public static <T> Consumer<TypedQuery<T>> noop() {
            return (TypedQuery<T> tq) -> {
            };
        }

        public static <T> Consumer<TypedQuery<T>> parameterByName(String name, Object value) {
            return (TypedQuery<T> tq) -> {
                tq.setParameter(name, value);
            };
        }

        public static <T> Consumer<TypedQuery<T>> parametersByName(Object[][] parameters) {
            return (TypedQuery<T> tq) -> {
                for (int i = 0; i < parameters.length; i += 1) {
                    final Object[] kv = parameters[i];
                    final String k = (String) kv[0];
                    final Object v = kv[1];
                    tq.setParameter(k, v);
                }
            };

        }

        public static <T> Consumer<TypedQuery<T>> parametersByName(Map<String, Object> kvMap) {
            return (TypedQuery<T> tq) -> {
                kvMap.entrySet().forEach(e -> {
                    tq.setParameter(e.getKey(), e.getValue());
                });
            };
        }

        public static <T> Consumer<TypedQuery<T>> parameter(int position, Object value) {
            return (TypedQuery<T> tq) -> {
                tq.setParameter(position, value);
            };
        }

        public static <T> Consumer<TypedQuery<T>> defineStartPositionMaxResult(int startPosition, int maxResult) {
            return (TypedQuery<T> tq) -> {
                tq.setFirstResult(startPosition);
                tq.setMaxResults(maxResult);
            };
        }

        public static <T> Consumer<TypedQuery<T>> flushMode(FlushModeType flushModeType) {
            return (TypedQuery<T> tq) -> {
                tq.setFlushMode(flushModeType);
            };
        }

        public static <T> Consumer<TypedQuery<T>> lockMode(LockModeType lockModeType) {
            return (TypedQuery<T> tq) -> {
                tq.setLockMode(lockModeType);
            };
        }

        public static <T> Consumer<TypedQuery<T>> hint(String hintName, Object value) {
            return (TypedQuery<T> tq) -> {
                tq.setHint(hintName, value);
            };
        }
    }

    static class TypedQueryResultFunctions {

        static <T> Function<TypedQuery<T>, T> singleResult() {
            return (tq) -> tq.getSingleResult();
        }

        static <T> Function<TypedQuery<T>, List<T>> resultList() {
            return (tq) -> tq.getResultList();
        }

        static <T> Function<TypedQuery<T>, Stream<T>> resultStream() {
            return (tq) -> tq.getResultStream();
        }
    }

    //----
    // ql queries full functional
    @Transactional(TxType.MANDATORY)
    public <T, V> V findResult(Function<EntityManager, TypedQuery<T>> f,
            Consumer<TypedQuery<T>> c,
            Function<TypedQuery<T>, V> f2
    ) {
        final TypedQuery<T> tq = f.apply(em);
        final V v = f2.apply(tq);
        return v;
    }

    //----
    // ql queries short hands
    @Transactional(TxType.MANDATORY)
    public <T> T findSingleResult(String qlString, Class<T> resultClass, Consumer<TypedQuery<T>> c) {
        final T t = findResult(TypedQueryCreatorFunctions.createByQlString(qlString, resultClass),
                c,
                TypedQueryResultFunctions.singleResult()
        );
        return t;
    }

    @Transactional(TxType.MANDATORY)
    public <T> List<T> findResultList(String qlString, Class<T> resultClass, Consumer<TypedQuery<T>> c) {
        final List<T> lt = findResult(TypedQueryCreatorFunctions.createByQlString(qlString, resultClass),
                c,
                TypedQueryResultFunctions.resultList()
        );
        return lt;
    }

    @Transactional(TxType.MANDATORY)
    public <T> Stream<T> findResultStream(String qlString, Class<T> resultClass, Consumer<TypedQuery<T>> c) {
        final Stream<T> st = findResult(TypedQueryCreatorFunctions.createByQlString(qlString, resultClass),
                c,
                TypedQueryResultFunctions.resultStream()
        );
        return st;
    }

    //----
    // named queries short hands
    @Transactional(TxType.MANDATORY)
    public <T> T findNamedSingleResult(String name, Class<T> resultClass, Consumer<TypedQuery<T>> c) {
        final T t = findResult(TypedQueryCreatorFunctions.createByNamedQuery(name, resultClass),
                c,
                TypedQueryResultFunctions.singleResult()
        );
        return t;
    }

    @Transactional(TxType.MANDATORY)
    public <T> List<T> findNamedResultList(String name, Class<T> resultClass, Consumer<TypedQuery<T>> c) {
        final List<T> lt = findResult(TypedQueryCreatorFunctions.createByNamedQuery(name, resultClass),
                c,
                TypedQueryResultFunctions.resultList()
        );
        return lt;
    }

    @Transactional(TxType.MANDATORY)
    public <T> Stream<T> findNamedResultStream(String name, Class<T> resultClass, Consumer<TypedQuery<T>> c) {
        final Stream<T> st = findResult(TypedQueryCreatorFunctions.createByNamedQuery(name, resultClass),
                c,
                TypedQueryResultFunctions.resultStream()
        );
        return st;
    }

    //----
    @Transactional(TxType.MANDATORY)
    public <T> T findById(Long id, Class<T> resultClass) {
        T t = em.find(resultClass, id);
        return t;
    }

    //----
    @Transactional(TxType.MANDATORY)
    public <T> T create(T t) {
        this.em.persist(t);
        return t;
    }

    @Transactional(TxType.MANDATORY)
    public <T> T update(T t) {
        T tMerged = this.em.merge(t);
        return tMerged;
    }

}
