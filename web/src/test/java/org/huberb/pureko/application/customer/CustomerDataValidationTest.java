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
package org.huberb.pureko.application.customer;

import java.util.Set;
import java.util.function.Supplier;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.hibernate.validator.HibernateValidator;
import org.hibernate.validator.HibernateValidatorConfiguration;
import org.hibernate.validator.cfg.ConstraintMapping;
import org.hibernate.validator.cfg.defs.NotNullDef;
import org.hibernate.validator.cfg.defs.SizeDef;
import org.huberb.pureko.application.customer.CustomerData.FullAddress;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author berni3
 */
public class CustomerDataValidationTest {

    ValidatorFactory defaultValidatorFactory;
    Validator defaultValidator;

    @BeforeEach
    public void setUp() {
        this.defaultValidatorFactory = Validation.buildDefaultValidatorFactory();
        this.defaultValidator = this.defaultValidatorFactory.getValidator();
    }

    @AfterEach
    public void tearDown() {
        this.defaultValidatorFactory.close();
    }

    @Test
    public void validate1() {
        CustomerData cd = CustomerData.builder()
                .customerID("customerID1")
                .companyName("companyName1")
                .build();
        Set<ConstraintViolation<CustomerData>> cvcds = defaultValidator.validate(cd, javax.validation.groups.Default.class);
        String m = "" + cvcds;
        assertEquals(0, cvcds.size(), m);
    }

    @Test()
    public void test_customCustomerDataValidator_fails() {
        final Validator validator = customCustomerDataValidator().get();
        final CustomerData cd = CustomerData.builder()
                .customerID("customerID1")
                .companyName("companyName1")
                .build();
        final Set<ConstraintViolation<CustomerData>> cvcds = validator.validate(cd, javax.validation.groups.Default.class);
        final String m = "" + cvcds;
        assertEquals(0, cvcds.size(), m);
    }

    static Supplier<Validator> customCustomerDataValidator() {
        Supplier<Validator> validatorSupplier = () -> {
            final HibernateValidatorConfiguration configuration = Validation
                    .byProvider(HibernateValidator.class)
                    .configure();
            final ConstraintMapping constraintMapping = configuration.createConstraintMapping();
            constraintMapping
                    .type(CustomerData.class)
                    .field("customerID")
                    .constraint(new NotNullDef())
                    .field("companyName")
                    .ignoreAnnotations(true)
                    .constraint(new NotNullDef())
                    .constraint(new SizeDef().min(2).max(14))
                    .type(FullAddress.class)
                    .getter("address")
                    .constraint(new NotNullDef());
            final Validator validator = configuration.addMapping(constraintMapping)
                    .buildValidatorFactory()
                    .getValidator();
            return validator;
        };
        return validatorSupplier;
    }

    @Test
    public void test_customCustomerDataValidator_succeeds() {
        final Validator validator = customCustomerDataValidator().get();
        final CustomerData cd = CustomerData.builder()
                .customerID("customerID1")
                .companyName("companyName1")
                .build();
        final Set<ConstraintViolation<CustomerData>> cvcds = validator.validate(cd, javax.validation.groups.Default.class);
        final String m = "" + cvcds;
        assertEquals(0, cvcds.size(), m);
    }
}
