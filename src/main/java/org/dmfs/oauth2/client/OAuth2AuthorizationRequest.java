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

import org.dmfs.oauth2.client.pkce.PkceCodeChallenge;
import org.dmfs.rfc3986.Uri;

import java.net.URI;


/**
 * Represents an authorization request for an interactive grant type.
 *
 * @author Marten Gajda
 */
public interface OAuth2AuthorizationRequest
{

    /**
     * Creates a new {@link OAuth2AuthorizationRequest} using the given client id.
     *
     * @param clientId
     *         The client id of the client application that requests authorization.
     *
     * @return A new {@link OAuth2AuthorizationRequest} with the updated value.
     */
    OAuth2AuthorizationRequest withClientId(String clientId);

    /**
     * Creates a new {@link OAuth2AuthorizationRequest} using the given redirect {@link URI}.
     *
     * @param redirectUri
     *         The redirect URI the server is expected to redirect the user agent to.
     *
     * @return A new {@link OAuth2AuthorizationRequest} with the updated value.
     */
    OAuth2AuthorizationRequest withRedirectUri(Uri redirectUri);

    /**
     * Creates a new {@link OAuth2AuthorizationRequest} using the given {@link PkceCodeChallenge}.
     *
     * @param codeChallenge
     *
     * @return A new {@link OAuth2AuthorizationRequest} with a code challenge.
     */
    OAuth2AuthorizationRequest withCodeChallenge(PkceCodeChallenge codeChallenge);

    /**
     * Constructs the initial request URL using the given authorization endpoint URI.
     *
     * @param authorizationEndpoint
     *         A {@link URI} that represents the authorization endpoint URL.
     *
     * @return
     *
     * @throws IllegalStateException
     *         if a required value has not been passed yet.
     */
    URI authorizationUri(URI authorizationEndpoint);
}
