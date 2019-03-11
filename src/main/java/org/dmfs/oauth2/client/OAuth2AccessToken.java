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

import org.dmfs.httpessentials.exceptions.ProtocolException;
import org.dmfs.jems.optional.Optional;
import org.dmfs.rfc5545.DateTime;

import java.util.NoSuchElementException;


/**
 * Represents an access token that was issued by an OAuth2 authorization server.
 *
 * @author Marten Gajda
 */
public interface OAuth2AccessToken
{
    /**
     * Returns the actual access token String.
     *
     * @return
     *
     * @throws ProtocolException
     */
    public CharSequence accessToken() throws ProtocolException;

    /**
     * Returns the access token type.
     *
     * @return
     *
     * @throws ProtocolException
     */
    public CharSequence tokenType() throws ProtocolException;

    /**
     * Returns whether the response also contained a refresh token.
     *
     * @return
     */
    public boolean hasRefreshToken();

    /**
     * Returns the refresh token. Before calling this use {@link #hasRefreshToken()} to check if there actually is a refresh token.
     *
     * @return
     *
     * @throws NoSuchElementException
     *         If the token doesn't contain a refresh token.
     * @throws ProtocolException
     */
    public CharSequence refreshToken() throws ProtocolException;

    /**
     * Returns the expected expiration date of the access token.
     *
     * @return
     *
     * @throws ProtocolException
     */
    public DateTime expirationDate() throws ProtocolException;

    /**
     * The scope this {@link OAuth2AccessToken} was issued for. May be an empty scope if the scope is not known.
     *
     * @return An {@link OAuth2Scope}.
     *
     * @throws ProtocolException
     */
    public OAuth2Scope scope() throws ProtocolException;

    /**
     * Returns a value stored in the token response under the {@code parameterName}.
     *
     * @param parameterName
     *         the key under which the value is stored in the response
     */
    public Optional<CharSequence> extraParameter(final String parameterName);
}
