/*
 * Copyright 2016 dmfs GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dmfs.oauth2.client;

/**
 * Represents an OAuth2 scope.
 *
 * @author Marten Gajda
 */
public interface OAuth2Scope
{
    /**
     * Returns whether this scope is empty.
     *
     * @return <code>true</code> if this scope is empty, <code>false</code> otherwise.
     */
    boolean isEmpty();

    /**
     * Returns whether this scope contains the given scope token or not.
     *
     * @param token
     *         The scope token to check for.
     *
     * @return <code>true</code> if the scope token is contained in this scope, <code>false</code> otherwise.
     */
    boolean hasToken(String token);

    /**
     * Returns the number of tokens in this {@link OAuth2Scope}.
     *
     * @return The number of tokens in this {@link OAuth2Scope}.
     */
    int tokenCount();

    /**
     * Returns a string version of this scope as described in <a href="https://tools.ietf.org/html/rfc6749#section-3.3">RFC 6749, section 3.3</a>, i.e. a list
     * of scope tokens separated by spaces.
     *
     * @return A String containing a list scope tokens, separated by spaces, or an empty String if {@link #isEmpty()} returns <code>true</code>.
     */
    String toString();
}
