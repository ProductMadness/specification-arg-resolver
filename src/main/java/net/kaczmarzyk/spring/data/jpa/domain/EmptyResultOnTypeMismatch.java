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
import net.kaczmarzyk.spring.data.jpa.utils.Converter.ValueRejectedException;
import net.kaczmarzyk.spring.data.jpa.web.annotation.OnTypeMismatch;
import org.springframework.data.jpa.domain.Specification;

/**
 * <p>Wrapper that turns a {@code Specification} into a one that always produces in an empty result
 * (i.e. {@code where 0 = 1}) in case of a type mismatch (e.g. when type on path is {@code Long}
 * and the value from the HTTP parameter is not a numeric.</p>
 *
 * <p> It's useful for polymorphic "OR" queries such as {@code where id = ? or name = ?}.
 * A spec will be wrapped with this decorator if {@code onTypeMismatch} property of {@code @Spec}
 * is explicitly set to {@code OnTypeMismatch.EMPTY_RESULT}, i.e.: {@code @Spec(path="id", onTypeMismatch=EMPTY_RESULT)}.
 * </p>
 *
 * @author Tomasz Kaczmarzyk
 * @see OnTypeMismatch
 */
public class EmptyResultOnTypeMismatch<T> implements Specification<T> {

    private static final long serialVersionUID = 1L;

    private final Specification<T> wrappedSpec;

    public EmptyResultOnTypeMismatch(Specification<T> wrappedSpec) {
        this.wrappedSpec = wrappedSpec;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        try {
            return wrappedSpec.toPredicate(root, query, cb);
        } catch (ValueRejectedException e) {
            return cb.equal(cb.literal(0), cb.literal(1));
        }
    }

    public Specification<T> getWrappedSpec() {
        return wrappedSpec;
    }

    @Override
    public String toString() {
        return "EmptyResultOnTypeMismatch [wrappedSpec=" + wrappedSpec + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((wrappedSpec == null) ? 0 : wrappedSpec.hashCode());
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
        EmptyResultOnTypeMismatch other = (EmptyResultOnTypeMismatch) obj;
        if (wrappedSpec == null) {
            return other.wrappedSpec == null;
        } else {
            return wrappedSpec.equals(other.wrappedSpec);
        }
    }
}
