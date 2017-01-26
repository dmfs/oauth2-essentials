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

import org.dmfs.httpessentials.client.HttpRequestExecutor;
import org.dmfs.httpessentials.exceptions.ProtocolError;
import org.dmfs.httpessentials.exceptions.ProtocolException;
import org.dmfs.rfc3986.Uri;

import java.io.Serializable;
import java.net.URI;


/**
 * Interface of an OAuth2 grant types that requires interaction with the user. This interaction is usually carried out by a browser component that is launched
 * with an initial authorization request URI. The back channel is provided by a redirect that contains the response values in the URI fragment part.
 *
 * @author Marten Gajda
 */
public interface OAuth2InteractiveGrant extends OAuth2Grant
{

    /**
     * Returns the initial URL to load in the user agent.
     *
     * @return A {@link URI}.
     */
    public URI authorizationUrl();

    /**
     * Update the authentication flow with the redirect URI that was returned by the user agent. Unless this throws an Exception, the caller can assume that
     * it's safe to call {@link #accessToken(HttpRequestExecutor)} on the returned object.
     *
     * @param redirectUri
     *         The redirect {@link URI} as returned by the user agent.
     *
     * @return A new {@link OAuth2InteractiveGrant} object that represents the new state.
     *
     * @throws ProtocolError
     *         If the server returned an error.
     * @throws ProtocolException
     *         If the redirectUri is invalid.
     */
    public OAuth2InteractiveGrant withRedirect(Uri redirectUri) throws ProtocolError, ProtocolException;

    /**
     * Return a {@link Serializable} state object that can be used to retain the current authentication flow state whenever the original {@link
     * OAuth2InteractiveGrant} can't be preserved. The state object later can be used to create a new {@link OAuth2InteractiveGrant} that allows to continue the
     * authentication flow.
     * <p>
     * Note that not all {@link OAuth2InteractiveGrant} implementations may support this at any stage.
     *
     * @return An {@link OAuth2GrantState}.
     *
     * @throws UnsupportedOperationException
     *         if this grant type does not support exporting the current state.
     */
    public OAuth2GrantState state() throws UnsupportedOperationException;

    /**
     * The interface of a simple {@link Serializable} object that represents the state of an interactive grant.
     */
    public interface OAuth2GrantState extends Serializable
    {
        /**
         * Creates an {@link OAuth2InteractiveGrant} from this state for the given client.
         * <p>
         * Note: you must provide the same client that was provided to the {@link OAuth2InteractiveGrant} that returned this {@link OAuth2GrantState}.
         *
         * @param client
         *         The {@link OAuth2Client} that was used by the {@link OAuth2InteractiveGrant} that issued this {@link OAuth2GrantState}.
         *
         * @return An {@link OAuth2InteractiveGrant} that can be used to continue the authentication flow.
         */
        public OAuth2InteractiveGrant grant(OAuth2Client client);
    }
}
