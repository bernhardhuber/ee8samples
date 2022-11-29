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

import java.util.Set;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;

/**
 *
 * @author berni3
 */
@ApplicationScoped
public class SystemInfo {

    Logger LOG = Logger.getLogger(SystemInfo.class.getName());
    @Inject
    private BeanManager beanManager;

    void pu() {

    }

    void cdi() {
        final Set<Bean<?>> beans = beanManager.getBeans(Object.class,
                new AnnotationLiteral<Any>() {
        });
        for (Bean<?> bean : beans) {
            final String beanInfo = String.format("bean: "
                    + "bean class %s"
                    + "bean injections points %s"
                    + "bean name %s"
                    + "bean qualifiers %s"
                    + "bean scope %s"
                    + "bean sterotypes %s"
                    + "bean types %s"
                    + "bean alternative %s"
                    + "bean nullable %s",
                    bean.getBeanClass().getName(),
                    bean.getInjectionPoints(),
                    bean.getName(),
                    bean.getQualifiers(),
                    bean.getScope(),
                    bean.getStereotypes(),
                    bean.getTypes(),
                    bean.isAlternative(),
                    bean.isNullable()
            );
            LOG.info(() -> beanInfo);
        }
    }
}
