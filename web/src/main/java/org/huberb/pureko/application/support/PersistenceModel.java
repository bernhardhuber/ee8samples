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

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import org.huberb.pureko.application.support.PuName.ManagedType;
import org.huberb.pureko.application.support.PuName.PuType;

/**
 *
 * @author berni3
 */
@RequestScoped
public class PersistenceModel extends AbstractPersistenceModel {

    @PuName(puType = PuType.ee8samplePu, managedType = ManagedType.containerManaged)
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

}
