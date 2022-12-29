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

import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;
import net.kaczmarzyk.spring.data.jpa.utils.QueryContext;
import org.springframework.data.jpa.domain.Specification;


/**
 * @author Tomasz Kaczmarzyk
 */
public abstract class PathSpecification<T> implements Specification<T> {

    private static final long serialVersionUID = 1L;
    private final QueryContext queryContext;
    protected String path;


    public PathSpecification(QueryContext queryContext, String path) {
        this.queryContext = queryContext;
        this.path = path;
    }

    @SuppressWarnings("unchecked")
    protected <F> Path<F> path(Root<T> root) {
        Path<?> expr = null;
        for (String field : path.split("\\.")) {
            if (expr == null) {
                if (queryContext != null && queryContext.getEvaluated(field, root) != null) {
                    expr = queryContext.getEvaluated(field, root);
                } else {
                    expr = root.get(field);
                }
            } else {
                expr = expr.get(field);
            }
        }
        return (Path<F>) expr;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((path == null) ? 0 : path.hashCode());
        result = prime * result + ((queryContext == null) ? 0 : queryContext.hashCode());
        return result;
    }

    @SuppressWarnings("rawtypes")
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
        PathSpecification other = (PathSpecification) obj;
        if (path == null) {
            if (other.path != null) {
                return false;
            }
        } else if (!path.equals(other.path)) {
            return false;
        }
        if (queryContext == null) {
            return other.queryContext == null;
        } else {
            return queryContext.equals(other.queryContext);
        }
    }

}
