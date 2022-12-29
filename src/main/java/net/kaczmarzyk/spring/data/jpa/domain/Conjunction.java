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
import org.springframework.data.jpa.domain.Specification;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static java.util.stream.Collectors.toList;

/**
 * Helper for easier joining lists of specs with {@code AND} operator
 *
 * @author Tomasz Kaczmarzyk
 */
public class Conjunction<T> implements Specification<T>, FakeSpecWrapper<T> {

    private static final long serialVersionUID = 1L;

    private final Collection<Specification<T>> innerSpecs;

    /**
     * In case of paged search, method {@link #toPredicate(Root, CriteriaQuery, CriteriaBuilder)}
     * will be executed on the same object in two different contexts.
     * <p>
     * The first time during standard search.
     * The second time during count(*) query which is executed for paging purposes.
     * <p>
     * Fakes should be initialized in both.
     */
    private final Set<CriteriaQuery<?>> queriesWithInitializedFakes = new HashSet<>();

    @SafeVarargs
    public Conjunction(Specification<T>... innerSpecs) {
        this(Arrays.asList(innerSpecs));
    }

    public Conjunction(Collection<Specification<T>> innerSpecs) {
        this.innerSpecs = innerSpecs;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initializeFakes(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        if (!queriesWithInitializedFakes.contains(query)) {
            for (Specification<T> spec : innerSpecs) {
                if (spec instanceof FakeSpecWrapper) {
                    ((FakeSpecWrapper<T>) spec).initializeFakes(root, query, cb);
                }
                if (spec instanceof Fake) {
                    spec.toPredicate(root, query, cb);
                    continue;
                }
            }
        }
        queriesWithInitializedFakes.add(query);
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        initializeFakes(root, query, cb);

        return cb.and(
                innerSpecs.stream()
                        .filter(spec -> !(spec instanceof Fake))
                        .map(spec -> spec.toPredicate(root, query, cb))
                        .filter(Objects::nonNull)
                        .collect(toList()).toArray(new Predicate[]{})
        );
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((innerSpecs == null) ? 0 : innerSpecs.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Conjunction<?> other = (Conjunction<?>) obj;
        if (innerSpecs == null) {
            return other.innerSpecs == null;
        } else {
            return innerSpecs.equals(other.innerSpecs);
        }
    }

    @Override
    public String toString() {
        return "Conjunction [innerSpecs=" + innerSpecs + "]";
    }
}
