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
import org.dmfs.rfc3986.Uri;
import org.dmfs.rfc5545.Duration;

import java.io.IOException;
import java.net.URI;


/**
 * Represents an OAuth2 client to a specific {@link OAuth2AuthorizationProvider}.
 *
 * @author Marten Gajda
 */
public interface OAuth2Client
{

    /**
     * Executes the given {@link HttpRequest} and returns an {@link OAuth2AccessToken}.
     *
     * @param tokenRequest
     *         An {@link HttpRequest} to execute.
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
    OAuth2AccessToken accessToken(HttpRequest<OAuth2AccessToken> tokenRequest, HttpRequestExecutor executor) throws RedirectionException,
            UnexpectedStatusException, IOException, ProtocolError, ProtocolException;

    /**
     * Constructs the initial URL for an interactive authorization grant.
     *
     * @param authorizationRequest
     *         The {@link OAuth2AuthorizationRequest} to launch.
     *
     * @return The URL that needs to be opened in a user agent.
     */
    URI authorizationUrl(OAuth2AuthorizationRequest authorizationRequest);

    /**
     * Generates a random {@link CharSequence} to be used for security measures. The length of the result is implementation specific. Implementations must make
     * sure the result contains a sufficient amount of entropy.
     *
     * @return A random {@link CharSequence}.
     */
    CharSequence randomChars();

    /**
     * The redirect URI of this client as registered with the server.
     *
     * @return A {@link URI}.
     */
    Uri redirectUri();

    /**
     * Returns the default lifetime of an access token.
     *
     * @return A {@link Duration}.
     */
    Duration defaultTokenTtl();
}
