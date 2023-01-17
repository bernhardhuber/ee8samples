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
package org.huberb.pureko.application.support.persistence;

import java.lang.annotation.Documented;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Qualifier;
import org.huberb.pureko.application.support.persistence.PuName.PuType;

/**
 *
 * @author berni3
 */
@Target({METHOD, FIELD, PARAMETER, TYPE})
@Retention(RUNTIME)
@Documented
@Qualifier
@Inherited
public @interface PuName {

    //String value();
    PuType puType();

    ManagedType managedType() default ManagedType.containerManaged;

    public enum PuType {
        ee8samplePu("ee8samplesPu");
        private final String puName;

        private PuType(String puName) {
            this.puName = puName;
        }

        public String getPuName() {
            return puName;
        }

    }

    public enum ManagedType {
        beanManaged, containerManaged;
    }

    public final static class Literal extends AnnotationLiteral<PuName> {

        public static final Literal INSTANCE = new Literal();

        private static final long serialVersionUID = 1L;

    }
}
