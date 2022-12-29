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
package net.kaczmarzyk.spring.data.jpa.web.annotation;

import jakarta.persistence.criteria.JoinType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies a join part of a query, e.g. {@code select c from Customer c inner join c.addresses a}
 *
 * @author Tomasz Kaczmarzyk
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.TYPE})
@Repeatable(RepeatedJoin.class)
public @interface Join {

    /**
     * Specifies a collection property to join on, e.g. "addresses"
     */
    String path();

    /**
     * Specifies an alias for the joined part, e.g. "a"
     */
    String alias();

    /**
     * Whether the query should return distinct results or not
     */
    boolean distinct() default true;

    JoinType type() default JoinType.INNER;
}
