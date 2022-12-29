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
import net.kaczmarzyk.spring.data.jpa.utils.Converter;
import net.kaczmarzyk.spring.data.jpa.utils.QueryContext;

import java.util.Objects;

/**
 * <p>Filers with "is null" or "is not null" where clause (e.g. {@code where nickName is not null}).
 * This file is the inverse of Null.java</p>
 *
 * <p>Requires boolean parameter.</p>
 *
 * @author Steven McLaughlin
 */
public class NotNull<T> extends PathSpecification<T> {

    private static final long serialVersionUID = 1L;
    private final Converter converter;
    protected String expectedValue;

    public NotNull(QueryContext queryContext, String path, String[] httpParamValues, Converter converter) {
        super(queryContext, path);
        if (httpParamValues == null || httpParamValues.length != 1) {
            throw new IllegalArgumentException();
        }
        this.expectedValue = httpParamValues[0];
        this.converter = converter;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        if (converter.convert(expectedValue, Boolean.class)) {
            return cb.isNotNull(path(root));
        } else {
            return cb.isNull(path(root));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        NotNull<?> notNull = (NotNull<?>) o;
        return Objects.equals(expectedValue, notNull.expectedValue)
                && Objects.equals(converter, notNull.converter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), expectedValue, converter);
    }

    @Override
    public String toString() {
        return "NotNull["
                + "expectedValue='" + expectedValue + '\''
                + ", converter=" + converter
                + ", path=" + path
                + ']';
    }
}
