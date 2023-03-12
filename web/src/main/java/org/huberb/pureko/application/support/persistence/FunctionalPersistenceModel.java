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
package org.huberb.pureko.application.support.persistence;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;
import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.NamedQuery;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

/**
 *
 * @author berni3
 */
public abstract class FunctionalPersistenceModel {

    private FunctionalPersistenceModel() {
    }

    /**
     * Create a {@link Query}.
     */
    public static class QueryCreatorFunctions {

        public static <T> Function<EntityManager, Query> createByNativeString(String sqlString) {
            return (EntityManager _em) -> {
                return _em.createNativeQuery(sqlString);
            };
        }

        public static <T> Function<EntityManager, Query> createByNativeString(String sqlString, Class<T> resultClass) {
            return (EntityManager _em) -> {
                return _em.createNativeQuery(sqlString, resultClass);
            };
        }

    }

    /**
     * Create a {@link TypedQuery}.
     */
    public static class TypedQueryCreatorFunctions {

        /**
         * Create a typed query from a JPA-QL String.
         *
         * @param <T>
         * @param qlString
         * @param resultClass
         * @return
         *
         * @see EntityManager#createQuery(java.lang.String, java.lang.Class)
         */
        public static <T> Function<EntityManager, TypedQuery<T>> createByQlString(String qlString, Class<T> resultClass) {
            return (EntityManager _em) -> {
                return _em.createQuery(qlString, resultClass);
            };
        }

        /**
         * Create a typed query from a predefined JPA query.
         *
         * @param <T>
         * @param name
         * @param resultClass
         * @return
         *
         * @see NamedQuery
         * @see EntityManager#createNamedQuery(java.lang.String,
         * java.lang.Class)
         */
        public static <T> Function<EntityManager, TypedQuery<T>> createByNamedQuery(String name, Class<T> resultClass) {
            return (EntityManager _em) -> {
                return _em.createNamedQuery(name, resultClass);
            };
        }
    }

    /**
     * Define consumers for setting up a query.
     */
    public static class QueryConsumers {

        public static Consumer<Query> noop() {
            return (Query q) -> {
            };
        }

        public static Consumer<Query> parameterByName(String name, Object value) {
            return (Query tq) -> {
                tq.setParameter(name, value);
            };
        }

        public static Consumer<Query> parametersByName(Object[][] parameters) {
            return (Query tq) -> {
                for (int i = 0; i < parameters.length; i += 1) {
                    final Object[] kv = parameters[i];
                    final String k = (String) kv[0];
                    final Object v = kv[1];
                    tq.setParameter(k, v);
                }
            };
        }

        public static Consumer<Query> parametersByName(Map<String, Object> kvMap) {
            return (Query tq) -> {
                kvMap.entrySet().forEach(e -> {
                    tq.setParameter(e.getKey(), e.getValue());
                });
            };
        }

        public static Consumer<Query> parameter(int position, Object value) {
            return (Query tq) -> {
                tq.setParameter(position, value);
            };
        }

        public static Consumer<Query> startPositionMaxResult(int startPosition, int maxResult) {
            return (Query tq) -> {
                tq.setFirstResult(startPosition);
                tq.setMaxResults(maxResult);
            };
        }

        public static Consumer<Query> startPosition(int startPosition) {
            return (Query tq) -> {
                tq.setFirstResult(startPosition);
            };
        }

        public static Consumer<Query> maxResult(int maxResult) {
            return (Query tq) -> {
                tq.setMaxResults(maxResult);
            };
        }

        public static Consumer<Query> flushMode(FlushModeType flushModeType) {
            return (Query tq) -> {
                tq.setFlushMode(flushModeType);
            };
        }

        public static Consumer<Query> lockMode(LockModeType lockModeType) {
            return (Query tq) -> {
                tq.setLockMode(lockModeType);
            };
        }

        public static Consumer<Query> hint(String hintName, Object value) {
            return (Query tq) -> {
                tq.setHint(hintName, value);
            };
        }

        public static Consumer<Query> consumers(Consumer<Query>[] consumers) {
            Consumer<Query> c;

            if (consumers != null && consumers.length >= 0) {
                c = consumers[0];
                for (int i = 1; i < consumers.length; i += 1) {
                    c = c.andThen(consumers[i]);
                }
            } else {
                c = noop();
            }
            return c;
        }

        public static Consumer<Query> consumers(List<Consumer<Query>> consumers) {
            Consumer<Query> c;
            if (consumers != null && consumers.size() >= 0) {
                c = consumers.get(0);
                for (int i = 1; i < consumers.size(); i += 1) {
                    c = c.andThen(consumers.get(i));
                }
            } else {
                c = noop();
            }
            return c;
        }

    }

    /**
     * Provide query result(s).
     */
    public static class QueryResultFunctions {

        public static Function<Query, Object> singleResult() {
            return (tq) -> tq.getSingleResult();
        }

        public static Function<Query, List> resultList() {
            return (tq) -> tq.getResultList();
        }

        public static Function<Query, Stream> resultStream() {
            return (tq) -> tq.getResultStream();
        }

    }

    /**
     * Provide type query result(s).
     */
    public static class TypedQueryResultFunctions {

        public static <T> Function<TypedQuery<T>, T> singleResult() {
            return (tq) -> tq.getSingleResult();
        }

