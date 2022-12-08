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
import javax.persistence.Query;
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

    protected PersistenceModel() {
    }

    public PersistenceModel(EntityManager em) {
        this.em = em;
    }

    public EntityManager getEntityManager() {
        return this.em;
    }

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

//    public static class TypedQueryConsumers {
//
//        public static <T> Consumer<TypedQuery<T>> noop() {
//            return (TypedQuery<T> tq) -> {
//            };
//        }
//
//        public static <T> Consumer<TypedQuery<T>> parameterByName(String name, Object value) {
//            return (TypedQuery<T> tq) -> {
//                tq.setParameter(name, value);
//            };
//        }
//
//        public static <T> Consumer<TypedQuery<T>> parametersByName(Object[][] parameters) {
//            return (TypedQuery<T> tq) -> {
//                for (int i = 0; i < parameters.length; i += 1) {
//                    final Object[] kv = parameters[i];
//                    final String k = (String) kv[0];
//                    final Object v = kv[1];
//                    tq.setParameter(k, v);
//                }
//            };
//        }
//
//        public static <T> Consumer<TypedQuery<T>> parametersByName(Map<String, Object> kvMap) {
//            return (TypedQuery<T> tq) -> {
//                kvMap.entrySet().forEach(e -> {
//                    tq.setParameter(e.getKey(), e.getValue());
//                });
//            };
//        }
//
//        public static <T> Consumer<TypedQuery<T>> parameter(int position, Object value) {
//            return (TypedQuery<T> tq) -> {
//                tq.setParameter(position, value);
//            };
//        }
//
//        public static <T> Consumer<TypedQuery<T>> startPositionMaxResult(int startPosition, int maxResult) {
//            return (TypedQuery<T> tq) -> {
//                tq.setFirstResult(startPosition);
//                tq.setMaxResults(maxResult);
//            };
//        }
//
//        public static <T> Consumer<TypedQuery<T>> startPosition(int startPosition) {
//            return (TypedQuery<T> tq) -> {
//                tq.setFirstResult(startPosition);
//            };
//        }
//
//        public static <T> Consumer<TypedQuery<T>> maxResult(int maxResult) {
//            return (TypedQuery<T> tq) -> {
//                tq.setMaxResults(maxResult);
//            };
//        }
//
//        public static <T> Consumer<TypedQuery<T>> flushMode(FlushModeType flushModeType) {
//            return (TypedQuery<T> tq) -> {
//                tq.setFlushMode(flushModeType);
//            };
//        }
//
//        public static <T> Consumer<TypedQuery<T>> lockMode(LockModeType lockModeType) {
//            return (TypedQuery<T> tq) -> {
//                tq.setLockMode(lockModeType);
//            };
//        }
//
//        public static <T> Consumer<TypedQuery<T>> hint(String hintName, Object value) {
//            return (TypedQuery<T> tq) -> {
//                tq.setHint(hintName, value);
//            };
//        }
//
//        public static <T> Consumer<TypedQuery<T>> consumers(Consumer<TypedQuery<T>>[] consumers) {
//            Consumer<TypedQuery<T>> c;
//
//            if (consumers != null && consumers.length >= 0) {
//                c = consumers[0];
//                for (int i = 1; i < consumers.length; i += 1) {
//                    c = c.andThen(consumers[i]);
//                }
//            } else {
//                c = noop();
//            }
//            return c;
//        }
//
//        public static <T> Consumer<TypedQuery<T>> consumers(List<Consumer<TypedQuery<T>>> consumers) {
//            Consumer<TypedQuery<T>> c;
//            if (consumers != null && consumers.size() >= 0) {
//                c = consumers.get(0);
//                for (int i = 1; i < consumers.size(); i += 1) {
//                    c = c.andThen(consumers.get(i));
//                }
//            } else {
//                c = noop();
//            }
//            return c;
//        }
//    }
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
    public <T, V> V findResultUsingTypedQuery(Function<EntityManager, Query> f,
            Consumer<Query> c,
            Function<Query, V> f2) {
        final Query tq = f.apply(em);
        final V v = f2.apply(tq);
        return v;
    }

    //----
    // ql queries full functional
    @Transactional(TxType.MANDATORY)
    public <T, V> V findResultUsingQuery(Function<EntityManager, TypedQuery<T>> f,
            Consumer<Query> c,
            Function<TypedQuery<T>, V> f2) {
        final TypedQuery<T> tq = f.apply(em);
        final V v = f2.apply(tq);
        return v;
    }

    //----
    // ql queries short hands
    @Transactional(TxType.MANDATORY)
    public <T> T findSingleResult(String qlString, Class<T> resultClass, Consumer<Query> c) {
        final T t = findResultUsingQuery(
                TypedQueryCreatorFunctions.createByQlString(qlString, resultClass),
                c,
                TypedQueryResultFunctions.singleResult()
        );
        return t;
    }

    @Transactional(TxType.MANDATORY)
    public <T> List<T> findResultList(String qlString, Class<T> resultClass, Consumer<Query> c) {
        final List<T> lt = findResultUsingQuery(
                TypedQueryCreatorFunctions.createByQlString(qlString, resultClass),
                c,
                TypedQueryResultFunctions.resultList()
        );
        return lt;
    }

    @Transactional(TxType.MANDATORY)
    public <T> Stream<T> findResultStream(String qlString, Class<T> resultClass, Consumer<Query> c) {
        final Stream<T> st = findResultUsingQuery(
                TypedQueryCreatorFunctions.createByQlString(qlString, resultClass),
                c,
                TypedQueryResultFunctions.resultStream()
        );
        return st;
    }

    //----
    // named queries short hands
    @Transactional(TxType.MANDATORY)
    public <T> T findNamedSingleResult(String name, Class<T> resultClass, Consumer<Query> c) {
        final T t = findResultUsingQuery(TypedQueryCreatorFunctions.createByNamedQuery(name, resultClass),
                c,
                TypedQueryResultFunctions.singleResult()
        );
        return t;
    }

    @Transactional(TxType.MANDATORY)
    public <T> List<T> findNamedResultList(String name, Class<T> resultClass, Consumer<Query> c) {
        final List<T> lt = findResultUsingQuery(
                TypedQueryCreatorFunctions.createByNamedQuery(name, resultClass),
                c,
                TypedQueryResultFunctions.resultList()
        );
        return lt;
    }

    @Transactional(TxType.MANDATORY)
    public <T> Stream<T> findNamedResultStream(String name, Class<T> resultClass, Consumer<Query> c) {
        final Stream<T> st = findResultUsingQuery(
                TypedQueryCreatorFunctions.createByNamedQuery(name, resultClass),
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
    public <T> T create(T entity) {
        em.persist(entity);
        return entity;
    }

    @Transactional(TxType.MANDATORY)
    public <T> T update(T entity) {
        final T result;
        if (em.contains(entity)) {
            em.persist(entity);
            result = entity;
        } else {
            result = em.merge(entity);
        }
        return result;
    }

    @Transactional(TxType.MANDATORY)
    public <T> void remove(T entity) {
        em.remove(entity);
    }

}
