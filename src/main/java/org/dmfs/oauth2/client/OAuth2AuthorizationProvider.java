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

import org.dmfs.httpessentials.client.HttpRequest;
import org.dmfs.httpessentials.client.HttpRequestExecutor;
import org.dmfs.httpessentials.exceptions.ProtocolError;
import org.dmfs.httpessentials.exceptions.ProtocolException;
import org.dmfs.httpessentials.exceptions.RedirectionException;
import org.dmfs.httpessentials.exceptions.UnexpectedStatusException;
import org.dmfs.rfc5545.Duration;

import java.io.IOException;
import java.net.URI;


/**
 * Represents an OAuth2 authorization provider.
 *
 * @author Marten Gajda
 */
public interface OAuth2AuthorizationProvider
{

    /**
     * Executes the given {@link HttpRequest} for this provider and returns an {@link OAuth2AccessToken}.
     *
     * @param tokenRequest
     *         The {@link HttpRequest} to execute.
     * @param executor
     *         An {@link HttpRequestExecutor} to execute the request.
     *
     * @return An {@link OAuth2AccessToken}
     *
     * @throws RedirectionException
     * @throws UnexpectedStatusException
     * @throws IOException
     * @throws ProtocolError
     * @throws ProtocolException
     */
    public OAuth2AccessToken accessToken(HttpRequest<OAuth2AccessToken> tokenRequest, HttpRequestExecutor executor) throws RedirectionException,
            UnexpectedStatusException, IOException, ProtocolError, ProtocolException;

    /**
     * Constructs the initial authorization URL for the given {@link OAuth2AuthorizationRequest}.
     *
     * @param authorizationRequest
     *         The {@link OAuth2AuthorizationRequest} to launch.
     *
     * @return A URI that represents the URL to be opened in the user agent.
     */
    public URI authorizationUrl(OAuth2AuthorizationRequest authorizationRequest);

    /**
     * Default access token time to live for this server. This is the time to live that's applied if a server doesn't return any <code>expires_in</code>
     * values.
     *
     * @return A {@link Duration}.
     */
    public Duration defaultTokenTtl();
}
