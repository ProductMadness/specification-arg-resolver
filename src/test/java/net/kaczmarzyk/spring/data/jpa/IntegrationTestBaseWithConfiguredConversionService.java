/**
 * Copyright 2014-2020 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.kaczmarzyk.spring.data.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import net.kaczmarzyk.spring.data.jpa.utils.Converter;
import net.kaczmarzyk.spring.data.jpa.web.annotation.OnTypeMismatch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;

import java.util.List;

/**
 * @author Tomasz Kaczmarzyk
 * @author TP Diffenbach
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ApplicationWithConfiguredConversionService.class})
@WebAppConfiguration
@Transactional
public abstract class IntegrationTestBaseWithConfiguredConversionService {

    @Autowired
    protected CustomerRepository customerRepo;

    @PersistenceContext
    protected EntityManager em;

    protected Converter defaultConverter = Converter.withTypeMismatchBehaviour(OnTypeMismatch.EMPTY_RESULT, null);
    protected MockMvc mockMvc;
    @Autowired
    WebApplicationContext wac;
    @Autowired
    List<HandlerMethodArgumentResolver> argumentResolvers;

    @BeforeEach
    public void setupMockMvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

}