        public static <T> Function<TypedQuery<T>, List<T>> resultList() {
            return (tq) -> tq.getResultList();
        }

        public static <T> Function<TypedQuery<T>, Stream<T>> resultStream() {
            return (tq) -> tq.getResultStream();
        }
    }

    //----
    @Transactional(TxType.MANDATORY)
    public static <T, V> V findResultUsingTypedQuery(Supplier<EntityManager> entityManagerSupp,
            Function<EntityManager, Query> queryCreatorFunction,
            Consumer<Query> c,
            Function<Query, V> queryResultFunction) {
        EntityManager _em = entityManagerSupp.get();
        final Query tq = queryCreatorFunction.apply(_em);
        c.accept(tq);
        final V v = queryResultFunction.apply(tq);
        return v;
    }

    //----
    // ql queries full functional
    @Transactional(TxType.MANDATORY)
    public static <T, V> V findResultUsingQuery(Supplier<EntityManager> entityManagerSupp,
            Function<EntityManager, TypedQuery<T>> typedQueryCreatorFunction,
            Consumer<Query> c,
            Function<TypedQuery<T>, V> typedQueryResultFunction) {
        EntityManager _em = entityManagerSupp.get();
        final TypedQuery<T> tq = typedQueryCreatorFunction.apply(_em);
        c.accept(tq);
        final V v = typedQueryResultFunction.apply(tq);
        return v;
    }

    //----
    // ql queries short hands
    @Transactional(TxType.MANDATORY)
    public static <T> T findSingleResult(Supplier<EntityManager> entityManagerSupp,
            String qlString,
            Class<T> resultClass,
            Consumer<Query> c) {
        final T t = findResultUsingQuery(
                entityManagerSupp,
                TypedQueryCreatorFunctions.createByQlString(qlString, resultClass),
                c,
                TypedQueryResultFunctions.singleResult()
        );
        return t;
    }

    @Transactional(TxType.MANDATORY)
    public static <T> List<T> findResultList(Supplier<EntityManager> entityManagerSupp,
            String qlString,
            Class<T> resultClass,
            Consumer<Query> c) {
        final List<T> lt = findResultUsingQuery(
                entityManagerSupp,
                TypedQueryCreatorFunctions.createByQlString(qlString, resultClass),
                c,
                TypedQueryResultFunctions.resultList()
        );
        return lt;
    }

    @Transactional(TxType.MANDATORY)
    public static <T> Stream<T> findResultStream(Supplier<EntityManager> entityManagerSupp,
            String qlString,
            Class<T> resultClass,
            Consumer<Query> c) {
        final Stream<T> st = findResultUsingQuery(
                entityManagerSupp,
                TypedQueryCreatorFunctions.createByQlString(qlString, resultClass),
                c,
                TypedQueryResultFunctions.resultStream()
        );
        return st;
    }

    //----
    // named queries short hands
    @Transactional(TxType.MANDATORY)
    public static <T> T findNamedSingleResult(Supplier<EntityManager> entityManagerSupp,
            String name,
            Class<T> resultClass,
            Consumer<Query> c) {
        final T t = findResultUsingQuery(
                entityManagerSupp,
                TypedQueryCreatorFunctions.createByNamedQuery(name, resultClass),
                c,
                TypedQueryResultFunctions.singleResult()
        );
        return t;
    }

    @Transactional(TxType.MANDATORY)
    public static <T> List<T> findNamedResultList(Supplier<EntityManager> entityManagerSupp,
            String name, Class<T> resultClass,
            Consumer<Query> c) {
        final List<T> lt = findResultUsingQuery(
                entityManagerSupp,
                TypedQueryCreatorFunctions.createByNamedQuery(name, resultClass),
                c,
                TypedQueryResultFunctions.resultList()
        );
        return lt;
    }

    @Transactional(TxType.MANDATORY)
    public static <T> Stream<T> findNamedResultStream(Supplier<EntityManager> entityManagerSupp,
            String name, Class<T> resultClass, Consumer<Query> c) {
        final Stream<T> st = findResultUsingQuery(
                entityManagerSupp,
                TypedQueryCreatorFunctions.createByNamedQuery(name, resultClass),
                c,
                TypedQueryResultFunctions.resultStream()
        );
        return st;
    }

    //----
    @Transactional(TxType.MANDATORY)
    public static <T> T findById(Supplier<EntityManager> entityManagerSupp,
            Long id,
            Class<T> resultClass) {
        EntityManager em = entityManagerSupp.get();
        T t = em.find(resultClass, id);
        return t;
    }

    //----
    @Transactional(TxType.MANDATORY)
    public static <T> T create(Supplier<EntityManager> entityManagerSupp, T entity) {
        EntityManager _em = entityManagerSupp.get();
        _em.persist(entity);
        return entity;
    }

    @Transactional(TxType.MANDATORY)
    public static <T> T update(Supplier<EntityManager> entityManagerSupp, T entity) {
        EntityManager _em = entityManagerSupp.get();
        final T result;
        if (_em.contains(entity)) {
            _em.persist(entity);
            result = entity;
        } else {
            result = _em.merge(entity);
        }
        return result;
    }

    @Transactional(TxType.MANDATORY)
    public static <T> void remove(Supplier<EntityManager> entityManagerSupp, T entity) {
        EntityManager _em = entityManagerSupp.get();
        _em.remove(entity);
    }

}
