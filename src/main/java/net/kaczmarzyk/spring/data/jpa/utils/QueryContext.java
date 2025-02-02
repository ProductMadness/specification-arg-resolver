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
package net.kaczmarzyk.spring.data.jpa.utils;

import jakarta.persistence.criteria.Fetch;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;

import java.util.function.Function;

/**
 * Ugly way to share context between different specifications -- e.g. joins (see {@code JoinSpecificationResolver})
 *
 * @author Tomasz Kaczmarzyk
 */
public interface QueryContext {

    Join<?, ?> getEvaluated(String key, Root<?> root);

    void putLazyVal(String key, Function<Root<?>, Join<?, ?>> value);

    Fetch<?, ?> getEvaluatedJoinFetch(String key);

    void putEvaluatedJoinFetch(String key, Fetch<?, ?> fetch);

}
