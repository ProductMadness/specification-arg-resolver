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
package net.kaczmarzyk.spring.data.jpa.domain;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import net.kaczmarzyk.spring.data.jpa.Customer;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.springframework.data.jpa.domain.Specification;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Tomasz Kaczmarzyk
 */
public class ConjunctionUnitTest {

    Root<Customer> root = mock(Root.class);
    CriteriaQuery<Customer> query = mock(CriteriaQuery.class);
    CriteriaBuilder criteriaBuilder = mock(CriteriaBuilder.class);

    @Test
    public void executesFakeSpecBeforeRegularOne() {
        Specification<Customer> fakeSpec = mock(FakeSpec.class);
        Specification<Customer> regularSpec = mock(Like.class);

        Conjunction<Customer> conjunction = new Conjunction<>(fakeSpec, regularSpec);

        conjunction.toPredicate(root, query, criteriaBuilder);

        InOrder inOrder = inOrder(fakeSpec, regularSpec);
        inOrder.verify(fakeSpec).toPredicate(root, query, criteriaBuilder);
        inOrder.verify(regularSpec).toPredicate(root, query, criteriaBuilder);
    }

    @Test
    public void executesFakeSpecBeforeRegularOnes() {
        Specification<Customer> fakeSpec = mock(FakeSpec.class);
        Specification<Customer> regularSpec1 = withMockedToPredicateMethod(mock(Like.class));
        Specification<Customer> regularSpec2 = withMockedToPredicateMethod(mock(Equal.class));

        Conjunction<Customer> conjunction = new Conjunction<>(regularSpec2, fakeSpec, regularSpec1);

        conjunction.toPredicate(root, query, criteriaBuilder);

        InOrder inOrder = inOrder(fakeSpec, regularSpec1, regularSpec2);
        inOrder.verify(fakeSpec).toPredicate(root, query, criteriaBuilder);
        inOrder.verify(regularSpec2).toPredicate(root, query, criteriaBuilder);
        inOrder.verify(regularSpec1).toPredicate(root, query, criteriaBuilder);
    }

    public Specification<Customer> withMockedToPredicateMethod(Specification<Customer> spec) {
        when(spec.toPredicate(any(), any(), any()))
                .thenReturn(mock(Predicate.class));

        return spec;
    }

    private interface FakeSpec extends Specification<Customer>, Fake {
    }
}
